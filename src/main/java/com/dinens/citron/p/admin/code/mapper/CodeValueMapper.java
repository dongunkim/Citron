package com.dinens.citron.p.admin.code.mapper;

import java.util.List;

import com.dinens.citron.p.admin.code.domain.CodeValueVO;

public interface CodeValueMapper {
	
	public List<CodeValueVO> selectCodeValueList(CodeValueVO codeValueVO);

	public CodeValueVO selectCodeValueDetail(CodeValueVO codeValueVO);

	public int insertCodeValue(CodeValueVO codeValueVO);
	public int updateCodeValue(CodeValueVO codeValueVO);
	public int deleteCodeValue(CodeValueVO codeValueVO);
}
