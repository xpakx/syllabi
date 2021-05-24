package io.github.xpax.syllabi.controller;

import io.github.xpax.syllabi.entity.Student;
import io.github.xpax.syllabi.entity.dto.StudentWithUserId;
import io.github.xpax.syllabi.entity.dto.UpdateStudentRequest;
import io.github.xpax.syllabi.entity.dto.UserToStudentRequest;
import io.github.xpax.syllabi.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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

    @Secured("ROLE_USER_ADMIN")
    @DeleteMapping("/users/{userId}/student")
    public ResponseEntity<?> deleteStudent(@PathVariable Integer userId) {
        studentService.deleteStudent(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Secured("ROLE_USER_ADMIN")
    @PutMapping("/users/{userId}/student")
    public ResponseEntity<Student> updateStudent(@RequestBody UpdateStudentRequest request,
                                                 @PathVariable Integer userId) {
        return new ResponseEntity<>(studentService.updateStudent(request, userId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_COURSE_ADMIN') or " +
            "@permissionEvaluator.canViewStudyGroup(#groupId, authentication.principal.username)")
    @GetMapping("/groups/{groupId}/students")
    public ResponseEntity<Page<StudentWithUserId>> getStudentsForStudyGroup(@PathVariable Integer groupId,
                                                                            @RequestParam Optional<Integer> page,
                                                                            @RequestParam Optional<Integer> size) {
        return new ResponseEntity<>(
                studentService.getGroupStudents(groupId, page.orElse(0), size.orElse(20)),
                HttpStatus.OK
        );
    }
}
