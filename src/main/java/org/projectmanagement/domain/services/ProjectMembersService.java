package org.projectmanagement.domain.services;

import org.projectmanagement.application.dto.project_members.ProjectMemberCreateDTO;
import org.projectmanagement.domain.entities.ProjectMembers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectMembersService {

    List<ProjectMembers> getAllProjectMembersByProjectId( UUID projectId);

    List<ProjectMembers> getAllProjectsMemberIsPartOfByUserId(UUID userId);

    Optional<ProjectMembers> createProjectMember(ProjectMemberCreateDTO projectMember);

    Optional<ProjectMembers> updateProjectMember(ProjectMemberCreateDTO projectMember);

    void deleteProjectMemberById(UUID id);

    void deleteMemberFromAllProjectsByUserId(UUID userId);

}
