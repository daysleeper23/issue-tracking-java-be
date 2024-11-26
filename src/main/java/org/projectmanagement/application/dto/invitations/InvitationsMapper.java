package org.projectmanagement.application.dto.invitations;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.projectmanagement.domain.entities.Invitations;

import java.util.List;

@Mapper
public interface InvitationsMapper {

    InvitationsMapper mapper = Mappers.getMapper(InvitationsMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Invitations dtoToEntity(InvitationsCreate invitationsCreateDTO);

    List<InvitationsInfo> entitiesToInvitationInfos(List<org.projectmanagement.domain.entities.Invitations> invitations);

    @Mapping(target = "isAdmin", source = "admin")
    InvitationsInfo entityToInvitationInfo(Invitations invitations);
}
