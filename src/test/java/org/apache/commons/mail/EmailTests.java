package org.apache.commons.mail;

import javax.mail.internet.MimeMessage;
import javax.mail.MessagingException;
import java.util.Properties;
import javax.mail.Session;
import static org.junit.Assert.*;
import java.util.Date;
import org.junit.Before; 
import org.junit.Test; 
import org.junit.After;


public class EmailTests {

	public Email email;
	
	@Before
	public void setUp() {
		email = new SimpleEmail();
		email.setHostName("Jared");
	}
	
	@After
	public void tearDown(){
		email = null;
	}
	
	
	@Test	// Testing Email addBcc(String.. emails)
	public void testAddBcc_ValidInputs() throws EmailException{
		assertEquals(email, email.addBcc("jared.wensley@gmail.com", "jwens@umich.edu", "jared.wensley@wensley.net", "Zane.smooth@yahoo.gov"));
	}
	
	@Test 	// Testing Email addBcc(String.. emails)
	public void testAddBcc_nullEmail() {
		try {
			email.addBcc();
		}
		catch(EmailException e){
			assertEquals("Address List provided was invalid", e.getMessage());
		}
	}
	
	@Test //Tests addCc(string email)
	public void addCc_Test() throws EmailException {
		assertEquals(email,email.addCc("jared.wensley@wensley.net")); 
	}
	
	@Test // Tests addReplyTo (string email, string name)
	public void addReplyTo_Test() throws EmailException{
		assertEquals(email, email.addReplyTo("jared.wensley@wensley.net", "jared"));
	}
	
	@Test // Tests setFrom(string email)
	public void setFrom_Test() throws EmailException {
		assertEquals(email,email.setFrom("jared.wensley@wensley.net", "jared"));
	}
	
	@Test // Tests addHeader(String name, String value) 1
	public void addHeader_EmptyValue_Test() throws IllegalArgumentException {
		try {
			email.addHeader("jared", "");
		}
		catch(IllegalArgumentException e){
			assertEquals("value can not be null or empty", e.getMessage());
		}
	}
	
	@Test // Tests addHeader(String name, String value) 2
	public void addHeader_EmptyName_Test() throws IllegalArgumentException {
		try {
			email.addHeader("", "5");
		}
		catch(IllegalArgumentException e){
			assertEquals("name can not be null or empty", e.getMessage());
		}
	}
	
	@Test // Tests addHeader(String name, String value) 3
	public void addHeader_success_Test() throws IllegalArgumentException {
		email.addHeader("jared", "5");
		
		assertEquals("5", email.headers.get("jared"));
		
	}
	
	// Test buildMimeMessage() 1
	@Test // Create a mock class to test buildMimeMessage()
	public void buldMimeMessage_Test() throws EmailException{
		   
		   email.setHostName("smtp.example.com");
		   email.setFrom("test@example.com");
		   email.addTo("recipient@example.com");
		   email.setSubject("Test Subject");
		   email.setMsg("Test Message");

		   email.buildMimeMessage();

		   assertNotNull(email.getMimeMessage()); // Ensure the MimeMessage is built
	}
	
	// Test buildMimeMessage() 2
	@Test 
	public void buldMimeMessage_Test2() throws EmailException{
		   email.setHostName("i@t");
		   email.setFrom("c@g ");
		   email.addTo("d@d ");
		   email.setSubject("b@s ");
		   email.setMsg("a@ ");
		   
		   email.setContent("TestWords", "TestingTheBestWords");
		  
		   email.buildMimeMessage();
		   
		   

		   assertNotNull(email.getMimeMessage()); // Ensure the MimeMessage is built
		  // assertEquals("TestWords", email.getMimeMessage().getDataHandler().getContentType());
	}
	
	// Test buildMimeMessage() 3
	@Test(expected = IllegalStateException.class)
	public void testBuildMimeMessage_CalledTwice_ShouldThrowException() throws EmailException {
	   
	    email.setHostName("smtp.example.com");
	    email.setFrom("test@example.com");
	    email.addTo("recipient@example.com");

	    email.buildMimeMessage(); // First call (OK)
	    email.buildMimeMessage(); // Second call (Should throw exception)
	}
	
