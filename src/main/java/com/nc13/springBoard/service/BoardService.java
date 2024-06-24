package com.nc13.springBoard.service;

import com.nc13.springBoard.model.BoardDTO;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final String NAMESPACE = "com.nc13.mappers.BoardMapper";
    //한 페이지에 들어갈 글의 갯수
    private final int PAGE_SIZE = 20;

    @Autowired
    private SqlSession session;

    public List<BoardDTO> selectAll(int pageNo) {
        //(PAGE_SIZE - 1) * pageNo ~ PAGE_SIZE * pageNo - 1
        //MAPPER에 2개 값을 넘겨주는데, DTO를 만들어도 되지만 Map을 넘겨줘도 된다.
        HashMap<String, Integer> paramMap = new HashMap<>();
        paramMap.put("startRow", (pageNo - 1) * PAGE_SIZE);
        paramMap.put("size", PAGE_SIZE);

        return session.selectList(NAMESPACE + ".selectAll", paramMap);
    }

    public void insert(BoardDTO board) {
        session.insert(NAMESPACE + ".insert", board);
    }

    public BoardDTO selectOne(int id) {
        return session.selectOne(NAMESPACE + ".selectOne", id);
    }

    public void update(BoardDTO attempt) {
        session.update(NAMESPACE + ".update", attempt);
    }

    public void delete(int id) {
        session.delete(NAMESPACE + ".delete", id);
    }

    public int selectMaxPage() {
        //글의 총 개수
        int maxRow = session.selectOne(NAMESPACE + ".selectMaxRow");

        //총 페이지 개수
        return (maxRow % PAGE_SIZE == 0) ? maxRow / PAGE_SIZE : maxRow / PAGE_SIZE + 1;

    }
}
