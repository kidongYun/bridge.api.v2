package com.kidongyun.bridge.api.service;

import com.kidongyun.bridge.api.entity.Member;
import com.kidongyun.bridge.api.repository.member.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Objects;

@Slf4j
@Service
public class MemberService implements UserDetailsService {
    private MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
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

        return memberRepository.save(member);
    }

    public boolean isExist(String email) {
        if(Objects.isNull(email)) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "'email' parameter must not be null");
        }

        return memberRepository.existsById(email);
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
