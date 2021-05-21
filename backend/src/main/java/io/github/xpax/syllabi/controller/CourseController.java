package io.github.xpax.syllabi.controller;

import io.github.xpax.syllabi.entity.dto.CourseForPage;
import io.github.xpax.syllabi.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/courses")
public class CourseController {
    public final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public ResponseEntity<Page<CourseForPage>> getAllCourses(@RequestParam Optional<Integer> page,
                                                             @RequestParam Optional<Integer> size) {
        return new ResponseEntity<>(
                courseService.getAllCourses(page.orElse(0), size.orElse(20)),
                HttpStatus.OK
        );
    }
}
