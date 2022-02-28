package com.dinens.citron.p.admin.admingroupauth.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dinens.citron.p.admin.admingroupauth.domain.AdminGroupAuthVO;
import com.dinens.citron.p.admin.admingroupauth.domain.InitAdminGroupAuthVO;
import com.dinens.citron.p.admin.admingroupauth.mapper.AdminGroupAuthMapper;
import com.dinens.citron.p.admin.menu.domain.MenuTreeVO;

@Service("adminGroupAuthService")
public class AdminGroupAuthService {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired	
	private AdminGroupAuthMapper adminGroupAuthMapper;
	
	public List<InitAdminGroupAuthVO> selectAdminGroupAuthList() throws Exception {
		return adminGroupAuthMapper.selectAdminGroupAuthList();
	}

	public List<MenuTreeVO> selectMenuAuthTree(AdminGroupAuthVO adminGroupAuthVO) throws Exception {
		return adminGroupAuthMapper.selectMenuAuthTree(adminGroupAuthVO);
	}
}
