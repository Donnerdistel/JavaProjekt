import java.util.*;
import java.util.stream.Collectors;

/*
Name	                Matrikelnummer	    E-Mail
Sebastian Britner	    1485271	            s4sebrit@uni-trier.de
Jens Hartmann	        1470700	            s4jehart@uni-trier.de
Jan Niclas Ruppenthal	1481198	            s4jsrupp@uni-trier.de
 */

/*
This class creates every available stream.
First, we put every person in the same key.
Then we create all streams.
 */
public class StreamPersons {

    static private Map<String,Set<Person>> StreamMap;      // stream with set of persons

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
                .filter(e -> e.getValue().size() >= 1000)
                .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));

        // create Strm Object for each stream
        StreamMap
                .entrySet()
                .stream()
                .forEach(
                        e -> new Strm(e.getKey(), e.getValue())
                );


        //print_Stream_maps();
    }

    void print_Stream_maps() {
        StreamMap
                .entrySet()
                .stream()
                .forEach(
                        e -> System.out.println(e.getKey() + " "
                                + "(" + e.getValue().size() + ")"));
    }
}
