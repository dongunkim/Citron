package com.dinens.citron.p.admin.menu.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dinens.citron.p.admin.common.constants.Constants;
import com.dinens.citron.p.admin.common.domain.Result;
import com.dinens.citron.p.admin.common.service.SeqInfoService;
import com.dinens.citron.p.admin.common.util.MessageUtils;
import com.dinens.citron.p.admin.menu.domain.MenuLocaleVO;
import com.dinens.citron.p.admin.menu.domain.MenuTreeVO;
import com.dinens.citron.p.admin.menu.domain.MenuVO;
import com.dinens.citron.p.admin.menu.mapper.MenuLocaleMapper;
import com.dinens.citron.p.admin.menu.mapper.MenuMapper;

@Service("menuService")
public class MenuService {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired	
	private MenuMapper menuMapper;

	@Autowired	
	private MenuLocaleMapper menuLocaleMapper;

	@Autowired
	private SeqInfoService seqInfoService;

	private final String MENU_TABLE_NAME = "T_MENU";

	public List<MenuVO> selectAuthTopMenuList(String locale, String adminId) throws Exception {
		return menuMapper.selectAuthTopMenuList(locale, adminId);
	}
	
	public List<MenuVO> selectAuthSubMenuList(String locale, String adminId, String topMenuId) throws Exception {
		return menuMapper.selectAuthSubMenuList(locale, adminId, topMenuId);
	}

	public List<MenuTreeVO> selectMenuTree(MenuVO menuVO) throws Exception {
		return menuMapper.selectMenuTree(menuVO);
	}

	public MenuVO selectMenuDetail(MenuVO menuVO) throws Exception {
		MenuVO vo = menuMapper.selectMenuDetail(menuVO);
		MenuLocaleVO menuLocaleVO = new MenuLocaleVO();
		menuLocaleVO.setMenuId(vo.getMenuId());
		List<MenuLocaleVO> list = menuLocaleMapper.selectMenuLocaleList(menuLocaleVO);
		String[] localeCode = new String[list.size()];
		String[] menuNameLocale = new String[list.size()];
		for(int i = 0 ; i < list.size() ; i++) {
			localeCode[i] = list.get(i).getLocale();
			menuNameLocale[i] = list.get(i).getMenuName();
		}
		vo.setLocaleCode(localeCode);
		vo.setMenuNameLocale(menuNameLocale);
		return vo;
	}

	public Result insertMenu(MenuVO menuVO) throws Exception {
		if(menuMapper.selectDisplayOrderCount(menuVO) > 0)
			return new Result("FAIL", Constants.Error.DUPLICATE_DISPLAY_ORDER, MessageUtils.getMessage("server.common.result.error.105.message"));

		String menuId = seqInfoService.selectNextId(MENU_TABLE_NAME);
		menuVO.setMenuId(menuId);
		menuMapper.insertMenu(menuVO);
		
		MenuLocaleVO menuLocaleVO = new MenuLocaleVO();
		menuLocaleVO.setMenuId(menuVO.getMenuId());
		menuLocaleVO.setCreateId(menuVO.getCreateId());
		for(int i = 0 ; i < menuVO.getLocaleCode().length ; i++) {
			menuLocaleVO.setLocaleCode(menuVO.getLocaleCode()[i]);
			menuLocaleVO.setMenuName(menuVO.getMenuNameLocale()[i]);
			menuLocaleVO.setDisplayOrder(Integer.toString(i+1));
			menuLocaleMapper.insertMenuLocale(menuLocaleVO);
		}

		return new Result("OK");
	}

	public Result updateMenu(MenuVO menuVO) {
		if( menuMapper.selectDisplayOrderCount(menuVO) > 0)
			return new Result("FAIL", Constants.Error.DUPLICATE_DISPLAY_ORDER, MessageUtils.getMessage("server.common.result.error.105.message"));
		
		menuMapper.updateMenu(menuVO);

		MenuLocaleVO menuLocaleVO = new MenuLocaleVO();
		menuLocaleVO.setCreateId(menuVO.getUpdateId());
		menuLocaleVO.setMenuId(menuVO.getMenuId());
		menuLocaleMapper.deleteMenuLocale(menuLocaleVO);
		for(int i = 0 ; i < menuVO.getLocaleCode().length ; i++) {
			menuLocaleVO.setLocaleCode(menuVO.getLocaleCode()[i]);
			menuLocaleVO.setMenuName(menuVO.getMenuNameLocale()[i]);
			menuLocaleVO.setDisplayOrder(Integer.toString(i+1));
			menuLocaleMapper.insertMenuLocale(menuLocaleVO);
		}

		return new Result("OK");
	}
	
	public void deleteMenu(MenuVO menuVO) {
		menuMapper.deleteMenu(menuVO);
		MenuLocaleVO menuLocaleVO = new MenuLocaleVO();
		menuLocaleVO.setMenuId(menuVO.getMenuId());
		menuLocaleMapper.deleteMenuLocale(menuLocaleVO);
	}
}
