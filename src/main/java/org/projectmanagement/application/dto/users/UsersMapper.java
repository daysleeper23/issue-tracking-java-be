package org.projectmanagement.application.dto.users;

import org.mapstruct.factory.Mappers;
import org.projectmanagement.domain.entities.Users;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UsersMapper {
    UsersMapper INSTANCE = Mappers.getMapper(UsersMapper.class);

    public static UsersRead toUsersRead(Users user) {
        return new UsersRead(user.getId(), user.getName(), user.getEmail(), user.getPasswordHash(), user.getTitle(), user.getIsActive(), user.getCompanyId(), user.getIsOwner(), user.getUpdatedAt());
    }

    void toUsersFromUsersCreate(UsersCreate userCreate, @MappingTarget Users user);
}
