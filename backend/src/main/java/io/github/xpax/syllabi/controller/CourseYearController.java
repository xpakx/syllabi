package io.github.xpax.syllabi.controller;

import io.github.xpax.syllabi.entity.CourseYear;
import io.github.xpax.syllabi.entity.StudyGroup;
import io.github.xpax.syllabi.entity.dto.CourseYearDetails;
import io.github.xpax.syllabi.entity.dto.CourseYearRequest;
import io.github.xpax.syllabi.entity.dto.StudyGroupForPage;
import io.github.xpax.syllabi.entity.dto.StudyGroupRequest;
import io.github.xpax.syllabi.service.CourseYearService;
import io.github.xpax.syllabi.service.StudyGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/years")
public class CourseYearController {
    private final CourseYearService courseYearService;
    private final StudyGroupService studyGroupService;

    @Autowired
    public CourseYearController(CourseYearService courseYearService, StudyGroupService studyGroupService) {
        this.courseYearService = courseYearService;
        this.studyGroupService = studyGroupService;
    }

    @GetMapping("/{yearId}")
    public ResponseEntity<CourseYearDetails> getCourseYear(@PathVariable Integer yearId) {
        return new ResponseEntity<>(courseYearService.getCourseYear(yearId), HttpStatus.OK);
    }

    @Secured("ROLE_COURSE_ADMIN")
    @DeleteMapping("/{yearId}")
    public ResponseEntity<?> deleteCourseYear(@PathVariable Integer yearId) {
        courseYearService.deleteCourseYear(yearId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_COURSE_ADMIN') or " +
            "@permissionEvaluator.canEditCourseYear(#yearId, authentication.principal.username)")
    @PutMapping("/{yearId}")
    public ResponseEntity<CourseYear> editCourseYear(@RequestBody @Valid CourseYearRequest yearRequest,
                                                     @PathVariable Integer yearId) {
        return new ResponseEntity<>(
                courseYearService.updateCourseYear(yearRequest, yearId),
                HttpStatus.OK
        );
    }

    @Secured("ROLE_COURSE_ADMIN")
    @PostMapping("/{yearId}/groups")
    public ResponseEntity<StudyGroup> addNewStudyGroup(@RequestBody StudyGroupRequest studyGroupRequest,
                                                       @PathVariable Integer yearId) {
        return new ResponseEntity<>(
                studyGroupService.addNewStudyGroup(studyGroupRequest, yearId),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{yearId}/groups")
    public ResponseEntity<Page<StudyGroupForPage>> getAllGroupsForYear(@PathVariable Integer yearId,
                                                                       @RequestParam Optional<Integer> page,
                                                                       @RequestParam Optional<Integer> size) {
        return new ResponseEntity<>(
                studyGroupService.getAllGroupsByCourseYear(yearId, page.orElse(0), size.orElse(20)),
                HttpStatus.OK
        );
    }
}
