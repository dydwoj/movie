package com.example.movie.dto;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
// 인증 받은 사용자는 User를 상속받아서 아래처럼 만들어서 사용한다
public class AuthMemberDTO extends User {

    private MemberDTO memberDTO;

    // username : id 개념
    public AuthMemberDTO(String username, String password,
            Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public AuthMemberDTO(MemberDTO memberDTO) {
        super(memberDTO.getEmail(), memberDTO.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + memberDTO.getMemberRole())));
        this.memberDTO = memberDTO;
    }

}
