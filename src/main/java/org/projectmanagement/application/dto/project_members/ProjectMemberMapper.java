package org.projectmanagement.application.dto.ProjectMember;

import org.projectmanagement.domain.entities.ProjectMembers;

import java.time.Instant;
import java.util.UUID;

public class  ProjectMemberMapper {

    public static ProjectMembers createDTOtoProjectMembers(ProjectMemberCreateDTO dto) {
        return ProjectMembers.builder()
                .userId(dto.getUserId())
                .projectId(dto.getProjectId())
                .subscribed(dto.getSubscribed())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

    }

    public static ProjectMembers updateDTOtoProjectMembers(UUID id, ProjectMemberUpdateDTO dto) {
        return ProjectMembers.builder()
                .id(id)
                .subscribed(dto.getSubscribed())
                .build();
    }
}
