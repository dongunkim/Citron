package com.dinens.citron.p.admin.common.util;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;

/**
 * <PRE>
 * 1. ClassName: MailUtils
 * 2. FileName : MailUtils.java
 * 3. Package  : com.dinens.citron.common.util
 * 4. Comment  : 메일관련 함수 모음
 * 5. 작성자   : JYPark
 * 6. 작성일   : 2019. 7. 31.
 * 7. 변경이력
 *		이름	:	일자	: 변경내용
 *     ———————————————————————————————————
 *		JYPark:	2019. 07. 31.	: 신규 개발.
  * </PRE>
 */

@Component
public class MailUtils {
	
	private static Logger logger = LoggerFactory.getLogger(MailUtils.class);
		
	private static String mailSenderName   = "Citron";				//보내는 이름
	private static String mailSenderEmail  = "system@citron.com";	//보내는 메일주소
		
	private static String emailMode;
	@Value("${email.mode}")
	private void setMode(String mode) {
		emailMode = mode;
	}
	private static String sendgridApiKey;	
	@Value("${sendgrid.api.key}")
	private void setApiKey(String apiKey) {
		sendgridApiKey = apiKey;
	}
	
	/**
	 * 메일 발송 처리 with SMTP
 	 * 
	 */
	public static boolean sendMail(Map<String, Object> mailInfoMap) throws Exception {
		
		boolean sendResult = true;
		switch(emailMode) {
		case "api":
			sendResult = MailUtils.sendgridMail(mailInfoMap);				
			break;
		case "google":
			sendResult = MailUtils.googleMail(mailInfoMap);
			break;
		case "smtp":
			sendResult = MailUtils.smtpMail(mailInfoMap);
			break;
		}
		return sendResult;
	}
	
