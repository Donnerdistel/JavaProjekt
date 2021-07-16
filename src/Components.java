import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Components {
	private class ComponentInfo {
		Person	representative;
		int		size;
	};
	private List<ComponentInfo> componentList;
	private Map<Person,Integer> personToComponentNumber;
	private CoauthorGraph cg;
	
	private int bfs(Person start, int componentNumber) {
		Set<Person> s1 = new HashSet<Person>();
		Set<Person> s2 = new HashSet<Person>();
		int n = 1;
		s1.add(start);
		personToComponentNumber.put(start, componentNumber);
		while (!s1.isEmpty()) {
			for (Person p:s1) {
				Collection<Person> coauthors = cg.getCouthors(p);
				if (coauthors == null)
					continue;
				for (Person c:coauthors) {
					if (personToComponentNumber.get(c) != null)
						continue;
					s2.add(c);
					personToComponentNumber.put(c, componentNumber);
					n++;
				}
			}
			Set<Person> h = s1;
			s1 = s2;
			s2 = h;
			s2.clear();
		}
		return n;
	}
	
	Components(CoauthorGraph cg) {
		this.cg = cg;
		personToComponentNumber = new HashMap<Person,Integer>();
		componentList = new ArrayList<ComponentInfo>();
		
		int componentNumber = 0;
		Collection<Person> persons = Person.getPersons();
		for (Person pers: persons) {
			if (personToComponentNumber.get(pers) != null)
				continue;
			int s = bfs(pers, componentNumber);
			ComponentInfo ci = new ComponentInfo();
			ci.size = s;
			ci.representative = pers;
			componentList.add(ci);
			componentNumber++;
		}
	}
	
	void statistics(PrintStream out) {
		if (componentList == null || componentList.size() == 0)
			return;
		int threshold = 20;
		out.println("number of components = " + componentList.size());
		int si[] = new int[threshold];
		for (ComponentInfo ci:componentList) {
			int s = ci.size;
			if (s < threshold)
				si[s]++;
			else
				out.println(ci.representative.getPrimaryName().getName() + " (" + s + ")");
		}
		out.println("small components :  (size / # )");
		for (int i=1; i<threshold; i++)
			out.println(i + " / " + si[i]);
		
	}
	
	boolean areConnected(Person p1, Person p2) {
		if (p1 == null || p2 == null)
			return false;
		Integer i1 = personToComponentNumber.get(p1);
		Integer i2 = personToComponentNumber.get(p2);
		if (i1 == null || i2 == null)
			return false;
		return i1.intValue() == i2.intValue();		
	}
}