package io.github.xpax.syllabi.controller;

import io.github.xpax.syllabi.entity.dto.InstituteForPage;
import io.github.xpax.syllabi.service.InstituteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/institutes")
public class InstituteController {
    private final InstituteService instituteService;

    @Autowired
    public InstituteController(InstituteService instituteService) {
        this.instituteService = instituteService;
    }

    @GetMapping
    public ResponseEntity<Page<InstituteForPage>> getAllInstitutes(@RequestParam Optional<Integer> page,
                                                                   @RequestParam Optional<Integer> size) {
        return new ResponseEntity<>(
                instituteService.getAllInstitutes(page.orElse(0), size.orElse(20)),
                HttpStatus.OK
        );
    }
}
