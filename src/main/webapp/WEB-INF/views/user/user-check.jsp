<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>     
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<h1>회원 확인 페이지</h1>
${user.uiNickname}님이 맞는지 한번더 확인할게요.<br>
<input type="password" name="uiPwd" id="uiPwd" placeholder="비밀번호를 입력해주세요.">
<button>확인</button>

수정하기 -> 체크 -> 수정페이지
탈퇴하기 -> 사유쓰고 -> 체크 
</body>
</html>