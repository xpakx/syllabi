package io.github.xpax.syllabi.controller;

import io.github.xpax.syllabi.entity.Semester;
import io.github.xpax.syllabi.entity.dto.CourseForPage;
import io.github.xpax.syllabi.entity.dto.SemesterRequest;
import io.github.xpax.syllabi.entity.dto.SemesterSummary;
import io.github.xpax.syllabi.service.CourseService;
import io.github.xpax.syllabi.service.SemesterService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/semesters")
public class SemesterController {
    private final SemesterService semesterService;
    private final CourseService courseService;

    public SemesterController(SemesterService semesterService, CourseService courseServiceService) {
        this.semesterService = semesterService;
        this.courseService = courseServiceService;
    }

    @GetMapping("/{semesterId}")
    public ResponseEntity<SemesterSummary> getSemester(@PathVariable Integer semesterId) {
        return new ResponseEntity<>(semesterService.getSemester(semesterId), HttpStatus.OK);
    }

    @Secured("ROLE_COURSE_ADMIN")
    @DeleteMapping("/{semesterId}")
    public ResponseEntity<?> deleteStudyGroup(@PathVariable Integer semesterId) {
        semesterService.deleteSemester(semesterId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Secured("ROLE_COURSE_ADMIN")
    @PutMapping("/{semesterId}")
    public ResponseEntity<Semester> editStudyGroup(@RequestBody SemesterRequest semesterRequest,
                                                     @PathVariable Integer semesterId) {
        return new ResponseEntity<>(semesterService.updateSemester(semesterRequest, semesterId), HttpStatus.OK);
    }

    @GetMapping("/{semesterId}/courses")
    public ResponseEntity<Page<CourseForPage>> getAllCoursesForSemester(@RequestParam Optional<Integer> page,
                                                                       @RequestParam Optional<Integer> size,
                                                                       @PathVariable Integer semesterId) {
        return new ResponseEntity<>(
                courseService.getAllCoursesBySemesterId(page.orElse(0), size.orElse(20), semesterId),
                HttpStatus.OK
        );
    }
}
