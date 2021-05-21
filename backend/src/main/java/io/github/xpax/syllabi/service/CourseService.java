package io.github.xpax.syllabi.service;

import io.github.xpax.syllabi.entity.Course;
import io.github.xpax.syllabi.entity.Institute;
import io.github.xpax.syllabi.entity.dto.CourseDetails;
import io.github.xpax.syllabi.entity.dto.CourseForPage;
import io.github.xpax.syllabi.entity.dto.CourseRequest;
import io.github.xpax.syllabi.entity.dto.NewCourseRequest;
import io.github.xpax.syllabi.error.NotFoundException;
import io.github.xpax.syllabi.repo.CourseRepository;
import io.github.xpax.syllabi.repo.InstituteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final InstituteRepository instituteRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository, InstituteRepository instituteRepository) {
        this.courseRepository = courseRepository;
        this.instituteRepository = instituteRepository;
    }

    public Page<CourseForPage> getAllCourses(Integer page, Integer size) {
        return courseRepository.findAllProjectedBy(PageRequest.of(page, size), CourseForPage.class);
    }

    public CourseDetails getCourse(Integer courseId) {
        return courseRepository.findProjectedById(courseId, CourseDetails.class)
                .orElseThrow( () -> new NotFoundException("Course with id " + courseId + " does not exist"));
    }

    public Course addNewCourse(NewCourseRequest courseRequest) {
        Institute institute = getInstitute(courseRequest);
        Course courseToAdd = buildCourse(courseRequest, institute).build();
        return courseRepository.save(courseToAdd);
    }





    private Institute getInstitute(CourseRequest courseRequest) {
        if(courseRequest.getOrganizerId() != null)
            return instituteRepository.getOne(courseRequest.getOrganizerId());
        else
            return null;
    }

    private Course.CourseBuilder buildCourse(CourseRequest courseRequest, Institute institute) {
        return Course.builder()
                .courseCode(courseRequest.getCourseCode())
                .iscedCode(courseRequest.getIscedCode())
                .erasmusCode(courseRequest.getErasmusCode())
                .name(courseRequest.getName())
                .ects(courseRequest.getEcts())
                .language(courseRequest.getLanguage())
                .facultative(courseRequest.getFacultative())
                .stationary(courseRequest.getStationary())
                .shortDescription(courseRequest.getShortDescription())
                .description(courseRequest.getDescription())
                .assessmentRules(courseRequest.getAssessmentRules())
                .effects(courseRequest.getEffects())
                .organizer(institute)
                .requirements(courseRequest.getRequirements());
    }


}
