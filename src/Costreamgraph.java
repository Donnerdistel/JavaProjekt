import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

public class Costreamgraph {

    private Map<Strm, Set<Strm>> streamWithStreams;

    private Map<Strm, Strm> maxStream;


    Costreamgraph() {
        // initialize
        streamWithStreams = new ConcurrentHashMap<>();
        maxStream = new ConcurrentHashMap<>();
        int maximum;
        int inter;


        Set<Strm> copyStreams = new HashSet<>(Strm.getStreams());
        for (Strm s1 : Strm.getStreams()){
            System.out.println(s1);
            if (streamWithStreams.get(s1) == null) {
                streamWithStreams.put(s1, new HashSet<Strm>());
            }
            maximum = s1.getMaxIntersection();
            // remove duplicated streams
            copyStreams.remove(s1);
            for (Strm s2 : copyStreams){
                streamWithStreams.put(s2, new HashSet<Strm>());

                Set<Person> intersection = new HashSet<Person>(s1.names());
                intersection.retainAll(s2.names());      // get intersection of s1 and s2

                if((inter = intersection.size()) > 0){
                    // add to inputs of s1 and s2
                    streamWithStreams.get(s1).add(s2);
                    streamWithStreams.get(s2).add(s1);

                    if(inter > maximum){
                        maximum = inter;
                        s1.setMaxIntersection(maximum);
                        maxStream.put(s1, s2);    // s1 and s2 have highest weight

                        // same for s2
                        maxStream.put(s2, s1);    // s1 and s2 have highest weight
                        s2.setMaxIntersection(maximum);
                    }
                }
            }


        }

        toTxt();
    }

    public void toTxt(){
        File file = new File("CostreamGraph.txt");

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));

            for (Map.Entry<Strm, Set<Strm>> s1 : streamWithStreams.entrySet()){
                writer.write(s1.getKey().getKey() + " (" + s1.getKey().getSize() + ") :");
                writer.newLine();
                writer.write("\t out");
                writer.newLine();
                writer.write("\t\t" + maxStream.get(s1.getKey()).getKey());
                writer.newLine();
                writer.write("\t in");
                writer.newLine();
                AtomicReference<String> in = new AtomicReference<>("");
                s1.getValue().forEach(s -> {
                    in.set(in + "\t\t" + s.getKey() + "\n");
                });
                writer.write(in.toString());
            }

            writer.close();
        }
        catch(IOException ioe) {
            System.err.println(ioe);
        }
    }

    public static void main(String[] args) {
        System.setProperty("entityExpansionLimit", "2500000");
        if (args.length < 1) {
            System.out.println("Usage: java CoauthorGraph [input]");
            System.exit(0);
        }
        new Parser(args[0]);
        new StreamPersons();


        Costreamgraph csg = new Costreamgraph();
        System.out.println("\nbuild complete costream graph");
    }

}