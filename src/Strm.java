import java.util.HashSet;
import java.util.Set;

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
