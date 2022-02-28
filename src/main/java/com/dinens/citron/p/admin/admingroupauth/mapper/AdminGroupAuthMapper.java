package com.dinens.citron.p.admin.admingroupauth.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.dinens.citron.p.admin.admingroupauth.domain.AdminGroupAuthVO;
import com.dinens.citron.p.admin.admingroupauth.domain.InitAdminGroupAuthVO;
import com.dinens.citron.p.admin.menu.domain.MenuTreeVO;
import com.dinens.citron.p.admin.menu.domain.MenuVO;

@Component
public interface AdminGroupAuthMapper {
	public List<InitAdminGroupAuthVO> selectAdminGroupAuthList();
	public List<MenuTreeVO> selectMenuAuthTree(AdminGroupAuthVO adminGroupAuthVO);

	public int insertAdminGroupAuth(AdminGroupAuthVO adminGroupAuthVO);
	public int deleteAdminGroupAuth(AdminGroupAuthVO adminGroupAuthVO);

}
