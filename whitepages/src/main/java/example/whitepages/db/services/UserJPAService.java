package example.whitepages.db.services;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import example.whitepages.db.UserService;
import example.whitepages.db.entities.Users;
import example.whitepages.utils.DoubleLoginException;
import example.whitepages.utils.IDNotFoundException;

public class UserJPAService implements UserService {
	
	@PersistenceContext
	private EntityManager em;
	
	@Override
	@Transactional
	public Users saveUser(Users user) throws DoubleLoginException {
		// check if there is a user with the same login
		String queryCheck = "SELECT UsersList FROM Users UsersList WHERE UsersList.login='"+user.getlogin()
			+"' AND UsersList.id!="+user.getid();
		if (!em.createQuery(queryCheck, Users.class).getResultList().isEmpty()) {
			throw new DoubleLoginException("This login is already exist");
		}
		
		if (user.getid()==0)
		{
			em.persist(user);
		}
		else
		{
			em.merge(user);
		}
		
		return user;
	}
	
	@Override
	public List<Users> listUser() {
		return em.createQuery("SELECT UsersList FROM Users UsersList ORDER BY UsersList.id DESC", Users.class).getResultList();
	}
	
	@Override
	public Users findUser(int id) {
		return em.find(Users.class, id);
	}
	
	@Override
	public Users findUserByLogin(String login) {
		String queryCheck = "SELECT UsersList FROM Users UsersList WHERE UsersList.login='"+login+"'";
		
		return em.createQuery(queryCheck, Users.class).getSingleResult();
	}
	
	@Override
	@Transactional
	public void removeUser(int id) throws IDNotFoundException {
		Users user = em.find(Users.class, id);
		if (user!=null){
			em.remove(user);
		} else {
			// this cannot be
			throw new IDNotFoundException("Internal error: contact with id "+id+" not found in file ");
		}
	}
	
}
