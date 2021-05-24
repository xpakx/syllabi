package io.github.xpax.syllabi.controller;

import io.github.xpax.syllabi.entity.StudyGroup;
import io.github.xpax.syllabi.entity.dto.StudyGroupDetails;
import io.github.xpax.syllabi.entity.dto.StudyGroupRequest;
import io.github.xpax.syllabi.service.StudyGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/groups")
public class StudyGroupController {
    private final StudyGroupService studygroupService;

    @Autowired
    public StudyGroupController(StudyGroupService studygroupService) {
        this.studygroupService = studygroupService;
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<StudyGroupDetails> getStudyGroup(@PathVariable Integer groupId) {
        return new ResponseEntity<>(studygroupService.getStudyGroup(groupId), HttpStatus.OK);
    }

    @Secured("ROLE_COURSE_ADMIN")
    @DeleteMapping("/{groupId}")
    public ResponseEntity<?> deleteStudyGroup(@PathVariable Integer groupId) {
        studygroupService.deleteStudyGroup(groupId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_COURSE_ADMIN') or " +
            "@permissionEvaluator.canEditStudyGroup(#groupId, authentication.principal.username)")
    @PutMapping("/{groupId}")
    public ResponseEntity<StudyGroup> editStudyGroup(@RequestBody StudyGroupRequest groupRequest,
                                                     @PathVariable Integer groupId) {
        return new ResponseEntity<>(studygroupService.updateStudyGroup(groupRequest, groupId), HttpStatus.OK);
    }
}
