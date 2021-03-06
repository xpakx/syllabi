package io.github.xpax.syllabi.controller;

import io.github.xpax.syllabi.entity.Admission;
import io.github.xpax.syllabi.entity.AdmissionForm;
import io.github.xpax.syllabi.entity.StudentProgram;
import io.github.xpax.syllabi.entity.dto.*;
import io.github.xpax.syllabi.service.AdmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @Secured("ROLE_ADMISSION_ADMIN")
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

    //@PostAuthorize("hasRole('ROLE_ADMISSION_ADMIN') or hasRole('ROLE_RECRUITER') or" +
    //        "returnObject.getBody().getUser().getId().toString() == authentication.principal.username")
    @GetMapping("/students/admissions/{formId}")
    public ResponseEntity<AdmissionFormDetails> showAdmission(@PathVariable Integer formId) {
        return new ResponseEntity<>(admissionService.getForm(formId), HttpStatus.OK);
    }

    @Secured({"ROLE_ADMISSION_ADMIN", "ROLE_RECRUITER"})
    @PutMapping("/students/admissions/{formId}")
    public ResponseEntity<AdmissionForm> verifyByAdmin(@PathVariable Integer formId,
                                                       @RequestBody AdmissionFormVerifyRequest admissionRequest) {
        return new ResponseEntity<>(
                admissionService.verifyAdmissionForm(formId, admissionRequest),
                HttpStatus.OK
        );
    }

    @Secured({"ROLE_ADMISSION_ADMIN", "ROLE_RECRUITER"})
    @GetMapping("/admissions/{admissionId}/forms")
    public ResponseEntity<Page<AdmissionFormSummary>> getAdmissionForms(@PathVariable Integer admissionId,
                                                                 @RequestParam Optional<Integer> page,
                                                                 @RequestParam Optional<Integer> size) {
        return new ResponseEntity<>(
                admissionService.getAllForms(admissionId, page.orElse(0), size.orElse(20)),
                HttpStatus.OK);
    }

    @Secured({"ROLE_ADMISSION_ADMIN", "ROLE_RECRUITER"})
    @GetMapping("/admissions/{admissionId}/results")
    public ResponseEntity<Page<AdmissionFormSummary>> getAdmissionResults(@PathVariable Integer admissionId) {
        return new ResponseEntity<>(
                admissionService.getResults(admissionId),
                HttpStatus.OK);
    }

    @Secured("ROLE_ADMISSION_ADMIN")
    @PutMapping("/admissions/{admissionId}/limit")
    public ResponseEntity<Admission> changeStudentLimit(@PathVariable Integer admissionId,
                                                  @RequestBody AdmissionChangeLimit admissionRequest) {
        return new ResponseEntity<>(
                admissionService.changeLimit(admissionId, admissionRequest),
                HttpStatus.OK);
    }

    @Secured("ROLE_ADMISSION_ADMIN")
    @PostMapping("/admissions/{admissionId}/close")
    public ResponseEntity<Admission> changeStudentLimit (@PathVariable Integer admissionId,
                                                         @RequestBody CloseAdmissionRequest request) {
        return new ResponseEntity<>(
                admissionService.closeAdmissions(admissionId, request),
                HttpStatus.OK);
    }

    @Secured({"ROLE_ADMISSION_ADMIN", "ROLE_RECRUITER"})
    @GetMapping("/admissions/{admissionId}/forms/verified")
    public ResponseEntity<Page<AdmissionFormSummary>> getVerifiedAdmissionForms(@PathVariable Integer admissionId,
                                                                 @RequestParam Optional<Integer> page,
                                                                 @RequestParam Optional<Integer> size) {
        return new ResponseEntity<>(
                admissionService.getAllVerifiedForms(admissionId, page.orElse(0), size.orElse(20)),
                HttpStatus.OK);
    }

    @Secured({"ROLE_ADMISSION_ADMIN", "ROLE_RECRUITER"})
    @PostMapping("/users/{userId}/student/programs")
    public ResponseEntity<StudentProgram> addStudentProgram(@PathVariable Integer userId,
                                                            @RequestBody StudentProgramRequest request) {
        return new ResponseEntity<>(
                admissionService.createStudentProgram(userId, request),
                HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_ADMISSION_ADMIN') or #userId.toString() == authentication.principal.username")
    @GetMapping("/users/{userId}/admissions")
    public ResponseEntity<Page<AdmissionFormSummary>> getUserAdmissionForms(@PathVariable Integer userId,
                                                                         @RequestParam Optional<Integer> page,
                                                                         @RequestParam Optional<Integer> size) {
        return new ResponseEntity<>(
                admissionService.getAllUserForms(userId, page.orElse(0), size.orElse(20)),
                HttpStatus.OK
        );
    }

    @GetMapping("/admissions")
    public ResponseEntity<Page<Admission>> getAllAdmissions(@RequestParam Optional<Integer> page,
                                                                 @RequestParam Optional<Integer> size) {
        return new ResponseEntity<>(
                admissionService.getAllAdmissions(page.orElse(0), size.orElse(20)),
                HttpStatus.OK
        );
    }

    @Secured("ROLE_ADMISSION_ADMIN")
    @DeleteMapping("/admissions/{admissionId}")
    public ResponseEntity<?> deleteAdmission(@PathVariable Integer admissionId) {
        admissionService.deleteAdmission(admissionId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/admissions/{admissionId}")
    public ResponseEntity<AdmissionDetails> getAdmission(@PathVariable Integer admissionId) {
        return new ResponseEntity<>(
                admissionService.getAdmissionById(admissionId),
                HttpStatus.OK
        );
    }

    @GetMapping("/programs/{programId}/admissions")
    public ResponseEntity<Page<Admission>> getAllAdmissionsForProgram(@RequestParam Optional<Integer> page,
                                                            @RequestParam Optional<Integer> size,
                                                                      @PathVariable Integer programId) {
        return new ResponseEntity<>(
                admissionService.getAllAdmissionsForProgram(programId, page.orElse(0), size.orElse(20)),
                HttpStatus.OK
        );
    }
}
