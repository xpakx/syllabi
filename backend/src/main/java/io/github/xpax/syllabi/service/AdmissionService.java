package io.github.xpax.syllabi.service;

import io.github.xpax.syllabi.entity.*;
import io.github.xpax.syllabi.entity.dto.*;
import io.github.xpax.syllabi.error.NotFoundException;
import io.github.xpax.syllabi.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdmissionService {
    private final AdmissionRepository admissionRepository;
    private final ProgramRepository programRepository;
    private final UserRepository userRepository;
    private final AdmissionWeightRepository admissionWeightRepository;
    private final AdmissionFormRepository admissionFormRepository;

    @Autowired
    public AdmissionService(AdmissionRepository admissionRepository, ProgramRepository programRepository, UserRepository userRepository, AdmissionWeightRepository admissionWeightRepository, AdmissionFormRepository admissionFormRepository) {
        this.admissionRepository = admissionRepository;
        this.programRepository = programRepository;
        this.userRepository = userRepository;
        this.admissionWeightRepository = admissionWeightRepository;
        this.admissionFormRepository = admissionFormRepository;
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
                .closed(false)
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

    private AdmissionPoints transformPoint(AdmissionPointRequest request) {
        AdmissionWeight weight = admissionWeightRepository.getOne(request.getWeightId());
        return AdmissionPoints.builder()
                .points(request.getPoints())
                .weight(weight)
                .build();
    }

    public AdmissionForm createAdmissionForm(Integer admissionId, Integer userId,
                                                             AdmissionFormRequest admissionRequest) {
        Admission admission = admissionRepository.getOne(admissionId);
        User user = userRepository.getOne(userId);
        List<AdmissionPoints> points = admissionRequest.getPoints().stream()
                .map(this::transformPoint)
                .collect(Collectors.toList());
        AdmissionForm form = AdmissionForm.builder()
                .name(admission.getName())
                .surname(admissionRequest.getSurname())
                .documentId(admissionRequest.getDocumentId())
                .user(user)
                .admission(admission)
                .accepted(false)
                .verified(false)
                .discarded(false)
                .build();
        points.forEach((p) -> p.setForm(form));
        return admissionFormRepository.save(form);
    }

    public AdmissionForm getForm(Integer admissionId) {
        return admissionFormRepository.findById(admissionId)
                .orElseThrow(() -> new NotFoundException(("No admission form with id " + admissionId + " found!")));
    }

    public AdmissionForm verifyAdmissionForm(Integer formId, AdmissionFormVerifyRequest admissionRequest) {
        AdmissionForm form = admissionFormRepository.findById(formId)
                .orElseThrow(() -> new NotFoundException(("No admission form with id " + formId + " found!")));

        form.setName(admissionRequest.getName());
        form.setSurname(admissionRequest.getSurname());
        form.setDocumentId(admissionRequest.getDocumentId());
        if(admissionRequest.isVerify()) {
            form.setVerified(true);
        }
        else {
            form.setDiscarded(true);
        }
        return admissionFormRepository.save(form);
    }

    public Page<AdmissionForm> getAllForms(Integer admissionId, Integer page, Integer size) {
        return admissionFormRepository.getAllByAdmissionId(admissionId, PageRequest.of(page, size));
    }
}
