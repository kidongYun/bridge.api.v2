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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("api/v1/sign")
@Transactional
public class SignController {
    MemberService memberService;

    @Autowired
    public SignController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/up")
    public ResponseEntity<?> signUp(@RequestBody Member.Post post) {
        /* 이미 존재하는 email 일 경우 */
        if(memberService.isExist(post.getEmail())) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "'email' is already existed");
        }

        /* 권한 설정 */
        Member member = Member.of(post);
        member.setAuth("USER");

        return ResponseEntity.status(HttpStatus.OK).body(memberService.save(member).getEmail());
    }

    @PostMapping("/in")
    public ResponseEntity<?> signIn(@RequestBody Member.Post post) throws Exception {
        if(Objects.isNull(post) || Objects.isNull(post.getEmail()) || Objects.isNull(post.getPassword())) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "'email', 'password' must not be null");
        }

        /* 없는 이메일인 경우 */
        if(memberService.isNotExist(post.getEmail())) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "'email' is not existed");
        }

        /* 비밀번호가 안 맞는 경우 */
        Member member = memberService.findByEmail(post.getEmail());
        if(!Objects.equals(post.getPassword(), member.getPassword())) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "'password' is not matched");
        }

        return ResponseEntity.status(HttpStatus.OK).body(member.getEmail());
    }

    @GetMapping("/out")
    public ResponseEntity<?> signOut(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler()
                .logout(request, response, SecurityContextHolder.getContext().getAuthentication());

        return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK.getReasonPhrase());
    }
}
