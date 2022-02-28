package com.dinens.citron.p.admin.main.mapper;

import org.apache.ibatis.annotations.Param;

import com.dinens.citron.p.admin.admin.domain.AdminVO;
import com.dinens.citron.p.admin.common.domain.IssueHistVO;

/**
 * <PRE>
 * 1. ClassName: LoginMapper
 * 2. FileName : LoginMapper.java
 * 3. Package  : com.dinens.citron.p.admin.main.mapper
 * 4. Comment  : Login Mapper Class
 * 5. 작성자   : JYPark
 * 6. 작성일   : 2019. 9. 10.
 * 7. 변경이력
 *		이름	:	일자	: 변경내용
 *     ———————————————————————————————————
 *		JYPark:	2019. 09. 10.	: 신규 개발.
  * </PRE>
 */

public interface LoginMapper {
	public AdminVO loginCheck(AdminVO adminVO);
	public AdminVO checkAdminInfo(@Param (value="adminId")String adminId);
	public int checkIssueHist(IssueHistVO issueHistVO);	
}
