package io.github.xpax.syllabi.service;

import io.github.xpax.syllabi.entity.GroupLiterature;
import io.github.xpax.syllabi.entity.StudyGroup;
import io.github.xpax.syllabi.entity.dto.LiteratureForPage;
import io.github.xpax.syllabi.entity.dto.LiteratureRequest;
import io.github.xpax.syllabi.error.NotFoundException;
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

    public void deleteLiterature(Integer literatureId) {
        groupLiteratureRepository.deleteById(literatureId);
    }

    public GroupLiterature getLiterature(Integer literatureId) {
        return groupLiteratureRepository.findById(literatureId)
                .orElseThrow(() -> new NotFoundException("No literature with id "+literatureId+ " found!"));
    }

    public GroupLiterature addNewLiterature(LiteratureRequest literatureRequest, Integer groupId) {
        StudyGroup group = studyGroupRepository.getOne(groupId);
        GroupLiterature groupLiteratureToAdd = GroupLiterature.builder()
                .author(literatureRequest.getAuthor())
                .title(literatureRequest.getTitle())
                .edition(literatureRequest.getEdition())
                .pages(literatureRequest.getPages())
                .description(literatureRequest.getDescription())
                .studyGroup(group)
                .obligatory(literatureRequest.getObligatory())
                .build();
        return groupLiteratureRepository.save(groupLiteratureToAdd);
    }
}
