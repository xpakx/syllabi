package io.github.xpax.syllabi.controller;

import io.github.xpax.syllabi.entity.Admission;
import io.github.xpax.syllabi.entity.AdmissionForm;
import io.github.xpax.syllabi.entity.dto.*;
import io.github.xpax.syllabi.service.AdmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;


@RestController
public class AdmissionController {
    private final AdmissionService admissionService;

    @Autowired
    public AdmissionController(AdmissionService admissionService) {
        this.admissionService = admissionService;
    }

    @PostMapping("/programs/{programId}/admissions")
    public ResponseEntity<Admission> createAdmission(
            @RequestBody @Valid CreateAdmissionRequest admissionRequest,
            @PathVariable Integer programId) {
        return new ResponseEntity<>(
                admissionService.createAdmission(programId, admissionRequest),
                HttpStatus.OK
        );
    }

    @PostMapping("/admissions/{admissionId}/apply/{userId}")
    public ResponseEntity<AdmissionForm> apply(@RequestBody AdmissionFormRequest admissionRequest,
                                               @PathVariable Integer admissionId,
                                               @PathVariable Integer userId) {
        return new ResponseEntity<>(
                admissionService.createAdmissionForm(admissionId, userId, admissionRequest),
                HttpStatus.OK
        );
    }

    @GetMapping("/students/admissions/{formId}")
    public ResponseEntity<AdmissionForm> showAdmission(@PathVariable Integer formId) {
        return new ResponseEntity<>(admissionService.getForm(formId), HttpStatus.OK);
    }

    @PutMapping("/students/admissions/{formId}")
    public ResponseEntity<AdmissionForm> verifyByAdmin(@PathVariable Integer formId,
                                                       @RequestBody AdmissionFormVerifyRequest admissionRequest) {
        return new ResponseEntity<>(
                admissionService.verifyAdmissionForm(formId, admissionRequest),
                HttpStatus.OK
        );
    }

    @GetMapping("/admissions/{admissionId}/forms")
    public ResponseEntity<Page<AdmissionForm>> getAdmissionForms(@PathVariable Integer admissionId,
                                                                 @RequestParam Optional<Integer> page,
                                                                 @RequestParam Optional<Integer> size) {
        return new ResponseEntity<>(
                admissionService.getAllForms(admissionId, page.orElse(0), size.orElse(20)),
                HttpStatus.OK);
    }

    @GetMapping("/admissions/{admissionId}/results")
    public ResponseEntity<Page<AdmissionForm>> getAdmissionResults(@PathVariable Integer admissionId,
                                                                 @RequestParam Optional<Integer> page,
                                                                 @RequestParam Optional<Integer> size) {
        return new ResponseEntity<>(
                admissionService.getResults(admissionId, page.orElse(0), size.orElse(20)),
                HttpStatus.OK);
    }

    @PutMapping("/admissions/{admissionId}/limit")
    public ResponseEntity<Admission> changeStudentLimit (@PathVariable Integer admissionId,
                                                  @RequestBody AdmissionChangeLimit admissionRequest) {
        return new ResponseEntity<>(
                admissionService.changeLimit(admissionId, admissionRequest),
                HttpStatus.OK);
    }

    @PostMapping("/admissions/{admissionId}/close")
    public ResponseEntity<Admission> changeStudentLimit (@PathVariable Integer admissionId,
                                                         @RequestBody CloseAdmissionRequest request) {
        return new ResponseEntity<>(
                admissionService.closeAdmissions(admissionId, request),
                HttpStatus.OK);
    }

    @GetMapping("/admissions/{admissionId}/forms/verified")
    public ResponseEntity<Page<AdmissionForm>> getVerifiedAdmissionForms(@PathVariable Integer admissionId,
                                                                 @RequestParam Optional<Integer> page,
                                                                 @RequestParam Optional<Integer> size) {
        return new ResponseEntity<>(
                admissionService.getAllVerifiedForms(admissionId, page.orElse(0), size.orElse(20)),
                HttpStatus.OK);
    }
}
