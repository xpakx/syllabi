package io.github.xpax.syllabi.service;

import io.github.xpax.syllabi.entity.dto.LiteratureForPage;
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
}
