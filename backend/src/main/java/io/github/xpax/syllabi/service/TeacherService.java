package io.github.xpax.syllabi.service;

import io.github.xpax.syllabi.entity.Institute;
import io.github.xpax.syllabi.entity.Job;
import io.github.xpax.syllabi.entity.Teacher;
import io.github.xpax.syllabi.entity.User;
import io.github.xpax.syllabi.entity.dto.*;
import io.github.xpax.syllabi.error.NotFoundException;
import io.github.xpax.syllabi.error.TeacherExistsException;
import io.github.xpax.syllabi.repo.InstituteRepository;
import io.github.xpax.syllabi.repo.JobRepository;
import io.github.xpax.syllabi.repo.TeacherRepository;
import io.github.xpax.syllabi.repo.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TeacherService {
    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;
    private final InstituteRepository instituteRepository;
    private final JobRepository jobRepository;

    public TeacherService(TeacherRepository teacherRepository, UserRepository userRepository,
                          InstituteRepository instituteRepository, JobRepository jobRepository) {
        this.teacherRepository = teacherRepository;
        this.userRepository = userRepository;
        this.instituteRepository = instituteRepository;
        this.jobRepository = jobRepository;
    }

    public Teacher createTeacher(UserToTeacherRequest request, Integer userId) {
        if(teacherRepository.existsTeacherByUserId(userId)) {
            throw new TeacherExistsException(userId.toString());
        }

        User user = userRepository.getOne(userId);
        Institute institute = getInstitute(request);

        Job jobToAdd = Job.builder()
                .institute(institute)
                .name(request.getJobName())
                .build();
        Teacher teacherToAdd = Teacher.builder()
                .name(request.getName())
                .surname(request.getSurname())
                .title(request.getTitle())
                .phone(request.getPhone())
                .email(request.getEmail())
                .pbnId(request.getPbnId())
                .job(jobToAdd)
                .user(user)
                .build();

        jobToAdd.setTeacher(teacherToAdd);

        return teacherRepository.save(teacherToAdd);
    }

    public TeacherDetails getTeacher(Integer userId) {
        return teacherRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("No teacher for user with id "+userId+"!"));
    }

    @Transactional
    public void deleteTeacher(Integer userId) {
        teacherRepository.deleteByUserId(userId);
    }

    public Teacher updateTeacher(UpdateTeacherRequest request, Integer userId) {
        Teacher teacher = teacherRepository.getByUserId(userId)
                .orElseThrow(() -> new NotFoundException("No teacher for user with id "+userId+"!"));

        teacher.setName(request.getName());
        teacher.setSurname(request.getSurname());
        teacher.setPhone(request.getPhone());
        teacher.setEmail(request.getEmail());
        teacher.setPbnId(request.getPbnId());
        teacher.setTitle(request.getTitle());

        return teacherRepository.save(teacher);
    }

    public Job updateTeacherJob(UpdateJobRequest request, Integer userId) {
        Job job = jobRepository.findByTeacherUserId(userId)
                .orElseThrow(() -> new NotFoundException("No teacher for user with id "+userId+"!"));
        job.setName(request.getName());
        job.setInstitute(getInstituteForJob(request));
        return jobRepository.save(job);
    }

    public JobSummary getTeacherJob(Integer userId) {
        return jobRepository.getByTeacherUserId(userId)
                .orElseThrow(() -> new NotFoundException("No teacher for user with id "+userId+"!"));
    }



    private Institute getInstitute(UserToTeacherRequest teacherRequest) {
        if(teacherRequest.getInstituteId() != null)
            return instituteRepository.getOne(teacherRequest.getInstituteId());
        else
            return null;
    }

    private Institute getInstituteForJob(UpdateJobRequest teacherRequest) {
        if(teacherRequest.getInstituteId() != null)
            return instituteRepository.getOne(teacherRequest.getInstituteId());
        else
            return null;
    }
}
