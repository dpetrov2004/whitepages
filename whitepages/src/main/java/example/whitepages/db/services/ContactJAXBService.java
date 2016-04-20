package example.whitepages.db.services;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import example.whitepages.db.ContactService;
import example.whitepages.db.UserService;
import example.whitepages.db.entities.Contacts;
import example.whitepages.utils.AppController;
import example.whitepages.utils.IDNotFoundException;
import example.whitepages.web.PageFilters;

public class ContactJAXBService implements ContactService {

	@Inject
	UserService userService;
	
	@Override
	public void saveContact(Contacts contact) throws Exception {
		// null all empty fields
		if (contact.gettelephoneHome()=="") {contact.settelephoneHome(null);}
		if (contact.getaddress()=="") {contact.setaddress(null);}
		if (contact.getemail()=="") {contact.setemail(null);}
		try {
			Contacts contactsForXML;
			List<Contacts> contactsList;

			if (contact.getid()==0) {
				int lastID = 0;

				if (AppController.getInstance().getCurrentContactsXMLFile().exists()) {
					contactsForXML = (Contacts) AppController.getInstance().getCurrentUnmarshaller().unmarshal(
							AppController.getInstance().getCurrentContactsXMLFile());

					contactsList = contactsForXML.getcontact();
					// search for last id
					for (Contacts x : contactsList) {
						if (x.getid()>lastID) lastID = x.getid(); 
					}
				}
				else {
					contactsForXML = new Contacts();
					contactsList = new ArrayList<Contacts>();
				}

				contact.setid(lastID+1);				
				contactsList.add(contact);
				contactsForXML.setcontact(contactsList);

				AppController.getInstance().getCurrentMarshaller().marshal(
						contactsForXML, AppController.getInstance().getCurrentContactsXMLFile());
			} else {
				contactsForXML = (Contacts) AppController.getInstance().getCurrentUnmarshaller().unmarshal(
						AppController.getInstance().getCurrentContactsXMLFile());
				contactsList = contactsForXML.getcontact();
				// search for id
				boolean idWasFound = false;
				for (Contacts x : contactsList) {
					if (x.getid()==contact.getid()) {
						contactsList.remove(x);
						contactsList.add(contact);
						idWasFound = true;
						AppController.getInstance().getCurrentMarshaller().marshal(
							contactsForXML, AppController.getInstance().getCurrentContactsXMLFile());
						break;
					}
				}
				if (!idWasFound) {
					// this cannot be
					throw new IDNotFoundException("Internal error: contact with id "+contact.getid()+" not found in file");
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	public List<Contacts> listContact(PageFilters pageFilter) throws Exception {
		try {
			List<Contacts> contactsList = new ArrayList<Contacts>();

			if (AppController.getInstance().getCurrentContactsXMLFile().exists()) {
				Contacts contactsFromXML = (Contacts) AppController.getInstance().getCurrentUnmarshaller().unmarshal(
						AppController.getInstance().getCurrentContactsXMLFile());

				for (Contacts x : contactsFromXML.getcontact()) {
					String telephoneMobile = x.gettelephoneMobile();
					String telephoneHome = x.gettelephoneHome();
					if (telephoneMobile==null) telephoneMobile="";
					if (telephoneHome==null) telephoneHome="";
					if (x.getcreator().getid()==AppController.getInstance().getCurrentUser().getid() 
							&& x.getfirstName().toLowerCase().contains(pageFilter.getfirstName().toLowerCase())
							&& x.getlastName().toLowerCase().contains(pageFilter.getlastName().toLowerCase())
							&& (telephoneMobile.contains(pageFilter.gettelephone())
									|| telephoneHome.contains(pageFilter.gettelephone()))) {
						x.setcreator(userService.findUser(x.getcreator().getid()));
						contactsList.add(x);
					}	
				}
				contactsList.sort(new Contacts());
			}
			return contactsList;
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	public Contacts findContact(int id) throws Exception {
		try {
			Contacts contactsForXML = (Contacts) AppController.getInstance().getCurrentUnmarshaller().unmarshal(
					AppController.getInstance().getCurrentContactsXMLFile());
			List<Contacts> contactsList = contactsForXML.getcontact();
			// search for id
			for (Contacts x : contactsList) {
				if (x.getid()==id) {
					return x;
				}
			}
			// this cannot be
			throw new IDNotFoundException("Internal error: contact with id "+id+" not found in file");
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	public void removeContact(int id) throws Exception {
		try {
			Contacts contactsForXML = (Contacts) AppController.getInstance().getCurrentUnmarshaller().unmarshal(
					AppController.getInstance().getCurrentContactsXMLFile());
			List<Contacts> contactsList = contactsForXML.getcontact();
			// search for id
			boolean idWasFound = false;
			for (Contacts x : contactsList) {
				if (x.getid()==id) {
					contactsList.remove(x);
					idWasFound = true;
					AppController.getInstance().getCurrentMarshaller().marshal(
						contactsForXML, AppController.getInstance().getCurrentContactsXMLFile());
					break;
				}
			}
			if (!idWasFound) {
				// this cannot be
				throw new IDNotFoundException("Internal error: contact with id "+id+" not found in file");
			}
		} catch (Exception e) {
			throw e;
		}
	}

}
