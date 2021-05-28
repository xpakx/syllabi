package io.github.xpax.syllabi.service;

import io.github.xpax.syllabi.entity.Semester;
import io.github.xpax.syllabi.entity.dto.SemesterRequest;
import io.github.xpax.syllabi.error.NotFoundException;
import io.github.xpax.syllabi.repo.SemesterRepository;
import org.springframework.stereotype.Service;

@Service
public class SemesterService {
    private final SemesterRepository semesterRepository;

    public SemesterService(SemesterRepository semesterRepository) {
        this.semesterRepository = semesterRepository;
    }

    public Semester getSemester(Integer id) {
        return semesterRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No semester with id "+id+" found!"));
    }

    public void deleteSemester(Integer id) {
        semesterRepository.deleteById(id);
    }

    public Semester updateSemester(SemesterRequest request, Integer id) {
        Semester semester = semesterRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No semester with id "+id+" found!"));
        semester.setName(request.getName());
        semester.setNumber(request.getNumber());
        return semesterRepository.save(semester);
    }
}
