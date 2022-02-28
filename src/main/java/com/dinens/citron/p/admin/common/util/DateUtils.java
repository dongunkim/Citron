package com.dinens.citron.p.admin.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public String Sep = "";
    public String dateSep = ".";
    public String hourSep = ":";

    private DateUtils() {
        super();
    }

    /**
     * <pre>
     *  Description	: 나이 구하기 (return age between two date strings with default defined format.(&quot;yyyyMMdd&quot;))
     *  @param String from date string
     *  @param String to date string
     *  @return int 날짜 형식이 맞고, 존재하는 날짜일 때 2개 일자 사이의 나이 리턴
     *            형식이 잘못 되었거나 존재하지 않는 날짜: java.text.ParseException 발생
     * </pre>
     */
    public static int ageBetween(String from, String to)
            throws java.text.ParseException {

        return ageBetween(from, to, "yyyyMMdd");
    }

    /**
     * <pre>
     *  Description	: 나이 구하기 (return age between two date strings with user defined format.)
     *  @param String from date string
     *  @param String to date string
     *  @param format string representation of the date format. For example, &quot;yyyy-MM-dd&quot;.
     *  @return int 날짜 형식이 맞고, 존재하는 날짜일 때 2개 일자 사이의 나이 리턴
     *            형식이 잘못 되었거나 존재하지 않는 날짜: java.text.ParseException 발생
     * </pre>
     */
    public static int ageBetween(String from, String to, String format)
            throws java.text.ParseException {

        return (int) (daysBetween(from, to, format) / 365);
    }
    
    /**
     * <pre>
     *  Description	: 일자 사이의 기간 구하기
     *  return days between two date strings with user defined format.
     *  @param String from date string
     *  @param String to date string
     *  @return int 날짜 형식이 맞고, 존재하는 날짜일 때 2개 일자 사이의 일자 리턴
     *            형식이 잘못 되었거나 존재하지 않는 날짜: java.text.ParseException 발생
     * </pre>
     */
    public static int daysBetween(String from, String to, String format)
            throws java.text.ParseException {
        
        // 날짜 검사
        java.util.Date d1 = check(from, format);
        java.util.Date d2 = check(to, format);

        long duration = d2.getTime() - d1.getTime();

        return (int) (duration / (1000 * 60 * 60 * 24));
    }
        
    /**
     * <pre>
     *  Description	: 지정일을 지정된 형식으로 변환(시간포함)
     *  @return java.lang.String
     *  @param s java.lang.String
     *  @param format java.lang.String
     *  @exception java.lang.Exception 예외 설명.
     * </pre>
     */
    public static String getFormatDate(String s, String fix) {
    	if (s==null||s.equals("")) return "";
    	
    	String strDate = "";
        try {
        	if( s.length() >= 9 ) {
	        	strDate += s.substring(0,4) + fix;
	        	strDate += s.substring(4,6) + fix;
	        	strDate += s.substring(6,8);
	        	
	        	strDate += " " + s.substring(8,10);
	        	strDate += ":" + s.substring(10,12);
	        	strDate += ":" + s.substring(12,14);
	        	
        	}else if(s.length() == 8 ){
        		
        		strDate += s.substring(0,4) + fix;
	        	strDate += s.substring(4,6) + fix;
	        	strDate += s.substring(6,8);
	        	
        	} else {
        		strDate += s.substring(0,4) + fix;
	        	strDate += s.substring(4,6);
        	}
        } catch (Exception ex) {
            return strDate;
        }

        return strDate;
    } 
    
    /**
     * <pre>
     *  Description	: 지정일을 지정된 형식으로 변환(시간 미포함)
     *  @return java.lang.String
     *  @param s java.lang.String
     *  @param format java.lang.String
     *  @exception java.lang.Exception 예외 설명.
     * </pre>
     */
    public static String getFormatDateOnly(String s, String fix) {
    	if (s==null||s.equals("")) return "";
    	
    	String strDate = "";
        try {
        	if(s.length() >= 8 ){
        		
        		strDate += s.substring(0,4) + fix;
	        	strDate += s.substring(4,6) + fix;
	        	strDate += s.substring(6,8);
	        	
        	} else {
        		strDate += s.substring(0,4) + fix;
	        	strDate += s.substring(4,6);
        	}
        } catch (Exception ex) {
            return strDate;
        }

        return strDate;
    }
    
    /**
     * <pre>
     * Description  : 현재날짜-시간 조회 
     * @return Timestamp
     * </pre> 
     */
    public static String getCurrDateTime() {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    	
    	return sdf.format(new java.util.Date());
    }
    
    /**
     * <pre>
     *  Description	: 현재 일자를 지정된 형식으로 문자 변환
     *  For example, String time = DateTime.getFormatString(&quot;yyyy-MM-dd HH:mm:ss&quot;);
     * 
     *  @param java.lang.String pattern  "yyyyMMdd" or "yyyy-MM-dd" or "yyyy.MM.dd" etc...
     *                                   "HHmmss" or "HH:mm:ss" etc..
     *                                   "yyyyMMddHHmmss" or "yyyy-MM-dd HH:mm:ss" etc...
     *  @return formatted string representation of current day and time with  your pattern.
     * </pre>
     */
    public static String getDateTimeString(String pattern) {

        // 형식 지정
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(pattern, java.util.Locale.KOREA);

        // 현재일 가져오기
        String dateString = formatter.format(new java.util.Date());

        return dateString;
    }    
    
    /**
     * 자바 String을 Date로 변경하기
     * @param inputDate
     * @return
     */
	public static Date dateFormatter(String inputDate) {
		Date outputDate=null;
    	try{
    		//outputDate=new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z").parse(inputDate);
    		outputDate=new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss").parse(inputDate);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return outputDate;
    }  
	
    /**
     * <pre>
     *  Description	: 주어진 포맷의 일자 검사
     *  check date string validation with an user defined format.
     *  @param s date string you want to check.
     *  @param format string representation of the date format. For example, &quot;yyyy-MM-dd&quot;.
     *  @return date java.util.Date
     * </pre>
     */
    public static java.util.Date check(String s, String format)
            throws java.text.ParseException {
        // 파라메터 검사
        if (s == null) {
            throw new java.text.ParseException("date string to check is null", 0);
        }
        if (format == null) {
            throw new java.text.ParseException("format string to check date is null", 0);
        }

        // 날짜 형식 지정
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(format, java.util.Locale.KOREA);

        // 검사
        java.util.Date date = null;

        try {
            date = formatter.parse(s);
        } catch (java.text.ParseException e) {
            throw new java.text.ParseException(" wrong date:\"" + s + "\" with format \"" + format + "\"", 0);
        }

        // 날짜 포멧이 틀린 경우
        if (!formatter.format(date).equals(s)) {
            throw new java.text.ParseException("Out of bound date:\"" + s + "\" with format \"" + format + "\"", 0);
        }

        // 리턴
        return date;
    }
    
    /**
     * <pre>
     *  Description	: 일자를 검사
     *  check date string validation with an user defined format.
     *  @param s date string you want to check.
     *  @param format string representation of the date format. For example, &quot;yyyy-MM-dd&quot;.
     *  @return boolean true 날짜 형식이 맞고, 존재하는 날짜일 때
     *                  false 날짜 형식이 맞지 않거나, 존재하지 않는 날짜일 때
     * </pre>
     */
    public static boolean isValid(String s, String format) {

        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(format, java.util.Locale.KOREA);

        java.util.Date date = null;

        try {
            date = formatter.parse(s);
        } catch (java.text.ParseException e) {
            return false;
        }

        if (!formatter.format(date).equals(s))
            return false;

        return true;
    }
}
