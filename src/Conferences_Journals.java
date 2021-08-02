import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

public class Conferences_Journals
{

    private String subKeys;
    //private int countContributors;
    private Map<String, Set<Person>> listContributors = new TreeMap<String, Set<Person>>();

    public Conferences_Journals(Publication p)
    {
        int prefixIndex = p.getKey().lastIndexOf("/");
        subKeys = p.getKey().substring(0, prefixIndex);


//        Arrays.asList(p.getContributorsArray()).stream().forEach(personName -> {
//            //listContributors.put(subKeys, new HashSet<>());
//            if(listContributors.get(subKeys) == null){
//                listContributors.put(subKeys, p.getContributorsIDs());
//            }
//            listContributors.get(subKeys).addAll(p.getContributorsIDs());
//
//        });
    }

    public String getSubKeys() {
        return subKeys;
    }

    public void setSubKeys(String subKeys) {
        this.subKeys = subKeys;
    }

    public Map<String, Set<Person>> getListContributors() {
        return listContributors;
    }

    //    public int getCountContributors() {
//        return countContributors;
//    }
//
//    public void setCountContributors(int countContributors) {
//        this.countContributors = countContributors;
//    }

    @Override
    public String toString()
    {
        return subKeys + " (" + listContributors.size() + ")";
    }
}
