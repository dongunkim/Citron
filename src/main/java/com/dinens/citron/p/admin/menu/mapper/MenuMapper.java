package com.dinens.citron.p.admin.menu.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dinens.citron.p.admin.admin.domain.AdminVO;
import com.dinens.citron.p.admin.menu.domain.InitMenuVO;
import com.dinens.citron.p.admin.menu.domain.MenuTreeVO;
import com.dinens.citron.p.admin.menu.domain.MenuVO;

public interface MenuMapper {
	public List<InitMenuVO> selectAuthMenuList(@Param(value="locale") String locale, @Param(value="adminGroupId") String adminGroupId);
	public List<MenuVO> selectAuthTopMenuList(@Param(value="locale") String locale, @Param(value="adminId") String adminId);
	public List<MenuVO> selectAuthSubMenuList(@Param(value="locale") String locale, @Param(value="adminId") String adminId, @Param(value="topMenuId") String topMenuId);

	public List<MenuTreeVO> selectMenuTree(@Param(value="menuVO") MenuVO menuVO);
	public MenuVO selectMenuDetail(MenuVO menuVO);

	public int insertMenu(MenuVO menuVO);
	public int updateMenu(MenuVO menuVO);
	public int deleteMenu(MenuVO menuVO);
	
	public int selectDisplayOrderCount(@Param(value="menuVO") MenuVO menuVO);
}
