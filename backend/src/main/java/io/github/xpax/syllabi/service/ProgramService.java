package io.github.xpax.syllabi.service;

import io.github.xpax.syllabi.entity.Course;
import io.github.xpax.syllabi.entity.Institute;
import io.github.xpax.syllabi.entity.Program;
import io.github.xpax.syllabi.entity.dto.ProgramDetails;
import io.github.xpax.syllabi.entity.dto.ProgramRequest;
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
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProgramService {
    private final ProgramRepository programRepository;
    private final InstituteRepository instituteRepository;

    public ProgramService(ProgramRepository programRepository, InstituteRepository instituteRepository) {
        this.programRepository = programRepository;
        this.instituteRepository = instituteRepository;
    }

    public Program addNewProgram(ProgramRequest programRequest) {
        Institute institute = getInstitute(programRequest);
        Program programToAdd = buildProgram(institute, programRequest.getName()).description(programRequest.getDescription()).build();
        return programRepository.save(programToAdd);
    }

    public void deleteProgram(Integer programId) {
        programRepository.deleteById(programId);
    }

    public ProgramDetails getProgram(Integer programId) {
        return programRepository.findProjectedById(programId)
                .orElseThrow(() -> new NotFoundException("No program with id "+programId+" found!"));
    }

    public Program updateProgram(ProgramRequest programRequest, Integer programId) {
        Institute institute = getInstitute(programRequest);
        Program programToUpdate = programRepository.findById(programId)
                .orElseThrow(() -> new NotFoundException("No program with id "+programId+" found!"));

        programToUpdate.setDescription(programRequest.getDescription());
        programToUpdate.setOrganizer(institute);
        programToUpdate.setName(programRequest.getName());
        return programRepository.save(programToUpdate);
    }

    public Page<Program> getAllPrograms(Integer page, Integer size) {
        return programRepository.findAll(PageRequest.of(page, size));
    }

    private Institute getInstitute(ProgramRequest programRequest) {
        if(programRequest.getOrganizerId() != null)
            return instituteRepository.getOne(programRequest.getOrganizerId());
        else
            return null;
    }

    private Program.ProgramBuilder buildProgram(Institute institute, String name) {
        return Program.builder()
                .organizer(institute)
                .name(name);
    }
}
