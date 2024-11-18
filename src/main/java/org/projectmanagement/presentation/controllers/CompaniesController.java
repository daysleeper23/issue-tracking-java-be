package org.projectmanagement.presentation.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.projectmanagement.application.dto.companies.CompanyDTO;
import org.projectmanagement.domain.entities.Companies;
import org.projectmanagement.domain.services.CompaniesService;
import org.projectmanagement.presentation.response.GlobalResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/companies")
@RestController
@RequiredArgsConstructor
public class CompaniesController {

    private final CompaniesService companiesService;

    @PostMapping
    public ResponseEntity<GlobalResponse<Companies>> createCompany(
            @RequestBody @Valid CompanyDTO dto
            ){
        return new ResponseEntity<>(
                new GlobalResponse<>(HttpStatus.CREATED.value(), companiesService.createNewCompany(dto)),
                HttpStatus.CREATED);
    }

    @GetMapping("{id}")
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
            @RequestBody @Valid CompanyDTO dto
    ){
        return new ResponseEntity<>(
                new GlobalResponse<>(HttpStatus.OK.value(), companiesService.updateCompany(id,dto)),
                HttpStatus.OK
        );
    }

//    @DeleteMapping("{id}")
//    public ResponseEntity<GlobalResponse<Boolean>> archiveCompany(
//            @PathVariable @Valid String id
//    ){
//        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), companiesService.archiveCompany(id)));
//    }
}
