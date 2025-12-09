package com.skillforge.platform.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.skillforge.platform.filters.JwtAuthenticationFilter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:8080"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .exceptionHandling(exception -> exception.authenticationEntryPoint(new CustomAuthenticationEntryPoint()))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
//                        Interview Routes
                                .requestMatchers("/api/v0/interview/get/{enrollmentId}","/api/v0/interview/create/attempt/{studentId}/{enrollmentId}","/api/v0/interview/get/attempt/{enrollmentId}").authenticated()
//                        Student Progress Routes
                                .requestMatchers("/api/v0/progress/update/{enrollId}/{learningMaterialId}","/api/v0/progress/get/{enrollmentId}").authenticated()
//                        Student Profile Routes
                                .requestMatchers("/api/v0/student/get/{userId}","/api/v0/student/create/{userId}","/api/v0/student/edit/{studentId}").authenticated()
//                        Student Enrollment  Routes
                                .requestMatchers("/api/v0/enroll/create/{studentId}/{courseId}","/api/v0/enroll/get/{studentId}","/api/v0/enroll/get/courses/{studentId}","api/v0/enroll/get/courses/active/{studentId}","api/v0/enroll/get/courses/completed/{studentId}").authenticated()
//                        Quiz Routes
                                .requestMatchers("/api/v0/quiz/create/{topicId}/{instructorId}",
                                        "/api/v0/quiz/attempt/{quizId}/{studentId}/{enrollmentId}/{courseId}","/api/v0/quiz/topic/{topicId}","/api/v0/quiz/get/{quizId}","api/v0/quiz/attempt/get/{quizId}/{studentId}","/api/v0/quiz/generate").authenticated()
//                        Learning Material Routes
                                .requestMatchers("/api/v0/learning/create/{topicId}","/api/v0/learning/topic/{topicId}").authenticated()
//                        Topic Routes
                                .requestMatchers("/api/v0/topic/get/course/{courseId}","/api/v0/topic/create/{courseId}").authenticated()
//                        Course Routes
                                .requestMatchers("/api/v0/course/all","/api/v0/course/all/published","/api/v0/course/all/published/{instructorId}","/api/v0/course/all/draft/{instructorId}","/api/v0/course/all/{instructorId}","/api/v0/course/create/{instructorId}","/api/v0/course/get/{courseId}","/api/v0/course/getFull/{courseId}","/api/v0/course/setThumbnail/{courseId}","/api/v0/course/publish/{courseId}").authenticated()
//                        Instructor Routes
                                .requestMatchers("/api/v0/instructor/get/{userId}","/api/v0/instructor/create/{userId}","/api/v0/instructor/edit/{instructorId}").authenticated()
//                        User Routes
                                .requestMatchers("/api/v0/user/get").authenticated()
//                        Authentication Routes
                                .requestMatchers("/api/v0/auth/register","/api/v0/auth/login").permitAll()
//                        Swagger Routes
                                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                                .anyRequest().authenticated()
                );

        http.csrf(AbstractHttpConfigurer::disable);
        http.formLogin(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

}
