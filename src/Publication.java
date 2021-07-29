import java.util.*;
import java.util.stream.Collectors;
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

	public Set<Person> getContributorsIDs() {
		// Laufe durch alle Namen durch
        for (Map.Entry<String, PersonName> entry: PersonName.personnames.entrySet())
        {
            Person.persons().forEach(p -> {
                if (Arrays.stream(p.getNames()).anyMatch(e -> entry.getKey().equals(e.getName())))
                {
                    entry.getValue().setId(p);
                    return;
                }
            });
        }
		Set<Person> contributorsIDs = Arrays.stream(contributors)
				.map(PersonName::getId)
				.collect(Collectors.toSet());

		System.out.println(contributorsIDs.toString());
		return contributorsIDs;
	}


	public PersonName[] getContributorsArray()
	{
		return contributors;
	}


	public Stream<PersonName> names() {
		return Arrays.stream(contributors);
	}
	
	public String getDOI() {
		return doi;
	}

	@Override
	public String toString()
	{
		return key + " (" + contributors.length + ")";
	}
}
