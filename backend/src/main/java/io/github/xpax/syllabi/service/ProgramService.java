package io.github.xpax.syllabi.service;

import io.github.xpax.syllabi.entity.Course;
import io.github.xpax.syllabi.entity.Institute;
import io.github.xpax.syllabi.entity.Program;
import io.github.xpax.syllabi.entity.dto.ProgramRequest;
import io.github.xpax.syllabi.repo.CourseRepository;
import io.github.xpax.syllabi.repo.InstituteRepository;
import io.github.xpax.syllabi.repo.ProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProgramService {
    private final CourseRepository courseRepository;
    private final ProgramRepository programRepository;
    private final InstituteRepository instituteRepository;

    public ProgramService(CourseRepository courseRepository, ProgramRepository programRepository, InstituteRepository instituteRepository) {
        this.courseRepository = courseRepository;
        this.programRepository = programRepository;
        this.instituteRepository = instituteRepository;
    }

    public Program addNewProgram(ProgramRequest programRequest) {
        Institute institute = getInstitute(programRequest);
        Set<Course> courseSet = getCourseSet(programRequest);
        Program programToAdd = buildProgram(institute, courseSet, programRequest.getName()).description(programRequest.getDescription()).build();
        return programRepository.save(programToAdd);
    }


    private Institute getInstitute(ProgramRequest programRequest) {
        if(programRequest.getOrganizerId() != null)
            return instituteRepository.getOne(programRequest.getOrganizerId());
        else
            return null;
    }

    private Set<Course> getCourseSet(ProgramRequest programRequest) {
        return getCourses(programRequest)
                .stream()
                .map(courseRepository::getOne)
                .collect(Collectors.toSet());
    }

    private List<Integer> getCourses(ProgramRequest programRequest) {
        if(programRequest.getCoursesId() != null)
            return programRequest.getCoursesId();
        else
            return new ArrayList<>();
    }

    private Program.ProgramBuilder buildProgram(Institute institute, Set<Course> courseSet, String name) {
        return Program.builder()
                .organizer(institute)
                .name(name)
                .courses(courseSet);
    }
}
