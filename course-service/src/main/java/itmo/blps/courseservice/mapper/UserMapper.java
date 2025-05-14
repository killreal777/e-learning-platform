package itmo.blps.courseservice.mapper;

import itmo.blps.courseservice.dto.user.AuthenticationRequest;
import itmo.blps.courseservice.dto.user.RegistrationRequest;
import itmo.blps.courseservice.dto.user.UserDto;
import itmo.blps.courseservice.model.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper extends EntityMapper<UserDto, User> {

    User toEntity(RegistrationRequest registrationRequest);

    User toEntity(AuthenticationRequest authenticationRequest);
}
