package io.github.xpax.syllabi.controller;

import io.github.xpax.syllabi.entity.Program;
import io.github.xpax.syllabi.entity.dto.CourseForPage;
import io.github.xpax.syllabi.entity.dto.ProgramRequest;
import io.github.xpax.syllabi.service.CourseService;
import io.github.xpax.syllabi.service.ProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/programs")
public class ProgramController {
    private final CourseService courseService;
    private final ProgramService programService;

    @Autowired
    public ProgramController(CourseService courseService, ProgramService programService) {
        this.courseService = courseService;
        this.programService = programService;
    }

    @Secured("ROLE_COURSE_ADMIN")
    @PostMapping
    public ResponseEntity<Program> addNewProgram(@RequestBody ProgramRequest programRequest) {
        return new ResponseEntity<>(programService.addNewProgram(programRequest), HttpStatus.CREATED);
    }

    @GetMapping("/{programId}/courses")
    public ResponseEntity<Page<CourseForPage>> getAllCoursesForProgram(@RequestParam Optional<Integer> page,
                                                                       @RequestParam Optional<Integer> size,
                                                                       @PathVariable Integer programId) {
        return new ResponseEntity<>(
                courseService.getAllCoursesByProgramId(page.orElse(0), size.orElse(20), programId),
                HttpStatus.OK
        );
    }

    @Secured("ROLE_COURSE_ADMIN")
    @DeleteMapping("/{programId}")
    public ResponseEntity<?> deleteProgram(@PathVariable Integer programId) {
        programService.deleteProgram(programId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
