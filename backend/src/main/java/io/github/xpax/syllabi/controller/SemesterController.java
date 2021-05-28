package io.github.xpax.syllabi.controller;

import io.github.xpax.syllabi.entity.Semester;
import io.github.xpax.syllabi.entity.dto.StudyGroupDetails;
import io.github.xpax.syllabi.service.SemesterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/semesters")
public class SemesterController {
    private final SemesterService semesterService;

    public SemesterController(SemesterService semesterService) {
        this.semesterService = semesterService;
    }

    @GetMapping("/{semesterId}")
    public ResponseEntity<Semester> getSemester(@PathVariable Integer semesterId) {
        return new ResponseEntity<>(semesterService.getSemester(semesterId), HttpStatus.OK);
    }
}
