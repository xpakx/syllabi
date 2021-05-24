package io.github.xpax.syllabi.service;

import io.github.xpax.syllabi.entity.Student;
import io.github.xpax.syllabi.entity.User;
import io.github.xpax.syllabi.entity.dto.StudentWithUserId;
import io.github.xpax.syllabi.entity.dto.UpdateStudentRequest;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
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
    private UserToStudentRequest addStudentRequest;
    private User user;
    private Student student;
    private StudentWithUserId studentProj;
    private UpdateStudentRequest updateStudentRequest;
    private Page<StudentWithUserId> studentPage;

    private final ProjectionFactory factory = new SpelAwareProxyProjectionFactory();

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1)
                .build();
        addStudentRequest = new UserToStudentRequest();
        addStudentRequest.setName("Michael");
        addStudentRequest.setSurname("Garcia");

        student = Student.builder()
                .id(2)
                .name("Jane")
                .build();
        this.studentProj = factory.createProjection(StudentWithUserId.class, student);
        updateStudentRequest = new UpdateStudentRequest();
        updateStudentRequest.setName("Ann");
        updateStudentRequest.setSurname("Smith");

        studentPage = Page.empty();
    }

    private void injectMocks() {
        studentService = new StudentService(studentRepository, userRepository);
    }

    @Test
    void shouldAddStudent() {
        given(userRepository.getOne(1))
                .willReturn(user);
        injectMocks();

        studentService.createStudent(addStudentRequest, 1);

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

    @Test
    void shouldDeleteStudent() {
        injectMocks();

        studentService.deleteStudent(153);
        then(studentRepository)
                .should(times(1))
                .deleteByUserId(153);
    }

    @Test
    void shouldUpdateStudent() {
        given(studentRepository.getByUserId(anyInt()))
                .willReturn(Optional.of(student));
        injectMocks();

        studentService.updateStudent(updateStudentRequest, 3);

        ArgumentCaptor<Student> studentCaptor = ArgumentCaptor.forClass(Student.class);
        then(studentRepository)
                .should(times(1))
                .save(studentCaptor.capture());
        Student updatedStudent = studentCaptor.getValue();

        assertNotNull(updatedStudent);
        assertEquals("Ann", updatedStudent.getName());
        assertEquals("Smith", updatedStudent.getSurname());
    }

    @Test
    void shouldAskRepositoryForGroupStudents() {
        given(studentRepository.findAllStudentByGroupId(anyInt(), any(PageRequest.class)))
                .willReturn(studentPage);
        injectMocks();

        Page<StudentWithUserId> result = studentService.getGroupStudents(5, 0, 20);

        ArgumentCaptor<PageRequest> pageRequestCaptor = ArgumentCaptor.forClass(PageRequest.class);
        ArgumentCaptor<Integer> integerCaptor = ArgumentCaptor.forClass(Integer.class);

        then(studentRepository)
                .should(times(1))
                .findAllStudentByGroupId(integerCaptor.capture(), pageRequestCaptor.capture());
        PageRequest pageRequest = pageRequestCaptor.getValue();
        Integer groupId = integerCaptor.getValue();

        assertEquals(0, pageRequest.getPageNumber());
        assertEquals(20, pageRequest.getPageSize());

        assertThat(result, is(sameInstance(studentPage)));

        assertEquals(5, groupId);
    }
}
