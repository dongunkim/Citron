package com.dinens.citron.p.admin.common.common;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.stereotype.Component;

/**
 * <PRE>
 * 1. ClassName: ParamMap
 * 2. FileName : ParamMap.java
 * 3. Package  : com.dinens.citron.common.common
 * 4. Comment  : 사용자 정의 ParameterMap
 *               CustomMapArgumentResolver에서 설정
 * 5. 작성자   : JYPark
 * 6. 작성일   : 2019. 7. 31.
 * 7. 변경이력
 *		이름	:	일자	: 변경내용
 *     ———————————————————————————————————
 *		JYPark:	2019. 07. 31.	: 신규 개발.
  * </PRE>
 */

@Component
public class ParamMap {

	Map<String, Object> map = new HashMap<String, Object>();
	
	public Object get(String key) {
		return map.get(key);
	}
	
	public void put(String key, Object value) {
		map.put(key, value);
	}
	
	public Object remove(String key) {
		return map.remove(key);
	}
	
	public boolean containsKey(String key) {
		return map.containsKey(key);
	}
	
	public void clear() {
		map.clear();
	}
	
	public Set<Entry<String, Object>> entrySet() {
		return map.entrySet();
	}
	
	public Set<String> keySet() {
		return map.keySet();
	}
	
	public boolean isEmpty() {
		return map.isEmpty();
	}
	
	public void putAll(Map<? extends String, ? extends Object> m) {
		map.putAll(m);
	}
	
	public Map<String, Object> getMap() {
		return map;
	}
	
}
