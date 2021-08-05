import java.util.HashSet;
import java.util.Set;

/*
Name	                Matrikelnummer	    E-Mail
Sebastian Britner	    1485271	            s4sebrit@uni-trier.de
Jens Hartmann	        1470700	            s4jehart@uni-trier.de
Jan Niclas Ruppenthal	1481198	            s4jsrupp@uni-trier.de
 */

/*
This class is for every Stream in the xml file
Every object contains the key, the set of contributors and the size of that set.
Furthermore the variable maxIntersection contains the maximum size of the intersection with another stream.
 */
public class Strm {
    static private Set<Strm> streams;

    static {
        streams = new HashSet<Strm>();
    }

    static public Set<Strm> getStreams() {
        return streams;
    }

    private String key;
    private Set<Person> contributors;
    private int size;
    private int maxIntersection;

    Strm(String key, Set<Person> contributors) {
        this.key = key;
        this.contributors = contributors;
        size = this.contributors.size();
        streams.add(this);
        maxIntersection = -1;
    }

    public String getKey() {
        return key;
    }

    public int getSize() {
        return size;
    }

    public Set<Person> names() {
        return contributors;
    }

    public int getMaxIntersection() {
        return maxIntersection;
    }

    public void setMaxIntersection(int maxIntersection) {
        this.maxIntersection = maxIntersection;
    }

    @Override
    public String toString() {
        return "Strm{" +
                "key='" + key + '\'' +
                ", size=" + size +
                ", maxIntersection=" + maxIntersection +
                '}';
    }
}
