package org.projectmanagement.application.services;

import org.projectmanagement.application.dto.ProjectMember.ProjectMemberCreateDTO;
import org.projectmanagement.application.dto.ProjectMember.ProjectMemberMapper;
import org.projectmanagement.application.dto.ProjectMember.ProjectMemberUpdateDTO;
import org.projectmanagement.domain.entities.ProjectMembers;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProjectMembersServiceImpl {

    public List<ProjectMembers> getMembersByProjectId(UUID id) {
        List<ProjectMembers> pr = null; // send Id to the repo then to db to get projectmembmerslist.
        return pr;
    }

    public ProjectMembers createProjectMember(ProjectMemberCreateDTO dto) {
        ProjectMembers projectMember = ProjectMemberMapper.createDTOtoProjectMembers(dto);
        ProjectMembers createdProjectMember = null; //call to repo and save to db return the whole object.
        return createdProjectMember;
    }

    public ProjectMembers updateProjectMember(UUID id,ProjectMemberUpdateDTO dto) {
        ProjectMembers projectMembers = ProjectMemberMapper.updateDTOtoProjectMembers(id, dto);
        ProjectMembers updatedProjectMember = null; //call to repo and save to db return the whole object.
        return updatedProjectMember;
    }

    //what should it return to rest controller?
    public void deleteProjectMember(UUID id) {
        //send id to repo and delete the source.
    }
}
