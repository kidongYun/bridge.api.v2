package com.kidongyun.bridge.api.service;

import com.kidongyun.bridge.api.entity.Member;
import com.kidongyun.bridge.api.repository.member.MemberRepository;
import com.kidongyun.bridge.api.security.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class MemberService implements UserDetailsService {
    private MemberRepository memberRepository;
    private PasswordEncoder encoder;
    private TokenProvider tokenProvider;

    @Autowired
    public MemberService(MemberRepository memberRepository, PasswordEncoder encoder, TokenProvider tokenProvider) {
        this.memberRepository = memberRepository;
        this.encoder = encoder;
        this.tokenProvider = tokenProvider;
    }

    public Optional<Member> findByEmail(String email) throws Exception {
        if(Objects.isNull(email)) {
            return Optional.empty();
        }

        return memberRepository.findByEmail(email);
    }

    public Optional<Member> save(Member member) {
        if(Objects.isNull(member)) {
            return Optional.empty();
        }

        /* 패스워드 암호화 */
        member.setPassword(encoder.encode(member.getPassword()));

        return Optional.of(memberRepository.save(member));
    }

    public boolean isExist(String email) {
        if(Objects.isNull(email)) {
            return false;
        }

        return memberRepository.existsById(email);
    }

    public String encode(String plain) {
        if(Strings.isEmpty(plain)) {
            return null;
        }

        return encoder.encode(plain);
    }

    public boolean isMatch(String plain, String encoded) {
        if(Strings.isBlank(plain) || Strings.isBlank(encoded)) {
            return false;
        }

        return encoder.matches(plain, encoded);
    }

    public boolean isNotMatch(String plain, String encoded) {
        return !isMatch(plain, encoded);
    }

    public boolean isNotExist(String email) {
        return !this.isExist(email);
    }

    public String createToken(String email, List<String> roles) {
        return tokenProvider.createToken(email, roles);
    }

    @Override
    public Member loadUserByUsername(String email) throws UsernameNotFoundException {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
    }
}
