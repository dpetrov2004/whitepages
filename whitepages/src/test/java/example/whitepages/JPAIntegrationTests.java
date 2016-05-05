package example.whitepages;

import static org.junit.Assert.*;

import java.util.List;

import javax.inject.Inject;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import example.whitepages.db.ContactService;
import example.whitepages.db.UserService;
import example.whitepages.db.entities.Contacts;
import example.whitepages.db.entities.Users;
import example.whitepages.utils.AppController;
import example.whitepages.utils.DoubleLoginException;
import example.whitepages.utils.SessionSettings;
import example.whitepages.utils.StorageType;
import example.whitepages.web.PageFilters;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WhitepagesApplication.class)
@WebAppConfiguration
public class JPAIntegrationTests {
	
	@Inject
    private ContactService contactService;
	
	@Inject
    private UserService userService;
	
	@Inject
	SessionSettings sessionSettings;
	
	@BeforeClass
	public static void initTest() {
		AppController.getInstance().setCurrentStorageType(StorageType.MySQL);
		
		System.getProperties().put("spring.datasource.driver-class-name", "org.h2.Driver");
		System.getProperties().put("spring.datasource.url", "jdbc:h2:mem:test");
		System.getProperties().put("spring.datasource.username", "sa");
		System.getProperties().put("spring.datasource.password", "");
		System.getProperties().put("spring.jpa.hibernate.ddl-auto", "create");
		System.getProperties().put("spring.h2.console.enabled", "true");
	}
	
	@Test(expected=Exception.class)
	public void wrongUserAdd() throws Exception {
		assertNotNull(userService);
		
		Users wrongUser = new Users("dp", "12345", "Petrov Dmytro Leonidovich");
		try {
			userService.saveUser(wrongUser);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Test(expected=Exception.class)
	public void wrongContactAdd() throws Exception {
		assertNotNull(contactService);
		
		Users creator = new Users();
		creator.setid(1);
		Contacts wrongContact = new Contacts("Ivan", "Ivanenko", "Ivanovich", "+38(097)1111111", "", "", "", creator);
		try {
			contactService.saveContact(wrongContact);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Test(expected=DoubleLoginException.class)
	public void doubleUser() throws Exception {
		assertNotNull(userService);
		
		Users rightUser = new Users("testuser", "12345", "test user");
		try {
			userService.saveUser(rightUser);
		} catch (Exception e) {
			throw e;
		}
		Users wrongUser = new Users("testuser", "12345", "test user");
		try {
			userService.saveUser(wrongUser);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Test
    public void testUsers() throws Exception {
		assertNotNull(userService);
		
		Users rightUser = new Users("dpetrov", "12345", "Petrov Dmytro Leonidovich");
		try {
			userService.saveUser(rightUser);
		} catch (Exception e) {
			throw e;
		}
		
		Users userCheck = userService.findUserByLogin("dpetrov");
		assertNotNull(userCheck);
		assertEquals("dpetrov", userCheck.getlogin());
		
		boolean userCheckFounded = false;
		List<Users> userList = userService.listUser();
		for (Users x : userList) {
			if (x.getid()==userCheck.getid()) userCheckFounded = true;
		}
		assertEquals(true, userCheckFounded);
		
		userService.removeUser(userCheck.getid());
		
		Users userCheck2 = userService.findUser(userCheck.getid());
		assertNull(userCheck2);
    }
	
	@Test
    public void testContacts() throws Exception {
		assertNotNull(userService);
		assertNotNull(contactService);
		
		Users creator = new Users("test", "12345", "unknown");
		try {
			creator = userService.saveUser(creator);
		} catch (Exception e) {
			throw e;
		}
		sessionSettings.setCurrentUser(creator);
		
		Contacts rightContact = new Contacts("Ivan", "Ivanenko", "Ivanovich", "+380(97)1111111", "", "", "", creator);
		try {
			rightContact = contactService.saveContact(rightContact);
		} catch (Exception e) {
			throw e;
		}
		
		Contacts contactCheck = contactService.findContact(rightContact.getid());
		assertNotNull(contactCheck);
		assertEquals("Ivan", contactCheck.getfirstName());
		
		boolean contactCheckFounded = false;
		List<Contacts> contactList = contactService.listContact(new PageFilters());
		for (Contacts x : contactList) {
			if (x.getid()==contactCheck.getid()) contactCheckFounded = true;
		}
		assertEquals(true, contactCheckFounded);
		
		contactService.removeContact(contactCheck.getid());
		
		Contacts contactCheck2 = contactService.findContact(contactCheck.getid());
		assertNull(contactCheck2);
		
		userService.removeUser(creator.getid());
    }
	
}
