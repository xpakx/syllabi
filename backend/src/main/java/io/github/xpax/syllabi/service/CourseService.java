package io.github.xpax.syllabi.service;

import io.github.xpax.syllabi.entity.Course;
import io.github.xpax.syllabi.entity.Institute;
import io.github.xpax.syllabi.entity.dto.*;
import io.github.xpax.syllabi.error.NotFoundException;
import io.github.xpax.syllabi.repo.CourseRepository;
import io.github.xpax.syllabi.repo.InstituteRepository;
import io.github.xpax.syllabi.repo.ProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final InstituteRepository instituteRepository;
    private final ProgramRepository programRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository, InstituteRepository instituteRepository, ProgramRepository programRepository) {
        this.courseRepository = courseRepository;
        this.instituteRepository = instituteRepository;
        this.programRepository = programRepository;
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

    public Course updateCourse(UpdateCourseRequest courseRequest, Integer courseId) {
        Institute institute = getInstitute(courseRequest);
        Course courseToUpdate = buildCourse(courseRequest, institute)
                .id(courseId)
                .prerequisites(getPrerequisites(courseRequest)
                        .stream()
                        .map(courseRepository::getOne)
                        .collect(Collectors.toSet()))
                .programs(getPrograms(courseRequest)
                        .stream()
                        .map(programRepository::getOne)
                        .collect(Collectors.toSet()))
                .build();
        return courseRepository.save(courseToUpdate);
    }

    public void deleteCourse(Integer courseId) {
        courseRepository.deleteById(courseId);
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

    private List<Integer> getPrograms(UpdateCourseRequest courseRequest) {
        if(courseRequest.getPrograms() != null)
            return courseRequest.getPrograms();
        else
            return new ArrayList<>();
    }

    private List<Integer> getPrerequisites(UpdateCourseRequest courseRequest) {
        if(courseRequest.getPrerequisites() != null)
            return courseRequest.getPrerequisites();
        else
            return new ArrayList<>();
    }

}
