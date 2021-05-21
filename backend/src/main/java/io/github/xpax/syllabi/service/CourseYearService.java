package io.github.xpax.syllabi.service;

import io.github.xpax.syllabi.entity.Course;
import io.github.xpax.syllabi.entity.CourseYear;
import io.github.xpax.syllabi.entity.Teacher;
import io.github.xpax.syllabi.entity.dto.CourseYearForPage;
import io.github.xpax.syllabi.entity.dto.CourseYearRequest;
import io.github.xpax.syllabi.repo.CourseRepository;
import io.github.xpax.syllabi.repo.CourseYearRepository;
import io.github.xpax.syllabi.repo.TeacherRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CourseYearService {
    private final CourseRepository courseRepository;
    private final CourseYearRepository courseYearRepository;
    private final TeacherRepository teacherRepository;

    public CourseYearService(CourseRepository courseRepository, CourseYearRepository courseYearRepository,
                             TeacherRepository teacherRepository) {
        this.courseRepository = courseRepository;
        this.courseYearRepository = courseYearRepository;
        this.teacherRepository = teacherRepository;
    }

    public CourseYear addNewCourseYear(Integer parentId, CourseYearRequest yearRequest) {
        Course parent = courseRepository.getOne(parentId);
        Set<Teacher> teachers = getTeacherSet(yearRequest);
        CourseYear courseYearToAdd = buildCourseYear(yearRequest, parent, teachers)
                .build();
        return courseYearRepository.save(courseYearToAdd);
    }



    private Set<Teacher> getTeacherSet(CourseYearRequest yearRequest) {
        return getCoordinators(yearRequest)
                .stream()
                .map(teacherRepository::getOne)
                .collect(Collectors.toSet());
    }

    private List<Integer> getCoordinators(CourseYearRequest yearRequest) {
        if(yearRequest.getCoordinators() != null)
            return yearRequest.getCoordinators();
        else
            return new ArrayList<>();
    }

    private CourseYear.CourseYearBuilder buildCourseYear(CourseYearRequest yearRequest, Course parent, Set<Teacher> teachers) {
        return CourseYear.builder()
                .parent(parent)
                .coordinatedBy(teachers)
                .assessmentRules(yearRequest.getAssessmentRules())
                .description(yearRequest.getDescription())
                .commentary(yearRequest.getCommentary())
                .startDate(yearRequest.getStartDate())
                .endDate(yearRequest.getEndDate());
    }

    public Page<CourseYearForPage> getYearsForCourse(Integer courseId, Integer page, Integer size) {
        return courseYearRepository.findAllByParentId(courseId, PageRequest.of(page, size));
    }
}
