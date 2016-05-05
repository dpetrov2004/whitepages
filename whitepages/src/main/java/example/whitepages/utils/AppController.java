package example.whitepages.utils;

import java.io.File;

import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import example.whitepages.db.ContactService;
import example.whitepages.db.UserService;
import example.whitepages.db.services.ContactJAXBService;
import example.whitepages.db.services.ContactJPAService;
import example.whitepages.db.services.UserJAXBService;
import example.whitepages.db.services.UserJPAService;

@Component
public class AppController {
	
	private static AppController instance;
	private StorageType currentStorageType;
	private File currentContactsXMLFile;
	private File currentUsersXMLFile;
	private Marshaller currentMarshaller;
	private Unmarshaller currentUnmarshaller;
	
	public boolean checkCurrentStorageType(String storageType) {
		if (storageType!="MySQL" && storageType!="XML") return false;
		else return true;
	}
	
	@Bean
	public UserService produceUserService() {
		if (instance.currentStorageType==StorageType.MySQL)
			return new UserJPAService();
		else if (instance.currentStorageType==StorageType.XML)
			return new UserJAXBService();
		else {
			System.exit(2);
			return null;
		}
	}
	
	@Bean
	public ContactService produceContactService() {
		if (instance.currentStorageType==StorageType.MySQL)
			return new ContactJPAService();
		else if (instance.currentStorageType==StorageType.XML)
			return new ContactJAXBService();
		else {
			System.exit(2);
			return null;
		}
	}
	
	// prevent from non static instance creation
	private AppController() {
		super();
	}
	
	public static AppController getInstance () {		
		if (instance == null) {
			instance = new AppController();
		}		
		return instance;			
	}
	
	public StorageType getCurrentStorageType() {
		return currentStorageType;
	}
	public void setCurrentStorageType(StorageType storageType) {
		currentStorageType = storageType;
	}
	public File getCurrentContactsXMLFile() {
		return currentContactsXMLFile;
	}
	public void setCurrentContactsXMLFile(File contactsXMLFile) {
		currentContactsXMLFile = contactsXMLFile;
	}
	public File getCurrentUsersXMLFile() {
		return currentUsersXMLFile;
	}
	public void setCurrentUsersXMLFile(File usersXMLFile) {
		currentUsersXMLFile = usersXMLFile;
	}
	public Marshaller getCurrentMarshaller() {
		return currentMarshaller;
	}
	public void setCurrentMarshaller(Marshaller marshaller) {
		currentMarshaller = marshaller;
	}
	public Unmarshaller getCurrentUnmarshaller() {
		return currentUnmarshaller;
	}
	public void setCurrentUnmarshaller(Unmarshaller unmarshaller) {
		currentUnmarshaller = unmarshaller;
	}
	
}
