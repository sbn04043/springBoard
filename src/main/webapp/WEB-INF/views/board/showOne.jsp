<%@page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<html>
<head>
    <title>게시판</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
          rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
          crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
            crossorigin="anonymous"></script>
</head>
<body>
<h1>게시판</h1>
<div class="container-fluid">
    <div class="main h-100">
        <!-- taglib c는 기본적으로 JSP에서 자바 코드 대신 Marked-up Language 형식으로
            변수의 값, 조건문, 반복문 등을 출력할 수 있게 만들어주는 라이브러리다.
            <c:out value="이것은 JSP 코어태그입니다."/> 와 같이 사용된다.-->

        <div class="row justify-content-center">
            <div class="col-8 text-center">
                <table class="table table-striped">
                    <tr>
                        <th>글 번호</th>
                        <th colspan="3">제목</th>
                        <th>작성자</th>
                        <th>작성일</th>
                    </tr>
                    <!-- 가장 대표적으로 사용할 수 있는 Core 태그는 forEach 태그다.
                        items 어트리뷰트에는 순차적으로 꺼내올 컬렉션 개체를 지정한다.
                        var 어트리뷰트는 뽑아온 값을 뭐라고 호칭할지 지정한다.
                        for (BoardDTO b : list)를 forEach 태그로 표현하면 다음과 같다. -->
                    <c:forEach items="${list}" var="b">
                        <tr onclick="location.href='/board/showOne/${b.id}'">
                            <td>${b.id}</td>
                            <td colspan="3">${b.title}</td>
                            <td>${b.nickname}</td>
                            <!-- fmt는 formatter와 관련된 태그다.
                                주로 시간의 포맷을 정할 때 사용된다. -->
                            <td><fmt:formatDate value="${b.entryDate}" pattern="yyMMdd HH:mm:ss"/></td>
                        </tr>
                    </c:forEach>
                </table>
            </div>
            <div class="row justify-content-end">
                <div class="col-4">
                    <a class="btn btn-outline-success" href="/board/write">글 작성하기</a>
                </div>
            </div>
        </div>


    </div>
</div>
</body>