package com.dinens.citron.p.admin.code.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dinens.citron.p.admin.code.domain.CodeVO;
import com.dinens.citron.p.admin.code.domain.CodeValueLocaleVO;
import com.dinens.citron.p.admin.code.domain.CodeValueVO;
import com.dinens.citron.p.admin.code.mapper.CodeMapper;
import com.dinens.citron.p.admin.code.mapper.CodeValueLocaleMapper;
import com.dinens.citron.p.admin.code.mapper.CodeValueMapper;
import com.dinens.citron.p.admin.common.constants.Constants;
import com.dinens.citron.p.admin.common.domain.Result;
import com.dinens.citron.p.admin.common.util.MessageUtils;
import com.dinens.citron.p.admin.menu.domain.MenuLocaleVO;
import com.dinens.citron.p.admin.menu.domain.MenuVO;

@Service("codeService")
public class CodeService {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private CodeMapper codeMapper;

	@Autowired
	private CodeValueMapper codeValueMapper;

	@Autowired
	private CodeValueLocaleMapper codeValueLocaleMapper;

	public Map<String, Object> selectCodeList(CodeVO codeVO) throws Exception {
		
		List<CodeVO> list = codeMapper.selectCodeList(codeVO);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("list", list);

		return resultMap;
	}

	public CodeVO selectCodeDetail(CodeVO codeVO) {
		return codeMapper.selectCodeDetail(codeVO);
	}

	public Result insertCode(CodeVO codeVO) {
		CodeVO info = codeMapper.selectCodeDetail(codeVO);
		if(info != null) {
			return new Result("FAIL", Constants.Error.DUPLICATE, MessageUtils.getMessage("server.common.result.error.104.message"));
		}

		codeMapper.insertCode(codeVO);

		return new Result("OK");
	}

	public boolean updateCode(CodeVO codeVO) {
		return codeMapper.updateCode(codeVO) > 0 ? true : false;
	}
	
	public void deleteCode(CodeVO codeVO) {
		codeMapper.deleteCode(codeVO);
		CodeValueVO codeValueVO = new CodeValueVO();
		codeValueVO.setCodeId(codeVO.getCodeId());
		codeValueMapper.deleteCodeValue(codeValueVO);
	}

	public Map<String, Object> selectCodeValueList(CodeValueVO codeValueVO) throws Exception {				
		
		List<CodeValueVO> list = codeValueMapper.selectCodeValueList(codeValueVO);
						
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("list", list);

		return resultMap;
	}

	public CodeValueVO selectCodeValueDetail(CodeValueVO codeValueVO) {
		CodeValueVO vo = codeValueMapper.selectCodeValueDetail(codeValueVO);

		CodeValueLocaleVO codeValueLocaleVO = new CodeValueLocaleVO();
		codeValueLocaleVO.setCodeId(codeValueVO.getCodeId());
		codeValueLocaleVO.setValue(codeValueVO.getValue());
		List<CodeValueLocaleVO> list = codeValueLocaleMapper.selectCodeValueLocaleList(codeValueLocaleVO);
		String[] localeCode = new String[list.size()];
		String[] nameLocale = new String[list.size()];
		for(int i = 0 ; i < list.size() ; i++) {
			localeCode[i] = list.get(i).getLocale();
			nameLocale[i] = list.get(i).getName();
		}
		vo.setLocaleCode(localeCode);
		vo.setNameLocale(nameLocale);

		return vo;
	}

	public Result insertCodeValue(CodeValueVO codeValueVO) {
		CodeValueVO info = codeValueMapper.selectCodeValueDetail(codeValueVO);
		if(info != null) {
			return new Result("FAIL", Constants.Error.DUPLICATE, MessageUtils.getMessage("server.common.result.error.104.message"));
		}

		codeValueMapper.insertCodeValue(codeValueVO);
		
		CodeValueLocaleVO codeValueLocaleVO = new CodeValueLocaleVO();
		codeValueLocaleVO.setCodeId(codeValueVO.getCodeId());
		codeValueLocaleVO.setValue(codeValueVO.getValue());
		codeValueLocaleVO.setCreateId(codeValueVO.getCreateId());
		for(int i = 0 ; i < codeValueVO.getLocale().length() ; i++ ) {
			codeValueLocaleVO.setLocaleCode(codeValueVO.getLocaleCode()[i]);
			codeValueLocaleVO.setName(codeValueVO.getNameLocale()[i]);
			codeValueLocaleVO.setDisplayOrder(Integer.toString(i+1));
			codeValueLocaleMapper.insertCodeValueLocale(codeValueLocaleVO);
		}

		return new Result("OK");
	}

	public void updateCodeValue(CodeValueVO codeValueVO) {
		codeValueMapper.updateCodeValue(codeValueVO);
		
		CodeValueLocaleVO codeValueLocaleVO = new CodeValueLocaleVO();
		codeValueLocaleVO.setCodeId(codeValueVO.getCodeId());
		codeValueLocaleVO.setValue(codeValueVO.getValue());
		codeValueLocaleMapper.deleteCodeValueLocale(codeValueLocaleVO);

		codeValueLocaleVO.setCreateId(codeValueVO.getUpdateId());
		for(int i = 0 ; i < codeValueVO.getLocale().length() ; i++ ) {
			codeValueLocaleVO.setLocaleCode(codeValueVO.getLocaleCode()[i]);
			codeValueLocaleVO.setName(codeValueVO.getNameLocale()[i]);
			codeValueLocaleVO.setDisplayOrder(Integer.toString(i+1));
			codeValueLocaleMapper.insertCodeValueLocale(codeValueLocaleVO);
		}
	}
	
	public void deleteCodeValue(CodeValueVO codeValueVO) {
		codeValueMapper.deleteCodeValue(codeValueVO);
		
		CodeValueLocaleVO codeValueLocaleVO = new CodeValueLocaleVO();
		codeValueLocaleVO.setCodeId(codeValueVO.getCodeId());
		codeValueLocaleVO.setValue(codeValueVO.getValue());
		codeValueLocaleMapper.deleteCodeValueLocale(codeValueLocaleVO);
	}
}
