package example.whitepages.db;

import java.util.List;

import example.whitepages.db.entities.Users;

public interface UserService {
	
	public void saveUser(Users user) throws Exception;
	public List<Users> listUser() throws Exception;
	public Users findUser(int id) throws Exception;
	public Users findUserByLogin(String login) throws Exception;
	public void removeUser(int id) throws Exception;
	
}
