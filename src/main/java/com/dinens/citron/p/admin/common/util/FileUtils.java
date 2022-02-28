package com.dinens.citron.p.admin.common.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 * <PRE>
 * 1. ClassName: FileUtils
 * 2. FileName : FileUtils.java
 * 3. Package  : com.dinens.citron.common.util
 * 4. Comment  : File Utility Function 모음
 * 5. 작성자   : JYPark
 * 6. 작성일   : 2019. 7. 31.
 * 7. 변경이력
 *		이름	:	일자	: 변경내용
 *     ———————————————————————————————————
 *		JYPark:	2019. 07. 31.	: 신규 개발.
  * </PRE>
 */
@Component
public class FileUtils {

	static Logger logger = LoggerFactory.getLogger(FileUtils.class);
			
	private static String filePath;	
	@Value("${file.path}")
	private void setPath(String path) {
		filePath = path;
	}
	
	private static String imageTempPath;	
	@Value("${image.temp.path}")
	private void setTempPath(String path) {
		imageTempPath = path;
	}
	
	/**
	 * <pre>
	 * Request에서 업로드된 파일정보 Parsing
	 * MultipartHttpServletRequest를 이용해서 다중 파일 처리
	 * storedFileName에 Path포함
	 * </pre>
	 */
	public static List<Map<String, Object>> parseFileInfo(MultipartHttpServletRequest request, String uploadPath) throws Exception {
		
		List<MultipartFile> files = request.getFiles("fileLists");		
		
		String originalFileName = null;
		String fileExtension = null;
		String storedFileName = null;
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> listMap = null;		
				
		File file = new File(uploadPath);
		if (!file.exists()) {
			file.mkdirs();
		}		
		
		for(MultipartFile multipartFile: files) {			
			if (!multipartFile.isEmpty()) {
				originalFileName = multipartFile.getOriginalFilename();
				fileExtension = getFileExtension(originalFileName);						
				storedFileName = uploadPath + CommonUtils.getRandomString() + "." + fileExtension;
				
				file = new File(storedFileName);
				multipartFile.transferTo(file);
				
				listMap = new HashMap<String, Object>();				
				listMap.put("orgFileName", originalFileName);
				listMap.put("filePath", storedFileName);
				listMap.put("fileSize", multipartFile.getSize());
				list.add(listMap);
			}
		}		
		return list;
		
	}
	
	
	/**
	 * <pre>
	 * Request에서 업로드된 파일정보 Parsing
	 * MultipartFile을 이용해서 단일 파일 처리
	 * storedFileName에 Path포함하지 않음
	 * </pre>
	 */
	public static Map<String, Object> parseFileInfo(MultipartFile uploadFile, String uploadPath) throws Exception {
		
		if (uploadFile.isEmpty()) {
			logger.debug("Fail to store File. Since upload file is empty.");
			throw new Exception("Fail to store File.(Empty file)");
		}
				
		File file = new File(uploadPath);
		if (!file.exists()) {
			file.mkdirs();			
		}
				
		String originalFileName = uploadFile.getOriginalFilename();
		String fileExtension = getFileExtension(originalFileName);
		String storedFileName = CommonUtils.getRandomString() + "." + fileExtension;
						
		file = new File(uploadPath + storedFileName);
		logger.debug("storedFileName : " + uploadPath + storedFileName);
		uploadFile.transferTo(file);
		
		Map<String, Object> fileMap = new HashMap<String, Object>();		
		fileMap.put("originalFileName", originalFileName);
		fileMap.put("storedFileName", storedFileName);
		fileMap.put("fileSize", uploadFile.getSize());		
		
		return fileMap;
	}
		
