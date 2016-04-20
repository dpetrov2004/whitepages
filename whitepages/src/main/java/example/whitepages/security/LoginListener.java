package example.whitepages.security;

import javax.inject.Inject;

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import example.whitepages.db.UserService;
import example.whitepages.utils.AppController;

@Component
public class LoginListener implements ApplicationListener<InteractiveAuthenticationSuccessEvent> {
	
	@Inject
	private UserService userService;
	
	@Override
	public void onApplicationEvent(InteractiveAuthenticationSuccessEvent event)
	{
		try {
			AppController.getInstance().setCurrentUser(
				userService.findUserByLogin(SecurityContextHolder.getContext().getAuthentication().getName()));
		} catch (Exception e) {
			System.err.println(e.toString());
			System.exit(1);
		}
	}
	
}
