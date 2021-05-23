package io.github.xpax.syllabi.service;

import io.github.xpax.syllabi.entity.dto.InstituteDetails;
import io.github.xpax.syllabi.entity.dto.InstituteForPage;
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
}
