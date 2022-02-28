package com.dinens.citron.p.admin.common.util;

import java.util.UUID;

import com.dinens.citron.p.admin.common.domain.PagingVO;

/**
 * <PRE>
 * 1. ClassName: CommonUtils
 * 2. FileName : CommonUtils.java
 * 3. Package  : com.dinens.citron.common.util
 * 4. Comment  : Common Utility Function 모음
 * 5. 작성자   : JYPark
 * 6. 작성일   : 2019. 7. 31.
 * 7. 변경이력
 *		이름	:	일자	: 변경내용
 *     ———————————————————————————————————
 *		JYPark:	2019. 07. 31.	: 신규 개발.
  * </PRE>
 */
public class CommonUtils {
		
	public static final int COUNT_PER_PAGE = 15;
	
	/**
	 * UUID 생성
	 */
	public static String getRandomString() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
		
	
	/**
	 * Paging 처리에 필요한 값 설정 
	 */
	public static void setPaging (PagingVO pagingVO) {
				
		//페이지 설정
		int iNowPage = pagingVO.getNowPage();
		int iTotalCount = pagingVO.getTotalCount();
		int iCountPerPage = pagingVO.getCountPerPage();
		if(iCountPerPage == 0) {
			iCountPerPage = COUNT_PER_PAGE;
		}
		
		int iTotalPage = (int) Math.ceil((double)iTotalCount / (double)iCountPerPage);

		if(iNowPage >= iTotalPage){
			iNowPage = iTotalPage;
		}

		int iStartNum = (iNowPage - 1) * iCountPerPage;		
		
		pagingVO.setNowPage(iNowPage);
		pagingVO.setStartNum(iStartNum);
		pagingVO.setCountPerPage(iCountPerPage);
		pagingVO.setTotalPage(iTotalPage);		
	}
	
}
