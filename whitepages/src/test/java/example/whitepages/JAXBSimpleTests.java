package example.whitepages;

import static org.junit.Assert.*;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.mockito.Mockito.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import example.whitepages.db.UserService;
import example.whitepages.db.entities.Contacts;
import example.whitepages.db.entities.Users;
import example.whitepages.db.services.ContactJAXBService;
import example.whitepages.db.services.UserJAXBService;
import example.whitepages.utils.AppController;
import example.whitepages.utils.DoubleLoginException;
import example.whitepages.utils.SessionSettings;
import example.whitepages.utils.StorageType;
import example.whitepages.web.PageFilters;

public class JAXBSimpleTests {
	
	SessionSettings sessionSettings = new SessionSettings();
	UserService userService = new UserJAXBService();
	ContactJAXBService contactService = new ContactJAXBService();
	
	static final Users userListForMock = new Users();
	static final Contacts contactListForMock = new Contacts();
	
	@BeforeClass
	public static void initTest() {
		Marshaller marshaller = mock(Marshaller.class);
		Unmarshaller unmarshaller = mock(Unmarshaller.class);
		File mockedUsersXMLFile = mock(File.class);
		File mockedContactsXMLFile = mock(File.class);
		
		AppController.getInstance().setCurrentStorageType(StorageType.XML);
		AppController.getInstance().setCurrentUsersXMLFile(mockedUsersXMLFile);
		AppController.getInstance().setCurrentContactsXMLFile(mockedContactsXMLFile);
		AppController.getInstance().setCurrentMarshaller(marshaller);
		AppController.getInstance().setCurrentUnmarshaller(unmarshaller);
		
		try {
			doNothing().when(marshaller).marshal(any(Users.class), any(File.class));
			doNothing().when(marshaller).marshal(any(Contacts.class), any(File.class));
			when(unmarshaller.unmarshal(mockedUsersXMLFile)).thenReturn(userListForMock);
			when(unmarshaller.unmarshal(mockedContactsXMLFile)).thenReturn(contactListForMock);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		when(mockedUsersXMLFile.exists()).thenReturn(true);
		when(mockedContactsXMLFile.exists()).thenReturn(true);
		
		Users rightUser = new Users("admin", "12345", "Petrov Dmytro Leonidovich");
		rightUser.setid(1);
		List<Users> initialUserList = new ArrayList<Users>();
		initialUserList.add(rightUser);
		userListForMock.setuser(initialUserList);
		
		Contacts rightContact = new Contacts("Dmytro", "Petrov", "Leonidovich", "", "", "", "dpetrov2004@gmail.com", rightUser);
		rightContact.setid(1);
		List<Contacts> initialContactList = new ArrayList<Contacts>();
		initialContactList.add(rightContact);
		contactListForMock.setcontact(initialContactList);
	}
	
	@Test(expected=DoubleLoginException.class)
	public void doubleUser() throws Exception {
		Users wrongUser = new Users("admin", "12345", "Petrov Dmytro Leonidovich");
		try {
			userService.saveUser(wrongUser);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Test
    public void testUsers() throws Exception {
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
		Users creator = new Users("test", "12345", "unknown");
		try {
			creator = userService.saveUser(creator);
		} catch (Exception e) {
			throw e;
		}
		sessionSettings.setCurrentUser(creator);
		contactService.setSessionSettings(sessionSettings);
		
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
