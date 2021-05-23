package io.github.xpax.syllabi.service;

import io.github.xpax.syllabi.entity.Institute;
import io.github.xpax.syllabi.entity.dto.CourseForPage;
import io.github.xpax.syllabi.entity.dto.InstituteDetails;
import io.github.xpax.syllabi.entity.dto.InstituteForPage;
import io.github.xpax.syllabi.entity.dto.InstituteRequest;
import io.github.xpax.syllabi.error.NotFoundException;
import io.github.xpax.syllabi.repo.CourseRepository;
import io.github.xpax.syllabi.repo.InstituteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class InstituteService {
    private final InstituteRepository instituteRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public InstituteService(InstituteRepository instituteRepository, CourseRepository courseRepository) {
        this.instituteRepository = instituteRepository;
        this.courseRepository = courseRepository;
    }

    public Page<InstituteForPage> getAllInstitutes(Integer page, Integer size) {
        return instituteRepository.findProjectedBy(PageRequest.of(page, size));
    }

    public void deleteInstitute(Integer instituteId) {
        instituteRepository.deleteById(instituteId);
    }

    public InstituteDetails getInstitute(Integer instituteId) {
        return instituteRepository.findProjectedById(instituteId)
                .orElseThrow(() -> new NotFoundException("Institute with id "+instituteId+" not found!"));
    }

    public Institute addNewInstitute(InstituteRequest instituteRequest) {
        Institute parent = getParentInstitute(instituteRequest);

        Institute instituteToAdd = buildInstitute(instituteRequest, parent)
                .build();
        return instituteRepository.save(instituteToAdd);
    }

    public Institute updateInstitute(InstituteRequest instituteRequest, Integer instituteId) {
        Institute parent = getParentInstitute(instituteRequest);
        Institute institute = Institute.builder()
                .id(instituteId)
                .parent(parent)
                .code(instituteRequest.getCode())
                .name(instituteRequest.getName())
                .url(instituteRequest.getUrl())
                .phone(instituteRequest.getPhone())
                .address(instituteRequest.getAddress())
                .build();
        return instituteRepository.save(institute);
    }

    public Page<CourseForPage> getAllCoursesByOrganizerId(Integer page, Integer size, Integer instituteId) {
        return courseRepository.findByOrganizerId(instituteId, PageRequest.of(page, size));
    }

    private Institute getParentInstitute(InstituteRequest instituteRequest) {
        Institute parent = null;
        if(instituteRequest.getParentId() != null) {
            parent = instituteRepository.getOne(instituteRequest.getParentId());
        }
        return parent;
    }

    private Institute.InstituteBuilder buildInstitute(InstituteRequest instituteRequest, Institute parent) {
        return Institute.builder()
                .code(instituteRequest.getCode())
                .name(instituteRequest.getName())
                .url(instituteRequest.getUrl())
                .phone(instituteRequest.getPhone())
                .address(instituteRequest.getAddress())
                .parent(parent);
    }
}
