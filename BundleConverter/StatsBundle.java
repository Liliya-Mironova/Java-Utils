import java.util.*;

public class StatsBundle extends ListResourceBundle {
    public Object[][] getContents() {
        return contents;
    }

    private Object[][] contents = {
        { "GDP", new Integer(0) },
        { "Population", new Integer(1) },
        { "Literacy", new Double(0.0) },
    };
}
// 238Zydfhz/