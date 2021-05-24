package io.github.xpax.syllabi.service;

import io.github.xpax.syllabi.entity.Student;
import io.github.xpax.syllabi.entity.User;
import io.github.xpax.syllabi.entity.dto.StudentWithUserId;
import io.github.xpax.syllabi.entity.dto.UserToStudentRequest;
import io.github.xpax.syllabi.error.NotFoundException;
import io.github.xpax.syllabi.repo.StudentRepository;
import io.github.xpax.syllabi.repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
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
    private Student student;
    private StudentWithUserId studentProj;

    private final ProjectionFactory factory = new SpelAwareProxyProjectionFactory();

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1)
                .build();
        updateStudentRequest = new UserToStudentRequest();
        updateStudentRequest.setName("Michael");
        updateStudentRequest.setSurname("Garcia");

        student = Student.builder()
                .id(2)
                .name("Jane")
                .build();
        this.studentProj = factory.createProjection(StudentWithUserId.class, student);
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

    @Test
    void shouldReturnStudent() {
        given(studentRepository.findByUserId(anyInt(), any(Class.class)))
                .willReturn(Optional.of(studentProj));
        injectMocks();

        StudentWithUserId result = studentService.getStudent(2);

        assertNotNull(result);
        assertEquals(2, result.getId());
        assertEquals("Jane", result.getName());
    }

    @Test
    void shouldThrowExceptionIfStudentNotFound() {
        given(studentRepository.findByUserId(anyInt(), any(Class.class)))
                .willReturn(Optional.empty());
        injectMocks();
        assertThrows(NotFoundException.class, () -> studentService.getStudent(5));
    }
}
