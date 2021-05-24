package io.github.xpax.syllabi.service;

import io.github.xpax.syllabi.entity.Student;
import io.github.xpax.syllabi.entity.User;
import io.github.xpax.syllabi.entity.dto.StudentWithUserId;
import io.github.xpax.syllabi.entity.dto.UpdateStudentRequest;
import io.github.xpax.syllabi.entity.dto.UserToStudentRequest;
import io.github.xpax.syllabi.error.NotFoundException;
import io.github.xpax.syllabi.error.StudentExistsException;
import io.github.xpax.syllabi.repo.StudentRepository;
import io.github.xpax.syllabi.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository, UserRepository userRepository) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
    }

    public Student createStudent(UserToStudentRequest request, Integer userId) {
        if(studentRepository.existsStudentByUserId(userId)) {
            throw new StudentExistsException(userId.toString());
        }
        User user = userRepository.getOne(userId);
        Student studentToAdd = Student.builder()
                .surname(request.getSurname())
                .name(request.getName())
                .user(user)
                .build();
        return studentRepository.save(studentToAdd);
    }

    public StudentWithUserId getStudent(Integer userId) {
        return studentRepository.findByUserId(userId, StudentWithUserId.class)
                .orElseThrow(() -> new NotFoundException("No student for user with id "+userId+"!"));
    }

    @Transactional
    public void deleteStudent(Integer userId) {
        studentRepository.deleteByUserId(userId);
    }

    public Student updateStudent(UpdateStudentRequest request, Integer userId) {
        Student student = studentRepository.getByUserId(userId)
                .orElseThrow(() -> new NotFoundException("No student for user with id "+userId+"!"));
        student.setName(request.getName());
        student.setSurname(request.getSurname());
        return studentRepository.save(student);
    }
}
