package com.nc13.springBoard.service;

//Service
//컨트롤러는 사용자가 보낸 요청을 처리와 페이지 이동만 처리하고
//데이터베이스와의 통신은 Service가 담당할 것이다.

import com.nc13.springBoard.model.UserDTO;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    //스프링 프레임워크가 관리하는 객체를 불러올 때는
    //@Autowired라 적는다.
    @Autowired
    private final SqlSession SESSION;

    private final String NAMESPACE = "com.nc13.mappers.UserMapper";

    //로그인에 사용할 auth 메소드
    public UserDTO auth(UserDTO attempt) {
        //UserMapper.xml에 있는 쿼리를 SESSION이 실행하게 코드 작성

        //결과가 1개만 나와야 하니 SESSION의 selectOne을 실행시키고
        //실행시킬 쿼리의 id값과 파라미터를 보낸다.
        return SESSION.selectOne(NAMESPACE + ".auth", attempt);
    }

    public boolean validateUsername(String username) {
        return SESSION.selectOne(NAMESPACE + ".selectByUsername", username) == null;
    }

    public void register(UserDTO attempt) {
        SESSION.insert(NAMESPACE + ".register", attempt);
        //return SESSION
    }
}
