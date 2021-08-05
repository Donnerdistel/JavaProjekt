import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

/*
Name	                Matrikelnummer	    E-Mail
Sebastian Britner	    1485271	            s4sebrit@uni-trier.de
Jens Hartmann	        1470700	            s4jehart@uni-trier.de
Jan Niclas Ruppenthal	1481198	            s4jsrupp@uni-trier.de
 */

public class Person {
	static Set<Person> persons;
	
	static {
		persons = new HashSet<Person>();
	}
	
	static int size() {
		return persons.size();
	}
	
	static public Stream<Person> persons() {
		return persons.stream();
	}
	
	static public Collection<Person> getPersons() {
		return Collections.unmodifiableCollection(persons);
	}
	
	private int n;
	private PersonName[] names;
	private Publication[] publications;
	private boolean hasACMpubl;
	
	Person(PersonName[] names) {
		this.names = names;
		for (PersonName p: names)
			p.setId(this);
		persons.add(this);
		hasACMpubl = false;
	}
	
	public PersonName getPrimaryName() {
		return names[0];
	}
	
	public PersonName[]	getNames() {
		return names;
	}
	
	public Stream<Publication> publications() {
		if (publications == null)
			publications = new Publication[0];
		return Arrays.stream(publications);
	}
	
	public int getNumberOfPublications() {
		if (publications == null)
			return 0;
		return publications.length;
	}
	
	public boolean hasACMpublication() {
		return hasACMpubl;
	}
	
	private void increment() {
		n++;
	}
	
	private void addPublication(Publication p) {
		if (publications == null) {
			publications = new Publication[n];
		}
		publications[--n] = p;
		String doi = p.getDOI();
		if (doi != null && doi.startsWith("10.1145/"))
			hasACMpubl = true;
	}
	
	static void buildPersonPublicationEdges() {
		int c = 0;
        System.out.println("calculate person degrees");
        for (Publication p:Publication.getPublications()) {
        	for (PersonName n:p.getContributors()) {
        		n.getId().increment();
        		c++;
        	}
        }
        System.out.println(c + "  publication-person edges");
        
        System.out.println("build person-publication edges");
        for (Publication p:Publication.getPublications()) {
        	for (PersonName n:p.getContributors()) {
        		n.getId().addPublication(p);
        	}
        }
	}
}
