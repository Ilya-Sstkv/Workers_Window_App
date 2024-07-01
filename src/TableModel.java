import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class TableModel extends AbstractTableModel {

    private ArrayList<String []> dataTable;

    public TableModel(){
        dataTable = new ArrayList<>();
    }

    @Override
    public int getRowCount() {
        return dataTable.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return dataTable.get(rowIndex)[columnIndex];
    }

    @Override
    public String getColumnName(int columnIndex) {
        return switch (columnIndex) {
            default -> "Номер";
            case 1 -> "Имя";
            case 2 -> "Должность";
            case 3 -> "Год поступления";
        };
    }

    public int getIndex(int index){
        return Integer.parseInt(dataTable.get(index)[0]);
    }

    public void rewrite(ArrayList<Worker> workers){
        dataTable.clear();
        for(int i = 0; i < workers.size(); i++) {
            addRow(workers.get(i), i + 1);
        }
    }

    public void addRow(Worker obj, int size) {
        String[] row = new String[4];
        row[0] = String.valueOf(size);
        row[1] = obj.getName();
        row[2] = obj.getPosition();
        row[3] = String.valueOf(obj.getYear());
        dataTable.add(row);
    }

    public void search(String search, ArrayList<Worker> workers){
        rewrite(workers);
        if (!search.isBlank()) {
            for (int i = 0; i < dataTable.size(); i++) {
                if (!dataTable.get(i)[0].contains(search) &&
                        !dataTable.get(i)[1].contains(search) &&
                        !dataTable.get(i)[2].contains(search) &&
                        !dataTable.get(i)[3].contains(search)) {
                    dataTable.remove(i);
                    i--;
                }
            }
        }
    }
}