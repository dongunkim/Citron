package com.dinens.citron.p.admin.common.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dinens.citron.p.admin.admingroupauth.domain.InitAdminGroupAuthVO;
import com.dinens.citron.p.admin.admingroupauth.mapper.AdminGroupAuthMapper;
import com.dinens.citron.p.admin.common.exception.ApplicationException;
import com.dinens.citron.p.admin.common.util.MessageUtils;
import com.dinens.citron.p.admin.common.util.StringUtils;
import com.dinens.citron.p.admin.menu.domain.InitMenuVO;
import com.dinens.citron.p.admin.menu.mapper.MenuMapper;

/**
 * <PRE>
 * 1. ClassName: InitService
 * 2. FileName : InitService.java
 * 3. Package  : com.dinens.citron.common.service
 * 4. Comment  : Init Service Class (Memory Preloading)
 * 5. 작성자   : JYPark
 * 6. 작성일   : 2019. 7. 31.
 * 7. 변경이력
 *		이름	:	일자	: 변경내용
 *     ———————————————————————————————————
 *		JYPark:	2019. 07. 31.	: 신규 개발.
  * </PRE>
 */

@Service
public class InitService implements InitializingBean {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MenuMapper menuMapper;
	
	@Autowired
	private AdminGroupAuthMapper adminGroupAuthMapper;
		
	private Map<String, Object> menuMap = new HashMap<String, Object>();
	
	/*
	 * 로그인한 사용자의 Locale과 GroupId에 해당하는 메뉴 정보 조회
	 * DB에서 조회하지 않고 Memory에 있는 menuMep에서 해당 List만 추출해서 리턴
	 * Memory에 해당 Menu정보가 없을 경우에만 DB 조회  
	 */	
	@SuppressWarnings("unchecked")
	public List<InitMenuVO> getMenuInfo(String groupId, String locale) throws Exception {
		
		List<InitMenuVO> list = new ArrayList<InitMenuVO>();
		
		if(StringUtils.isNull(locale))
			locale = "kr";
		
		if (StringUtils.isNull(groupId)){	
			logger.debug("GroupId is null.");
			// [메뉴조회] 권한정보가 null 입니다.
			throw new ApplicationException(MessageUtils.getMessage("server.common.exception.400.view.menu.auth.message"));			
		}
		
		// 현재 메모리에 있으면 메모리에서 추출
		String mapKey = groupId + locale;
		if (menuMap.containsKey(mapKey)) {
			list = (List<InitMenuVO>) menuMap.get(mapKey);
		}
		else {
			// 현재 메모리에 없는 경우, DB 조회 시도
			list = menuMapper.selectAuthMenuList(locale, groupId);
		}
		
		if(list.size() == 0) {
			logger.debug("Nothing matched with GroupId[" + groupId + "].");
			// [메뉴조회] 권한에 해당하는 메뉴정보가 없습니다.
			throw new ApplicationException(MessageUtils.getMessage("server.common.exception.400.view.menu.notfound.message"));
		}
		
		return list;	
	}
	
	/*
	 * Memory에 있는 Menu 정보를 DB 정보와 동기화
	 * 관리자 URL을 통해서만 호출
	 */
	public boolean refresh() {
		List<InitMenuVO> menuInfo = menuMapper.selectAuthMenuList("", "");
		List<InitAdminGroupAuthVO> authInfo = adminGroupAuthMapper.selectAdminGroupAuthList();
		
		if (logger.isDebugEnabled()) logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		if(menuInfo == null || authInfo == null) {			
			if (logger.isDebugEnabled()) logger.debug("Retriving from DB is Fail.");
			return false;
		}
		
		String groupId = "";
		String menuId = "";		
		List<InitMenuVO> menuList = null;
		
		synchronized(menuMap) {
			
			for(InitAdminGroupAuthVO auth : authInfo) {
				if (!groupId.equals(auth.getAdminGroupId())) {
					groupId = auth.getAdminGroupId();
					menuList = new ArrayList<InitMenuVO>();
					
					if(menuMap.containsKey(groupId)) {
						menuMap.remove(groupId);					
						if (logger.isDebugEnabled()) logger.debug("Menu Information Updated : " + groupId);
					}
					else {
						if (logger.isDebugEnabled()) logger.debug("Menu Information Added : " + groupId);
					}
				}				
				
				menuId = auth.getMenuId();
				for (InitMenuVO menu : menuInfo) {				
					if (menuId.equals(menu.getMenuId())) {			
						menuList.add(menu);
						break;
					}
				}
				menuMap.put(groupId, menuList);						
			}
		}
		
		return true;
	}
	
	/*
	 * InitializingBean Interface의 Override 함수
	 * 서버 기동 시, Properties 설정이 끝난 직후 호출됨
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		menuMap.clear();		
		
		List<InitMenuVO> menuInfo = menuMapper.selectAuthMenuList("", "");
		List<InitAdminGroupAuthVO> authInfo = adminGroupAuthMapper.selectAdminGroupAuthList();		
				
		String groupId = "";
		String localeCode = "";
		String menuId = "";
		List<InitMenuVO> menuList = null;		
		for(InitAdminGroupAuthVO auth : authInfo) {
			if (!groupId.equals(auth.getAdminGroupId()) || !localeCode.equals(auth.getLocaleCode())) {				
				groupId = auth.getAdminGroupId();
				localeCode = auth.getLocaleCode();
				menuList = new ArrayList<InitMenuVO>();
			}
			
			menuId = auth.getMenuId();
			for (InitMenuVO menu : menuInfo) {				
				if (localeCode.equals(menu.getLocaleCode()) && menuId.equals(menu.getMenuId())) {			
					menuList.add(menu);
					break;
				}
			}
			String mapKey = groupId + localeCode;
			menuMap.put(mapKey, menuList);
		}
						
		if (logger.isDebugEnabled()) {			
			logger.debug("menuMap : " + menuMap);
			logger.debug("menuMap size : " + menuMap.size());	
		}		
	}	
	
}
