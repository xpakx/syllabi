package io.github.xpax.syllabi.service;

import io.github.xpax.syllabi.entity.CourseType;
import io.github.xpax.syllabi.entity.CourseYear;
import io.github.xpax.syllabi.entity.StudyGroup;
import io.github.xpax.syllabi.entity.Teacher;
import io.github.xpax.syllabi.entity.dto.StudyGroupForPage;
import io.github.xpax.syllabi.entity.dto.StudyGroupRequest;
import io.github.xpax.syllabi.repo.CourseTypeRepository;
import io.github.xpax.syllabi.repo.CourseYearRepository;
import io.github.xpax.syllabi.repo.StudyGroupRepository;
import io.github.xpax.syllabi.repo.TeacherRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StudyGroupService {
    private final CourseYearRepository courseYearRepository;
    private final CourseTypeRepository courseTypeRepository;
    private final TeacherRepository teacherRepository;
    private final StudyGroupRepository studyGroupRepository;

    public StudyGroupService(CourseYearRepository courseYearRepository, CourseTypeRepository courseTypeRepository,
                             TeacherRepository teacherRepository, StudyGroupRepository studyGroupRepository) {
        this.courseYearRepository = courseYearRepository;
        this.courseTypeRepository = courseTypeRepository;
        this.teacherRepository = teacherRepository;
        this.studyGroupRepository = studyGroupRepository;
    }

    public StudyGroup addNewStudyGroup(StudyGroupRequest studyGroupRequest, Integer yearId) {
        CourseYear year = courseYearRepository.getOne(yearId);
        CourseType type = getCourseType(studyGroupRequest);
        Set<Teacher> teachers = getTeacherSet(studyGroupRequest);
        StudyGroup studyGroupToAdd = buildStudyGroup(studyGroupRequest, year, type, teachers)
                .build();
        return studyGroupRepository.save(studyGroupToAdd);
    }

    public Page<StudyGroupForPage> getAllGroupsByCourseYear(Integer yearId, Integer page, Integer size) {
        return studyGroupRepository.findAllByYearId(yearId, PageRequest.of(page, size));
    }




    private CourseType getCourseType(StudyGroupRequest studyGroupRequest) {
        if(studyGroupRequest.getCourseTypeId() != null)
            return  courseTypeRepository.getOne(studyGroupRequest.getCourseTypeId());
        else
            return null;
    }

    private Set<Teacher> getTeacherSet(StudyGroupRequest studyGroupRequest) {
        return getTeachers(studyGroupRequest)
                .stream()
                .map(teacherRepository::getOne)
                .collect(Collectors.toSet());
    }

    private List<Integer> getTeachers(StudyGroupRequest studyGroupRequest) {
        if(studyGroupRequest.getTeachers() != null)
            return studyGroupRequest.getTeachers();
        else
            return new ArrayList<>();
    }

    private StudyGroup.StudyGroupBuilder buildStudyGroup(StudyGroupRequest studyGroupRequest,
                                                         CourseYear year,
                                                         CourseType type,
                                                         Set<Teacher> teachers) {
        return StudyGroup.builder()
                .description(studyGroupRequest.getDescription())
                .studentLimit(studyGroupRequest.getStudentLimit())
                .ongoing(studyGroupRequest.getOngoing())
                .name(studyGroupRequest.getName())
                .year(year)
                .type(type)
                .teachers(teachers);
    }
}
