package com.skillforge.platform.services.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillforge.platform.payloads.*;
import com.skillforge.platform.services.AIService;
import com.skillforge.platform.services.AWSS3Service;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AIServiceImpl implements AIService {

    private final AWSS3Service awsS3Service;
    private final RestTemplate restTemplate; // could be injected too
    private final ObjectMapper objectMapper;

    @Value("${ai.google.api.key}")
    private String GEMINI_API_KEY;

    @Value("${ai.google.gemini.image.url}")
    private String GEMINI_IMAGE_URL;

    @Value("${ai.google.gemini.text.flash.url}")
    private String GEMINI_TEXT_FLASH_URL;

    @Override
    public String generateImageAndStore(String prompt) throws Exception {
        // Prepare headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-goog-api-key", GEMINI_API_KEY);

        // Prepare request body
        String requestBody = new JSONObject()
                .put("contents", new JSONArray()
                        .put(new JSONObject()
                                .put("parts", new JSONArray()
                                        .put(new JSONObject()
                                                .put("text", prompt)
                                        )
                                )
                        )
                )
                .put("generationConfig", new JSONObject()
                        .put("responseModalities", new JSONArray()
                                .put("TEXT")
                                .put("IMAGE"))
                )
                .toString();

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        // Send POST request
        ResponseEntity<String> response = restTemplate.exchange(
                GEMINI_IMAGE_URL,
                HttpMethod.POST,
                entity,
                String.class
        );

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new IOException("Gemini API Error: " + response.getBody());
        }

        String responseBody = response.getBody();
        String base64Image = extractBase64Image(responseBody);

        // Convert base64 to file
        byte[] imageBytes = Base64.getDecoder().decode(base64Image);
        File tempFile = File.createTempFile("gemini_image_" + UUID.randomUUID(), ".png");
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(imageBytes);
        }

        // Upload to S3

        // Clean up
