package com.tma.userservice.service;

import com.tma.commonservice.constants.EmailConstant;
import com.tma.commonservice.dto.email.EmailId;
import com.tma.commonservice.dto.user.UserDto;
import com.tma.commonservice.entity.User;
import com.tma.commonservice.service.EmailService;
import com.tma.userservice.email.UserRegisterEmailTemplate;
import com.tma.userservice.exception.BadRequestException;
import com.tma.userservice.exception.ResourceNotFoundException;
import com.tma.userservice.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final EmailService emailService;

    public UserDto register(UserDto userDto) {
        validateUser(userDto);
        User user = mapToEntity(userDto);
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setRoles(Collections.singleton("ROLE_USER"));
        User savedUser = userRepository.save(user);
        sendEmail(userDto);
        return mapToDto(savedUser);
    }

    public UserDto addUser(UserDto userDto) {
        validateUser(userDto);
        User user = mapToEntity(userDto);
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
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

    public UserDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        return mapToDto(user);
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

    private void sendEmail(UserDto userDto) {
        UserRegisterEmailTemplate template = new UserRegisterEmailTemplate(EmailConstant.REGISTER_USER_EMAIL_TEMPLATE, userDto);
        EmailId toEmail = new EmailId(userDto.getName(), userDto.getEmail());
        EmailId fromEmail =  new EmailId("", EmailConstant.NO_REPLY);
        String subject = EmailConstant.REGISTER_USER_EMAIL_SUBJECT;
        emailService.sendMail(template, toEmail, fromEmail, subject);
    }

    private UserDto mapToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    private User mapToEntity(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }

}
