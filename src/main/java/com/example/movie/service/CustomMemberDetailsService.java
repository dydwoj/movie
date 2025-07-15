package com.example.movie.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.movie.dto.AuthMemberDTO;
import com.example.movie.dto.MemberDTO;
import com.example.movie.dto.PasswordDTO;
import com.example.movie.entity.Member;
import com.example.movie.repository.MemberRepository;
import com.example.movie.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Service
@Log4j2
public class CustomMemberDetailsService implements UserDetailsService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입 처리 메서드
    public void register(MemberDTO dto) throws IllegalStateException {
        // 중복확인
        validateEmail(dto.getEmail());
        // dto => entity
        Member member = Member.builder()
                .email(dto.getEmail())
                // 비밀번호 암호화
                .password(passwordEncoder.encode(dto.getPassword()))
                .nickName(dto.getNickName())
                .memberRole(dto.getMemberRole())
                .build();
        memberRepository.save(member);

    }

    // 이메일 중복 여부
    private void validateEmail(String email) {
        Member member = memberRepository.findByEmail(email);

        // IllegalStateException : RuntimeException (실행해야 나오는 예외)
        if (member != null) {
            throw new IllegalStateException("This email address is already used");
        }
    }

    // 로그인 처리 메서드
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        log.info("usernameTestRIGHTNOW : {}", email);

        Member member = memberRepository.findByEmail(email);

        if (member == null)
            throw new UsernameNotFoundException("이메일 확인");

        // entity => dto
        MemberDTO memberDTO = MemberDTO.builder()
                .mid(member.getMid())
                .email(member.getEmail())
                .password(member.getPassword())
                .nickName(member.getNickName())
                .memberRole(member.getMemberRole())
                .build();

        AuthMemberDTO authMemberDTO = new AuthMemberDTO(memberDTO);

        return authMemberDTO;
    }

    // 닉네임 변경
    @Transactional
    public void updateNickName(MemberDTO dto) {
        // 방법 1
        // 수정하고자 하는 멤버 조회 -> 수정 부분 변경 -> save
        /*
         * Member member = memberRepository.findByEmail(null);
         * member.changeNickName(null);
         * memberRepository.save(member);
         */
        // ======================================================================
        // 방법 2
        // 리파지토리에 메서드 생성
        memberRepository.updateNickName(dto.getNickName(), dto.getEmail());
    }

    // 비밀번호 변경
    public void updatePassword(PasswordDTO passwordDTO) throws IllegalStateException {
        // 수정하고자 하는 멤버 조회 -> 수정 부분 변경 -> save
        Member member = memberRepository.findByEmail(passwordDTO.getEmail());
        // 현재 비밀번호가 맞는지 확인
        if (!passwordEncoder.matches(passwordDTO.getCurrentPassword(), member.getPassword())) {
            throw new IllegalStateException("Password is not matches");
        } else {
            // 맞다면 수정
            member.changePassword(passwordEncoder.encode(passwordDTO.getNewPassword()));
            memberRepository.save(member);
        }
    }

    // 회원탈퇴
    @Transactional
    public void leaveMember(MemberDTO memberDTO) throws IllegalStateException {
        // 비밀번호가 일치하는지 확인
        Member member = memberRepository.findByEmail(memberDTO.getEmail());
        // 현재 비밀번호가 맞는지
        if (!passwordEncoder.matches(memberDTO.getPassword(), member.getPassword())) {
            throw new IllegalStateException("Password is not matches");
        } else {
            // 일치 => 리뷰제거, 회원탈퇴
            reviewRepository.deleteByMember(member);
            memberRepository.delete(member);
        }
    }

}
