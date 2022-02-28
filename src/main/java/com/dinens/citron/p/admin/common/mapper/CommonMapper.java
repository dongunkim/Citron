package com.dinens.citron.p.admin.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dinens.citron.p.admin.common.domain.EmailSendHistVO;
import com.dinens.citron.p.admin.common.domain.IssueHistVO;
import com.dinens.citron.p.admin.common.domain.KeyInfoVO;

/**
 * <PRE>
 * 1. ClassName: CommonMapper
 * 2. FileName : CommonMapper.java
 * 3. Package  : com.dinens.citron.common.mapper
 * 4. Comment  : Common mapper
 * 5. 작성자   : JYPark
 * 6. 작성일   : 2019. 7. 31.
 * 7. 변경이력
 *		이름	:	일자	: 변경내용
 *     ———————————————————————————————————
 *		JYPark:	2019. 07. 31.	: 신규 개발.
  * </PRE>
 */

public interface CommonMapper {
	
	// Secure Key
	public KeyInfoVO selectKeyInfo(@Param (value="sessionId") String sessionId);	
	public void insertKeyInfo(KeyInfoVO keyInfoVO);	
	public void deleteKeyInfo(@Param (value="sessionId") String sessionId);
	
	// Mail Send History
	public List<EmailSendHistVO> selectEmailSendHistList(EmailSendHistVO emailSendHistVO);
	public void insertEmailSendHistInfo(EmailSendHistVO emailSendHistVO);	
	
	// Issue History
	public int insertIssueHist(IssueHistVO issueHistVO);
}
