package com.dinens.citron.p.admin.code.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.dinens.citron.p.admin.code.domain.CodeVO;

public interface CodeMapper {
	
	public List<CodeVO> selectCodeList(@Param(value="codeVO") CodeVO codeVO);

	public CodeVO selectCodeDetail(CodeVO codeVO);

	public int insertCode(CodeVO codeVO);
	public int updateCode(CodeVO codeVO);
	public int deleteCode(CodeVO codeVO);
}
