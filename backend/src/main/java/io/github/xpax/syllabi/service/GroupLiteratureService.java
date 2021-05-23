package io.github.xpax.syllabi.service;

import io.github.xpax.syllabi.entity.dto.LiteratureForPage;
import io.github.xpax.syllabi.repo.GroupLiteratureRepository;
import io.github.xpax.syllabi.repo.StudyGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class GroupLiteratureService {
    private final GroupLiteratureRepository groupLiteratureRepository;
    private final StudyGroupRepository studyGroupRepository;

    @Autowired
    public GroupLiteratureService(GroupLiteratureRepository groupLiteratureRepository, StudyGroupRepository studyGroupRepository) {
        this.groupLiteratureRepository = groupLiteratureRepository;
        this.studyGroupRepository = studyGroupRepository;
    }

    public Page<LiteratureForPage> getAllLiterature(Integer page, Integer size, Integer groupId) {
        return groupLiteratureRepository.findAllByStudyGroupId(groupId, PageRequest.of(page, size));
    }
}
