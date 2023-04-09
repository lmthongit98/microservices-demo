package com.tma.userservice.email;

import com.tma.common.dto.email.EmailTemplate;
import com.tma.common.dto.user.UserDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegisterEmailTemplate  extends EmailTemplate {

    private String templateName;
    private String username;
    private String password;

    public UserRegisterEmailTemplate(String templateName, UserDto userDto) {
        this.templateName =  templateName;
        this.username = userDto.getUsername();
        this.password = userDto.getPassword();
    }

    @Override
    public String getTemplateName() {
        return templateName;
    }
}
