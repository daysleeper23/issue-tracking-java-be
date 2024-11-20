package org.projectmanagement.presentation.controllers;

import jakarta.validation.Valid;
import org.projectmanagement.application.dto.company_managers.CreateCompanyManagers;
import org.projectmanagement.application.dto.company_managers.UpdateCompanyManagers;
import org.projectmanagement.domain.entities.CompanyManagers;
import org.projectmanagement.domain.services.CompanyManagersService;
import org.projectmanagement.presentation.response.GlobalResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/{companyId}/companyManagers")
public class CompanyManagersController {
    CompanyManagersService companyManagersService;

    @Autowired
    CompanyManagersController(CompanyManagersService companyManagersService){
        this.companyManagersService = companyManagersService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<GlobalResponse<CompanyManagers>> getCompanyManagerById(@PathVariable @Valid UUID id) {
        return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.OK.value(), companyManagersService.getById(id)), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<GlobalResponse<List<CompanyManagers>>> getAllManagersByCompanyId(@PathVariable @Valid UUID companyId) {
        return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.OK.value(), companyManagersService.getAllManagersByCompanyId(companyId)), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<GlobalResponse<CompanyManagers>> createCompanyManager(@RequestBody @Valid CreateCompanyManagers dto) {
        return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.CREATED.value(), companyManagersService.createCompanyManager(dto)), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<GlobalResponse<CompanyManagers>> updateCompanyManager(@PathVariable @Valid UUID id,@RequestBody @Valid UpdateCompanyManagers dto) {
        CompanyManagers updatedCompanyManager = companyManagersService.updateCompanyManager(id, dto);
        return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.OK.value(), updatedCompanyManager), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GlobalResponse<String>> deleteById(@PathVariable @Valid UUID id) {
        companyManagersService.deleteById(id);
        return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.OK.value(), "User deleted"), HttpStatus.OK);
    }

}
