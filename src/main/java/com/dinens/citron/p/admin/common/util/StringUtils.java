package com.dinens.citron.p.admin.common.util;

import java.util.StringTokenizer;

//import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.gson.Gson;

/**
 * <PRE>
 * 1. ClassName: StringUtils
 * 2. FileName : StringUtils.java
 * 3. Package  : com.dinens.citron.common.util
 * 4. Comment  : String Utility Function 모음
 * 5. 작성자   : JYPark
 * 6. 작성일   : 2019. 7. 31.
 * 7. 변경이력
 *		이름	:	일자	: 변경내용
 *     ———————————————————————————————————
 *		JYPark:	2019. 07. 31.	: 신규 개발.
  * </PRE>
 */
public class StringUtils {

	/**
	 * <pre>
	 * 문자열 Split 함수
	 * </pre>
	 */
	public static String [] split (String value, String token) {
	    String[] ret = null;
	    StringTokenizer st = null;
	    int len = 0;
		
		value = (value==null?"":value.trim());
		st = new StringTokenizer(value, token);
		len = st.countTokens();
		ret = new String[len];
		
		for(int i=0;i<len;i++)
			ret[i] = st.nextToken();	
	    return ret;
	}
	
	/**
	 * <pre>
	 * Bean을 Json으로 변환하는 함수
	 * </pre> 
	 */
	public static String beanToJson(Object src)	{
		return new Gson().toJson(src);
	}
	
	
	/**
	 * <pre>
	 * Json을 Bean으로 변환하는 함수
	 * </pre>
	 */
	public static <T> T JsonTobean( String json, Class<T> classOfT )
	{
		if( classOfT == null )
			return null;
		return new Gson().fromJson(json, classOfT );
	}
	
	
	/**
	 * <pre>
	 * file(Json String)을 Bean으로 변환하는 함수
	 * </pre>
	 */
	public static <T> T fileTobean( String filePath, Class<T> classOfT )
	{
		if( classOfT == null )
			return null;
		return new Gson().fromJson( FileUtils.fileToString( filePath ), classOfT );
	}
	
	
	/**
	 * <pre>
	 * Null To ""
	 * </pre>
	 */
	public static String nullToEmpty( String value )
	{
		if(value == null)
			return "";
		else
			return value;
	}
	
	/**
	 * <pre>
	 * 문자열 좌우 공백/탭/개행문자 제거
	 * </pre>
	 */
	public static String trim(String str) throws Exception {

		if (str == null)
			return "";
		int st = 0;
		int count = 0;
		int len = 0;
		try {
			char[] val = str.toCharArray();
			count = val.length;
			len = count;
			while ((st < len)
					&& ((val[st] <= ' ') || (val[st] == ' ')
							|| (val[st] == '\r') || (val[st] == '\n')))
				st++;
			while ((st < len)
					&& ((val[len - 1] <= ' ') || (val[len - 1] == ' ')))
				len--;
		} catch (Exception e) {

			throw e;
		}
		return ((st > 0) || (len < count)) ? str.substring(st, len) : str;
	}
	
	/**
	 * <pre>
	 * 문자열 NULL 체크
	 * </pre>
	 */
	public static boolean isNull(String str) {
		if( "null".equals(str) || "NULL".equals(str) ) {
			return true;
		}
		
		return (str == null || "".equals(str)) ? true : false;
	}
	
	/**
	 * <pre>
	 * 전체  자리수(len) 안에서 c 값을 str 왼쪽에 padding한 문자열 반환
	 * </pre>
	 */
	public static String lpad(String str, int len, String c) throws Exception {
		if (str == null)
			return "";
		if (len < 0 || len < str.length())
			return str;
		StringBuffer buf = null;
		try {
			if (str.length() > len)
				return str.substring(str.length() - len);

			buf = new StringBuffer();
			for (int i = 0; i < len - str.length(); i++) {
				buf.append(c);
			}
			buf.append(str);
		} catch (Exception e) {			
			throw e;
		}
		return buf.toString();
	}
	
	/**
	 * <pre>
	 * 전체  자리수(len) 안에서 "0"을 str 왼쪽에 padding한 문자열 반환
	 * </pre>
	 */
	public static String lpad(String str, int len) throws Exception {

		return lpad(str, len, "0");
	}
	
	/**
	 * <pre>
	 * 전체  자리수(len) 안에서 c 값을 str 오른쪽에 padding한 문자열 반환
	 * </pre>
	 */
	public static String rpad(String str, int len, String c) throws Exception {
		if (str == null)
			return "";
		if (len < 0)
			return str;
		StringBuffer buf = null;
		try {
			if (str.length() > len)
				return str.substring(str.length() - len);

			buf = new StringBuffer();
			buf.append(str);
			for (int i = 0; i < len - str.length(); i++) {
				buf.append(c);
			}

		} catch (Exception e) {
			// log.error("lpad()" + e);
			throw e;
		}
		return buf.toString();
	}
	
	/**
	 * <pre>
	 * 전체  자리수(len) 안에서 "0"을 str 오른쪽에 padding한 문자열 반환
	 * </pre>
	 */
	public static String rpad(String str, int len) throws Exception {

		return rpad(str, len, "0");
	}
	
	/**
	 * HTML의 입력상자 (&ltINPUT&gt, &ltTEXTAREA&gt)에서 원래의 String 형태 그대로 보여지도록 바꿔주는 메소드<br>
	 * db query 시 데이타 특수문자 처리
	 * @param strIn - 입력상자에서도 그대로 보여지기를 원하는 String
	 * @return 형태 그대로 입력상자에서 보여지도록 바뀐 최종 String
	 */
	public static String toHtmlInputText(String strIn) throws Exception {
		String str = strIn;

		str = str.replaceAll("&", "&amp;");
		str = str.replaceAll("<", "&lt;");
		str = str.replaceAll(">", "&gt;");
		str = str.replaceAll("'", "&acute;");
		str = str.replaceAll("\"", "&quot;");

		return str;
	}
		
}
