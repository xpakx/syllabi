package io.github.xpax.syllabi.service;

import io.github.xpax.syllabi.entity.Course;
import io.github.xpax.syllabi.entity.CourseLiterature;
import io.github.xpax.syllabi.entity.dto.LiteratureForPage;
import io.github.xpax.syllabi.entity.dto.LiteratureRequest;
import io.github.xpax.syllabi.error.NotFoundException;
import io.github.xpax.syllabi.repo.CourseLiteratureRepository;
import io.github.xpax.syllabi.repo.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class CourseLiteratureService {
    private final CourseLiteratureRepository courseLiteratureRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public CourseLiteratureService(CourseLiteratureRepository courseLiteratureRepository, CourseRepository courseRepository) {
        this.courseLiteratureRepository = courseLiteratureRepository;
        this.courseRepository = courseRepository;
    }

    public Page<LiteratureForPage> getAllLiterature(Integer page, Integer size, Integer courseId) {
        return courseLiteratureRepository.findAllByCourseId(courseId, PageRequest.of(page, size));
    }

    public void deleteLiterature(Integer literatureId) {
        courseLiteratureRepository.deleteById(literatureId);
    }

    public CourseLiterature getLiterature(Integer literatureId) {
        return courseLiteratureRepository.findById(literatureId)
                .orElseThrow(() -> new NotFoundException("No literature with id "+literatureId+ " found!"));
    }

    public CourseLiterature addNewLiterature(LiteratureRequest literatureRequest, Integer courseId) {
        Course course = courseRepository.getOne(courseId);
        CourseLiterature courseLiteratureToAdd = CourseLiterature.builder()
                .author(literatureRequest.getAuthor())
                .title(literatureRequest.getTitle())
                .edition(literatureRequest.getEdition())
                .pages(literatureRequest.getPages())
                .description(literatureRequest.getDescription())
                .obligatory(literatureRequest.getObligatory())
                .course(course)
                .build();
        return courseLiteratureRepository.save(courseLiteratureToAdd);
    }

    public CourseLiterature updateLiterature(LiteratureRequest literatureRequest, Integer literatureId) {
        CourseLiterature courseLiterature = courseLiteratureRepository.findById(literatureId)
                .orElseThrow(() -> new NotFoundException("No literature with id "+literatureId+ " found!"));
        courseLiterature.setAuthor(literatureRequest.getAuthor());
        courseLiterature.setTitle(literatureRequest.getTitle());
        courseLiterature.setEdition(literatureRequest.getEdition());
        courseLiterature.setPages(literatureRequest.getPages());
        courseLiterature.setDescription(literatureRequest.getDescription());
        courseLiterature.setObligatory(literatureRequest.getObligatory());
        return courseLiteratureRepository.save(courseLiterature);
    }
}
