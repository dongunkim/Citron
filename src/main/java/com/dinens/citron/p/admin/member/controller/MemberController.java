package com.dinens.citron.p.admin.member.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dinens.citron.p.admin.code.domain.CodeValueVO;
import com.dinens.citron.p.admin.code.service.CodeService;
import com.dinens.citron.p.admin.common.domain.PagingVO;
import com.dinens.citron.p.admin.common.util.ExcelUtils;
import com.dinens.citron.p.admin.member.domain.MemberVO;
import com.dinens.citron.p.admin.member.service.MemberService;

/**
 * <PRE>
 * 1. ClassName: BoardController
 * 2. FileName : BoardController.java
 * 3. Package  : com.dinens.citron.p.admin.board.controller
 * 4. Comment  : 게시판 Controller
 * 5. 작성자   : JYPark
 * 6. 작성일   : 2019. 9. 4.
 * 7. 변경이력
 *		이름	:	일자	: 변경내용
 *     ———————————————————————————————————
 *		JYPark:	2019. 09. 04.	: 신규 개발.
  * </PRE>
 */

@Controller
@RequestMapping(value="/member/*")
public class MemberController {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private CodeService codeService;

	/*
	 * 멤버(회원) > 목록화면 이동
	 */
	@RequestMapping(value="/memberList", method = RequestMethod.POST)
	public String boardList() throws Exception {

		return "/member/memberList";
	}
	
	
	/*
	 * 멤버(회원) > 목록 조회
	 */
	@RequestMapping(value="/ajaxMemberList.json", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> ajaxMemberList(@ModelAttribute MemberVO memberVO, @ModelAttribute PagingVO pagingVO, Model model, HttpServletRequest request) throws Exception {
		
		//logger.debug("memberVO : " + memberVO);
		//logger.debug("pagingVO : " + pagingVO);
		
		Map<String, Object> resultMap = memberService.selectMemberList(memberVO, pagingVO);
		
		model.addAttribute("pagingVO", resultMap.get("pagingVO"));
		model.addAttribute("list", resultMap.get("list"));
		
		return resultMap;
	}

	
	/*
	 * 멤버(회원) 상세화면 이동
	 */
	@RequestMapping(value="/memberDetail", method = RequestMethod.POST)
	public String memberDetail(@ModelAttribute MemberVO memberVO, Model model) throws Exception {

		MemberVO member = memberService.selectMemberDetail(memberVO);
		model.addAttribute("member", member);		
		
		return "/member/memberDetail";
	}
	
	
	/*
	 * 멤버(회원) 엑셀 다운로드
	 */
	@RequestMapping(value="/memberListExcel", method = RequestMethod.POST)	                       
	public void boardListExcel(@ModelAttribute MemberVO memberVO, HttpServletResponse response) throws Exception {
		long start = System.currentTimeMillis();
						
		List<Map<String, Object>> list = memberService.selectMemberListExcel(memberVO);
		
		logger.debug("list Count : " + list.size());
		
		String fileName = "Member List (회원)";
		String[] columnNames = {"아이디", "이름", "회원유형", "상태", "가입일"};
		String[] columnIds = {"MEMBER_ID:L", "MEMBER_NAME:L", "ID_TYPE_NAME:C", "MEMBER_STATUS_NAME:C", "JOIN_TIME:C"};
		String[] columnWidths = {"5490", "5490", "2800", "2800", "5490"};
		
		List<String[]> columnInfo = new ArrayList<String[]>();
		columnInfo.add(columnNames);
		columnInfo.add(columnIds);
		columnInfo.add(columnWidths);
		
		ExcelUtils.downloadExcel(fileName, columnInfo, list, response);
				
		long end = System.currentTimeMillis();
		logger.debug("RunTime : " + Math.round(end - start)/1000.0000 + " (초)");		
	}
	
	
	/*
	 * 코드값 조회(임시 --> 추후 공통으로 이동)
	 */
	@RequestMapping(value="/codeValueList.json", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> codeValueList(@ModelAttribute CodeValueVO codeValueVO) throws Exception {

		return codeService.selectCodeValueList(codeValueVO);
	}

}
