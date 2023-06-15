package com.ezen.mannamatna.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ezen.mannamatna.service.UserInfoService;
import com.ezen.mannamatna.vo.BabsangInfoVO;
import com.ezen.mannamatna.vo.KakaoToken;
import com.ezen.mannamatna.vo.KakaoUserInfoVO;
import com.ezen.mannamatna.vo.UserInfoVO;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
public class UserInfoController {
	
	@Autowired
	UserInfoService uiService;
	
	@GetMapping("/") 
	public String home(@ModelAttribute UserInfoVO userInfoVO, Model m) {
		return "index"; 
	}
	
	@PostMapping("/") 
	public String home() {
		return "index"; 
	}
	
	@GetMapping("/login")
	public String login() {
		return "user/login";
	}
	
	@PostMapping("/login")
	public String gologin(@ModelAttribute UserInfoVO userInfoVO, BabsangInfoVO babsang, HttpSession session, Model m) {
		log.info("=============>{}",userInfoVO);
		if(uiService.login(userInfoVO, session)) {
			userInfoVO = (UserInfoVO) session.getAttribute("user");
			log.info("=============>{}",userInfoVO);
			if(userInfoVO.getUiActive()==1) {
				m.addAttribute("msg","탈퇴처리된 계정입니다.");
				session.invalidate();
				return "user/login";
			}
			m.addAttribute("url", "/main");
			m.addAttribute("msg", "로그인성공");
			return "common/msg";
	}
	m.addAttribute("msg","아이디나 비밀번호가 잘못되었습니다.");
	return "user/login";
}
	
	@GetMapping("/kakaoPost")
	  public String kakaoJoin(@RequestParam(value = "code",required = false) String code, HttpSession session,  Model m) throws IllegalStateException, IOException{
		UserInfoVO userInfoVO = null;
		if(code!=null){//카카오측에서 보내준 code가 있다면 출력
            System.out.println("code = " + code);
            KakaoToken kakaoToken = uiService.requestToken("/kakaoPost/",code); //카카오 토큰 요청
            userInfoVO = uiService.requestUser(kakaoToken.getAccess_token()); //유저정보 요청
            log.info("user = {}",userInfoVO);
            log.info("kakoToken = {}", kakaoToken);
//			session.setAttribute("user", userInfoVO); 
        }
		if(uiService.join(userInfoVO)) {
			m.addAttribute("msg","회원가입에 성공하셨습니다.");
			return "user/login";
		}
		return "user/kakaoPost";
	}
	
