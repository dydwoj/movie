package com.example.movie.dto;

import java.time.LocalDateTime;

import com.example.movie.entity.MemberRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class MemberDTO {

    private Long mid;

    @Email(message = "Check your email address")
    @NotBlank(message = "Insert your email")
    private String email;
    @NotBlank(message = "Insert your password")
    private String password;
    @NotBlank(message = "Insert your nickname")
    private String nickName;

    private MemberRole memberRole;

    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

}
