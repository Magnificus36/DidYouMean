package api.autocomplete;

import api.database.CSVCStub;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * A test class for the api.autocomplete.AutoCompleter class.
 * Created by Tim on 15-3-2016.
 */
public class AutoCompleterTest {
    CSVCStub DD;
    AutoCompleter AC;

    @org.junit.Before
    public void setUp() throws Exception {
        DD = new CSVCStub();
    }

    @org.junit.Test
    public void testGetTopN() throws Exception {
        DD.setData(null);
        AC = new AutoCompleter(DD);
        assertThat(AC.getTopN(5, "").length, is(0));

        DD.setData(generateData1());
        AC.resetTree();
        assertThat(AC.getTopN(7, ""), is(new String[]{"NR1", "NR2", "NR3", "NR4", "NR5", "NR6", "NR7"}));

        DD.setData(generateData2());
        AC.resetTree();
        assertThat(AC.getTopN(7, ""), anyOf(is(new String[]{"NR2", "NR123", "NR3", "NR4", "NR567", "NR6", "NR7"}), is(new String[]{"NR123", "NR2", "NR3", "NR4", "NR567", "NR6", "NR7"})));

        DD.setData(generateData3());
        AC.resetTree();
        assertThat(AC.getTopN(10,""), is(new String[]{"accu", "accuboor", "ac motor", "12v motor", "12v motor groen"}));
        assertThat(AC.getTopN(10,"ac"), is(new String[]{"accu", "accuboor", "ac motor"}));
        assertThat(AC.getTopN(10,"motor"), is(new String[]{}));
        assertThat(AC.getTopN(10,"12v motor"), is(new String[]{"12v motor", "12v motor groen"}));
        AC.learn("12v motor blauw", 1);
        assertThat(AC.getTopN(10,"12v motor"), is(new String[]{"12v motor", "12v motor groen", "12v motor blauw"}));
    }

    private Map<String, Integer> generateData1() { // standard
        Map<String, Integer> r = new HashMap<>();
        r.put("NR1", 500); //1
        r.put("NR2", 454); //2
        r.put("NR3", 354); //3
        r.put("NR4", 298); //4
        r.put("NR5", 238); //5
        r.put("NR6", 117); //6
        r.put("NR7", 2); //7
        return r;
    }

    private Map<String, Integer> generateData2() { // same values, this is sort of random because it depends on how the children are iterated. children are a set so no order.
        Map<String, Integer> r = new HashMap<>();
        r.put("NR123", 500); //2
        r.put("NR2", 500); //1
        r.put("NR3", 354); //3
        r.put("NR4", 220); //4
        r.put("NR567", 200); //5
        r.put("NR6", 117); //6
        r.put("NR7", 2); //7
        return r;
    }

    private Map<String, Integer> generateData3() { // more variations in keywords
        Map<String, Integer> r = new HashMap<>();
        r.put("accu", 500);
        r.put("accuboor", 454);
        r.put("ac motor", 354);
        r.put("12v motor", 301);
        r.put("12v motor groen", 298);
        return r;
    }
}