	// Test buildMimeMessage() 4
	@Test(expected = IllegalArgumentException.class)
	public void testBuildMimeMessage_ThrowsEmailException_WhenMessagingFails() throws Exception {
	    email.setHostName("smtp.example.com");
	    email.setFrom("test@example.com");
	    email.addTo("recipient@example.com");

	    // Simulate MessagingException by setting an invalid session
	    email.setMailSession(null);

	    email.buildMimeMessage(); // Should throw EmailException
	}
	
	// Test buildMimeMessage() 5
	@Test(expected = EmailException.class)
	public void testBuildMimeMessage_MissingRecipients_ShouldThrowException() throws EmailException {
	    email.setHostName("smtp.example.com");
	    email.setFrom("test@example.com");

	    email.buildMimeMessage(); // Should fail because no recipients exist
	}
	
	// Test buildMimeMessage() 6
	@Test(expected = EmailException.class)
	public void testBuildMimeMessage_MissingFromAddress_ShouldThrowException() throws EmailException {
	    email.setHostName("smtp.example.com");
	    email.addTo("recipient@example.com");

	    email.buildMimeMessage(); // Should fail because 'from' is missing
	}
	
	// Test buildMimeMessage() 7
	@Test
	public void testBuildMimeMessage_HeadersAddedCorrectly() throws EmailException {
	    email.setHostName("smtp.example.com");
	    email.setFrom("test@example.com");
	    email.addTo("recipient@example.com");

	    // Adding headers
	    email.addHeader("X-Test-Header", "HeaderValue");
	    email.addHeader("X-Custom-Header", "CustomValue");

	    email.buildMimeMessage();

	    MimeMessage mimeMessage = email.getMimeMessage();

	    assertNotNull(mimeMessage);
	    try {
	    	 assertEquals("HeaderValue", mimeMessage.getHeader("X-Test-Header")[0]); // Verify header 1
	 	    assertEquals("CustomValue", mimeMessage.getHeader("X-Custom-Header")[0]); // Verify header 2
	 	}
	    catch(MessagingException e){
	    	 fail("MessagingException thrown: " + e.getMessage());
	    }
	}
	
	@Test // tests the return this.hostName part of this method.
	public void getHostName_Test() {
		email.setHostName("jared");
		
		assertEquals("jared", email.getHostName());
	}
	
	@Test // tests the this.session != null line
	public void getHostName_Test_NotNull() {
        Properties props = new Properties();
        props.setProperty("mail.smtp.host", "jared.mailserver.com");              
        Session session = Session.getInstance(props);
            
        email.setMailSession(session);
        assertEquals("jared.mailserver.com", email.getHostName());
	}
	
	
	 @Test // by setting the SSL to true it tests more lines
	 public void testGetMailSession_SSLOnConnectEnabled() throws EmailException {
	     email.setHostName("jared.wensley@wensley.net");
	     email.setSSLOnConnect(true); 
	     email.setSslSmtpPort("465"); 

	     Session session = email.getMailSession();
	     Properties properties = session.getProperties();

	     assertEquals("465", properties.getProperty("mail.smtp.port"));
	     assertEquals("465", properties.getProperty("mail.smtp.socketFactory.port"));
	     assertEquals("javax.net.ssl.SSLSocketFactory", properties.getProperty("mail.smtp.socketFactory.class"));
	     assertEquals("false", properties.getProperty("mail.smtp.socketFactory.fallback"));
	 }
	
	 @Test // Testing the 
	 public void getSentDate_Test() {
		 Date TestingTime = new Date(1700000000000L);
		 
		 email.setSentDate(TestingTime);
		 Date TestAgainst = email.getSentDate();
		 
		 assertEquals(TestingTime.getTime(), TestAgainst.getTime());
	 }
	
	 @Test
	 public void getSocketConnectionTimeout_Test() {
		 email.setSocketConnectionTimeout(500);
		 assertEquals(500, email.getSocketConnectionTimeout());
	 }
	 
	 


}
