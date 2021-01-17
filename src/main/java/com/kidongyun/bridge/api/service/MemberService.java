package com.kidongyun.bridge.api.service;

import com.kidongyun.bridge.api.aspect.ExecuteLog;
import com.kidongyun.bridge.api.entity.Member;
import com.kidongyun.bridge.api.repository.member.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Objects;

@Slf4j
@Service
public class MemberService implements UserDetailsService {
    private MemberRepository memberRepository;
    private PasswordEncoder encoder;

    @Autowired
    public MemberService(MemberRepository memberRepository, PasswordEncoder encoder) {
        this.memberRepository = memberRepository;
        this.encoder = encoder;
    }

    public Member findByEmail(String email) throws Exception {
        if(Objects.isNull(email)) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "'email' parameter must not be null");
        }

        return memberRepository.findByEmail(email).orElseThrow(Exception::new);
    }

    public Member save(Member member) {
        if(Objects.isNull(member)) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "'member' parameter must not be null");
        }

        /* 패스워드 암호화 */
        member.setPassword(encoder.encode(member.getPassword()));

        return memberRepository.save(member);
    }

    public boolean isExist(String email) {
        if(Objects.isNull(email)) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "'email' parameter must not be null");
        }

        return memberRepository.existsById(email);
    }

    public String encode(String plain) {
        if(Strings.isEmpty(plain)) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "'plain' parameter must not be empty");
        }

        return encoder.encode(plain);
    }

    public boolean isMatch(String plain, String encoded) {
        if(Strings.isEmpty(plain) || Strings.isEmpty(encoded)) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "'plain', 'encoded' parameter must not be empty");
        }

        return encoder.matches(plain, encoded);
    }

    public boolean isNotMatch(String plain, String encoded) {
        return !isMatch(plain, encoded);
    }

    public boolean isNotExist(String email) {
        return !this.isExist(email);
    }

    @Override
    public Member loadUserByUsername(String email) throws UsernameNotFoundException {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
    }
}
