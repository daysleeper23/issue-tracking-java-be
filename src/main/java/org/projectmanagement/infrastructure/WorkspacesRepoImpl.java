package org.projectmanagement.infrastructure;

import jakarta.transaction.Transactional;
import org.projectmanagement.domain.entities.Workspaces;
import org.projectmanagement.domain.repository.WorkspacesRepoJpa;
import org.projectmanagement.domain.repository.WorkspacesRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class WorkspacesRepoImpl implements WorkspacesRepository {
    private final WorkspacesRepoJpa jpaRepo;

    public WorkspacesRepoImpl(WorkspacesRepoJpa workspaceJpaRepo) {
        this.jpaRepo = workspaceJpaRepo;
    }

    public Workspaces save(Workspaces workspace) {
        return jpaRepo.save(workspace);
    }

    public Optional<Workspaces> findById(UUID id) {
        return jpaRepo.findById(id);
    }

    public List<Workspaces> findAllWorkspaces(UUID companyId) {
        return jpaRepo.findAllByCompanyId(companyId);
    }

    @Transactional
    public Optional<Workspaces> findByIdAndUpdate(UUID id, Workspaces workspace) {
        if (jpaRepo.updateById(id, workspace) == 1) {
            return Optional.of(workspace);
        }
        return Optional.empty();
    }

    @Transactional
    public void deleteById(UUID id) {
        jpaRepo.deleteById(id);
    }
}