	/**
	 * <pre>
	 * 파일(Json String)을 String으로 변환
	 * </pre>
	 */
	public static String fileToString( String srcFile ) {
		FileReader 		fileReader 		= null;
		BufferedReader	bufferedReader	= null;
		StringBuffer	sb				= new StringBuffer();
		String			line			= null;
		
		try 
		{
			fileReader = new FileReader( srcFile );
			bufferedReader = new BufferedReader( fileReader );
			while( (line = bufferedReader.readLine() ) != null )
			{
				sb.append( line );
				sb.append( "\n" );
			}
		} 
		catch (FileNotFoundException e) 
		{			
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		}
		finally
		{
			try {
				fileReader.close();
				bufferedReader.close();
			} catch (IOException e) {				
				e.printStackTrace();
			}
		}		
		return sb.toString();
	}
	
	
	
	/**
	 * <pre>
	 * 파일 업로드 경로 구하기
	 * @param category : 업로드 카테고리 (게시판:board, 공지사항:notice, ...)
	 * @param type : 업로드 타입 (첨부:attach, 이미지삽입:image, ...)
	 * </pre>
	 */
	public static String getUploadPath(String category, String type) {		
		return filePath + category + "/" + type + "/";
	}
	
	/**
	 * <pre>
	 * 파일 업로드 임시 경로 구하기
	 * @param userId : 로그인 사용자 아이디	 
	 * </pre>
	 */
	public static String getTempPath(String userId) {				
		return imageTempPath + userId + "/";		
	}
	
	/**
	 * <pre>
	 * 파일명 분리
	 * 형식: D:\data.file -> data.file
	 * </pre>
	 */
	public static String getFileName(String data) {
		if(data.length()!=0){
			int sepPos1 = data.lastIndexOf('\\');
			int sepPos2 = data.lastIndexOf('/');
			data = data.substring((sepPos1>sepPos2?sepPos1:sepPos2)+1,data.length());
		}
		return data;

	}
		
	/**
	 * <pre>
	 * 파일경로 분리
	 * 형식: D:\data.file -> D:\
	 * </pre>
	 */
	public static String getFilePath(String data) {
		if(data.length()!=0){
			int sepPos1 = data.lastIndexOf('\\');
			int sepPos2 = data.lastIndexOf('/');
			data = data.substring(0, (sepPos1>sepPos2?sepPos1:sepPos2)+1);
		}
		return data;

	}
	
	/**
	 * <pre>
	 * 파일확장자 리턴
	 * 형식: test.jpg -> jpg
	 * </pre>
	 */
	public static String getFileExtension(String data) {
		if(data.length()!=0){
			data = data.substring(data.lastIndexOf('.')+1);
		}
		return data;
	}
	
	/**
	 * <pre>
	 * 파일 InputStream을 String으로 변환해서 리턴
	 * </pre>
	 */
	public static String copyToString(InputStream input) throws IOException {
		
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		
		int nRead;
		byte[] data = new byte[16384];
		while( (nRead = input.read(data)) != -1 ) {
		 buffer.write(data, 0, nRead);
		}
		buffer.flush();

		return new String(buffer.toByteArray());
	}
	
	/**
	 * <pre>
	 * 파일을 From에서 To로 이동
	 * @param from : srcFile
	 * @param to : destFile
	 * to 디렉토리가 존재하지 않을 경우 생성하나, to가 존재할 경우 IOException 
	 * </pre>
	 */
	public static void fileMove(File from, File to) throws Exception {
		org.apache.commons.io.FileUtils.moveFile(from, to);
	}
	
	/**
	 * <pre>
	 * (첨부)파일 다운로드
	 * </pre>
	 */
	public static void fileDownload(String originalFileName, String filePath, HttpServletResponse response) throws Exception {
		byte fileByte[] = org.apache.commons.io.FileUtils.readFileToByteArray(new File(filePath));
		
		response.setContentType("application/octet-stream");
		response.setContentLength(fileByte.length);
		response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode(originalFileName, "UTF-8") + "\";");
		response.setHeader("Content-Transfer-Encoding", "binary");
		response.getOutputStream().write(fileByte);
		response.getOutputStream().flush();
		response.getOutputStream().close();
	}
}
