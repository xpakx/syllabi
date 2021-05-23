package io.github.xpax.syllabi.controller;

import io.github.xpax.syllabi.entity.CourseLiterature;
import io.github.xpax.syllabi.entity.dto.LiteratureForPage;
import io.github.xpax.syllabi.service.CourseLiteratureService;
import io.github.xpax.syllabi.service.GroupLiteratureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class LiteratureController {
    private final CourseLiteratureService courseLiteratureService;
    private final GroupLiteratureService groupLiteratureService;

    @Autowired
    public LiteratureController(CourseLiteratureService courseLiteratureService,
                                GroupLiteratureService groupLiteratureService) {
        this.courseLiteratureService = courseLiteratureService;
        this.groupLiteratureService = groupLiteratureService;
    }

    @GetMapping("/courses/{courseId}/literature")
    public ResponseEntity<Page<LiteratureForPage>> getAllLiteratureByCourse(@RequestParam Optional<Integer> page,
                                                                            Optional<Integer> size,
                                                                            @PathVariable Integer courseId) {
        return new ResponseEntity<>(
                courseLiteratureService.getAllLiterature(page.orElse(0), size.orElse(20), courseId),
                HttpStatus.OK
        );
    }

    @Secured("ROLE_COURSE_ADMIN")
    @DeleteMapping("/courses/literature/{literatureId}")
    public ResponseEntity<?> deleteLiterature(@PathVariable Integer literatureId) {
        courseLiteratureService.deleteLiterature(literatureId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/courses/literature/{literatureId}")
    public ResponseEntity<CourseLiterature> getLiterature(@PathVariable Integer literatureId) {
        return new ResponseEntity<>(courseLiteratureService.getLiterature(literatureId), HttpStatus.OK);
    }
}
