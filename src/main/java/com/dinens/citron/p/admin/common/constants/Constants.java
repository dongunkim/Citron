package com.dinens.citron.p.admin.common.constants;

public class Constants {

	public class Upload {
		public static final String IMAGE_URI = "/img";					// 사용자 Upload Image Link URI
		public static final String IMAGE_TEMP_URI = "/img/temp";		// 사용자 Upload Image Temporary Link URI
		
		public static final String UPLOAD_CATEGORY_BOARD = "board";		// upload file 유형 (첨부)
		public static final String UPLOAD_CATEGORY_NOTICE = "notice";	// upload file 유형 (이미지 삽입)
		
		public static final String UPLOAD_TYPE_ATTACH = "attach";		// upload file 유형 (첨부)
		public static final String UPLOAD_TYPE_IMAGE = "image";			// upload file 유형 (이미지 삽입)
	}
	
	public class Error {
		public static final String RESULT_OK = "OK"; 					// 처리결과 성공
		public static final String RESULT_FAIL = "FAIL";	 			// 처리결과 실패
		public static final String SESSION_INVALID = "001"; 			// 세션 오류(NULL)
		public static final String VALUE_INVALID = "002"; 				// 입력값 오류(NULL)
		public static final String ENCRYPT_KEY_INVALID = "101"; 		// 암호키 오류(NULL)
		public static final String LOGIN_NOT_MATCH = "102"; 			// 로그인 불일치 (로그인시 체크)
		public static final String PASSWORD_NOT_MATCH = "103"; 			// 비밀번호 불일치 (비밀번호 변경시 체크)
		public static final String DUPLICATE = "104"; 					// 중복 오류
		public static final String DUPLICATE_DISPLAY_ORDER = "105"; 	// 중복 표시 순서 오류
		public static final String ID_NOT_EXIST = "106"; 				// 아이디 미존재 (비밀번호 찾기)
		public static final String VALUE_NOT_EXIST = "107"; 			// 등록정보 미존재 (비밀번호 찾기)
		public static final String EXPIRED_TEMP_PASSWORD = "108"; 		// 임시비밀번호 유효기간 만료
		public static final String EXPIRED_AUTH_NUMBER = "109"; 		// SMS 인증번호 유효기간 만료
		public static final String ADMIN_EXISTS = "110"; 				// 관리자 그룹을 사용하는 관리자 존재
	}
	
	public class Email {
		public static final String SEND_REASON_SIGN_UP = "01";			// 전송사유_회원가입
		public static final String SEND_REASON_FIND_PASSWORD = "02";	// 전송사유_비밀번호찾기
		public static final String SEND_STATUS_OK = "1";				// 전송상태_성공
		public static final String SEND_STATUS_FAIL = "2";				// 전송상태_실패
	}
	
	public class Code {
		public static final String MEMBER_STATUS = "0001";				// 회원 가입상태 코드
		public static final String MEMBER_TYPE = "0002";				// 회원 가입유형 코드
	}
	
	public class Issue {		
		public static final String ISSUE_TYPE_TEMP_PASSWORD = "01";		// 발급유형 - 임시비밀번호
		public static final String ISSUE_TYPE_AUTH_NUMBER = "02";		// 발급유형 - SMS인증번호
		public static final String EXPIRE_TERM_TEMP_PASSWORD = "1440";	// 임시비밀번호 유효기간 (24시간 - 단위 : 분)
		public static final String EXPIRE_TERM_AUTH_NUMBER = "3";		// SMS 인증번호 유효기간(3분 - 단위 : 분)
	}
	
	
}
