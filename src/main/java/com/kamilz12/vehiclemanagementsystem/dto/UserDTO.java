package com.kamilz12.vehiclemanagementsystem.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class UserDTO {
    @NotNull
    @Size(min = 1, message = "size >1")
    private String username;
    @Size(min = 1, message = "size >1")
    @NotNull
    private String password;

}
