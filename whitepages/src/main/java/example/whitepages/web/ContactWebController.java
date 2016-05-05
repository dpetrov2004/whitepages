package example.whitepages.web;

import java.util.Map;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import example.whitepages.db.ContactService;
import example.whitepages.db.entities.Contacts;
import example.whitepages.utils.AppController;
import example.whitepages.utils.SessionSettings;

@Controller
public class ContactWebController {

	@Inject
	private ContactService contactService;
	
	@Inject
	private SessionSettings sessionSettings;

	@RequestMapping("/contactList")
	public String contactList(@ModelAttribute("pageFilter") PageFilters pageFilter, Map<String, Object> map, 
			BindingResult result) {
		try {
			map.put("contactList", contactService.listContact(pageFilter));
		} catch (Exception e) {
			result.addError(new ObjectError("pageFilter", e.toString()));
		}
		map.put("pageFilter", pageFilter);

		return "contactList";
	}

	@RequestMapping(value = "/editContact/{contactId}", method = RequestMethod.GET)
	public String editContact(@PathVariable("contactId") int contactId, @ModelAttribute("pageFilter") PageFilters pageFilter, 
			Map<String, Object> map, BindingResult result) {
		if (contactId==0) {
			Contacts x = new Contacts();
			x.setcreator(sessionSettings.getCurrentUser());
			map.put("contact", x);
		}
		else {
			try {
				Contacts contactForEdit = contactService.findContact(contactId);
				if (contactForEdit==null) return "contactList";
				map.put("contact", contactForEdit);
			} catch (Exception e) {
				result.addError(new ObjectError("pageFilter", e.toString()));
				return "contactList";
			}
		}

		return "contactEdit";
	}

	@RequestMapping(value = "/editContact/{contactId}", method = RequestMethod.POST)
	public String saveContact(@ModelAttribute("contact") @Valid Contacts contact, BindingResult result) {
		if (result.hasErrors()) {
			return "contactEdit";
		}
		else {
			try {
				contactService.saveContact(contact);
			} catch (Exception e) {
				result.addError(new ObjectError("contact", e.toString()));
				return "contactEdit";
			}
			return "redirect:/contactList";
		}
	}

	@RequestMapping("/deleteContact/{contactId}")
	public String deleteContact(@PathVariable("contactId") int contactId, @ModelAttribute("pageFilter") PageFilters pageFilter, 
			BindingResult result) {
		try {
			contactService.removeContact(contactId);
		} catch (Exception e) {
			result.addError(new ObjectError("pageFilter", e.toString()));
			return "contactList";
		}
		return "redirect:/contactList";
	}

}