	/**
	 * 메일 발송 처리 with SMTP
 	 * 
	 */
    public static boolean smtpMail(Map<String, Object> mailInfoMap) throws Exception {
    	    	    	
    	String toAddr = (String)mailInfoMap.get("toAddr");
    	String title = (String)mailInfoMap.get("title");
    	String content = (String)mailInfoMap.get("content");
    	    	
    	if( StringUtils.isNull(toAddr) || 
    			StringUtils.isNull(title) || 
    				StringUtils.isNull(content)) {
    		return false;
    	}
    	
    	String smtpAddr         = "127.0.0.1";			//SMTP서버 주소
    	String smtpLoginId      = "anonymous";			//SMTP서버 ID (default=anonymous)
    	String smtpLoginPw      = "";					//SMTP서버 패스워드
    	
        Properties props = System.getProperties();
        boolean result = true;
        
        try{
        	String encode = "";
        	MimeMessage msg = null;
        	        	
        	// SMTP 서버 정보 설정	    		
    		encode = "UTF-8";	    		
            props.put("mail.smtp.host", smtpAddr);
            props.put("mail.smtp.auth", "false");
            props.put("mail.smtp.port", "25");
            
            //session 생성 및  MimeMessage생성
            Session session = null;
            if ("anonymous".equals(smtpLoginId)) {	            	
	            session = Session.getDefaultInstance(props, null);	
            }
            else {            	
            	SmtpAuthentication auth = new SmtpAuthentication(smtpLoginId, smtpLoginPw);                
                session = Session.getDefaultInstance(props, auth);
            }
            
            msg = new MimeMessage(session);
            
            //메일 본문을 MIME 형태로 변환후 메일에 첨부    		
            MimeBodyPart mBodyPart;
    		mBodyPart = new MimeBodyPart();
    		mBodyPart.setDisposition(Part.INLINE);
    		mBodyPart.setContent("bodytext","text/html; charset="+encode);
    		MimeMultipart mmContent = new MimeMultipart();
    		mmContent.addBodyPart(mBodyPart);
    		
    		//메일 컨텐츠에 작성한 본문 첨부
    		msg.setContent(mmContent);
    		mBodyPart.setContent(content,"text/html; charset="+encode);
            
    		// 편지보낸시간
            msg.setSentDate(new Date());
            
            InternetAddress from = new InternetAddress() ;
            from = new InternetAddress(mailSenderName+"<"+mailSenderEmail+">");
            
            // 이메일 발신자
            msg.setFrom(from);
                        
            // 이메일 수신자
            InternetAddress to = new InternetAddress(toAddr);
            msg.setRecipient(Message.RecipientType.TO, to);
             
            msg.setSubject(title, encode);				// 이메일 제목            
            msg.setText(content, encode);				// 이메일 내용             
            msg.setHeader("content-Type", "text/html");	// 이메일 헤더             
            
            logger.debug("content : " + content);
            
            javax.mail.Transport.send(msg);				// 메일 보내기
        
        }catch (AddressException addr_e) {
            addr_e.printStackTrace();
            result = false;
        
        }catch (MessagingException msg_e) {
            msg_e.printStackTrace();
            result = false;
        }
        
		return result;
    }
    
    
    /**
	 * 메일 발송 처리 with Google
 	 * 
	 */
    public static boolean googleMail(Map<String, Object> mailInfoMap) throws Exception {
    	    	    	
    	String toAddr = (String)mailInfoMap.get("toAddr");
    	String title = (String)mailInfoMap.get("title");
    	String content = (String)mailInfoMap.get("content");
    	    	
    	if( StringUtils.isNull(toAddr) || 
    			StringUtils.isNull(title) || 
    				StringUtils.isNull(content)) {
    		return false;
    	}
    	    	
        Properties props = System.getProperties();
        boolean result = true;
        
        try{
        	String encode = "";
        	MimeMessage msg = null;
        	        	
        	// GMail 설정        		
            encode = "EUC-KR";
            props.put("mail.smtp.starttls.enable", "true");     // gmail은 무조건 true 고정
            props.put("mail.smtp.host", "smtp.gmail.com");      // gmail smtp 서버 주소
            props.put("mail.smtp.auth", "true");                // gmail은 무조건 true 고정
            props.put("mail.smtp.port", "587");                 // gmail smtp 포트
            
            String id = "xxxxx@gmail.com";
	        String pw = "";
	        
            SmtpAuthentication auth = new SmtpAuthentication(id, pw);                
            Session session = Session.getDefaultInstance(props, auth);                                
            msg = new MimeMessage(session);
            
            //메일 본문을 MIME 형태로 변환후 메일에 첨부    		
            MimeBodyPart mBodyPart;
    		mBodyPart = new MimeBodyPart();
    		mBodyPart.setDisposition(Part.INLINE);
    		mBodyPart.setContent("bodytext","text/html; charset="+encode);
    		MimeMultipart mmContent = new MimeMultipart();
    		mmContent.addBodyPart(mBodyPart);
    		
    		//메일 컨텐츠에 작성한 본문 첨부
    		msg.setContent(mmContent);
    		mBodyPart.setContent(content,"text/html; charset="+encode);
            
    		// 편지보낸시간
            msg.setSentDate(new Date());
            
            InternetAddress from = new InternetAddress() ;
            from = new InternetAddress(mailSenderName+"<"+mailSenderEmail+">");
            
            // 이메일 발신자
            msg.setFrom(from);
                        
            // 이메일 수신자
            InternetAddress to = new InternetAddress(toAddr);
            msg.setRecipient(Message.RecipientType.TO, to);
             
            msg.setSubject(title, encode);				// 이메일 제목            
            msg.setText(content, encode);				// 이메일 내용             
            msg.setHeader("content-Type", "text/html");	// 이메일 헤더     
            
            javax.mail.Transport.send(msg);				// 메일 보내기
        
        }catch (AddressException addr_e) {
            addr_e.printStackTrace();
            result = false;
        
        }catch (MessagingException msg_e) {
            msg_e.printStackTrace();
            result = false;
        }
        
		return result;
    }
        
    
    /**
	 * 메일 발송 처리 with SendGrid (using API Key)
 	 * 
	 */
    public static boolean sendgridMail(Map<String, Object> mailInfoMap) throws Exception {
    	    	
    	Email from = new Email("master@citron.com");
        String subject = (String)mailInfoMap.get("title");
        Email to = new Email((String)mailInfoMap.get("toAddr"));
        Content content = new Content("text/html", (String)mailInfoMap.get("content"));
        Mail mail = new Mail(from, subject, to, content);
        
        SendGrid sg = new SendGrid(sendgridApiKey);
        Request request = new Request();
        
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            String buildStr = mail.build();
            logger.debug("buildStr : \n" + buildStr);
            request.setBody(buildStr);
            Response response = sg.api(request);
            
            logger.debug("response.getStatusCode : " + response.getStatusCode());
            logger.debug("response.getBody : " + response.getBody());
            logger.debug("response.getHeaders : " + response.getHeaders());
            
          } catch (IOException ex) {
            ex.printStackTrace();
            return false;
          }
        
    	return true;
    }
    
    
    /**
	 * 메일 컨텐츠를 가져옴
     * @throws IOException 
	 */
    public static String getMailHtml(String path) throws IOException {
    	        
    	ClassPathResource resource = new ClassPathResource(path);
    	return FileUtils.copyToString(resource.getInputStream());  	
        
    }

    
    /**
	 * SMTP 서버 or Google 메일 서버 접속용 인증클래스
	 */
    public static class SmtpAuthentication extends Authenticator {
	    PasswordAuthentication pa;
	 
	    // 구글의 보안 설정 변경해야 함
	    // 구글계정 > 보안 > 보안 수준이 낮은 앱의 액세스 : 사용
	    SmtpAuthentication(String id, String pw) {	        
	        pa = new PasswordAuthentication(id, pw);
	    }
	 
	    // 시스템에서 사용하는 인증정보
	    public PasswordAuthentication getPasswordAuthentication() {
	        return pa;
	    }
	}
}
