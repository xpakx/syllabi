package io.github.xpax.syllabi.controller;

import io.github.xpax.syllabi.entity.Course;
import io.github.xpax.syllabi.entity.CourseYear;
import io.github.xpax.syllabi.entity.dto.*;
import io.github.xpax.syllabi.service.CourseService;
import io.github.xpax.syllabi.service.CourseYearService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/courses")
public class CourseController {
    public final CourseService courseService;
    private final CourseYearService courseYearService;

    @Autowired
    public CourseController(CourseService courseService, CourseYearService courseYearService) {
        this.courseService = courseService;
        this.courseYearService = courseYearService;
    }

    @GetMapping
    public ResponseEntity<Page<CourseForPage>> getAllCourses(@RequestParam Optional<Integer> page,
                                                             @RequestParam Optional<Integer> size) {
        return new ResponseEntity<>(
                courseService.getAllCourses(page.orElse(0), size.orElse(20)),
                HttpStatus.OK
        );
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<CourseDetails> getCourseById(@PathVariable Integer courseId) {
        return new ResponseEntity<>(courseService.getCourse(courseId), HttpStatus.OK);
    }

    @Secured("ROLE_COURSE_ADMIN")
    @PostMapping
    public ResponseEntity<Course> addNewCourse(@RequestBody NewCourseRequest courseRequest) {
        return new ResponseEntity<>(courseService.addNewCourse(courseRequest), HttpStatus.CREATED);
    }

    @Secured("ROLE_COURSE_ADMIN")
    @PutMapping("/{courseId}")
    public ResponseEntity<Course> editCourse(@RequestBody UpdateCourseRequest courseRequest,
                                             @PathVariable Integer courseId) {
        return new ResponseEntity<>(courseService.updateCourse(courseRequest, courseId), HttpStatus.OK);
    }

    @Secured("ROLE_COURSE_ADMIN")
    @DeleteMapping("/{courseId}")
    public ResponseEntity<?> deleteCourse(@PathVariable Integer courseId) {
        courseService.deleteCourse(courseId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Secured("ROLE_COURSE_ADMIN")
    @PostMapping("/{courseId}/years")
    public ResponseEntity<CourseYear> addNewYearToCourse(@RequestBody CourseYearRequest yearRequest,
                                                       @PathVariable Integer courseId) {
        return new ResponseEntity<>(courseYearService.addNewCourseYear(courseId, yearRequest), HttpStatus.CREATED);
    }

}
