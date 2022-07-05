package com.kma.pich.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    @NotEmpty(message = "Email can't be empty")
    @Pattern(regexp = "[.A-Za-z\\d]+@[\\-A-Za-z\\d]+\\.[\\-A-Za-z\\d]+(\\.[\\-A-Za-z\\d]+)*", message = "Must be valid email")
    @Length(max = 40, message = "Email can't be longer than 40 characters")
    private String username;

    @NotEmpty(message = "Password can't be empty")
    @Length(min = 8, max = 40, message = "Password should be from 8 to 40 characters")
    private String password;

}
