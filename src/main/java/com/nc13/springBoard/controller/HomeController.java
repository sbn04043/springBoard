package com.nc13.springBoard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

//해당 클래스를 스프링 프레임워크가
//직접 생성/초기화 할 수 있도록 어노테이션을 붙여준다.
//해당 컨트롤러의 경우, 사용자가 특정 주소를 접속할 때
//어느 페이지를 보여줄 지를 정해주는
//@Controller 어노테이션을 사용한다.
@Controller
public class HomeController {
    //스프링은 URL 기반으로 특정 페이지의 실행 여부를 결정한다.
    //사용자가 특정 URL을 접속했을 때, 어떤 메소드를
    //실행시킬지 연결(mapping)시켜야 한다.
    //1. @RequestMapping(주소, 연결 방식)
    //2. @GetMapping(주소)
    //3. @PostMapping(주소)
    //과거엔 1번만을 사용했지만
    //이제는 2번 3번을 주로 사용한다.

    //맨 처음 인덱스페이지
    @GetMapping("/")
    public String showIndex() {
        //특정 페이지를 실행시키는 메소드는
        //해당 페이지의 폴더명 + 파일이름을
        //리턴한다.
        return "index";
    }
}
