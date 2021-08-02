import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamPersons {

    static private Map<String,Set<Person>> StreamMap;      // for each stream number of persons

    static {
        StreamMap = new HashMap<String,Set<Person>>();
    }

    static public Map<String,Set<Person>> getStreamMap() {
        return StreamMap;
    }

    StreamPersons() {
        StreamMap = new TreeMap<String,Set<Person>>();
        Publication.getPublications()
                .stream()
                .forEach(
                        e -> {
                            int prefixIndex = e.getKey().lastIndexOf("/");
                            String streamName = e.getKey().substring(0, prefixIndex);
                            // new Stream?
                            if(StreamMap.get(streamName) == null){
                                StreamMap.put(streamName, e.getContributorsIDs());
                            }else{
                                StreamMap.get(streamName).addAll(e.getContributorsIDs());
                            }
                        }
                );

        // ignore streams with less than 1000 persons
        StreamMap = StreamMap
                .entrySet()
                .stream()
                //.filter(e -> e.getValue().size() >= 30000)
                .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));

        // fill streams with StreamMap
        StreamMap
                .entrySet()
                .stream()
                .forEach(
                        e -> new Strm(e.getKey(), e.getValue())
                );


        print_Stream_maps();
    }

    // Methods
    void print_Stream_maps() {
        StreamMap
                .entrySet()
                .stream()
                .forEach(
                        e -> System.out.println(e.getKey() + " "
                                + "(" + e.getValue().size() + ")"));
    }

    // Main
    public static void main(String[] args) {
        System.setProperty("entityExpansionLimit", "2500000");
        if (args.length < 1) {  // Need to be changed to 1
            System.out.println("Usage: java Parser [input]");
            System.exit(0);
        }
        new Parser(args[0]);
        new StreamPersons();
    }
}
