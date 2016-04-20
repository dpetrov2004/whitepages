package example.whitepages.web;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainWebController {
	
	@RequestMapping("/")
	public String home(Map<String, Object> map) {
		map.put("pageFilter", new PageFilters());
		
		return "redirect:/contactList";
	}
	
	@RequestMapping("/login")
	public String login() {
		return "login";
	}
	
}

