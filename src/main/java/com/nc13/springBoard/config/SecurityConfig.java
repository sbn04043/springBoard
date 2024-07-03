package com.nc13.springBoard.config;

import com.nc13.springBoard.service.UserAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    //웹페이지에 대한 접근 권한 설정
    //1. 누구나 접근 가능
    //2. 특정 인원 접근 가능(관리자, 로그인 유저 등)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, UserAuthService userAuthService) throws Exception {
        //CSRF(Cross Site Request Forgery attack: 크로스 사이트 요청 위조 공격) 방지
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                //URL별 권한 설정
                .authorizeHttpRequests((authorize) -> authorize
                        //WEB-INF 폴더 안의 views 안의 모든 JSP 파일은 누구든 접근 가능
                        //**: 폴더 안의 모든 파일들 적용
                        //*: 폴더 안의 직속 파일들만 적용
                        .requestMatchers("/WEB-INF/**").permitAll()
                        //images 폴더 파일 누구든 접근 가능케
                        .requestMatchers("/images/**").permitAll()
                        //localhost:8080/user/* localhost:8080/*는 누구든 접근 가능
                        .requestMatchers("/user/*", "/").permitAll()
                        // /board/write는 ADMIN 사용자만 접근 가능
                        .requestMatchers("/board/write").hasAnyAuthority("ROLE_ADMIN")
                        //나머지는 인증된 유저만 접근 가능
                        .anyRequest().authenticated()
                )
                //커스텀 로그인 설정
                .formLogin((formLogIn) -> formLogIn
                        //로그인에서 사용할 페이지 설정
                        .loginPage("/")
                        //로그인 페이지에서 username을 어떤 name 어트리뷰트로 넘겨줄지 설정
                        .usernameParameter("username")
                        //password를 어떤 name 어트리뷰트로 넘겨줄지 설정
                        .passwordParameter("password")
                        //로그인 성공 시 이동할 페이지
                        .defaultSuccessUrl("/board/showAll/1")
                        //로그인 처리 URL
                        .loginProcessingUrl("/user/auth"))
                //만든 UserAuthService 등록
                .userDetailsService(userAuthService);

        return httpSecurity.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    public void configure(WebSecurity web) throws Exception {
        web.httpFirewall(defaultHttpFirewall());
    }

    @Bean
    public HttpFirewall defaultHttpFirewall() {
        return new DefaultHttpFirewall();
    }
}
