package io.github.xpax.syllabi.service;

import io.github.xpax.syllabi.entity.Institute;
import io.github.xpax.syllabi.entity.Program;
import io.github.xpax.syllabi.entity.Semester;
import io.github.xpax.syllabi.entity.dto.ProgramDetails;
import io.github.xpax.syllabi.entity.dto.ProgramRequest;
import io.github.xpax.syllabi.entity.dto.SemesterRequest;
import io.github.xpax.syllabi.error.NotFoundException;
import io.github.xpax.syllabi.repo.InstituteRepository;
import io.github.xpax.syllabi.repo.ProgramRepository;
import io.github.xpax.syllabi.repo.SemesterRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ProgramService {
    private final ProgramRepository programRepository;
    private final InstituteRepository instituteRepository;
    private final SemesterRepository semesterRepository;

    public ProgramService(ProgramRepository programRepository, InstituteRepository instituteRepository, SemesterRepository semesterRepository) {
        this.programRepository = programRepository;
        this.instituteRepository = instituteRepository;
        this.semesterRepository = semesterRepository;
    }

    public Program addNewProgram(ProgramRequest programRequest) {
        Institute institute = getInstitute(programRequest);
        Program programToAdd = buildProgram(institute, programRequest.getName())
                .description(programRequest.getDescription())
                .build();
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

    public Page<Semester> getAllSemesters(Integer programId, Integer page, Integer size) {
        return semesterRepository.findByProgramId(programId, PageRequest.of(page, size));
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

    public Semester addNewSemester(Integer programId, SemesterRequest request) {
        Program program = programRepository.getOne(programId);
        Semester semesterToAdd = Semester.builder()
                .number(request.getNumber())
                .name(request.getName())
                .program(program)
                .build();
        return semesterRepository.save(semesterToAdd);
    }
}
