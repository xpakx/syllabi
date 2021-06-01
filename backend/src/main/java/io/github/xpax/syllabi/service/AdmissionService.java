package io.github.xpax.syllabi.service;

import io.github.xpax.syllabi.entity.Admission;
import io.github.xpax.syllabi.entity.AdmissionWeight;
import io.github.xpax.syllabi.entity.Program;
import io.github.xpax.syllabi.entity.dto.AdmissionWeightRequest;
import io.github.xpax.syllabi.entity.dto.CreateAdmissionRequest;
import io.github.xpax.syllabi.repo.AdmissionRepository;
import io.github.xpax.syllabi.repo.ProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdmissionService {
    private final AdmissionRepository admissionRepository;
    private final ProgramRepository programRepository;

    @Autowired
    public AdmissionService(AdmissionRepository admissionRepository, ProgramRepository programRepository) {
        this.admissionRepository = admissionRepository;
        this.programRepository = programRepository;
    }


    public Admission createAdmission(Integer programId, CreateAdmissionRequest admissionRequest) {
        Program program = programRepository.getOne(programId);
        List<AdmissionWeight> weights = admissionRequest.getWeights().stream()
                .map(this::transformWeight)
                .collect(Collectors.toList());
        Admission admission = Admission.builder()
                .endDate(admissionRequest.getEndDate())
                .startDate(admissionRequest.getStartDate())
                .name(admissionRequest.getName())
                .phase(0)
                .program(program)
                .weights(weights)
                .build();
        weights.forEach((w) -> w.setAdmission(admission));
        return admissionRepository.save(admission);
    }

    private AdmissionWeight transformWeight(AdmissionWeightRequest request) {
        return AdmissionWeight.builder()
                .weight(request.getWeight())
                .name(request.getName())
                .build();
    }
}
