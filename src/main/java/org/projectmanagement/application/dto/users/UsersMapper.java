package org.projectmanagement.application.dto.users;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.projectmanagement.domain.entities.Users;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UsersMapper {
    UsersMapper INSTANCE = Mappers.getMapper(UsersMapper.class);

    static UsersRead toUsersRead(Users user) {
        return new UsersRead(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPasswordHash(),
                user.getTitle(),
                user.getIsActive(),
                user.getCompanyId(),
                user.getIsOwner(),
                user.getAvatarUrl(),
                user.getUpdatedAt(),
                user.getCreatedAt());
    }

    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "companyId", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toUsersFromUsersUpdate(UsersUpdate userUpdate, @MappingTarget Users user);
}
