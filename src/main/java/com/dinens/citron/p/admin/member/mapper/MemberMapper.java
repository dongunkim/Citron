package com.dinens.citron.p.admin.member.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.dinens.citron.p.admin.common.domain.PagingVO;
import com.dinens.citron.p.admin.member.domain.MemberVO;

/**
 * <PRE>
 * 1. ClassName: MemberMapper
 * 2. FileName : MemberMapper.java
 * 3. Package  : com.dinens.citron.p.admin.member.mapper
 * 4. Comment  : 멤버(회원) Mapper Class
 * 5. 작성자   : JYPark
 * 6. 작성일   : 2019. 9. 7.
 * 7. 변경이력
 *		이름	:	일자	: 변경내용
 *     ———————————————————————————————————
 *		JYPark:	2019. 09. 07.	: 신규 개발.
  * </PRE>
 */

public interface MemberMapper {
	// Member Table
	public int selectMemberListCount(@Param(value="memberVO") MemberVO memberVO);
	public List<MemberVO> selectMemberList(@Param(value="memberVO") MemberVO memberVO,
									       @Param(value="pagingVO") PagingVO pagingVO);
	public List<Map<String, Object>> selectMemberListExcel(@Param(value="memberVO") MemberVO memberVO);
	public MemberVO selectMemberDetail(@Param(value="memberId") String memberId);
}
