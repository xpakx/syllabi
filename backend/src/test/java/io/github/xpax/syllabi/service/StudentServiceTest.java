package io.github.xpax.syllabi.service;

import io.github.xpax.syllabi.entity.Student;
import io.github.xpax.syllabi.entity.User;
import io.github.xpax.syllabi.entity.dto.UserToStudentRequest;
import io.github.xpax.syllabi.repo.StudentRepository;
import io.github.xpax.syllabi.repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private UserRepository userRepository;
    private StudentService studentService;
    private UserToStudentRequest updateStudentRequest;
    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1)
                .build();
        updateStudentRequest = new UserToStudentRequest();
        updateStudentRequest.setName("Michael");
        updateStudentRequest.setSurname("Garcia");
    }

    private void injectMocks() {
        studentService = new StudentService(studentRepository, userRepository);
    }

    @Test
    void shouldAddStudent() {
        given(userRepository.getOne(1))
                .willReturn(user);
        injectMocks();

        studentService.createStudent(updateStudentRequest, 1);

        ArgumentCaptor<Student> studentCaptor = ArgumentCaptor.forClass(Student.class);
        then(studentRepository)
                .should(times(1))
                .save(studentCaptor.capture());
        Student createdStudent = studentCaptor.getValue();

        assertNotNull(createdStudent);
        assertEquals("Michael", createdStudent.getName());
        assertEquals("Garcia", createdStudent.getSurname());
        assertNull(createdStudent.getId());
    }
}
