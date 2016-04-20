package example.whitepages.db.services;

import java.util.ArrayList;
import java.util.List;

import example.whitepages.db.UserService;
import example.whitepages.db.entities.Users;
import example.whitepages.utils.AppController;
import example.whitepages.utils.DoubleLoginException;
import example.whitepages.utils.IDNotFoundException;

public class UserJAXBService implements UserService {
	
	@Override
	public void saveUser(Users user) throws Exception {
		try {
			Users usersForXML;
			List<Users> usersList;

			if (user.getid()==0) {
				int lastID = 0;

				if (AppController.getInstance().getCurrentUsersXMLFile().exists()) {
					usersForXML = (Users) AppController.getInstance().getCurrentUnmarshaller().unmarshal(
							AppController.getInstance().getCurrentUsersXMLFile());

					usersList = usersForXML.getuser();
					// search for last id
					for (Users x : usersList) {
						if (x.getid()>lastID) lastID = x.getid(); 
					}
				}
				else {
					usersForXML = new Users();
					usersList = new ArrayList<Users>();
				}

				user.setid(lastID+1);				
				usersList.add(user);
				usersForXML.setuser(usersList);

				AppController.getInstance().getCurrentMarshaller().marshal(
						usersForXML, AppController.getInstance().getCurrentUsersXMLFile());
			} else {
				usersForXML = (Users) AppController.getInstance().getCurrentUnmarshaller().unmarshal(
						AppController.getInstance().getCurrentUsersXMLFile());
				usersList = usersForXML.getuser();
				// search for id and also check if there is a user with the same login
				boolean idWasFound = false;
				for (Users x : usersList) {
					if (x.getid()==user.getid()) {
						if (!idWasFound) {
							usersList.remove(x);
							usersList.add(user);
							idWasFound = true;
						}
					} else if (x.getlogin()==user.getlogin()) {
						throw new DoubleLoginException("This login is already exist");
					}
				}
				if (!idWasFound) {
					// this cannot be
					throw new IDNotFoundException("Internal error: user with id "+user.getid()+" not found in file");
				}
				AppController.getInstance().getCurrentMarshaller().marshal(
					usersForXML, AppController.getInstance().getCurrentUsersXMLFile());
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	public List<Users> listUser() throws Exception {
		try {
			List<Users> usersList = new ArrayList<Users>();

			if (AppController.getInstance().getCurrentUsersXMLFile().exists()) {
				Users usersFromXML = (Users) AppController.getInstance().getCurrentUnmarshaller().unmarshal(
						AppController.getInstance().getCurrentUsersXMLFile());

				for (Users x : usersFromXML.getuser()) {
					usersList.add(x);	
				}
				usersList.sort(new Users());
			}
			return usersList;
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	public Users findUser(int id) throws Exception {
		try {
			Users usersForXML = (Users) AppController.getInstance().getCurrentUnmarshaller().unmarshal(
					AppController.getInstance().getCurrentUsersXMLFile());
			List<Users> usersList = usersForXML.getuser();
			// search for id
			for (Users x : usersList) {
				if (x.getid()==id) {
					return x;
				}
			}
			// this cannot be
			throw new IDNotFoundException("Internal error: user with id "+id+" not found in file");
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	public Users findUserByLogin(String login) throws Exception {
		try {
			Users usersForXML = (Users) AppController.getInstance().getCurrentUnmarshaller().unmarshal(
					AppController.getInstance().getCurrentUsersXMLFile());
			List<Users> usersList = usersForXML.getuser();
			// search for login
			for (Users x : usersList) {
				if (x.getlogin().equals(login)) {
					return x;
				}
			}
			// this cannot be
			throw new IDNotFoundException("Internal error: user with login "+login+" not found in file");
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	public void removeUser(int id) throws Exception {
		try {
			Users usersForXML = (Users) AppController.getInstance().getCurrentUnmarshaller().unmarshal(
					AppController.getInstance().getCurrentUsersXMLFile());
			List<Users> usersList = usersForXML.getuser();
			// search for id
			boolean idWasFound = false;
			for (Users x : usersList) {
				if (x.getid()==id) {
					usersList.remove(x);
					idWasFound = true;
					AppController.getInstance().getCurrentMarshaller().marshal(
						usersForXML, AppController.getInstance().getCurrentUsersXMLFile());
					break;
				}
			}
			if (!idWasFound) {
				// this cannot be
				throw new IDNotFoundException("Internal error: user with id "+id+" not found in file");
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
}
