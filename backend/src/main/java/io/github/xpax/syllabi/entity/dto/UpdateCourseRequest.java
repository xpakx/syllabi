package io.github.xpax.syllabi.entity.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateCourseRequest extends CourseRequest {
    List<Integer> prerequisites;
    List<Integer> semesters;
}