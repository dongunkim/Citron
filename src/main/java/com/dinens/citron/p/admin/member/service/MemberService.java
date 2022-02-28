package com.dinens.citron.p.admin.member.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dinens.citron.p.admin.code.domain.CodeValueVO;
import com.dinens.citron.p.admin.code.mapper.CodeValueMapper;
import com.dinens.citron.p.admin.common.domain.PagingVO;
import com.dinens.citron.p.admin.common.util.CommonUtils;
import com.dinens.citron.p.admin.member.domain.MemberVO;
import com.dinens.citron.p.admin.member.mapper.MemberMapper;

/**
 * <PRE>
 * 1. ClassName: MemberService
 * 2. FileName : MemberService.java
 * 3. Package  : com.dinens.citron.p.admin.member.service
 * 4. Comment  : Member Service Class
 * 5. 작성자   : JYPark
 * 6. 작성일   : 2019. 9. 7.
 * 7. 변경이력
 *		이름	:	일자	: 변경내용
 *     ———————————————————————————————————
 *		JYPark:	2019. 09. 07.	: 신규 개발.
  * </PRE>
 */

@Service("memberService")
public class MemberService {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired	
	private MemberMapper memberMapper;
	
	@Autowired	
	private CodeValueMapper codeValueMapper;
	
	/*
	 * 멤버(회원) 목록 조회
	 */
	public Map<String, Object> selectMemberList(MemberVO memberVO, PagingVO pagingVO) throws Exception {
		int totalCount = memberMapper.selectMemberListCount(memberVO);		
		if (totalCount > 0) {
			pagingVO.setTotalCount(totalCount);
			CommonUtils.setPaging(pagingVO);
		}
		logger.debug("pagingVO : " + pagingVO);
		List<MemberVO> list = memberMapper.selectMemberList(memberVO, pagingVO);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("pagingVO", pagingVO);
		resultMap.put("list", list);
		
		return resultMap;
	}
	
	
	/*
	 * 멤버(회원) 목록 Excel 조회
	 */
	public List<Map<String, Object>> selectMemberListExcel(MemberVO memberVO) throws Exception {
		return memberMapper.selectMemberListExcel(memberVO);
	}
	
	
	/*
	 * 멤버(회원) 상세정보 조회
	 */
	public MemberVO selectMemberDetail(MemberVO memberVO) throws Exception {				
		String memberId = memberVO.getMemberId();
		MemberVO member = memberMapper.selectMemberDetail(memberId);
		logger.debug("member =============> " + member);		
				
		return member;
	}
	
}
