package org.projectmanagement.application.dto.invitations;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface InvitationsMapper {

    InvitationsMapper mapper = Mappers.getMapper(InvitationsMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    org.projectmanagement.domain.entities.Invitations dtoToEntity(Invitations invitationsDTO);

    List<InvitationsInfo> entitiesToInvitationInfos(List<org.projectmanagement.domain.entities.Invitations> invitations);
}
