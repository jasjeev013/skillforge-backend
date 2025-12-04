package com.skillforge.platform.controllers;

import com.skillforge.platform.payloads.ApiResponseObject;
import com.skillforge.platform.payloads.InstructorDto;
import com.skillforge.platform.services.InstructorService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v0/instructor")
public class InstructorController {
    private InstructorService instructorService;

    @GetMapping("/get/{userId}")
    ResponseEntity<ApiResponseObject> getInstructorProfile(@PathVariable String userId){
        return instructorService.getInstructorProfile(userId);
    }
    @PostMapping("/create/{userId}")
    ResponseEntity<ApiResponseObject> createInstructorProfile(@PathVariable String userId, @RequestBody InstructorDto instructorDto){
        return instructorService.createInstructorProfile(userId,instructorDto);
    }

    @PutMapping("/edit/{instructorId}")
    ResponseEntity<ApiResponseObject> editInstructorProfile(@PathVariable String instructorId,@RequestBody InstructorDto instructorDto){
        return instructorService.editInstructorProfile(instructorId,instructorDto);
    }
}
