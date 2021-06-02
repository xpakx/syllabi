package io.github.xpax.syllabi.entity.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CloseAdmissionRequest {
    List<Integer> acceptedStudentsIds;
}
