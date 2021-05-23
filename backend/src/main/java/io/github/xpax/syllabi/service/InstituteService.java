package io.github.xpax.syllabi.service;

import io.github.xpax.syllabi.entity.Institute;
import io.github.xpax.syllabi.entity.dto.InstituteDetails;
import io.github.xpax.syllabi.entity.dto.InstituteForPage;
import io.github.xpax.syllabi.entity.dto.InstituteRequest;
import io.github.xpax.syllabi.error.NotFoundException;
import io.github.xpax.syllabi.repo.InstituteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class InstituteService {
    private final InstituteRepository instituteRepository;

    @Autowired
    public InstituteService(InstituteRepository instituteRepository) {
        this.instituteRepository = instituteRepository;
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
