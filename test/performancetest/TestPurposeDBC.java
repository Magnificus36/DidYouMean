package performancetest;

import api.database.CSVControl;
import api.database.IDBControl;

import java.io.IOException;
import java.util.Map;

/**
 * Controls the api.database. Uses CSV files generated by google analytics. These CSV files should contain the following in the following order:
 * Search Term,Total Unique Searches,Results Pageviews / Search,% Search Exits,% Search Refinements,Time after Search,Average Search Depth.
 * These CSV files are edited in such a way that only the actual data is left. the comments are out, as well as the names of the columns and the day index, total unique searches part.
 *
 * @author Tim
 */
public class TestPurposeDBC implements IDBControl {
    public static final String[] FILENAMES = {"./csv/Data1.csv", "./csv/Data2.csv", "./csv/Data3.csv", "./csv/Data4.csv"};
    private String[] paths;
    private Map<String,Integer> data;

    public TestPurposeDBC() {
        try {
            data = new CSVControl(FILENAMES).getData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Map<String, Integer> getData() {
        return data;
    }

    public void empty(){
        data = null;
    }
}
