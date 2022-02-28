package com.dinens.citron.p.admin.code.mapper;

import java.util.List;

import com.dinens.citron.p.admin.code.domain.CodeValueLocaleVO;

public interface CodeValueLocaleMapper {
	
	public List<CodeValueLocaleVO> selectCodeValueLocaleList(CodeValueLocaleVO codeValueLocaleVO);

	public int insertCodeValueLocale(CodeValueLocaleVO codeValueLocaleVO);
	public int deleteCodeValueLocale(CodeValueLocaleVO codeValueLocaleVO);
}
