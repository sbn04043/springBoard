package com.nc13.springBoard.controller;

import com.nc13.springBoard.model.BoardDTO;
import com.nc13.springBoard.model.ReplyDTO;
import com.nc13.springBoard.model.UserDTO;
import com.nc13.springBoard.service.BoardService;
import com.nc13.springBoard.service.ReplyService;
import com.nc13.springBoard.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.w3c.dom.html.HTMLSelectElement;

import java.io.*;
import java.net.http.HttpRequest;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Controller
@RequestMapping("/board/")
public class BoardController {
    @Autowired
    private BoardService boardService;
    @Autowired
    private ReplyService replyService;

    @GetMapping("showAll")
    public String showAll(Model model) {
        return "redirect:/board/showAll/1";
    }

    @GetMapping("showAll/{pageNo}")
    public String showAll(HttpSession session, Model model, @PathVariable int pageNo) {
        UserDTO logIn = (UserDTO) session.getAttribute("logIn");
        if (logIn == null) return "redirect:/";

        //가장 마지막 페이지 번호
        int maxPage = boardService.selectMaxPage();
        model.addAttribute("maxPage", maxPage);
        //pageNo를 사용해
        //시작 페이지 번호, 끝 페이지 번호를 계산한다.
        int startPage;
        int endPage;

        if (maxPage < 5) {
            //0. maxPage가 5 미만일 경우
            startPage = 1;
            endPage = maxPage;
        } else if (pageNo < 4) {
            //1. 현재 page 3 이하: 1 ~ 5
            startPage = 1;
            endPage = 5;
        } else if (pageNo < maxPage - 2) {
            //2. 현재 page 4 ~ (end-3): (page-2) ~ (page+2)
            startPage = pageNo - 2;
            endPage = pageNo + 2;
        } else {
            //3. 현재 page end - 2 ~ end: (end-4) ~ end
            startPage = maxPage - 4;
            endPage = maxPage;
        }

        model.addAttribute("curPage", pageNo);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        List<BoardDTO> list = boardService.selectAll(pageNo);
        model.addAttribute("list", list);

        return "board/showAll";
    }

    @GetMapping("write")
    public String write(HttpSession session) {
        UserDTO logIn = (UserDTO) session.getAttribute("logIn");
        if (logIn == null) return "redirect:/";

        return "/board/write";
    }

    @PostMapping("write")
    public String write(BoardDTO board, HttpSession session, MultipartFile[] file) {
        //file은 register에서 올린 파일들
        UserDTO logIn = (UserDTO) session.getAttribute("logIn");
        if (logIn == null) return "redirect:/";

        board.setWriterId(logIn.getId());

        String path = "c:\\uploads";

        //폴더가 없으면 만든다.(경로까지 만들 수 있음)
        File pathDir = new File(path);
        if (!pathDir.exists()) {
            pathDir.mkdirs();
        }

        //업로드 한 파일 이름을 지칭


        try {
            for (MultipartFile mf : file) {
                File f = new File(path, mf.getOriginalFilename());
                //들어온 파일을 지정한 path에 저장(복사)
                mf.transferTo(f);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        boardService.insert(board);
        return "redirect:/board/showOne/" + board.getId();
    }

    @GetMapping("showOne/{id}")
    public String showOne(@PathVariable int id, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        UserDTO logIn = (UserDTO) session.getAttribute("logIn");
        if (logIn == null) return "redirect:/";

        BoardDTO boardDTO = boardService.selectOne(id);
        if (boardDTO == null) {
            redirectAttributes.addFlashAttribute("message", "존재하지 않는 글 번호입니다.");
            return "redirect:/showMessage";
        }

        List<ReplyDTO> replyList = replyService.selectAll(id);

        model.addAttribute("replyList", replyList);
        model.addAttribute("boardDTO", boardDTO);

        return "/board/showOne";
    }

    @GetMapping("update/{id}")
    public String update(@PathVariable int id, HttpSession session, RedirectAttributes redirectAttributes, Model model) {
        UserDTO logIn = (UserDTO) session.getAttribute("logIn");
        if (logIn == null) return "redirect:/";

        BoardDTO boardDTO = boardService.selectOne(id);
        if (boardDTO == null) {
            redirectAttributes.addFlashAttribute("message", "존재하지 않는 글 번호입니다.");
            return "redirect:/showMessage";
        }

        if (boardDTO.getWriterId() != logIn.getId()) {
            redirectAttributes.addFlashAttribute("message", "권한이 없습니다.");
            return "redirect:/showMessage";
        } else {
            model.addAttribute("boardDTO", boardDTO);
            return "/board/update";
        }
    }

    @PostMapping("update/{id}")
    public String update(@PathVariable int id, HttpSession session, RedirectAttributes redirectAttributes, BoardDTO attempt) {
        UserDTO logIn = (UserDTO) session.getAttribute("logIn");
        if (logIn == null) {
            return "redirect:/";
        }

        BoardDTO boardDTO = boardService.selectOne(id);
        if (boardDTO == null) {
            redirectAttributes.addFlashAttribute("message", "존재하지 않는 글 번호입니다.");
            return "redirect:/showMessage";
        }

        if (boardDTO.getWriterId() != logIn.getId()) {
            redirectAttributes.addFlashAttribute("message", "권한이 없습니다.");
            return "redirect:/showMessage";
        }

        attempt.setId(id);
        boardService.update(attempt);
        return "redirect:/board/showOne/" + id;
    }

    @GetMapping("delete/{id}")
    public String delete(@PathVariable int id, HttpSession session, RedirectAttributes redirectAttributes) {
        UserDTO logIn = (UserDTO) session.getAttribute("logIn");
        if (logIn == null) return "redirect:/";

        BoardDTO boardDTO = boardService.selectOne(id);
        if (boardDTO == null) {
            redirectAttributes.addFlashAttribute("message", "존재하지 않는 글 번호입니다.");
            return "redirect:/showMessage";
        }

        if (boardDTO.getWriterId() != logIn.getId()) {
            redirectAttributes.addFlashAttribute("message", "삭제할 권한이 없습니다");
            return "redirect:/showMessage";
        }

        boardService.delete(id);
        return "redirect:/board/showAll";
    }

    //일반 컨트롤러 내부의 Restful API로써,
    //JSON의 결과값을 리턴해야하는 경우
    //맵핑 어노테이션 위에 ResponseBody 어노테이션을 붙여준다.
    @ResponseBody
    @PostMapping("uploads")
    public Map<String, Object> uploads(MultipartHttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        String uploadPath = "";

        MultipartFile file = request.getFile("upload");
        String fileName = file.getOriginalFilename();

        //aaa.text, bbb.jpg 등으로 들어오는 파일의 확장자 명을 가져온다.(text, jpg)
        String extension = fileName.substring(fileName.lastIndexOf("."));
        String uploadName = UUID.randomUUID() + extension;

        //톰캣의 실제 주소 찾는 방법
        String realPath = request.getServletContext().getRealPath("/board/uploads/");
        Path realDir = Paths.get(realPath);
        if (!Files.exists(realDir)) {
            try {
                Files.createDirectories(realDir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        File uploadFile = new File(realPath + uploadName);
        try {
            file.transferTo(uploadFile);
        } catch (IOException e) {
            System.out.println("파일 전송 중 에러");
            e.printStackTrace();
        }

        uploadPath = "/board/uploads/" + uploadName;

        resultMap.put("uploaded", true);
        resultMap.put("url", uploadPath);

        return resultMap;
    }
}
