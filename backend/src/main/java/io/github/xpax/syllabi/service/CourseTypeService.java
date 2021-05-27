package io.github.xpax.syllabi.service;

import io.github.xpax.syllabi.entity.CourseType;
import io.github.xpax.syllabi.entity.dto.CourseTypeRequest;
import io.github.xpax.syllabi.error.NotFoundException;
import io.github.xpax.syllabi.repo.CourseTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class CourseTypeService {
    private final CourseTypeRepository courseTypeRepository;

    @Autowired
    public CourseTypeService(CourseTypeRepository courseTypeRepository) {
        this.courseTypeRepository = courseTypeRepository;
    }

    public CourseType addNewCourseType(CourseTypeRequest courseTypeRequest) {
        CourseType courseType = buildCourseType(courseTypeRequest)
                .build();
        return courseTypeRepository.save(courseType);
    }

    public CourseType getCourseType(Integer typeId) {
        return courseTypeRepository.findById(typeId)
                .orElseThrow(() -> new NotFoundException("Type with id "+typeId+" not found!"));
    }

    public void deleteCourseType(Integer typeId) {
        courseTypeRepository.deleteById(typeId);
    }

    public CourseType updateCourseType(CourseTypeRequest courseTypeRequest, Integer typeId) {
        CourseType courseType = courseTypeRepository.findById(typeId)
                .orElseThrow(() -> new NotFoundException("Type with id "+typeId+" not found!"));

        courseType.setName(courseTypeRequest.getName());
        return courseTypeRepository.save(courseType);
    }

    public Page<CourseType> getAllCourseTypes(int page, int size) {
        return courseTypeRepository.findAll(PageRequest.of(page, size));
    }

    private CourseType.CourseTypeBuilder buildCourseType(CourseTypeRequest courseTypeRequest) {
        return CourseType.builder()
                .name(courseTypeRequest.getName());
    }

}
