package example.whitepages.web;

import java.util.ArrayList;
import java.util.Map;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import example.whitepages.db.UserService;
import example.whitepages.db.entities.Users;
import example.whitepages.utils.DoubleLoginException;

@Controller
public class UserWebController {
	
	private InMemoryUserDetailsManager inMemoryUserDetailsManager;
	
	@Autowired
    public UserWebController(InMemoryUserDetailsManager inMemoryUserDetailsManager) {
       this.inMemoryUserDetailsManager = inMemoryUserDetailsManager;
    }
	
	@Inject
	private UserService userService;
	
	@RequestMapping("/userList")
	public String userList(@ModelAttribute("pageFilter") PageFilters pageFilter, Map<String, Object> map, 
			BindingResult result) {
		try {
			map.put("userList", userService.listUser());
		} catch (Exception e) {
			result.addError(new ObjectError("pageFilter", e.toString()));
		}

		return "userList";
	}
	
	@RequestMapping(value = "/editUser/{userId}", method = RequestMethod.GET)
	public String editUser(@PathVariable("userId") int userId, @ModelAttribute("pageFilter") PageFilters pageFilter, 
			Map<String, Object> map, BindingResult result) {
		if (userId==0) {
			map.put("user", new Users());
		}
		else {
			try {
				Users userForEdit = userService.findUser(userId);
				if (userForEdit==null) return "userList";
				map.put("user", userForEdit);
			} catch (Exception e) {
				result.addError(new ObjectError("pageFilter", e.toString()));
				return "userList";
			}
		}

		return "userEdit";
	}
	
	@RequestMapping(value = "/editUser/{userId}", method = RequestMethod.POST)
	public String saveUser(@ModelAttribute("user") @Valid Users user, BindingResult result) {
		if (result.hasErrors()) {
			return "userEdit";
		}
		else {
			boolean itsNewUser = false; 
			if (user.getid()==0) itsNewUser = true;
			try {
				userService.saveUser(user);
				if (itsNewUser) {
					// we must add this new user to Spring Security
					User newUserForSpringSecurity = new User(user.getlogin(), user.getpassword(), new ArrayList<GrantedAuthority>());
					inMemoryUserDetailsManager.createUser(newUserForSpringSecurity);
				}
			} catch (DoubleLoginException e) {
				result.addError(new FieldError("user", "login", e.getMessage()));
				return "userEdit";
			} catch (Exception e) {
				result.addError(new ObjectError("user", e.toString()));
				return "userEdit";
			}
			return "redirect:/userList";
		}
	}
	
	@RequestMapping("/newUser")
	public String newUser(Map<String, Object> map) {
		map.put("user", new Users());
		
		return "newUser";
	}
	
	@RequestMapping(value = "/addNewUser", method = RequestMethod.POST)
	public String addNewUser(@ModelAttribute("user") @Valid Users user, BindingResult result) {
		if (result.hasErrors()) {
			return "newUser";
		}
		else {
			try {			
				userService.saveUser(user);
				// we must add this new user to Spring Security
				User newUserForSpringSecurity = new User(user.getlogin(), user.getpassword(), new ArrayList<GrantedAuthority>());
				inMemoryUserDetailsManager.createUser(newUserForSpringSecurity);
			} catch (DoubleLoginException e) {
				result.addError(new FieldError("user", "login", e.getMessage()));
				return "newUser";
			} catch (Exception e) {
				result.addError(new ObjectError("user", e.toString()));
				return "newUser";
			}
			return "redirect:/login";
		}
	}
	
}

