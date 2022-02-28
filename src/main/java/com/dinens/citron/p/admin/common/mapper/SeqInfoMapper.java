package com.dinens.citron.p.admin.common.mapper;

import org.apache.ibatis.annotations.Param;

import com.dinens.citron.p.admin.common.domain.SeqInfoVO;

/**
 * <PRE>
 * 1. ClassName: SeqInfoMapper
 * 2. FileName : SeqInfoMapper.java
 * 3. Package  : com.dinens.citron.p.admin.common.mapper
 * 4. Comment  : Sequence Info mapper
 * 5. 작성자   : JYPark
 * 6. 작성일   : 2019. 9. 4.
 * 7. 변경이력
 *		이름	:	일자	: 변경내용
 *     ———————————————————————————————————
 *		JYPark:	2019. 09. 04.	: 신규 개발.
  * </PRE>
 */

public interface SeqInfoMapper {

	// Sequence Info
	public SeqInfoVO selectSeqInfo(@Param(value="tableName") String tableName);	
	public void updateNextSeq(@Param(value="tableName") String tableName);
	public void updateSeqCycle(@Param(value="vo") SeqInfoVO seqInfoVO);	
}
