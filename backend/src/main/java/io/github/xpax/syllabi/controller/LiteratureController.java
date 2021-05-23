package io.github.xpax.syllabi.controller;

import io.github.xpax.syllabi.entity.dto.LiteratureForPage;
import io.github.xpax.syllabi.service.CourseLiteratureService;
import io.github.xpax.syllabi.service.GroupLiteratureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