//        tempFile.delete();

        return awsS3Service.uploadImage(tempFile);
    }


    @Override
    public QuizResponseDto generateAIQuiz(QuizRequestDto quizRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-goog-api-key", GEMINI_API_KEY);

        String userPrompt = String.format("""
            Create a JSON object representing a quiz. Output only **minified JSON**, no extra text.

            Structure:
            {
              "quizTitle": "",
              "description": "",
              "questions": [
                {
                  "questionType": "MCQ or TRUE_FALSE",
                  "questionText": "",
                  "options": {"A": "", "B": "", "C": "", "D": ""},
                  "correctAnswer": "",
                  "explanation": "",
                  "points": 1,
                  "orderIndex": 1
                }
              ]
            }

            **Rules:**
            - Include **minimum 5 questions**.
            - Approx. 70%% MCQ and 30%% TRUE_FALSE.
            - MCQ must have exactly 4 options.
            - TRUE_FALSE must NOT include the options object.
            - explanation: 1-line justification.
            - questionText must be clear and aligned with topic.
            - orderIndex must start at 1 and increment.
            - points always 1 unless difficulty is HARD — then 2.

            **Quiz Inputs**:
            - Title: %s
            - Description: %s
            - Difficulty: %s
            """,
                quizRequest.getTitle(),
                quizRequest.getDescription(),
                "MEDIUM"
        );

        JSONObject payload = new JSONObject()
                .put("system_instruction", new JSONObject()
                        .put("parts", new JSONArray().put(new JSONObject()
                                .put("text", """
                                    You are an expert assessment designer. Generate concise, accurate quiz questions.
                                    Follow the JSON schema exactly.
                                    No additional explanation.
                                    """))))
                .put("contents", new JSONArray()
                        .put(new JSONObject().put("parts",
                                new JSONArray().put(new JSONObject().put("text", userPrompt))))
                )
                .put("generationConfig", new JSONObject()
                        .put("responseMimeType", "application/json")
                        .put("temperature", 0.3)
                        .put("thinkingConfig", new JSONObject().put("thinkingBudget", 2048))
                        .put("maxOutputTokens", 3000)
                        .put("responseSchema", getQuizJSONSchema())
                );

        return sendRequestToGemini(headers, payload);
    }
    @Override
    public InterviewResponseDto generateInterviewQuestions(InterviewRequestDto interviewRequest) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-goog-api-key", GEMINI_API_KEY);

        // Build the user prompt — customized for INTERVIEW questions
        String userPrompt = String.format("""
            Create a JSON object representing an interview.
            Output only **minified JSON**, no extra text.

            JSON Structure:
            {
              "interviewTitle": "",
              "interviewDescription": "",
              "interviewQuestionDtos": [
                {
                  "questionText": "",
                  "orderIndex": 1
                }
              ]
            }

            **Rules:**
            - Generate **10 to 12** purely subjective interview questions.
            - Questions must be open-ended.
            - No MCQ, no True/False — only subjective.
            - OrderIndex must start at 1 and increase sequentially.
            - Questions must be based on the course title and its topics.
            - Avoid vague or generic questions.
            - Each question should evaluate understanding, reasoning, and clarity.

            **Course Title**: %s

            **Topics**:
            %s
            """,
                interviewRequest.getCourseTitle(),
                String.join(", ", interviewRequest.getTopicTitles())
        );

        // Build the Gemini payload
        JSONObject payload = new JSONObject()
                .put("system_instruction", new JSONObject()
                        .put("parts", new JSONArray().put(new JSONObject()
                                .put("text", """
                                    You are an expert technical interviewer.
                                    Generate clear, challenging, topic-focused interview questions.
                                    Follow the JSON format exactly.
                                    No extra explanations.
                                    """))))
                .put("contents", new JSONArray()
                        .put(new JSONObject().put("parts",
                                new JSONArray().put(new JSONObject().put("text", userPrompt)))))
            .put("generationConfig", new JSONObject()
                .put("responseMimeType", "application/json")
                .put("temperature", 0.4)
                .put("maxOutputTokens", 2000)
                .put("thinkingConfig", new JSONObject().put("thinkingBudget", 1024))
                .put("responseSchema", getInterviewJSONSchema())
        );

        return sendInterviewRequestToGemini(headers, payload);
    }

    @Override
    public InterviewAttemptResponseDto evaluateInterviewAttempt(InterviewAttemptRequestDto attemptRequest) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-goog-api-key", GEMINI_API_KEY);

        // Build AI prompt
        StringBuilder qaBuilder = new StringBuilder();
        for (int i = 0; i < attemptRequest.getQuestions().length; i++) {
            qaBuilder.append(String.format("""
            {
              "questionId": "%s",
              "questionText": "%s",
              "studentAnswer": "%s"
            },
            """,
                    attemptRequest.getQuestionIds()[i],
                    attemptRequest.getQuestions()[i],
                    attemptRequest.getAnswers()[i]
            ));
        }

        String aiPrompt = String.format("""
                You MUST respond ONLY with valid JSON.
                DO NOT add explanations, markdown, or extra text.
                If any field is unknown, return null or empty values.
                Your response MUST be strictly valid JSON and fully closed.
                Format EXACTLY as this schema:
                        
                {
                  "aiOverallScore": number,
                  "aiSkillEvaluation": { "string": number },
                  "aiFeedbackSummary": "string",
                  "interviewAnswerDtos": [
                    {
                      "interviewQuestionId": "string",
                      "studentAnswer": "string",
                      "aiEvaluation": "string",
                      "aiScore": number,
                      "timeSpentSeconds": number
                    }
                  ]
                }
                        
        """, qaBuilder.toString());

        // Prepare Gemini request payload
        JSONObject payload = new JSONObject()
                .put("system_instruction", new JSONObject()
                        .put("parts", new JSONArray().put(new JSONObject().put("text", """
                            You are a strict but fair technical evaluator.
                            Produce well-structured JSON only.
                            """))))
                .put("contents", new JSONArray()
                        .put(new JSONObject().put("parts",
                                new JSONArray().put(new JSONObject().put("text", aiPrompt)))))
                .put("generationConfig", new JSONObject()
                        .put("responseMimeType", "application/json")
                        .put("responseSchema", getInterviewAttemptSchema())
                        .put("temperature", 0.3)
                        .put("maxOutputTokens", 3000)
                        .put("thinkingConfig", new JSONObject().put("thinkingBudget", 2048))
                );

        // Execute the API request
        HttpEntity<String> request = new HttpEntity<>(payload.toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(
                GEMINI_TEXT_FLASH_URL,
                HttpMethod.POST,
                request,
                String.class
        );

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Gemini answer evaluation failed: " + response.getBody());
        }

        return parseInterviewEvaluation(response.getBody());
    }
    public String fixJson(String raw) {
        // Remove markdown artifacts
        raw = raw.replace("```json", "")
                .replace("```", "")
                .trim();

        // Try to find the last closing brace
        int lastBrace = raw.lastIndexOf("}");
        if (lastBrace != -1) {
            raw = raw.substring(0, lastBrace + 1);
        }

        return raw;
    }

    public InterviewAttemptResponseDto parseInterviewEvaluation(String json) {
        try {
            JSONObject wrapper = new JSONObject(json);

            String extractedJson =
                    wrapper.getJSONArray("candidates")
                            .getJSONObject(0)
                            .getJSONObject("content")
                            .getJSONArray("parts")
                            .getJSONObject(0)
                            .getString("text");
            String fixedJson = fixJson(extractedJson);

            System.out.println("FINAL JSON SENT TO JACKSON:");
            System.out.println(fixedJson);

            return objectMapper.readValue(fixedJson, InterviewAttemptResponseDto.class);

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse interview evaluation", e);
        }
    }

    private static JSONObject getInterviewJSONSchema() {
        return new JSONObject()
                .put("type", "OBJECT")
                .put("properties", new JSONObject()
                        .put("interviewTitle", new JSONObject().put("type", "STRING"))
                        .put("interviewDescription", new JSONObject().put("type", "STRING"))
                        .put("interviewQuestionDtos", new JSONObject()
                                .put("type", "ARRAY")
                                .put("items", new JSONObject()
                                        .put("type", "OBJECT")
                                        .put("properties", new JSONObject()
                                                .put("questionText", new JSONObject().put("type", "STRING"))
                                                .put("orderIndex", new JSONObject().put("type", "INTEGER"))
                                        )
                                        .put("required", new JSONArray()
                                                .put("questionText")
                                                .put("orderIndex")
                                        )
                                )
                        )
                )
                .put("required", new JSONArray()
                        .put("interviewTitle")
                        .put("interviewDescription")
                        .put("interviewQuestionDtos")
                );
    }
    private InterviewResponseDto sendInterviewRequestToGemini(HttpHeaders headers, JSONObject payload) {
        HttpEntity<String> request = new HttpEntity<>(payload.toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(
                GEMINI_TEXT_FLASH_URL,
                HttpMethod.POST,
                request,
                String.class
        );

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Gemini API Error: " + response.getBody());
        }

        return parseInterviewGeminiResponse(response.getBody());
    }
    public InterviewResponseDto parseInterviewGeminiResponse(String json) {
        try {
            JSONObject root = new JSONObject(json);

            String extractedJson =
                    root.getJSONArray("candidates")
                            .getJSONObject(0)
                            .getJSONObject("content")
                            .getJSONArray("parts")
                            .getJSONObject(0)
                            .getString("text");

            return objectMapper.readValue(extractedJson, InterviewResponseDto.class);

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Gemini interview response", e);
        }
    }

    private static JSONObject getQuizJSONSchema() {
        return new JSONObject()
                .put("type", "OBJECT")
                .put("properties", new JSONObject()
                        .put("quizTitle", new JSONObject().put("type", "STRING"))
                        .put("description", new JSONObject().put("type", "STRING"))
                        .put("questions", new JSONObject()
                                .put("type", "ARRAY")
                                .put("items", new JSONObject()
                                        .put("type", "OBJECT")
                                        .put("properties", new JSONObject()
                                                .put("questionType", new JSONObject().put("type", "STRING"))
                                                .put("questionText", new JSONObject().put("type", "STRING"))
                                                .put("options", new JSONObject()
                                                        .put("type", "OBJECT")
                                                        .put("nullable", true)
                                                        .put("properties", new JSONObject()
                                                                .put("A", new JSONObject().put("type", "STRING"))
                                                                .put("B", new JSONObject().put("type", "STRING"))
                                                                .put("C", new JSONObject().put("type", "STRING"))
                                                                .put("D", new JSONObject().put("type", "STRING"))
                                                        )
                                                        .put("required", new JSONArray()
                                                                .put("A")
                                                                .put("B")
                                                                .put("C")
                                                                .put("D")
                                                        )
                                                )

                                                .put("correctAnswer", new JSONObject().put("type", "STRING"))
                                                .put("explanation", new JSONObject().put("type", "STRING"))
                                                .put("points", new JSONObject().put("type", "INTEGER"))
                                                .put("orderIndex", new JSONObject().put("type", "INTEGER"))
                                        )
                                        .put("required", new JSONArray()
                                                .put("questionType")
                                                .put("questionText")
                                                .put("correctAnswer")
                                                .put("explanation")
                                                .put("points")
                                                .put("orderIndex")
                                        )
                                )
                        )
                )
                .put("required", new JSONArray()
                        .put("quizTitle")
                        .put("description")
                        .put("questions")
                );
    }
    private static JSONObject getInterviewAttemptSchema() {
        return new JSONObject()
                .put("type", "OBJECT")
                .put("properties", new JSONObject()
                        .put("aiOverallScore", new JSONObject().put("type", "NUMBER"))
                        .put("aiSkillEvaluation", new JSONObject()
                                .put("type", "OBJECT")
                                .put("properties", new JSONObject()
                                        .put("theory", new JSONObject().put("type", "NUMBER"))
                                        .put("clarity", new JSONObject().put("type", "NUMBER"))
                                        .put("problemSolving", new JSONObject().put("type", "NUMBER"))
                                )
                        )
                        .put("aiFeedbackSummary", new JSONObject().put("type", "STRING"))
                        .put("interviewAnswerDtos", new JSONObject()
                                .put("type", "ARRAY")
                                .put("items", new JSONObject()
                                        .put("type", "OBJECT")
                                        .put("properties", new JSONObject()
                                                .put("interviewQuestionId", new JSONObject().put("type", "STRING"))
                                                .put("studentAnswer", new JSONObject().put("type", "STRING"))
                                                .put("aiEvaluation", new JSONObject().put("type", "STRING"))
                                                .put("aiScore", new JSONObject().put("type", "NUMBER"))
                                                .put("timeSpentSeconds", new JSONObject().put("type", "INTEGER"))
                                        )
                                )
                        )
                )
                .put("required", new JSONArray()
                        .put("aiOverallScore")
                        .put("aiSkillEvaluation")
                        .put("aiFeedbackSummary")
                        .put("interviewAnswerDtos")
                );
    }

    private QuizResponseDto sendRequestToGemini(HttpHeaders headers, JSONObject payload) {
        HttpEntity<String> request = new HttpEntity<>(payload.toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(
                GEMINI_TEXT_FLASH_URL,
                HttpMethod.POST,
                request,
                String.class
        );

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Gemini API Error: " + response.getBody());
        }
        return parseGeminiResponse(response.getBody());
    }

    public QuizResponseDto parseGeminiResponse(String json) {
        try {
            JSONObject root = new JSONObject(json);

            String extractedJson =
                    root.getJSONArray("candidates")
                            .getJSONObject(0)
                            .getJSONObject("content")
                            .getJSONArray("parts")
                            .getJSONObject(0)
                            .getString("text");

            return objectMapper.readValue(extractedJson, QuizResponseDto.class);

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Gemini quiz response", e);
        }
    }



    private String extractBase64Image(String responseBody) {
        JSONObject json = new JSONObject(responseBody);
        JSONArray candidates = json.getJSONArray("candidates");

        if (candidates.isEmpty()) {
            throw new RuntimeException("No candidates found in response.");
        }

        JSONObject firstCandidate = candidates.getJSONObject(0);
        JSONArray parts = firstCandidate
                .getJSONObject("content")
                .getJSONArray("parts");

        // Loop through all parts and find inlineData
        for (int i = 0; i < parts.length(); i++) {
            JSONObject part = parts.getJSONObject(i);
            if (part.has("inlineData")) {
                JSONObject inlineData = part.getJSONObject("inlineData");
                if (inlineData.has("data")) {
                    return inlineData.getString("data");
                }
            }
        }

        throw new RuntimeException("No inlineData image found in any part of the response.");
    }

}
