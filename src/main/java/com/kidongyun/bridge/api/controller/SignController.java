package com.kidongyun.bridge.api.controller;

import com.kidongyun.bridge.api.entity.Member;
import com.kidongyun.bridge.api.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("api/v1/sign")
public class SignController {
    private MemberService memberService;

    @Autowired
    public SignController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/up")
    public ResponseEntity<?> signUp(@Valid @RequestBody Member.SignUp up) {
        /* 이미 존재하는 email 일 경우 */
        if(memberService.isExist(up.getEmail())) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "'email' is already existed");
        }

        /* 권한 설정 */
        Member member = Member.of(up);
        member.setRoles(List.of("USER"));

        /* 데이터베이스에 회원가입 정보 저장 */
        Member result = memberService.save(member).orElseThrow(() -> new HttpServerErrorException(
                HttpStatus.INTERNAL_SERVER_ERROR, "'" + member.getEmail() + "' 고객님 가입 진행 중에 오류가 발생했습니다"));

        return ResponseEntity.status(HttpStatus.OK).body(result.getEmail());
    }

    @PostMapping("/in")
    public ResponseEntity<?> signIn(@Valid @RequestBody Member.SignIn in) throws Exception {
        /* 없는 이메일인 경우 */
        if(memberService.isNotExist(in.getEmail())) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "'" + in.getEmail() + "' 계정은 존재하지 않습니다");
        }

        /* 이메일에 해당하는 회원정보 가져오기 */
        Member member = memberService.findByEmail(in.getEmail())
                .orElseThrow(() -> new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "'" + in.getEmail() + "' 계정의 회원정보를 가져올 수 없습니다"));

        /* 비밀번호가 안 맞는 경우 */
        if(memberService.isNotMatch(in.getPassword(), member.getPassword())) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다");
        }

        /* 인증 정보 저장 */
        String token = memberService.createToken(in.getEmail(), member.getRoles());

        return ResponseEntity.status(HttpStatus.OK).body(Map.of("email", member.getEmail(), "token", token));
    }

    @GetMapping
    public ResponseEntity<?> sign(Principal principal) {
        return ResponseEntity.status(HttpStatus.OK).body(principal.getName());
    }

    @GetMapping("/out")
    public ResponseEntity<?> signOut(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler()
                .logout(request, response, SecurityContextHolder.getContext().getAuthentication());

        return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK.getReasonPhrase());
    }
}
