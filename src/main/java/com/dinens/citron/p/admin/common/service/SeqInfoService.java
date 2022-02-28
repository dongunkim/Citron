package com.dinens.citron.p.admin.common.service;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dinens.citron.p.admin.common.domain.SeqInfoVO;
import com.dinens.citron.p.admin.common.mapper.SeqInfoMapper;
import com.dinens.citron.p.admin.common.util.StringUtils;

/**
 * <PRE>
 * 1. ClassName: SeqInfoService
 * 2. FileName : SeqInfoService.java
 * 3. Package  : com.dinens.citron.p.admin.common.service
 * 4. Comment  : Sequence Info Service Class
 * 5. 작성자   : JYPark
 * 6. 작성일   : 2019. 9. 4.
 * 7. 변경이력
 *		이름	:	일자	: 변경내용
 *     ———————————————————————————————————
 *		JYPark:	2019. 09. 04.	: 신규 개발.
  * </PRE>
 */

@Service
public class SeqInfoService {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private SeqInfoMapper seqInfoMapper;
	
	final String SEPERATOR = "-";
	
	
	/*
	 * TABLE NAME에 해당하는 Next ID 조회
	 * 시퀀스 타입 : SIMPLE	시퀀스 No만으로 구성		갱신 없음
	 *              PREFIX	Prefix-SeqNo			갱신 없음
	 *              YEAR	YYYY-SeqNo				년단위 갱신
	 *              MONTH	YYYYMM-SeqNo			월단위 갱신
	 *              DAY		YYYYMMDD-SeqNo			일단위 갱신
	 *              MIX-Y	Prefix-YYYY-SeqNo		년단위 갱신
	 *              MIX-M	Prefix-YYYYMM-SeqNo		월단위 갱신
	 *              MIX-D	Prefix-YYYYMMDD-SeqNo	일단위 갱신
	 */
	public String selectNextId(String tableName) throws Exception {
		
		seqInfoMapper.updateNextSeq(tableName);
		
		SeqInfoVO seqInfo = seqInfoMapper.selectSeqInfo(tableName);		
		String seqType = seqInfo.getSeqType();
		String nextId = null;
		int currNo = 0;
		
		switch(seqType) {
			case "SIMPLE":
				currNo = seqInfo.getCurrNo();
				nextId = StringUtils.lpad(String.valueOf(currNo), 
											seqInfo.getCipers(), seqInfo.getFillChar());				
				break;
			case "PREFIX":
				currNo = seqInfo.getCurrNo();
				nextId = seqInfo.getPrefix() + SEPERATOR + 
							StringUtils.lpad(String.valueOf(currNo), 
												seqInfo.getCipers(), seqInfo.getFillChar());
				break;
			case "YEAR":
				currNo = checkYear(tableName, seqInfo.getCurrYear()) ? seqInfo.getCurrNo() : 1;
				nextId = seqInfo.getCurrYear() + SEPERATOR + 
							StringUtils.lpad(String.valueOf(currNo), 
												seqInfo.getCipers(), seqInfo.getFillChar());
				break;
			case "MONTH":
				currNo = checkMonth(tableName, seqInfo.getCurrMonth()) ? seqInfo.getCurrNo() : 1;
				nextId = seqInfo.getCurrYear() + seqInfo.getCurrMonth() + SEPERATOR + 
							StringUtils.lpad(String.valueOf(currNo), 
												seqInfo.getCipers(), seqInfo.getFillChar());
				break;
			case "DAY":
				currNo = checkDate(tableName, seqInfo.getCurrDay()) ? seqInfo.getCurrNo() : 1;
				nextId = seqInfo.getCurrYear() + seqInfo.getCurrMonth() + seqInfo.getCurrDay() + SEPERATOR + 
							StringUtils.lpad(String.valueOf(currNo), 
												seqInfo.getCipers(), seqInfo.getFillChar());				
				break;
			case "MIX-Y":
				currNo = checkYear(tableName, seqInfo.getCurrYear()) ? seqInfo.getCurrNo() : 1;
				nextId = seqInfo.getPrefix() + SEPERATOR +
							seqInfo.getCurrYear() + seqInfo.getCurrMonth() + SEPERATOR + 
								StringUtils.lpad(String.valueOf(currNo), 
													seqInfo.getCipers(), seqInfo.getFillChar());
				break;
			case "MIX-M":
				currNo = checkMonth(tableName, seqInfo.getCurrMonth()) ? seqInfo.getCurrNo() : 1;
				nextId = seqInfo.getPrefix() + SEPERATOR +
							seqInfo.getCurrYear() + seqInfo.getCurrMonth() + SEPERATOR + 
								StringUtils.lpad(String.valueOf(currNo), 
													seqInfo.getCipers(), seqInfo.getFillChar());
				break;
			case "MIX-D":
				currNo = checkDate(tableName, seqInfo.getCurrDay()) ? seqInfo.getCurrNo() : 1;
				nextId = seqInfo.getPrefix() + SEPERATOR +
							seqInfo.getCurrYear() + seqInfo.getCurrMonth() + seqInfo.getCurrDay() + SEPERATOR + 
								StringUtils.lpad(String.valueOf(currNo), 
													seqInfo.getCipers(), seqInfo.getFillChar());
				break;
		}
		
		return nextId;
	}
	
