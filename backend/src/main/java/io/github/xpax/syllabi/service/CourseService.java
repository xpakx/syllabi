package io.github.xpax.syllabi.service;

import io.github.xpax.syllabi.entity.dto.CourseDetails;
import io.github.xpax.syllabi.entity.dto.CourseForPage;
import io.github.xpax.syllabi.error.NotFoundException;
import io.github.xpax.syllabi.repo.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class CourseService {
    private final CourseRepository courseRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public Page<CourseForPage> getAllCourses(Integer page, Integer size) {
        return courseRepository.findAllProjectedBy(PageRequest.of(page, size), CourseForPage.class);
    }

    public CourseDetails getCourse(Integer courseId) {
        return courseRepository.findProjectedById(courseId, CourseDetails.class)
                .orElseThrow( () -> new NotFoundException("Course with id " + courseId + " does not exist"));
    }
}
