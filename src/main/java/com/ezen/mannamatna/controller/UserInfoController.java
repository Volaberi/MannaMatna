package com.ezen.mannamatna.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ezen.mannamatna.mapper.UserInfoMapper;
import com.ezen.mannamatna.service.BabsangInfoService;
import com.ezen.mannamatna.service.UserInfoService;
import com.ezen.mannamatna.vo.BabsangInfoVO;
import com.ezen.mannamatna.vo.UserInfoVO;

import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
public class UserInfoController {
	
	@Autowired
	UserInfoService uiService;
	@Autowired
	BabsangInfoService babsangInfoService;
	
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
	public String gologin(@ModelAttribute UserInfoVO userInfoVO, @ModelAttribute BabsangInfoVO babsangInfoVO, HttpSession session, Model m) {
		log.info("=============>{}",uiService.login(userInfoVO, session));
		if(uiService.login(userInfoVO, session)) {
			// 로그인 후 메인으로 가면서 babsangList 를 가져옴
			m.addAttribute("babsangList", babsangInfoService.getBabsangInfoVOs(babsangInfoVO));
		return "babsang/babsang-list";
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
		return "user/user-profile";
	}
	
	@GetMapping("/check-update") // 프로필 수정 버튼을 누른 경우 
	public String checkUpdate() {
		return "user/user-check-update";
	}
	
	@PostMapping("/check-update") // 수정 버튼을 누르고 비밀번호가 일치한 경우
	public String checkUpdateOk() {
		return "user/user-profile-update";
	}
	
	@GetMapping("/profile-update")
	public String updateProfile() {
		return "user/user-profile-update";
	}
	
	@PostMapping("/profile-update")
	public String updateProfileOk(@ModelAttribute UserInfoVO userInfoVO, HttpSession session, Model m) throws IllegalStateException, IOException {
		UserInfoVO sessionUserInfo = (UserInfoVO) session.getAttribute("user");
		userInfoVO.setUiNum(sessionUserInfo.getUiNum());
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
		return "user/user-withdraw";
	}
	
	
	
}
