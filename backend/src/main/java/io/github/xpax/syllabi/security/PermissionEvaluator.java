package io.github.xpax.syllabi.security;

import io.github.xpax.syllabi.repo.CourseYearRepository;
import io.github.xpax.syllabi.repo.StudyGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PermissionEvaluator {
    private final StudyGroupRepository studyGroupRepository;
    private final CourseYearRepository courseYearRepository;

    @Autowired
    public PermissionEvaluator(StudyGroupRepository studyGroupRepository, CourseYearRepository courseYearRepository) {
        this.studyGroupRepository = studyGroupRepository;
        this.courseYearRepository = courseYearRepository;
    }

    public boolean canEditStudyGroup(Integer studyGroupId, String userId) {
        return studyGroupRepository.existsTeacherByStudyGroupWithUserId(studyGroupId, Integer.valueOf(userId));
    }

    public boolean canViewStudyGroup(Integer studyGroupId, String userId) {
        return canEditStudyGroup(studyGroupId, userId);
    }

    public boolean canEditCourseYear(Integer courseYearId, String userId) {
        return courseYearRepository.existsTeacherByCourseYearIdAndUserId(courseYearId, Integer.valueOf(userId));
    }

    public boolean canViewCourseYear(Integer courseYearId, String userId) {
        return canEditCourseYear(courseYearId, userId);
    }
}