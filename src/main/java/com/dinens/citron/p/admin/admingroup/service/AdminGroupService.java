package com.dinens.citron.p.admin.admingroup.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dinens.citron.p.admin.admin.domain.AdminVO;
import com.dinens.citron.p.admin.admin.mapper.AdminMapper;
import com.dinens.citron.p.admin.admingroup.domain.AdminGroupVO;
import com.dinens.citron.p.admin.admingroup.mapper.AdminGroupMapper;
import com.dinens.citron.p.admin.admingroupauth.domain.AdminGroupAuthVO;
import com.dinens.citron.p.admin.admingroupauth.mapper.AdminGroupAuthMapper;
import com.dinens.citron.p.admin.common.constants.Constants;
import com.dinens.citron.p.admin.common.domain.PagingVO;
import com.dinens.citron.p.admin.common.domain.Result;
import com.dinens.citron.p.admin.common.service.SeqInfoService;
import com.dinens.citron.p.admin.common.util.CommonUtils;
import com.dinens.citron.p.admin.common.util.MessageUtils;

@Service("adminGroupService")
public class AdminGroupService {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private AdminGroupMapper adminGroupMapper;
	
	@Autowired
	private AdminGroupAuthMapper adminGroupAuthMapper;

	@Autowired
	private AdminMapper adminMapper;

	@Autowired
	private SeqInfoService seqInfoService;

	private final String ADMIN_GROUP_TABLE_NAME = "T_ADMIN_GROUP";

	public List<AdminGroupVO> selectAdminGroupList(AdminGroupVO adminGroupVO) throws Exception {
		return adminGroupMapper.selectAdminGroupList(adminGroupVO, new PagingVO());
	}

	public Map<String, Object> selectAdminGroupList(AdminGroupVO adminGroupVO, PagingVO pagingVO) throws Exception {
		int totalCount = adminGroupMapper.selectAdminGroupListCount(adminGroupVO);		
		if (totalCount > 0) {
			pagingVO.setTotalCount(totalCount);
			CommonUtils.setPaging(pagingVO);
		}
		logger.debug("pagingVO : " + pagingVO);
		List<AdminGroupVO> list = adminGroupMapper.selectAdminGroupList(adminGroupVO, pagingVO);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("pagingVO", pagingVO);
		resultMap.put("list", list);
		
		return resultMap;
	}

	public List<Map<String, Object>> selectAdminGroupListExcel(AdminGroupVO adminGroupVO) throws Exception {
		return adminGroupMapper.selectAdminGroupListExcel(adminGroupVO);
	}

	public AdminGroupVO selectAdminGroupDetail(AdminGroupVO adminGroupVO) {
		return adminGroupMapper.selectAdminGroupDetail(adminGroupVO);
	}

	public void insertAdminGroup(AdminGroupVO adminGroupVO) throws Exception {
		// Admin Group Insert
		String adminGroupId = seqInfoService.selectNextId(ADMIN_GROUP_TABLE_NAME);
		adminGroupVO.setAdminGroupId(adminGroupId);
		adminGroupMapper.insertAdminGroup(adminGroupVO);
		
		// Admin Group Auth Insert
		AdminGroupAuthVO adminGroupAuthVO = new AdminGroupAuthVO();
		adminGroupAuthVO.setAdminGroupId(adminGroupId);
		adminGroupAuthVO.setCreateId(adminGroupVO.getCreateId());
		String[] arrMenuId = adminGroupVO.getMenuIds().split(",");
		for(int i = 0 ; i < arrMenuId.length ; i++) {
			adminGroupAuthVO.setMenuId(arrMenuId[i]);
			adminGroupAuthMapper.insertAdminGroupAuth(adminGroupAuthVO);
		}
	}

	public void updateAdminGroup(AdminGroupVO adminGroupVO) {
		// Admin Group Update
		adminGroupMapper.updateAdminGroup(adminGroupVO);
		
		// Admin Group Auth Delete
		AdminGroupAuthVO adminGroupAuthVO = new AdminGroupAuthVO();
		adminGroupAuthVO.setAdminGroupId(adminGroupVO.getAdminGroupId());

		adminGroupAuthMapper.deleteAdminGroupAuth(adminGroupAuthVO);

		// Admin Group Auth Insert
		adminGroupAuthVO.setCreateId(adminGroupVO.getUpdateId());
		String[] arrMenuId = adminGroupVO.getMenuIds().split(",");
		for(int i = 0 ; i < arrMenuId.length ; i++) {
			adminGroupAuthVO.setMenuId(arrMenuId[i]);
			adminGroupAuthMapper.insertAdminGroupAuth(adminGroupAuthVO);
		}
	}
	
	public Result deleteAdminGroup(AdminGroupVO adminGroupVO) {
		
		AdminVO adminVO = new AdminVO();
		adminVO.setAdminGroupId(adminGroupVO.getAdminGroupId());
		if(adminMapper.selectAdminListCount(adminVO) > 0) {
			return new Result("FAIL", Constants.Error.ADMIN_EXISTS, MessageUtils.getMessage("server.common.result.error.110.message"));
		};
		
		adminGroupMapper.deleteAdminGroup(adminGroupVO);

		AdminGroupAuthVO adminGroupAuthVO = new AdminGroupAuthVO();
		adminGroupAuthVO.setAdminGroupId(adminGroupVO.getAdminGroupId());
		adminGroupAuthMapper.deleteAdminGroupAuth(adminGroupAuthVO);

		return new Result("OK");
	}
}
