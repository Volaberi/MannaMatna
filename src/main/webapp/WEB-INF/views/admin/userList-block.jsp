<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/sideBar.jsp"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>
<c:set var="path" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet"
	href="${path}/resources/css/userList-withdraw.css">
</head>
<body>
	<div class="content">
		<div class="title" id="withdrawTitle"
			onclick="location.href='/manageUser'">탈퇴회원관리</div>
		<div class="title" id="blockTitle"
			onclick="location.href='/blockUser'">정지회원관리</div>
		<hr>
		<table class="userTable">
			<tr>
				<th>회원번호</th>
				<th>닉네임</th>
				<th>아이디</th>
				<th>가입날짜</th>
				<th>성별</th>
				<th>연령대</th>
				<th></th>
			</tr>
			<c:if test="${empty pageUiActive2.list}">
				<tr>
					<td colspan="7">정지된 회원이 없습니다.</td>
				</tr>
			</c:if>
			<c:forEach items="${pageUiActive2.list}" var="userInfoVO">
				<tr>
					<td>${userInfoVO.uiNum}</td>
					<td>${userInfoVO.uiNickname}</td>
					<td>${userInfoVO.uiId}</td>
					<td>${userInfoVO.uiCredat}</td>
					<c:if test="${userInfoVO.uiGender==true}">
						<td>남</td>
					</c:if>
					<c:if test="${userInfoVO.uiGender==false}">
						<td>여</td>
					</c:if>
					<c:if test="${userInfoVO.uiAge==50}">
						<td>${userInfoVO.uiAge}대이상</td>
					</c:if>
					<c:if test="${userInfoVO.uiAge<50}">
						<td>${userInfoVO.uiAge}대</td>
					</c:if>
					<td style="text-align: right;"><button class="Btn"
							onclick="location.href='/blockCancle/${userInfoVO.uiNum}'">정지해제</button></td>
				</tr>
			</c:forEach>
		</table>
		<br>
		<!-- 페이징 -->
		<div id="pageDiv"></div>
		<c:if test="${!(empty pageUiActive2.list)}">
			<script>
				const pages = ${pageUiActive2.pages};//총페이지
				const page = ${pageUiActive2.pageNum};//현재페이지
				const start = Math.floor((page-1)/5)*5+1;//묶음페이지에서 첫페이지
				const end = (start + 4) > pages ? pages : (start + 4);//묶음페이지에서 끝페이지

				let html = '';
			
				if(11<=page){
					html +='<a href="/manage?page=1"><<</a>  '
				}//page가 11 이상일때만 맨 처음으로 버튼 보임
				if(start!=1){
					html += '<a href="/manage?page=' + (start-1) + '"><</a>'
				}//<버튼 누르면 현재 묶음페이지의 start에서 -1로 (묶음페이지가 첫페이지(1~5)가 아닐 때만 보임)
			
				for(let i=start; i<=end; i++){
					if(i==page){
						html += '<a class="active">' + i + '</a>';
					}else{
						html += ' <a href="/manage?page=' + i + '">' + i + '</a>' ;
					}
				}
			
				if(end!=pages){
					html += ' <a href="/manage?page=' + (end+1) + '">></a>';
				}//>버튼 누르면 묶음페이지 +1로 (묶음페이지가 첫페이지(1~5)가 아닐 때만 보임)
				if((end/5)<=(Math.ceil(pages/5))-2){
					html +='  <a href="/manage?page=' + pages + '">>></a>'
				}//>>버튼 누르면 마지막 묶음페이지로 */
				document.querySelector('#pageDiv').innerHTML = html;
			</script>
			<script>
			let title = document.getElementById('withdrawTitle');
			console.log(title);
			console.log(title.classList);
			title.classList.add('cancle');
			</script>
		</c:if>
	</div>
</body>
</html>