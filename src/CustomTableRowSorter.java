import javax.swing.table.TableRowSorter;
import java.util.Comparator;

public class CustomTableRowSorter extends TableRowSorter<TableModel> {

    public CustomTableRowSorter(TableModel model){
        super(model);
        setComparator(0, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                int num1 = Integer.parseInt(o1);
                int num2 = Integer.parseInt(o2);
                return Integer.compare(num1, num2);
            }
        });
    }
}