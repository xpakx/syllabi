package io.github.xpax.syllabi.service;

import io.github.xpax.syllabi.entity.Institute;
import io.github.xpax.syllabi.entity.Job;
import io.github.xpax.syllabi.entity.Teacher;
import io.github.xpax.syllabi.entity.User;
import io.github.xpax.syllabi.entity.dto.UserToTeacherRequest;
import io.github.xpax.syllabi.error.TeacherExistsException;
import io.github.xpax.syllabi.repo.InstituteRepository;
import io.github.xpax.syllabi.repo.TeacherRepository;
import io.github.xpax.syllabi.repo.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class TeacherService {
    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;
    private final InstituteRepository instituteRepository;

    public TeacherService(TeacherRepository teacherRepository, UserRepository userRepository,
                          InstituteRepository instituteRepository) {
        this.teacherRepository = teacherRepository;
        this.userRepository = userRepository;
        this.instituteRepository = instituteRepository;
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



    private Institute getInstitute(UserToTeacherRequest teacherRequest) {
        if(teacherRequest.getInstituteId() != null)
            return instituteRepository.getOne(teacherRequest.getInstituteId());
        else
            return null;
    }
}
