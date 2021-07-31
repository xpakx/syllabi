package io.github.xpax.syllabi.service;

import io.github.xpax.syllabi.entity.*;
import io.github.xpax.syllabi.entity.dto.*;
import io.github.xpax.syllabi.error.AdmissionsBlockedException;
import io.github.xpax.syllabi.error.EntityExistsException;
import io.github.xpax.syllabi.error.NotFoundException;
import io.github.xpax.syllabi.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class AdmissionService {
    private final AdmissionRepository admissionRepository;
    private final ProgramRepository programRepository;
    private final UserRepository userRepository;
    private final AdmissionWeightRepository admissionWeightRepository;
    private final AdmissionFormRepository admissionFormRepository;
    private final AdmissionPointsRepository admissionPointsRepository;

    private final StudentRepository studentRepository;
    private final StudentProgramRepository studentProgramRepository;

    @Autowired
    public AdmissionService(AdmissionRepository admissionRepository, ProgramRepository programRepository, UserRepository userRepository, AdmissionWeightRepository admissionWeightRepository, AdmissionFormRepository admissionFormRepository, AdmissionPointsRepository admissionPointsRepository, StudentRepository studentRepository, StudentProgramRepository studentProgramRepository) {
        this.admissionRepository = admissionRepository;
        this.programRepository = programRepository;
        this.userRepository = userRepository;
        this.admissionWeightRepository = admissionWeightRepository;
        this.admissionFormRepository = admissionFormRepository;
        this.admissionPointsRepository = admissionPointsRepository;
        this.studentRepository = studentRepository;
        this.studentProgramRepository = studentProgramRepository;
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
                .studentLimit(admissionRequest.getStudentLimit())
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

    @Transactional
    public AdmissionForm createAdmissionForm(Integer admissionId, Integer userId,
                                                             AdmissionFormRequest admissionRequest) {

        if(admissionFormRepository.existsAdmissionFormByUserIdAndAdmissionId(userId, admissionId)) {
            throw new EntityExistsException("Form for this admission and user already exists!");
        }

        Admission admission = admissionRepository.findById(admissionId)
                .orElseThrow(() -> new NotFoundException(("No admission form with id " + admissionId + " found!")));

        if(admission.isClosed()) {
            throw new AdmissionsBlockedException("Admissions are closed!");
        }

        Date now = Calendar.getInstance().getTime();
        if(admission.getStartDate().after(now) || admission.getEndDate().before(now)) {
            DateTimeFormatter dataFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate startDate = admission.getStartDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            LocalDate endDate = admission.getEndDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            throw new AdmissionsBlockedException("Registering is not possible before "+
                    dataFormatter.format(startDate) +
                    " and after " + dataFormatter.format(endDate));
        }

        User user = userRepository.getOne(userId);
        List<AdmissionPoints> points = admissionRequest.getPoints().stream()
                .map(this::transformPoint)
                .collect(Collectors.toList());
        AdmissionForm form = AdmissionForm.builder()
                .name(admissionRequest.getName())
                .surname(admissionRequest.getSurname())
                .documentId(admissionRequest.getDocumentId())
                .user(user)
                .admission(admission)
                .accepted(false)
                .verified(false)
                .discarded(false)
                .points(points)
                .build();
        points.forEach((p) -> p.setForm(form));
        return admissionFormRepository.save(form);
    }

    public AdmissionFormDetails getForm(Integer admissionId) {
        return admissionFormRepository.findProjectedById(admissionId)
                .orElseThrow(() -> new NotFoundException(("No admission form with id " + admissionId + " found!")));
    }

    public AdmissionForm verifyAdmissionForm(Integer formId, AdmissionFormVerifyRequest admissionRequest) {
        AdmissionForm form = admissionFormRepository.findById(formId)
                .orElseThrow(() -> new NotFoundException(("No admission form with id " + formId + " found!")));

        form.setName(admissionRequest.getName());
        form.setSurname(admissionRequest.getSurname());
        form.setDocumentId(admissionRequest.getDocumentId());
        if(admissionRequest.isVerify()) {
            form.setDiscarded(false);
            form.setVerified(true);
            List<AdmissionPoints> points = admissionPointsRepository.findByFormId(formId);
            int sum = admissionRequest.getPoints().stream()
                    .mapToInt((p) -> p.getPoints() * getWeightFromPointsList(points, p))
                    .sum();
            form.setPointsSum(sum);
        }
        else {
            form.setVerified(false);
            form.setDiscarded(true);
        }
        return admissionFormRepository.save(form);
    }

    private Integer getWeightFromPointsList(List<AdmissionPoints> pointsList, AdmissionPointRequest p) {
        return pointsList.stream()
                .map(AdmissionPoints::getWeight)
                .filter(weight -> Objects.equals(weight.getId(), p.getWeightId()))
                .map(AdmissionWeight::getWeight)
                .findAny().orElse(0);
    }

    public Page<AdmissionFormSummary> getAllForms(Integer admissionId, Integer page, Integer size) {
        return admissionFormRepository.getAllByAdmissionId(admissionId, PageRequest.of(page, size));
    }

    public Page<AdmissionFormSummary> getResults(Integer admissionId) {
        Admission admission = admissionRepository.findById(admissionId)
                .orElseThrow(() -> new NotFoundException(("No admission with id " + admissionId + " found!")));
        return admissionFormRepository.getAllByAdmissionIdAndVerified(admissionId, true,
                PageRequest.of(0, admission.getStudentLimit(), Sort.Direction.DESC,"pointsSum"));
    }

    public Page<AdmissionFormSummary> getAllVerifiedForms(Integer admissionId, Integer page, Integer size) {
        return admissionFormRepository.getAllByAdmissionIdAndVerified(admissionId, true,
                PageRequest.of(page, size, Sort.Direction.DESC,"pointsSum"));
    }

    public Admission changeLimit(Integer admissionId, AdmissionChangeLimit admissionRequest) {
        Admission admission = admissionRepository.findById(admissionId)
                .orElseThrow(() -> new NotFoundException(("No admission form with id " + admissionId + " found!")));
        admission.setStudentLimit(admissionRequest.getStudentLimit());
        return admissionRepository.save(admission);
    }

    @Transactional
    public Admission closeAdmissions(Integer admissionId, CloseAdmissionRequest request) {
        Admission admission = admissionRepository.findById(admissionId)
                .orElseThrow(() -> new NotFoundException(("No admission form with id " + admissionId + " found!")));
        List<AdmissionForm> forms = admissionFormRepository.findAllByAdmissionId(admissionId);

        admission.setClosed(true);

        forms.stream()
                .filter((f) -> request.getAcceptedStudentsIds().contains(f.getId()))
                .forEach((f) -> f.setAccepted(true));
        forms.stream()
                .filter((f) -> !f.isAccepted())
                .forEach((f) -> f.setDiscarded(true));

        admissionFormRepository.saveAll(forms);
        return admissionRepository.save(admission);
    }

    public StudentProgram createStudentProgram(Integer userId, StudentProgramRequest request) {
        Student student = studentRepository.findById(userId)
                .orElse(getNewStudent(userId, request));
        Program program = programRepository.getOne(request.getProgramId());
        StudentProgram studentProgram = StudentProgram.builder()
                .student(student)
                .program(program)
                .semester(1)
                .build();
        return studentProgramRepository.save(studentProgram);
    }

    private Student getNewStudent(Integer userId, StudentProgramRequest request) {
        return studentRepository.save(Student.builder()
                .name(request.getName())
                .surname(request.getSurname())
                .user(userRepository.getOne(userId))
                .build());
    }

    public Page<AdmissionFormSummary> getAllUserForms(Integer userId, Integer page, Integer size) {
        return admissionFormRepository.getAllByUserId(userId, PageRequest.of(page, size));
    }

    public Page<Admission> getAllAdmissions(Integer page, Integer size) {
        return admissionRepository.getAllByClosed(false, PageRequest.of(page, size));
    }

    public void deleteAdmission(Integer admissionId) {
        admissionRepository.deleteById(admissionId);
    }

    public AdmissionDetails getAdmissionById(Integer admissionId) {
        return admissionRepository.findProjectedById(admissionId)
                .orElseThrow(() -> new NotFoundException(("No admission form with id " + admissionId + " found!")));
    }

    public Page<Admission> getAllAdmissionsForProgram(Integer programId, Integer page, Integer size) {
        return admissionRepository.getAllByProgramId(programId, PageRequest.of(page, size));
    }
}
