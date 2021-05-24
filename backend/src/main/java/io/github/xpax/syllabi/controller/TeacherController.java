package io.github.xpax.syllabi.controller;

import io.github.xpax.syllabi.entity.Teacher;
import io.github.xpax.syllabi.entity.dto.TeacherDetails;
import io.github.xpax.syllabi.entity.dto.UserToTeacherRequest;
import io.github.xpax.syllabi.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
public class TeacherController {

    private final TeacherService teacherService;

    @Autowired
    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @Secured("ROLE_USER_ADMIN")
    @PostMapping("/users/{userId}/teacher")
    public ResponseEntity<Teacher> addNewTeacher(@RequestBody UserToTeacherRequest request,
                                                 @PathVariable Integer userId) {
        return new ResponseEntity<>(teacherService.createTeacher(request, userId), HttpStatus.CREATED);
    }

    @GetMapping("/users/{userId}/teacher")
    public ResponseEntity<TeacherDetails> getTeacher(@PathVariable Integer userId) {
        return new ResponseEntity<>(teacherService.getTeacher(userId), HttpStatus.OK);
    }
}