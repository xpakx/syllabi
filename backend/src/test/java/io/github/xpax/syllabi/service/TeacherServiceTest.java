package io.github.xpax.syllabi.service;

import io.github.xpax.syllabi.entity.Institute;
import io.github.xpax.syllabi.entity.Teacher;
import io.github.xpax.syllabi.entity.User;
import io.github.xpax.syllabi.entity.dto.UserToTeacherRequest;
import io.github.xpax.syllabi.repo.InstituteRepository;
import io.github.xpax.syllabi.repo.TeacherRepository;
import io.github.xpax.syllabi.repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {
    @Mock
    private TeacherRepository teacherRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private InstituteRepository instituteRepository;

    private TeacherService teacherService;

    private User user;
    private Institute institute;
    private UserToTeacherRequest request;

    private final ProjectionFactory factory = new SpelAwareProxyProjectionFactory();

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1)
                .username("shrdy")
                .build();
        institute = Institute.builder()
                .id(2)
                .name("Institute of Anthropology")
                .build();

        request = new UserToTeacherRequest();
        request.setName("Sarah");
        request.setSurname("Hrdy");
        request.setInstituteId(2);
        request.setJobName("Researcher");
    }

    private void injectMocks() {
        teacherService = new TeacherService(teacherRepository, userRepository, instituteRepository);
    }

    @Test
    void shouldAddTeacher() {
        given(userRepository.getOne(1))
                .willReturn(user);
        given(instituteRepository.getOne(2))
                .willReturn(institute);
        injectMocks();

        teacherService.createTeacher(request, 1);

        ArgumentCaptor<Teacher> teacherCaptor = ArgumentCaptor.forClass(Teacher.class);

        then(teacherRepository)
                .should(times(1))
                .save(teacherCaptor.capture());

        Teacher addedTeacher = teacherCaptor.getValue();

        assertNotNull(addedTeacher);
        assertEquals("Sarah", addedTeacher.getName());
        assertEquals("Hrdy", addedTeacher.getSurname());

        assertNotNull(addedTeacher.getJob());

        assertNull(addedTeacher.getId());

        assertNotNull(addedTeacher.getJob());
        assertEquals("Researcher", addedTeacher.getJob().getName());
        assertNotNull(addedTeacher.getJob().getInstitute());
        assertEquals(2, addedTeacher.getJob().getInstitute().getId());
        assertEquals("Institute of Anthropology", addedTeacher.getJob().getInstitute().getName());
    }
}
