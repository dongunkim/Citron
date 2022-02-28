package com.dinens.citron.p.admin.main.mapper;

import com.dinens.citron.p.admin.admin.domain.AdminVO;

/**
 * <PRE>
 * 1. ClassName: MainMapper
 * 2. FileName : MainMapper.java
 * 3. Package  : com.dinens.citron.p.admin.main.mapper
 * 4. Comment  : Main Mapper Class
 * 5. 작성자   : JYPark
 * 6. 작성일   : 2019. 9. 10.
 * 7. 변경이력
 *		이름	:	일자	: 변경내용
 *     ———————————————————————————————————O
 *		JYPark:	2019. 09. 10.	: 신규 개발.
  * </PRE>
 */

public interface MainMapper {
	public AdminVO selectAdminDetail(String adminId);
	public int updateAdmin(AdminVO adminVO);
	public int updateAdminPassword(AdminVO adminVO);
}
