package io.github.xpax.syllabi.controller;

import io.github.xpax.syllabi.entity.CourseLiterature;
import io.github.xpax.syllabi.entity.GroupLiterature;
import io.github.xpax.syllabi.entity.dto.LiteratureForPage;
import io.github.xpax.syllabi.entity.dto.LiteratureRequest;
import io.github.xpax.syllabi.service.CourseLiteratureService;
import io.github.xpax.syllabi.service.GroupLiteratureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @Secured("ROLE_COURSE_ADMIN")
    @PostMapping("/courses/{courseId}/literature")
    public ResponseEntity<CourseLiterature> addNewLiterature(@RequestBody LiteratureRequest literatureRequest,
                                                             @PathVariable Integer courseId) {
        return new ResponseEntity<>(
                courseLiteratureService.addNewLiterature(literatureRequest, courseId),
                HttpStatus.CREATED
        );
    }

    @Secured("ROLE_COURSE_ADMIN")
    @PutMapping("/courses/literature/{literatureId}")
    public ResponseEntity<CourseLiterature> updateLiterature(@RequestBody LiteratureRequest literatureRequest,
                                                             @PathVariable Integer literatureId) {
        return new ResponseEntity<>(
                courseLiteratureService.updateLiterature(literatureRequest, literatureId),
                HttpStatus.OK
        );
    }

    @GetMapping("groups/{groupId}/literature")
    public ResponseEntity<Page<LiteratureForPage>> getAllLiteratureByGroup(@RequestParam Optional<Integer> page,
                                                                           Optional<Integer> size,
                                                                           @PathVariable Integer groupId) {
        return new ResponseEntity<>(
                groupLiteratureService.getAllLiterature(page.orElse(0), size.orElse(20), groupId),
                HttpStatus.OK
        );
    }

    @Secured("ROLE_COURSE_ADMIN")
    @DeleteMapping("/groups/literature/{literatureId}")
    public ResponseEntity<?> deleteGroupLiterature(@PathVariable Integer literatureId) {
        groupLiteratureService.deleteLiterature(literatureId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/groups/literature/{literatureId}")
    public ResponseEntity<GroupLiterature> getGroupLiterature(@PathVariable Integer literatureId) {
        return new ResponseEntity<>(groupLiteratureService.getLiterature(literatureId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_COURSE_ADMIN') or " +
            "@permissionEvaluator.canEditStudyGroup(#groupId, authentication.principal.username)")
    @PostMapping("/groups/{groupId}/literature")
    public ResponseEntity<GroupLiterature> addNewGroupLiterature(@RequestBody LiteratureRequest literatureRequest,
                                                                 @PathVariable Integer groupId) {
        return new ResponseEntity<>(
                groupLiteratureService.addNewLiterature(literatureRequest, groupId),
                HttpStatus.CREATED
        );
    }
}
