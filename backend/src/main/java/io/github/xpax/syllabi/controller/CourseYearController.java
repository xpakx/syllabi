package io.github.xpax.syllabi.controller;

import io.github.xpax.syllabi.entity.dto.CourseYearDetails;
import io.github.xpax.syllabi.service.CourseYearService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/years")
public class CourseYearController {
    private final CourseYearService courseYearService;

    @Autowired
    public CourseYearController(CourseYearService courseYearService) {
        this.courseYearService = courseYearService;
    }

    @GetMapping("/{yearId}")
    public ResponseEntity<CourseYearDetails> getCourseYear(@PathVariable Integer yearId) {
        return new ResponseEntity<>(courseYearService.getCourseYear(yearId), HttpStatus.OK);
    }
}
