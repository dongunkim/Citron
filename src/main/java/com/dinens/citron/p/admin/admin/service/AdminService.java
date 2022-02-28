package com.dinens.citron.p.admin.admin.service;

import java.security.PrivateKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dinens.citron.p.admin.admin.domain.AdminVO;
import com.dinens.citron.p.admin.admin.mapper.AdminMapper;
import com.dinens.citron.p.admin.common.constants.Constants;
import com.dinens.citron.p.admin.common.domain.KeyInfoVO;
import com.dinens.citron.p.admin.common.domain.PagingVO;
import com.dinens.citron.p.admin.common.domain.Result;
import com.dinens.citron.p.admin.common.service.CommonService;
import com.dinens.citron.p.admin.common.util.CipherUtils;
import com.dinens.citron.p.admin.common.util.CommonUtils;
import com.dinens.citron.p.admin.common.util.MessageUtils;

@Service("adminService")
public class AdminService {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired	
	private AdminMapper adminMapper;
	
	@Autowired	
	private CommonService commonService;

	public List<AdminVO> selectAdminList(AdminVO adminVO) throws Exception {
		return adminMapper.selectAdminList(adminVO, new PagingVO());
	}

	public Map<String, Object> selectAdminList(AdminVO adminVO, PagingVO pagingVO) throws Exception {
		int totalCount = adminMapper.selectAdminListCount(adminVO);		
		if (totalCount > 0) {
			pagingVO.setTotalCount(totalCount);
			CommonUtils.setPaging(pagingVO);
		}
		logger.debug("pagingVO : " + pagingVO);
		List<AdminVO> list = adminMapper.selectAdminList(adminVO, pagingVO);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("pagingVO", pagingVO);
		resultMap.put("list", list);
		
		return resultMap;
	}

	public List<Map<String, Object>> selectAdminListExcel(AdminVO adminVO) throws Exception {
		return adminMapper.selectAdminListExcel(adminVO);
	}

	public AdminVO selectAdminDetail(AdminVO adminVO) {
		return adminMapper.selectAdminDetail(adminVO);
	}

	public Result insertAdmin(AdminVO adminVO, String encrypt) throws Exception {
		// Custom 암호화 전송인 경우
		if ("Y".equals(encrypt)) {
			// Key정보 조회
			KeyInfoVO keyInfo = commonService.selectKeyInfo();
			PrivateKey privateKey = keyInfo.getPrivateKey();
			
			// 데이터 복호화        	
        	adminVO.setPassword(CipherUtils.decryptRSA(adminVO.getPassword(), privateKey));
        	adminVO.setAdminName(CipherUtils.decryptRSA(adminVO.getAdminName(), privateKey));
        	adminVO.setPhoneNumber(CipherUtils.decryptRSA(adminVO.getPhoneNumber(), privateKey));
        	adminVO.setEmail(CipherUtils.decryptRSA(adminVO.getEmail(), privateKey));
		}
		
		// Secure Random Salt 생성(14자리)
		String salt = CipherUtils.genSecureRandomSalt(14);		
		adminVO.setPasswordSalt(salt);
		
		AdminVO info = adminMapper.selectAdminDetail(adminVO);
		if(info != null) {
			return new Result("FAIL", Constants.Error.DUPLICATE, MessageUtils.getMessage("server.common.result.error.104.message"));
		}

		adminMapper.insertAdmin(adminVO);

		return new Result("OK");
	}
	
	public boolean updateAdmin(AdminVO adminVO) {
		return adminMapper.updateAdmin(adminVO) > 0 ? true : false;
	}
	
	public boolean updateAdminPassword(AdminVO adminVO) {
		return adminMapper.updateAdminPassword(adminVO) > 0 ? true : false;
	}
	
	public boolean deleteAdmin(AdminVO adminVO) {
		return adminMapper.deleteAdmin(adminVO) > 0 ? true : false;
	}
}
