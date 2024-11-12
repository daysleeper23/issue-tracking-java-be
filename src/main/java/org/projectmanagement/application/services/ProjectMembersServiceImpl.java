package org.projectmanagement.application.services;

import org.projectmanagement.application.dto.ProjectMember.ProjectMemberCreateDTO;
import org.projectmanagement.domain.entities.ProjectMembers;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class ProjectMembersServiceImpl {

    public List<ProjectMembers> getMembersByProjectId(UUID id) {
        List<ProjectMembers> pr = null;
        return pr;
    }

    public ProjectMembers createProjectMember(ProjectMemberCreateDTO dto) {
        ProjectMembers projectMember = ProjectMembers.builder()
                .userId(dto.getUserId())
                .projectId(dto.getProjectId())
                .subscribed(dto.getSubscribed())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
        ProjectMembers createdProjectMember = null; //call to repo and save to db
        return createdProjectMember;
    }
}
