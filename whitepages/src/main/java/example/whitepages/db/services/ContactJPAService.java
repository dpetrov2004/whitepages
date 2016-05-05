package example.whitepages.db.services;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import example.whitepages.db.ContactService;
import example.whitepages.db.entities.Contacts;
import example.whitepages.utils.IDNotFoundException;
import example.whitepages.utils.SessionSettings;
import example.whitepages.web.PageFilters;

public class ContactJPAService implements ContactService {
	
	@PersistenceContext
	private EntityManager em;
	
	@Inject
	private SessionSettings sessionSettings;
	
	@Override
	@Transactional
	public Contacts saveContact(Contacts contact) {
		// null all empty fields
		if (contact.gettelephoneHome()=="") {contact.settelephoneHome(null);}
		if (contact.getaddress()=="") {contact.setaddress(null);}
		if (contact.getemail()=="") {contact.setemail(null);}
		if (contact.getid()==0)
		{
			em.persist(contact);
		}
		else
		{
			em.merge(contact);
		}
		
		return contact;
	}
	
	@Override
	public List<Contacts> listContact(PageFilters pageFilter) {
		String whereCondition = "";
		if (pageFilter.getfirstName()!="") {
			whereCondition+="ContactsList.firstName LIKE '%"+pageFilter.getfirstName()+"%'";
		}
		if (pageFilter.getlastName()!="") {
			if (whereCondition!="") whereCondition+=" AND ";
			whereCondition+="ContactsList.lastName LIKE '%"+pageFilter.getlastName()+"%'";
		}
		if (pageFilter.gettelephone()!="") {
			if (whereCondition!="") whereCondition+=" AND ";
			whereCondition+="(ContactsList.telephoneMobile LIKE '%"+pageFilter.gettelephone()+"%' OR ContactsList.telephoneHome LIKE '%"+pageFilter.gettelephone()+"%')";
		}
		if (whereCondition!="") whereCondition=" AND "+whereCondition;
		return em.createQuery("SELECT ContactsList FROM Contacts ContactsList WHERE ContactsList.creator.id="
			+sessionSettings.getCurrentUser().getid()+whereCondition+" ORDER BY ContactsList.id DESC", Contacts.class).getResultList();
	}
	
	@Override
	public Contacts findContact(int id) {
		return em.find(Contacts.class, id);
	}
	
	@Override
	@Transactional
	public void removeContact(int id) throws IDNotFoundException {
		Contacts contact = em.find(Contacts.class, id);
		if (contact!=null){
			em.remove(contact);
		} else {
			// this cannot be
			throw new IDNotFoundException("Internal error: contact with id "+id+" not found in file ");
		}
	}
	
}