	/*
	 * 현재 년도와 시퀀스 테이블의 년도 비교
	 * @Return true  동일한 경우
	 *         false 동일하지 않은 경우(DB 업데이트)
	 *          
	 */
	private boolean checkYear(String tableName, String year) {
		Calendar today = Calendar.getInstance();
		
		if(today.get(Calendar.YEAR) != Integer.parseInt(year)){
			SeqInfoVO seqInfoVO = new SeqInfoVO();
			seqInfoVO.setTableName(tableName);
			seqInfoVO.setCurrYear(String.valueOf(today.get(Calendar.YEAR)));
			seqInfoVO.setCurrNo(1);
			
			seqInfoMapper.updateSeqCycle(seqInfoVO);
			return false;
		}
		return true;
	}
	
	/*
	 * 현재 월과 시퀀스 테이블의 월 비교
	 * @Return true  동일한 경우
	 *         false 동일하지 않은 경우(DB 업데이트)
	 *          
	 */
	private boolean checkMonth(String tableName, String month) {
		Calendar today = Calendar.getInstance();
		
		if((today.get(Calendar.MONTH) + 1) != Integer.parseInt(month)){
			SeqInfoVO seqInfoVO = new SeqInfoVO();
			seqInfoVO.setTableName(tableName);
			seqInfoVO.setCurrYear(String.valueOf(today.get(Calendar.YEAR)));
			seqInfoVO.setCurrMonth(String.valueOf(today.get(Calendar.MONTH) + 1));
			seqInfoVO.setCurrNo(1);
			
			seqInfoMapper.updateSeqCycle(seqInfoVO);
			return false;
		}
		return true;
	}
	
	/*
	 * 현재 일자와 시퀀스 테이블의 일자 비교
	 * @Return true  동일한 경우
	 *         false 동일하지 않은 경우(DB 업데이트)
	 *          
	 */
	private boolean checkDate(String tableName, String day) {
		Calendar today = Calendar.getInstance();
				
		if(today.get(Calendar.DATE) != Integer.parseInt(day)){
			SeqInfoVO seqInfoVO = new SeqInfoVO();
			seqInfoVO.setTableName(tableName);
			seqInfoVO.setCurrYear(String.valueOf(today.get(Calendar.YEAR)));
			seqInfoVO.setCurrMonth(String.valueOf(today.get(Calendar.MONTH) + 1));
			seqInfoVO.setCurrDay(String.valueOf(today.get(Calendar.DATE)));
			seqInfoVO.setCurrNo(1);
			
			seqInfoMapper.updateSeqCycle(seqInfoVO);
			return false;
		}
		return true;
	}
}
