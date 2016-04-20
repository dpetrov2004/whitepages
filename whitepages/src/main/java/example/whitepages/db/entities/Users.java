package example.whitepages.db.entities;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("serial")
@Entity
@XmlRootElement
public class Users implements Comparator<Users>, Serializable {
	@Id @GeneratedValue
	private int id;
	@Version @NotNull
	private int version;
	@NotNull @Size(min = 3, max = 50) @Pattern(regexp="^([a-z]+)$")
	private String login;
	@NotNull @Size(min = 5, max = 50)
	private String password;
	@NotNull @Size(min = 5, max = 100)
	private String fullName;
	@OneToMany(mappedBy="creator")
	private List<Contacts> contacts;

	@Transient // for XML only
	private List<Users> user;

	// Getters and setters
	public int getid() {return id;}
	public void setid(int id) {this.id = id;}
	public int getversion() {return version;}
	public void setversion(int version) {this.version = version;}
	public String getlogin() {return login;}
	public void setlogin(String login) {this.login = login;}
	public String getpassword() {return password;}
	public void setpassword(String password) {this.password = password;}
	public String getfullName() {return fullName;}
	public void setfullName(String fullName) {this.fullName = fullName;}
	public List<Contacts> getcontacts() {return contacts;}
	public void setcontacts(List<Contacts> contacts) {this.contacts = contacts;}

	public List<Users> getuser() {return user;}
	public void setuser(List<Users> user) {this.user = user;}
	
	@Override
	public int compare(Users obj1, Users obj2) {
		if(obj1.getid() > obj2.getid()) {
			return 1;
		}
		else if(obj1.getid() < obj2.getid()) {
			return -1;
		}
		else {
			return 0;
		}
	}
}
