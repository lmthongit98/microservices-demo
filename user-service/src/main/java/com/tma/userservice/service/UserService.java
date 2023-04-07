package com.tma.userservice.service;

import com.tma.userservice.dto.UserDto;
import com.tma.userservice.entity.User;
import com.tma.userservice.exception.BadRequestException;
import com.tma.userservice.exception.ResourceNotFoundException;
import com.tma.userservice.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserDto register(UserDto userDto) {
        validateUser(userDto);
        User user = mapToEntity(userDto);
        user.setRoles(Collections.singleton("ROLE_USER"));
        User savedUser = userRepository.save(user);
        return mapToDto(savedUser);
    }

    public UserDto addUser(UserDto userDto) {
        validateUser(userDto);
        User user = mapToEntity(userDto);
        User savedUser = userRepository.save(user);
        return mapToDto(savedUser);
    }

    public List<UserDto> getAll() {
        return userRepository.findAll().stream().map(this::mapToDto).toList();
    }

    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found for id " + id));
    }

    public void delete(Long id) {
        User user = findUserById(id);
        userRepository.delete(user);
    }

    public void update(UserDto userDto) {
        User user = findUserById(userDto.getId());
        modelMapper.typeMap(UserDto.class, User.class)
                .addMappings(mapper -> mapper.skip(User::setPassword)).map(userDto, user);
        userRepository.save(user);
    }

    private void validateUser(UserDto user) {
        if(!(StringUtils.isNotBlank(user.getUsername()) && StringUtils.isNotBlank(user.getPassword()))) {
            throw new BadRequestException("Username or password is invalid!");
        }
        boolean isUsernameExist = userRepository.existsByUsername(user.getUsername());
        if (isUsernameExist) {
            throw new BadRequestException("Username is exist!");
        }
    }

    private UserDto mapToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    private User mapToEntity(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }

}
