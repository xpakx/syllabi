package io.github.xpax.syllabi.controller;

import io.github.xpax.syllabi.entity.dto.StudyGroupDetails;
import io.github.xpax.syllabi.service.StudyGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
