package com.dinens.citron.p.admin.common.util;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * <PRE>
 * 1. ClassName: ExcelUtils
 * 2. FileName : ExcelUtils.java
 * 3. Package  : com.dinens.citron.common.util
 * 4. Comment  : Excel Download 함수
 *               참고) Header와 Body의 CellStyle은 고정됨
 * 5. 작성자   : JYPark
 * 6. 작성일   : 2019. 7. 31.
 * 7. 변경이력
 *		이름	:	일자	: 변경내용
 *     ———————————————————————————————————
 *		JYPark:	2019. 07. 31.	: 신규 개발.
  * </PRE>
 */

@Component
public class ExcelUtils {

	protected static Logger logger = LoggerFactory.getLogger(ExcelUtils.class);
	
	// 메모리에서 처리할 Row수
	// 이 Row만큼 처리하고 메모리를 비움 (대용량 처리 시에 outOfMemory 방지)
	private final static int ROW_ACCESS_SIZE = 1000;
	
	/**
	 * <pre>
	 * Excel 다운로드 함수
	 * @param fileName : 다운로드 엑셀 파일명
	 *        columnInfo : 엑셀에 표시될 컬럼명 + DB컬럼명
	 *        list : 데이터
	 *        response : HTTP 응답 객체
	 * </pre>
	 */
	@SuppressWarnings("resource")
	public static void downloadExcel(String fileName, List<String[]> columnInfo, List<Map<String, Object>> list, HttpServletResponse response) throws Exception {
		
		SXSSFWorkbook wb = new SXSSFWorkbook(ROW_ACCESS_SIZE);
		Sheet sheet = wb.createSheet();
			
		if (columnInfo.size() != 3) throw new IndexOutOfBoundsException();		
		String[] columnNames = columnInfo.get(0);
		String[] columnIds = columnInfo.get(1);
		String[] columnWidths = columnInfo.get(2);
		
		try {
			// 헤더영역
			makeHeader(columnNames, columnWidths, wb, sheet);		
			
			// 데이터영역
			makeBody(columnIds, wb, sheet, list);	
						
			response.setContentType("application/msexcel");
			response.setHeader("Set-Cookie", "fileDownload=true;path=/");
			response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(fileName, "UTF-8").replace("+", "%20")+".xlsx");
			wb.write(response.getOutputStream());
						
		} catch (Exception e) {
			e.printStackTrace();
			response.setHeader("Set-Cookie", "fileDownload=false;path=/"); 
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); 
			response.setHeader("Content-Type","text/html; charset=utf-8");
						
			OutputStream out = response.getOutputStream();
			byte[] data = new String("Fail...").getBytes();
			out.write(data, 0, data.length);
			out.close();
			
		} finally {
			wb.dispose();
		}
	}
	
	/**
	 * <pre>
	 * Excel Header 생성 함수
	 * - 헤더 스타일 생성: 배경색(라이트그린), Border(가는선), 정렬(가운데)
	 * - 헤더 타이틀 생성
	 * @param columnNames : 엑셀에 표시될 컬럼명
	 *        wb : 다운로드할 Excel Workbook 객체
	 *        sheet : 다운로드할 Excel Sheet 객체
	 * </pre>
	 */
	private static void makeHeader(String[] columnNames, String[] columnWidths, SXSSFWorkbook wb, Sheet sheet) throws Exception {
		
		CellStyle headStyle = wb.createCellStyle();
		
		// 스타일 - 경계선
		headStyle.setBorderTop(BorderStyle.THIN);
		headStyle.setBorderBottom(BorderStyle.THIN);
		headStyle.setBorderLeft(BorderStyle.THIN);
		headStyle.setBorderRight(BorderStyle.THIN);
		
		// 스타일 - 배경
		headStyle.setFillForegroundColor(HSSFColorPredefined.LIGHT_GREEN.getIndex());
		headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				
		// 스타일 - 정렬
		headStyle.setAlignment(HorizontalAlignment.CENTER);		
		
		Row row = null;
		Cell cell = null;
		
		// 헤더 생성
		row = sheet.createRow(0);
		for (int i=0; i<columnNames.length; i++) {
			cell = row.createCell(i);
			cell.setCellStyle(headStyle);
			cell.setCellValue(columnNames[i]);
			sheet.setColumnWidth(i, Integer.parseInt(columnWidths[i]));
		}		
	}
	
	/**
	 * <pre>
	 * Excel Body 생성 함수
	 * - 바디 스타일 생성: Border(가는선)
	 * - 바디 데이터 생성 
	 * @param columnIds : 엑셀에 표시될 데이터 DB 컬럼명 + 데이터 정렬값(C/L/R) -> 분리자 (':')
	 *        wb : 다운로드할 Excel Workbook 객체
	 *        sheet : 다운로드할 Excel Sheet 객체
	 *        list : 데이터
	 * </pre>
	 */	
	private static void makeBody(String[] columnIds, SXSSFWorkbook wb, Sheet sheet, List<Map<String, Object>> list) throws Exception {
			
		// 중앙정렬(Default) 셀스타일
		CellStyle bodyStyle = wb.createCellStyle();
		bodyStyle.setBorderTop(BorderStyle.THIN);
		bodyStyle.setBorderBottom(BorderStyle.THIN);
		bodyStyle.setBorderLeft(BorderStyle.THIN);
		bodyStyle.setBorderRight(BorderStyle.THIN);
		bodyStyle.setAlignment(HorizontalAlignment.CENTER);
		
		// 좌측정렬 셀스타일
		CellStyle leftStyle = wb.createCellStyle();
		leftStyle.setAlignment(HorizontalAlignment.LEFT);
		leftStyle.setBorderTop(BorderStyle.THIN);
		leftStyle.setBorderBottom(BorderStyle.THIN);
		leftStyle.setBorderLeft(BorderStyle.THIN);
		leftStyle.setBorderRight(BorderStyle.THIN);
		
		// 우측정렬 셀스타일
		CellStyle rightStyle = wb.createCellStyle();
		rightStyle.setAlignment(HorizontalAlignment.RIGHT);
		rightStyle.setBorderTop(BorderStyle.THIN);
		rightStyle.setBorderBottom(BorderStyle.THIN);
		rightStyle.setBorderLeft(BorderStyle.THIN);
		rightStyle.setBorderRight(BorderStyle.THIN);
		
		
		Row row = null;
		Cell cell = null;
		
		// 데이터 생성
		Map<String, Object> tMap;		
		for(int i=0; i<list.size(); i++) {
			tMap = list.get(i);			
			row = sheet.createRow(i+1);
			
			for(int j=0; j<columnIds.length; j++) {
				cell = row.createCell(j);
				String[] sArr = columnIds[j].split(":");
				
				if (sArr[1].equals("L")) cell.setCellStyle(leftStyle);
				else if (sArr[1].equals("R")) cell.setCellStyle(rightStyle);
				else cell.setCellStyle(bodyStyle);
				
				cell.setCellValue(tMap.get(sArr[0]).toString());
			}			
		}
	}
}
