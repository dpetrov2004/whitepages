package example.whitepages.db.entities;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("serial")
@Entity
@XmlRootElement
public class Contacts implements Comparator<Contacts>, Serializable {
	@Id
	@GeneratedValue
	private int id;
	@Version @NotNull
	private int version;
	@NotNull @Size(min = 4, max = 50)
	private String firstName;
	@NotNull @Size(min = 4, max = 50)
	private String lastName;
	@NotNull @Size(min = 4, max = 50)
	private String sirName;
	@NotNull @Pattern(regexp="^$|^\\+380\\(([0-9]){2}\\)([0-9]){7}$")
	private String telephoneMobile;
	@Pattern(regexp="^$|^\\+380\\(([0-9]){2}\\)([0-9]){7}$")
	private String telephoneHome;
	private String address;
	@Pattern(regexp="^$|^([a-z0-9_\\.-]+)@([a-z0-9_\\.-]+)\\.([a-z\\.]{2,6})$")
	private String email;
	@ManyToOne @JoinColumn(name="creatorId")
	private Users creator;
	
	@Transient // for XML only
	private List<Contacts> contact;
	
	public Contacts() {
		super();
	}
	
	public Contacts(String firstName, String lastName, String sirName, String telephoneMobile, String telephoneHome, 
			String address, String email, Users creator) {
		super();
		
		this.firstName = firstName;
		this.lastName = lastName;
		this.sirName = sirName;
		this.telephoneMobile = telephoneMobile;
		this.telephoneHome = telephoneHome;
		this.address = address;
		this.email = email;
		this.creator = creator;
	}
	
	// Getters and setters
	public int getid() {return id;}
	public void setid(int id) {this.id = id;}
	public int getversion() {return version;}
	public void setversion(int version) {this.version = version;}
	public String getfirstName() {return firstName;}
	public void setfirstName(String firstname) {this.firstName = firstname;}
	public String getlastName() {return lastName;}
	public void setlastName(String lastname) {this.lastName = lastname;}
	public String getsirName() {return sirName;}
	public void setsirName(String sirName) {this.sirName = sirName;}
	public String gettelephoneMobile() {return telephoneMobile;}
	public void settelephoneMobile(String telephoneMobile) {this.telephoneMobile = telephoneMobile;}
	public String gettelephoneHome() {return telephoneHome;}
	public void settelephoneHome(String telephoneHome) {this.telephoneHome = telephoneHome;}
	public String getaddress() {return address;}
	public void setaddress(String address) {this.address = address;}
	public String getemail() {return email;}
	public void setemail(String email) {this.email = email;}
	public Users getcreator() {return creator;}
	public void setcreator(Users creator) {this.creator = creator;}
	
	public List<Contacts> getcontact() {return contact;}
	public void setcontact(List<Contacts> contact) {this.contact = contact;}
	
	@Override
	public int compare(Contacts obj1, Contacts obj2) {
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