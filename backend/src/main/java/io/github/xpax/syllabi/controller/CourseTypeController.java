package io.github.xpax.syllabi.controller;

import io.github.xpax.syllabi.entity.CourseType;
import io.github.xpax.syllabi.entity.dto.CourseTypeRequest;
import io.github.xpax.syllabi.service.CourseTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/types")
public class CourseTypeController {
    private final CourseTypeService courseTypeService;

    @Autowired
    public CourseTypeController(CourseTypeService courseTypeService) {
        this.courseTypeService = courseTypeService;
    }

    @Secured("ROLE_COURSE_ADMIN")
    @PostMapping
    public ResponseEntity<CourseType> addNewCourseType(@RequestBody CourseTypeRequest courseTypeRequest) {
        return new ResponseEntity<>(courseTypeService.addNewCourseType(courseTypeRequest), HttpStatus.CREATED);
    }

    @GetMapping("/{typeId}")
    public ResponseEntity<CourseType> getCourseType(@PathVariable Integer typeId) {
        return new ResponseEntity<>(courseTypeService.getCourseType(typeId), HttpStatus.OK);
    }

    @Secured("ROLE_COURSE_ADMIN")
    @DeleteMapping("/{typeId}")
    public ResponseEntity<?> deleteCourseType(@PathVariable Integer typeId) {
        courseTypeService.deleteCourseType(typeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Secured("ROLE_COURSE_ADMIN")
    @PutMapping("/{typeId}")
    public ResponseEntity<CourseType> editCourseType(@RequestBody CourseTypeRequest typeRequest,
                                                     @PathVariable Integer typeId) {
        return new ResponseEntity<>(courseTypeService.updateCourseType(typeRequest, typeId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<CourseType>> getAllTypes(@RequestParam Optional<Integer> page,
                                                        @RequestParam Optional<Integer> size) {
        return new ResponseEntity<>(
                courseTypeService.getAllCourseTypes(page.orElse(0), size.orElse(20)),
                HttpStatus.OK
        );
    }
}
