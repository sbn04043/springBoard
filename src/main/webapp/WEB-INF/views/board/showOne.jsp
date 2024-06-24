<%@page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<html>
<head>
    <title>${boardDTO.id}번 게시글</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
          rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
          crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
            crossorigin="anonymous">
    </script>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<body>
<div class="container-fluid">
    <div class="row justify-content-center">
        <div class="col-6">
            <table class="table table-striped">
                <tr>
                    <th>글 번호</th>
                    <th colspan="2">${boardDTO.id}</th>
                </tr>
                <tr>
                    <th>제목</th>
                    <th colspan="2">${boardDTO.title}</th>
                </tr>
                <tr>
                    <th>작성자</th>
                    <th colspan="2">${boardDTO.nickname}</th>
                </tr>
                <tr>
                    <th>작성일</th>
                    <th colspan="2"><fmt:formatDate value="${boardDTO.entryDate}"
                                                    pattern="yyyy년 MM월 dd일 E요일 HH시 mm분 ss초"/></th>
                </tr>
                <tr>
                    <th>수정일</th>
                    <th colspan="2"><fmt:formatDate value="${boardDTO.modifyDate}"
                                                    pattern="yyyy년 MM월 dd일 E요일 HH시 mm분 ss초"/></th>
                </tr>
                <tr class="text-center">
                    <td colspan="3">내용</td>
                </tr>
                <tr>
                    <td colspan="3">${boardDTO.content}</td>
                </tr>
                <c:if test="${boardDTO.writerId eq logIn.id}">
                    <tr>
                        <td class="text-start">
                            <a class="btn btn-outline-success" href="/board/update/${boardDTO.id}">수정하기
                            </a></td>
                        <td class="text-end">
                            <button class="btn btn-outline-danger" onclick="deleteBoard(${boardDTO.id})">삭제하기
                            </button>
                        </td>
                    </tr>
                </c:if>
                <tr>
                    <td colspan="2" class="text-center">
                        <a class="btn btn-outline-secondary" href="/board/showAll">목록으로</a>
                    </td>
                </tr>
            </table>

            <table class="table table-primary table-striped">
                <tr class="text-center">
                    <td class="col">댓글</td>
                </tr>
                <c:forEach items="${replyList}" var="reply">
                    <tr>
                        <td>${reply.id}</td>
                        <td>${reply.nickname}</td>
                        <c:choose>
                            <c:when test="${reply.writerId eq logIn.id}">
                                <form action="/reply/update/${reply.id}" method="post">
                                    <td>
                                        <input type="text" class="form-control" name="content"
                                               value="${reply.content}">
                                    </td>
                                    <td>
                                        <span>
                                            AT <fmt:formatDate value="${reply.modifyDate}" pattern="y년M월d일"/>
                                        </span>
                                    </td>
                                    <td>
                                        <input type="submit" class="btn btn-outline-primary" value="수정">
                                    </td>
                                    <td>
                                        <a href="reply/delete/${reply.id}" class="btn btn-outline-warning">삭제</a>
                                    </td>
                                </form>
                            </c:when>
                            <c:otherwise>
                                <td>
                                    <input type="text" class="form-control" name="content"
                                           value="${reply.content}" disabled>
                                </td>
                                <td>
                                        <span>
                                            AT <fmt:formatDate value="${reply.modifyDate}" pattern="y년M월d일"/>
                                        </span>
                                </td>
                            </c:otherwise>
                        </c:choose>
                    </tr>
                </c:forEach>
                <tr>
                    <form action="/reply/write/${boardDTO.id}" method="post">
                        <td colspan="5">
                            <input type="text" name="content" class="form-control" placeholder="댓글">
                        </td>
                        <td>
                            <input type="submit" class="btn btn-outline-success" value="작성">
                        </td>
                    </form>
                </tr>
            </table>
        </div>
    </div>
</div>
<script>
    function deleteBoard(id) {
        console.log(id);
        swal.fire({
            title: '정말로 삭제하시겠습니까?',
            confirmButtonText: '삭제하기',
            cancelButtonText: '취소',
            showCancelButton: true,
            icon: 'warning'
        }).then((result) => {
            console.log(result);
            if (result.isConfirmed) {
                swal.fire({
                    title: '삭제되었습니다'
                }).then((result) => {
                    location.href = "/board/delete/" + id;
                })
            }
        });
    }


</script>
</body>
</html>