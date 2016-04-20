package example.whitepages.security;

import java.util.List;
import java.util.Properties;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import example.whitepages.db.UserService;
import example.whitepages.db.entities.Users;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Inject
	private UserService userService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
			.antMatchers("/", "/newUser", "/addNewUser").permitAll()
			.anyRequest().authenticated()
			.and()
		.formLogin()
			.loginPage("/login").permitAll()
			.and()
		.logout()
			.permitAll();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(inMemoryUserDetailsManager());
	}

	@Bean
	public InMemoryUserDetailsManager inMemoryUserDetailsManager() throws Exception {
		// our data store not only in databases, so we''ll use custom inMemoryAuthentication
		Properties users = new Properties();
		try {
			List<Users> allUsers = userService.listUser();
			for (Users user : allUsers) {
				users.put(user.getlogin(),user.getpassword()+",enabled,ROLE_USER");
			}
		} catch (Exception e) { throw e; }
		return new InMemoryUserDetailsManager(users);
	}
	
}
