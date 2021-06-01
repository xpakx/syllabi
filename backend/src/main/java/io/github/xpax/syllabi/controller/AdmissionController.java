package io.github.xpax.syllabi.controller;

import io.github.xpax.syllabi.entity.Admission;
import io.github.xpax.syllabi.entity.AdmissionForm;
import io.github.xpax.syllabi.entity.dto.AdmissionFormRequest;
import io.github.xpax.syllabi.entity.dto.CreateAdmissionRequest;
import io.github.xpax.syllabi.service.AdmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


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

    @GetMapping("/students/admissions/{fromId}")
    public ResponseEntity<AdmissionForm> showAdmission(@PathVariable Integer admissionId) {
        return new ResponseEntity<>(admissionService.getForm(admissionId), HttpStatus.OK);
    }
}
