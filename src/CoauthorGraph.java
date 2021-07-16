import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CoauthorGraph {

    private Map<Person,Person[]> graph;   // Map<Person,Set<Person>>
    private boolean completed = false;
    private int edges;

    private Collection<Person> collectCoauthors(Person p) {
        Set<Person> s = p.publications().flatMap(Publication::names).
                map(PersonName::getId).collect(Collectors.toSet());
        s.remove(p);
        return s;
    }

    private Person[] buildCouthorsArray(Person person) {
        Collection<Person> c = collectCoauthors(person);
        Person[] coauthors = c.toArray(new Person[c.size()]);
        graph.put(person, coauthors);
        return coauthors;
    }

    public Collection<Person> getCouthors(Person person) {
        if (person == null)
            return null;
        Person[] coauthors = graph.get(person);
        if (coauthors == null)
            coauthors = buildCouthorsArray(person);
        return Collections.unmodifiableList(Arrays.asList(coauthors));
    }

    public int getNumberOfCoauthors(Person person) {
        if (person == null)
            return 0;
        Person[] coauthors = graph.get(person);
        if (coauthors == null)
            coauthors = buildCouthorsArray(person);
        return coauthors.length;
    }


    public int getNumberOfEdges() {
        /*
         * 	force the construction of the complete graph
         */
        if (!completed) {
            edges = Person.persons().mapToInt(p -> getNumberOfCoauthors(p)).sum() / 2;
            completed = true;
        }
        return edges;
    }

    CoauthorGraph() {
        graph = new ConcurrentHashMap<Person,Person[]>();
    }

    public static void main(String[] args) {
        System.setProperty("entityExpansionLimit", "2500000");
        if (args.length < 1) {
            System.out.println("Usage: java CoauthorGraph [input]");
            System.exit(0);
        }
        new Parser(args[0]);

        CoauthorGraph cg = new CoauthorGraph();
        /*
         * Coauthors of one person
         */
        Person bw = PersonName.createName("Benjamin Weyers").getId();
        System.out.println("#coauthors(Benjamin Weyers)=" + cg.getNumberOfCoauthors(bw));
        /*
        for (Person ca: cg.getCouthors(bw)) {
                System.out.print(ca.getPrimaryName().getName() + " / ");
        }
        */

        Map<Person,Long> s = bw.publications().flatMap(Publication::names).
                map(PersonName::getId).
                collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        s.remove(bw);
        for (Map.Entry<Person, Long> e: s.entrySet()) {
            System.out.println(e.getKey().getPrimaryName().getName() + " (" + e.getValue() + ")");
        }

        System.out.println("\nbuild complete coauthor graph");
        System.out.println(cg.getNumberOfEdges() + "  edges");

        Components co = new Components(cg);
        co.statistics(System.out);
        Person hf = PersonName.createName("Henning Fernau").getId();
        System.out.println("Benjamin Weyers  and  Henning Fernau  " +
                (co.areConnected(bw, hf) ? "are " : "are NOT ") +
                "connected in the dblp coauthor graph");
    }

}