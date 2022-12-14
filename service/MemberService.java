package com.sparta.post03.service;


import com.sparta.post03.dto.request.LoginRequestDto;
import com.sparta.post03.dto.response.ResponseDto;
import com.sparta.post03.dto.TokenDto;
import com.sparta.post03.dto.request.MemberRequestDto;
import com.sparta.post03.dto.response.MemberResponseDto;
import com.sparta.post03.entity.RefreshToken;
import com.sparta.post03.entity.Member;
import com.sparta.post03.exception.entityException.MemberException.BadPasswordConfirmException;
import com.sparta.post03.exception.entityException.MemberException.BadPasswordException;
import com.sparta.post03.exception.entityException.MemberException.DuplicateMemberException;
import com.sparta.post03.exception.entityException.MemberException.MemberNotFoundException;
import com.sparta.post03.exception.entityException.PostException.AccessTokenNotExistException;
import com.sparta.post03.exception.entityException.PostException.TokenInvalidException;
import com.sparta.post03.jwt.provider.JwtProvider;
import com.sparta.post03.repository.RefreshTokenRopository;
import com.sparta.post03.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    private final RefreshTokenRopository refreshTokenRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final JwtProvider jwtProvider;

    //????????? ???????????? ????????? ????????? ??????????????? method
    public Member isPresentMember(String username){
        Optional<Member> optionalMember = memberRepository.findByUsername(username);
        return optionalMember.orElse(null);
    }
    //????????????
    @Transactional
    public ResponseEntity<?> registerUser(MemberRequestDto memberRequestDto){

        //????????????
        if(null != isPresentMember(memberRequestDto.getUsername())){
            throw new DuplicateMemberException();
        }

        //???????????? ??????
        if(!memberRequestDto.getPassword().equals(memberRequestDto.getPasswordConfirm())){
            throw new BadPasswordConfirmException();
//            return new ResponseEntity<>(ErrorCode.BAD_PASSWORD_COMFIRM.getMessage(), HttpStatus.CONFLICT);
        }

        Member member = Member.builder()
                .username(memberRequestDto.getUsername())
                .password(passwordEncoder.encode(memberRequestDto.getPassword()))
                .build();
        memberRepository.save(member);
        return ResponseEntity.ok().body(ResponseDto.success(
                MemberResponseDto.builder()
                        .id(member.getId())
                        .username(member.getUsername())
                        .createdAt(member.getCreatedAt())
                        .modifiedAt(member.getModifiedAt())
                        .build()
        ));
    }

    //?????????
    public ResponseDto<?> login(LoginRequestDto loginRequestDto, HttpServletResponse httpServletResponse) {

        Member member = isPresentMember(loginRequestDto.getUsername());

        //???????????? ????????? ??????
        if(null == member){
              throw new MemberNotFoundException();
//            return ResponseDto.fail("MEMBER_NOT_FOUND", "???????????? ?????? ??? ????????????.");
        }

        //??????????????? ????????? ??????
        if(!member.validatePassword(passwordEncoder, loginRequestDto.getPassword())){
              throw new BadPasswordException();
//            return ResponseDto.fail("INVALID_MEMBER", "???????????? ????????? ????????????.");
        }


        // 1. Login ID/PW ??? ???????????? AuthenticationToken ??????
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword());

        // 2. ????????? ?????? (????????? ???????????? ??????) ??? ??????????????? ??????
        //    authenticate ???????????? ????????? ??? ??? CustomUserDetailsService ?????? ???????????? loadUserByUsername ???????????? ?????????
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        TokenDto tokenDto = jwtProvider.generateTokenDto(authentication);

        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        httpServletResponse.addHeader("Access-Token", tokenDto.getGrantType() + " " + tokenDto.getAccessToken());
        httpServletResponse.addHeader("Refresh-Token", tokenDto.getRefreshToken());

        return ResponseDto.success(
                MemberResponseDto.builder()
                        .id(member.getId())
                        .username(member.getUsername())
                        .createdAt(member.getCreatedAt())
                        .modifiedAt(member.getModifiedAt())
                        .build()
        );
    }
}
