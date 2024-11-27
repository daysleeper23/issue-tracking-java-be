package org.projectmanagement.presentation.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.projectmanagement.application.dto.companies.Company;
import org.projectmanagement.domain.entities.Companies;
import org.projectmanagement.domain.services.CompaniesService;
import org.projectmanagement.presentation.response.GlobalResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@RequestMapping("/companies")
@RestController
@RequiredArgsConstructor
public class CompaniesController {

    private final CompaniesService companiesService;


    @PostMapping("/companies")
    public ResponseEntity<GlobalResponse<Companies>> createCompany(
            @RequestBody @Valid Company dto
            ){
        return new ResponseEntity<>(
                new GlobalResponse<>(HttpStatus.CREATED.value(), companiesService.createNewCompany(dto)),
                HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GlobalResponse<Companies>> getCompany(
            @PathVariable String id
    ){
        return new ResponseEntity<>(
                new GlobalResponse<>(HttpStatus.OK.value(), companiesService.getCompany(id)),
                HttpStatus.OK
        );
    }

    @PutMapping("{id}")
    public ResponseEntity<GlobalResponse<Companies>> updateCompany(
            @PathVariable String id,
            @RequestBody @Valid Company dto
    ){
        return new ResponseEntity<>(
                new GlobalResponse<>(HttpStatus.OK.value(), companiesService.updateCompany(id,dto)),
                HttpStatus.OK
        );
    }

}
