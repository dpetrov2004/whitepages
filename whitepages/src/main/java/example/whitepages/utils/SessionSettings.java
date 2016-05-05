package example.whitepages.utils;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import example.whitepages.db.entities.Users;

@Component
@Scope(value=WebApplicationContext.SCOPE_SESSION, proxyMode=ScopedProxyMode.TARGET_CLASS)
public class SessionSettings {
	private Users currentUser;
	
	public Users getCurrentUser() {
		return currentUser;
	}
	public void setCurrentUser(Users user) {
		currentUser = user;
	}
}