	@GetMapping("/kakaoLogin") 
	public String kakaoLogin(@RequestParam(value = "code",required = false) String code, HttpSession session,  Model m) throws IllegalStateException, IOException{
		UserInfoVO userInfoVO = null;
		KakaoUserInfoVO kakaoUserInfoVO = new KakaoUserInfoVO();
		// 카카오 로그인해서 id 돌려받고, 그 아이디를 가진 유저가 있는지 인포 돌아서 확인 이때 비번은 0000 고정임
		
		if(code!=null){//카카오측에서 보내준 code가 있다면 출력
            System.out.println("code = " + code);
            KakaoToken kakaoToken = uiService.requestToken("/kakaoLogin/",code); //카카오 토큰 요청
            log.info("여기서 들어감! kakaoUserInfoVO={}",kakaoUserInfoVO);
            userInfoVO = uiService.requestUser(kakaoToken.getAccess_token()); //유저정보 요청
            kakaoUserInfoVO.setKuiId(userInfoVO.getKuiId()); // userInfoVO가 가지고있는 카카오 id값을 kakaoUserInfoVO에 넣음
            log.info("로그인요청한 kakaoUserInfoVO={}",kakaoUserInfoVO);
            if(uiService.kakaoLogin(kakaoUserInfoVO, session)) { // 카카오유저테이블에 그 id를 가지는 카카오유저가있다면
            	// UserInfoVO sessionUserInfo = (UserInfoVO) session.getAttribute("user");
            	 // session.setAttribute("user", userInfoVO);//해당 유저번호를 담아서 리턴하는거 추가해야함
            	 // log.info("서비스에서 넘어온 sessionUserInfo={}",sessionUserInfo);
            	 // userInfoVO.setUiNum(sessionUserInfo.getUiNum());
            	 // log.info("화면에 나올 kakaoUserInfoVO={}",kakaoUserInfoVO);
            	 // session.setAttribute("user", userInfoVO); 
            	// 서비스에 카카오로그인 메소드로 로직을 좀 몰아넣음
            	// 일단은 주석처리하고 작업완료!
            	 m.addAttribute("url","/main"); 
            	 m.addAttribute("msg", "로그인성공");
            	 return "common/msg";
            }
            m.addAttribute("msg","카카오 가입 유저가 아닙니다.");
    		return "user/login";
        }
	
		m.addAttribute("msg","아이디나 비밀번호가 잘못되었습니다.");
		return "user/login";
}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "user/login";
	}
	
	@GetMapping("/join") 
	public String join() {
		return "user/join"; 
	}
	
	@PostMapping("/join-ok")
	public String joinOk(@ModelAttribute UserInfoVO userInfoVO,HttpSession session, Model m) throws IllegalStateException, IOException {
		log.info("조인ok=====>{}",userInfoVO);
		if(uiService.join(userInfoVO)) {
			m.addAttribute("msg","회원가입에 성공하셨습니다.");
			return "user/login";
		}
		return "user/join";
	}
	
	@PostMapping("/idChk")
	@ResponseBody
	public Map<String, Integer> idChk(@ModelAttribute UserInfoVO userInfoVO, @RequestBody Map<String, String> checkMap) {
		Map<String, Integer> map = new HashMap<>();
		userInfoVO.setUiId(checkMap.get("uiId"));
		log.info("여기는 컨트롤러1===========>{}",userInfoVO.getUiId());
		int result = uiService.idChk(userInfoVO);
		log.info("여기는 컨트롤러2===========>{}",result);
		map.put("result", result);
		return map; 
	}
	
	@PostMapping("/nicknameChk")
	@ResponseBody
	public Map<String, Integer> nicknameChk(@ModelAttribute UserInfoVO userInfoVO, @RequestBody Map<String, String> checkMap) {
		userInfoVO.setUiNickname(checkMap.get("uiNickname"));
		log.info("여기는 컨트롤러1===========>{}",userInfoVO.getUiNickname());
		int result = uiService.nicknameChk(userInfoVO);
		log.info("여기는 컨트롤러2===========>{}",result);
		Map<String, Integer> map = new HashMap<>();
		map.put("result", result);
		return map; 
	}
	
	
	@GetMapping("/profile") // 프로필 화면 연결
	public String profile(@ModelAttribute UserInfoVO userInfoVO, HttpSession session) {
		userInfoVO = (UserInfoVO) session.getAttribute("user");
		log.info("컨트롤러/프로필요청 ==>{}",userInfoVO);
		return "user/user-profile";
	}
	
	@GetMapping("/check-update") // 프로필 수정 버튼을 누른 경우 
	public String checkUpdate() {
		return "user/user-check-update";
	}
	
	@PostMapping("/check-update") // 수정 버튼을 누르고 비밀번호가 일치한 경우
	public String checkUpdateOk(@ModelAttribute UserInfoVO userInfoVO, HttpSession session) {
		userInfoVO = (UserInfoVO) session.getAttribute("user");
		return "/user/user-profile-update";
	}
	
	@GetMapping("/user/user-profile-update")
	public String updateProfile() {
		return "user/user-profile-update";
	}
	
	@PostMapping("/profile-update")
	public String updateProfileOk(@ModelAttribute UserInfoVO userInfoVO, HttpSession session, Model m) throws IllegalStateException, IOException {
		UserInfoVO sessionUserInfo = (UserInfoVO) session.getAttribute("user");
		userInfoVO.setUiNum(sessionUserInfo.getUiNum());
		userInfoVO.setBiNum(sessionUserInfo.getBiNum());
		userInfoVO.setUiCredat(sessionUserInfo.getUiCredat());
		userInfoVO.setUiId(sessionUserInfo.getUiId());
		userInfoVO.setUiActive(sessionUserInfo.getUiActive());
		userInfoVO.setUiDel(sessionUserInfo.getUiDel());
		if(uiService.update(userInfoVO, session)) {
			m.addAttribute("msg","정보수정에 성공하셨습니다.");
			session.setAttribute("user", userInfoVO);
			return "user/user-profile";
		}
		m.addAttribute("msg","정보수정에 실패하였습니다.");
		return "user/user-profile";
	}
	
	
	
	@GetMapping("/withdraw")
	public String withdraw() {
		// 사유씀 
		return "user/user-withdraw"; 
	}
	
	@PostMapping("/withdraw") // 탈퇴사유를 입력하고 확인을 누른 경우 
	public String goWithdraw(@ModelAttribute UserInfoVO userInfoVO,HttpSession session) {
		UserInfoVO sessionUserInfo = (UserInfoVO) session.getAttribute("user");
		sessionUserInfo.setUiDel(userInfoVO.getUiDel()); //사유입력한거 일단 임시 저장
		userInfoVO = sessionUserInfo;
		return "user/user-check-withdraw";
	}
	
	@GetMapping("/user/user-check-withdraw") // 비밀번호 재확인
	public String checkWithdraw() {
		return "user/user-check-withdraw";
	}
	
	@PostMapping("/check-withdraw")
	public String checkWithdrawOk(@ModelAttribute UserInfoVO userInfoVO,HttpSession session,Model m) {
		UserInfoVO sessionUserInfo = (UserInfoVO) session.getAttribute("user");
		sessionUserInfo.setUiActive(1); //엑티브 1처리
		userInfoVO = sessionUserInfo;
		log.info("컨트롤러 체크-윗드로우 ==>{}",userInfoVO);
		if(uiService.delete(userInfoVO, session)) { // 앞에서 입력한 사유를 이때 저장함 (업데이트)
			m.addAttribute("msg","정상적으로 탈퇴처리되었습니다.");
			session.invalidate();
		}	
		return "babsang/babsang-list";
	}
	
}
