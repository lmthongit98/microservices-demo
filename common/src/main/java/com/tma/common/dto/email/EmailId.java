package com.tma.common.dto.email;

//import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
public class EmailId {
    private String fullName;
//    @Pattern(
//            regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$",
//            message = "Invalid email format(example:abc@test.com)"
//    )
    private String email;

    public EmailId() {
    }

    public EmailId(String fullName, String email) {
        this.fullName = fullName;
        this.email = email;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

