import java.util.HashMap;
import java.util.Map;

/*
Name	                Matrikelnummer	    E-Mail
Sebastian Britner	    1485271	            s4sebrit@uni-trier.de
Jens Hartmann	        1470700	            s4jehart@uni-trier.de
Jan Niclas Ruppenthal	1481198	            s4jsrupp@uni-trier.de
 */

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
