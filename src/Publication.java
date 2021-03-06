import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
Name	                Matrikelnummer	    E-Mail
Sebastian Britner	    1485271	            s4sebrit@uni-trier.de
Jens Hartmann	        1470700	            s4jehart@uni-trier.de
Jan Niclas Ruppenthal	1481198	            s4jsrupp@uni-trier.de
 */

public class Publication {
	static private Set<Publication> publications;
	
	static {
		publications = new HashSet<Publication>();
	}
	
	static public Collection<Publication> getPublications() {
		return Collections.unmodifiableCollection(publications);
	}
	
	static int size() {
		return publications.size();
	}
	
	private String key;
	private PersonName[] contributors;
	private Set<Person> contributorsIDs;
	private String doi;
	
	Publication(String key, PersonName[] contributors, String doi) {
		this.key = key;
		this.contributors = contributors;
		this.doi = doi;
		publications.add(this);
	}
	
	public String getKey() {
		return key;
	}
	
	public Iterable<PersonName> getContributors() { return Arrays.asList(contributors); }

	public Set<Person> getContributorsIDs() {
		this.contributorsIDs = Arrays.stream(contributors)
				.map(PersonName::getId)
				.collect(Collectors.toSet());

		// Test
		//System.out.println(contributorsIDs.toString());
		//contributorsIDs.forEach(p -> System.out.println(p.getPrimaryName().getName()));
		return contributorsIDs;
	}
	
	public Stream<PersonName> names() {
		return Arrays.stream(contributors);
	}
	
	public String getDOI() {
		return doi;
	}
}
