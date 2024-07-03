package com.nc13.springBoard.controller;

import com.nc13.springBoard.model.UserDTO;
import com.nc13.springBoard.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/user/")
@RequiredArgsConstructor
public class UserController {
    //실제 SQL 통신을 담당할 Service 객체
    @Autowired
    private UserService userService;
    @Autowired
    private BCryptPasswordEncoder encoder;

    //사용자가 로그인 할 때 실행하는 auth 메소드
    //POST GET 방식으로 웹페이지의 값을 받을 땐
    //파라미터에 해당 form의 name 어트리뷰트와 같은 일믕르 가진
    //파라미터를 적으면 된다.
    //해당 name 어트리뷰트를 필드로 가진 클래스 객체를 파라미터로 잡으면
    //자동으로 데이터가 비인딩 된다.
    @PostMapping("auth")
    public String auth(UserDTO user, HttpSession session) {
        System.out.println("userController.auth()");
        UserDTO result = userService.auth(user);
        if (result != null) {
            session.setAttribute("logIn", result);
            return "redirect:/board/showAll";
        }
        //메소드 실행 후 특정 URL로 이동
        return "redirect:/";
    }

    @GetMapping("register")
    public String showRegister() {
        return "user/register";
    }

    @PostMapping("register")
    public String register(UserDTO user, RedirectAttributes redirectAttributes) {
        if (userService.validateUsername(user.getUsername())) {
            user.setPassword(encoder.encode(user.getPassword()));
            userService.register(user);
            System.out.println("회원가입 성공");
        } else {
            //회원가입 실패 시, /error라는 곳으로 전송하되
            //어떤 에러인지 알 수 있게 메세지를 담아 보낸다.
            //다른 URL에 값을 담아 보내려면 RedirectAttributes 사용
            //addAttribute처럼 session에 저장하지만 다음 페이지에 보낸 후 지운다.
            redirectAttributes.addFlashAttribute("message", "중복된 아이디로는 가입하실 수 없습니다");

            return "redirect:/showMessage";
        }
        return "redirect:/";
    }


}
