package org.projectmanagement.application.services;

import org.projectmanagement.application.dto.project_members.ProjectMemberCreateDTO;
import org.projectmanagement.application.dto.project_members.ProjectMemberMapper;
import org.projectmanagement.application.dto.project_members.ProjectMemberUpdateDTO;
import org.projectmanagement.domain.entities.ProjectMembers;
import org.projectmanagement.domain.entities.Projects;
import org.projectmanagement.domain.entities.Users;
import org.projectmanagement.domain.exceptions.ResourceNotFoundException;
import org.projectmanagement.domain.repository.ProjectMembersRepository;
import org.projectmanagement.domain.repository.ProjectsRepository;
import org.projectmanagement.domain.repository.UsersRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProjectMembersServiceImpl {
    private ProjectMembersRepository projectMembersRepository;
    private ProjectsRepository projectRepository;
    private UsersRepository usersRepository;

    ProjectMembersServiceImpl(ProjectMembersRepository projectMembersRepository,
                              ProjectsRepository projectRepository,
                              UsersRepository usersRepository
    ) {

        this.projectMembersRepository = projectMembersRepository;
        this.projectRepository = projectRepository;
        this.usersRepository = usersRepository;
    }

    public List<ProjectMembers> getAllProjectMembersByProjectId(UUID id) {
        Projects projectFromDB = projectRepository.findOneById(id).orElse(null);

        if (projectFromDB == null) {
            throw new ResourceNotFoundException("Project with id: " + id + " was not found.");
        }

        List<ProjectMembers> projectMembers = projectMembersRepository.findAllProjectMembersByProjectId(id);
        return projectMembers;
    }

    public List<ProjectMembers> getAllProjectsMemberIsPartOfByUserId(UUID userId) {
        Users userFromDB = usersRepository.findById(userId).orElse(null);

        if (userFromDB == null) {
            throw new ResourceNotFoundException("User with id: " + userId + " was not found.");
        }

        List<ProjectMembers> projectMembers = projectMembersRepository.findAllProjectsMemberIsPartOfByUserId(userId);
        return projectMembers;
    }

    public ProjectMembers createProjectMember(ProjectMemberCreateDTO dto) {
        return projectMembersRepository.save(
                ProjectMembers.builder()
                        .id(UUID.randomUUID())
                        .userId(dto.userId())
                        .projectId(dto.projectId())
                        .subscribed(dto.subscribed())
                        .createdAt(Instant.now())
                        .updatedAt(Instant.now())
                        .build()
        );
    }

    public ProjectMembers updateProjectMember(UUID id, ProjectMemberUpdateDTO dto) {

        ProjectMembers projectMemberToUpdate = projectMembersRepository.findOneById(id).orElse(null);
        if (projectMemberToUpdate == null) {
            throw new ResourceNotFoundException("Project Member with id: " + id + " was not found.");
        }

        ProjectMemberMapper.INSTANCE.toProjectMemberFromProjectMemberDTO(dto, projectMemberToUpdate);

        ProjectMembers updatedProjectMember = projectMembersRepository.save(projectMemberToUpdate);
        return updatedProjectMember;
    }


    public void deleteProjectMemberById(UUID id) {
        ProjectMembers projectMemberFromDB = projectMembersRepository.findOneById(id).orElse(null);

        if (projectMemberFromDB == null) {
            throw new ResourceNotFoundException("Project Member with id: " + id + " was not found.");
        }

        projectMembersRepository.deleteProjectMemberById(id);

    }

    public void deleteProjectMemberByProjectIdAndUserId(UUID projectId, UUID userId) {
        Projects projectFromDb = projectRepository.findOneById(projectId).orElse(null);

        if (projectFromDb == null) {
            throw new ResourceNotFoundException("Project with id: " + projectId + " was not found.");
        }

        Users userFromDB = usersRepository.findById(userId).orElse(null);

        if (userFromDB == null) {
            throw new ResourceNotFoundException("User with id: " + userId + " was not found.");
        }

        projectMembersRepository.deleteProjectMemberByProjectIdAndUserId(projectId, userId);

    }
}
