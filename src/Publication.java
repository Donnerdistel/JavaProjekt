import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

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
	
	public Iterable<PersonName> getContributors() {
		return Arrays.asList(contributors);
	}
	
	public Stream<PersonName> names() {
		return Arrays.stream(contributors);
	}
	
	public String getDOI() {
		return doi;
	}
}
