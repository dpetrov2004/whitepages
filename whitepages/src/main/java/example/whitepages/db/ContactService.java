package example.whitepages.db;

import java.util.List;

import example.whitepages.db.entities.Contacts;
import example.whitepages.web.PageFilters;

public interface ContactService {
	
	public Contacts saveContact(Contacts contact) throws Exception;
	public List<Contacts> listContact(PageFilters pageFilter) throws Exception;
	public Contacts findContact(int id) throws Exception;
	public void removeContact(int id) throws Exception;
	
}
