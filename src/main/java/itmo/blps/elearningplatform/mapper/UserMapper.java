package itmo.blps.elearningplatform.mapper;

import itmo.blps.elearningplatform.dto.user.AuthenticationRequest;
import itmo.blps.elearningplatform.dto.user.RegistrationRequest;
import itmo.blps.elearningplatform.dto.user.UserDto;
import itmo.blps.elearningplatform.model.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper extends EntityMapper<UserDto, User> {

    User toEntity(RegistrationRequest registrationRequest);

    User toEntity(AuthenticationRequest authenticationRequest);
}
