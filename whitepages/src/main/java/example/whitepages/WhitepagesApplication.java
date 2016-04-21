package example.whitepages;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import javax.xml.bind.JAXBContext;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;

import example.whitepages.db.entities.Contacts;
import example.whitepages.db.entities.Users;
import example.whitepages.utils.AppController;
import example.whitepages.utils.StorageType;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class}) 
public class WhitepagesApplication {

	public static boolean processProperty(Properties properties, String propertyName) {
		if (!properties.containsKey(propertyName)) {
			System.err.println(propertyName+" property was not found in property file");
			return false;
		} else System.getProperties().put(propertyName, properties.get(propertyName));

		return true;
	}

	public static void main(String[] args) throws Exception {
		// set common JPA properties
		System.getProperties().put("spring.datasource.driver-class-name", "com.mysql.jdbc.Driver");
		System.getProperties().put("spring.jpa.hibernate.ddl-auto", "update");

		// set JSP pages for rendering with InternalResourceViewResolver 
		System.getProperties().put("spring.mvc.view.prefix", "/WEB-INF/jsp/");
		System.getProperties().put("spring.mvc.view.suffix", ".jsp");

		if (!System.getProperties().containsKey("lardi.conf")) {
			System.err.println("lardi.conf system property is not set");
			return;
		}
		String pathToFile = System.getProperty("lardi.conf");
		FileInputStream fis;
		Properties properties = new Properties();
		try {
			fis = new FileInputStream(pathToFile);
			properties.load(fis);
		} catch (Exception e) {
			System.err.println("Error while reading property file "+pathToFile);
			return;
		}

		if (!properties.containsKey("whitepages.storagetype")) {
			System.err.println("whitepages.storagetype property was not found in property file");
			return;
		}
		String storageType = (String)properties.get("whitepages.storagetype");
		try {
			AppController.getInstance().setCurrentStorageType(StorageType.valueOf(storageType));
		} catch (Exception e) {
			System.err.println("Wrong whitepages.storagetype property. Only MySQL or XML allowed");
			return;
		}
		switch (AppController.getInstance().getCurrentStorageType()) {
		case MySQL:
			// set user specified JPA properties
			if (!processProperty(properties, "spring.datasource.url")) return;
			if (!processProperty(properties, "spring.datasource.username")) return;
			if (!processProperty(properties, "spring.datasource.password")) return;
			break;
		case XML:
			if (!properties.containsKey("whitepages.pathToXMLDB")) {
				System.err.println("whitepages.pathToXMLDB property was not found in property file");
				return;
			}
			String pathToXMLDB = (String)properties.get("whitepages.pathToXMLDB");
			AppController.getInstance().setCurrentContactsXMLFile(new File(pathToXMLDB+"contactList.xml"));
			AppController.getInstance().setCurrentUsersXMLFile(new File(pathToXMLDB+"usersList.xml"));
			JAXBContext context = JAXBContext.newInstance(Contacts.class, Users.class);
			AppController.getInstance().setCurrentMarshaller(context.createMarshaller());
			AppController.getInstance().setCurrentUnmarshaller(context.createUnmarshaller());
			 
			// disable JPA autoconfiguration
			System.getProperties().put("spring.autoconfigure.exclude", 
						"org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration, "
						+ "org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration");
			break;
		}

		// set storageType
		/*AppController.getInstance().setCurrentStorageType(StorageType.MySQL);
		switch (AppController.getInstance().getCurrentStorageType()) {
		case MySQL:
			// set user specified JPA properties
			System.getProperties().put("spring.datasource.driver-class-name", "org.h2.Driver");
			System.getProperties().put("spring.datasource.url", "jdbc:h2:mem:test");
			System.getProperties().put("spring.datasource.username", "sa");
			System.getProperties().put("spring.datasource.password", "");
			System.getProperties().put("spring.jpa.hibernate.ddl-auto", "create");
			System.getProperties().put("spring.h2.console.enabled", "true");
			break;
		case XML:
			AppController.getInstance().setCurrentContactsXMLFile(new File("f:\\contactList.xml"));
			AppController.getInstance().setCurrentUsersXMLFile(new File("f:\\usersList.xml"));
			JAXBContext context = JAXBContext.newInstance(Contacts.class, Users.class);
			AppController.getInstance().setCurrentMarshaller(context.createMarshaller());
			AppController.getInstance().setCurrentUnmarshaller(context.createUnmarshaller());
			// disable JPA autoconfiguration
			System.getProperties().put("spring.autoconfigure.exclude", 
						"org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration, "
						+ "org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration");
			break;
		}
		// set port for Tomcat
		System.getProperties().put("server.port", 8012);*/

		SpringApplication.run(WhitepagesApplication.class, args);
	}

}