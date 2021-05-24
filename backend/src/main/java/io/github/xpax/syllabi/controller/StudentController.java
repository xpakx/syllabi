package io.github.xpax.syllabi.controller;

import io.github.xpax.syllabi.entity.Student;
import io.github.xpax.syllabi.entity.dto.StudentWithUserId;
import io.github.xpax.syllabi.entity.dto.UserToStudentRequest;
import io.github.xpax.syllabi.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
public class StudentController {
    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @Secured("ROLE_USER_ADMIN")
    @PostMapping("/users/{userId}/student")
    public ResponseEntity<Student> createStudent(@RequestBody UserToStudentRequest request,
                                                 @PathVariable Integer userId) {
        return new ResponseEntity<>(studentService.createStudent(request, userId), HttpStatus.CREATED);
    }

    @GetMapping("/users/{userId}/student")
    public ResponseEntity<StudentWithUserId> getStudent(@PathVariable Integer userId) {
        return new ResponseEntity<>(studentService.getStudent(userId), HttpStatus.OK);
    }
}
