package com.dinens.citron.p.admin.menu.mapper;

import java.util.List;

import com.dinens.citron.p.admin.menu.domain.MenuLocaleVO;

public interface MenuLocaleMapper {
	public List<MenuLocaleVO> selectMenuLocaleList(MenuLocaleVO menuLocaleVO);

	public int insertMenuLocale(MenuLocaleVO menuLocaleVO);
	public int deleteMenuLocale(MenuLocaleVO menuLocaleVO);
}
