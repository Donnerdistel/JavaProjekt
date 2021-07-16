import java.util.HashMap;
import java.util.Map;

public class PersonName {
	static Map<String,PersonName> personnames;
	
	static {
		personnames = new HashMap<String,PersonName>();
	}
	
	static int size() {
		return personnames.size();
	}
	
	private String name;
	private Person id;
	
	private PersonName(String name) {
		this.name = name;
		personnames.put(name, this);
	}
	
	static PersonName createName(String name) {
		PersonName pn = personnames.get(name);
		if (pn == null) {
			pn = new PersonName(name);
		}
		return pn;
	}
	
	public void setId(Person id) {
		this.id = id;
	}
	
	public Person getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
}
