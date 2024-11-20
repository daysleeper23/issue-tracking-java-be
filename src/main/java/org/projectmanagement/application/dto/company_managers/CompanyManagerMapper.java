package org.projectmanagement.application.dto.company_managers;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.projectmanagement.domain.entities.CompanyManagers;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CompanyManagerMapper {

    CompanyManagerMapper INSTANCE = Mappers.getMapper(CompanyManagerMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateCompanManagersDTOToCompanyManagers(UpdateCompanyManagers dto, @MappingTarget CompanyManagers companyManager);

}
