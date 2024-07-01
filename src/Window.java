import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.*;
import java.util.ArrayList;
import java.io.*;

public class Window extends JFrame {

    private ArrayList<Worker> workers = new ArrayList<>();
    private TableModel model = new TableModel(); //Модель таблицы
    private JTable table = new JTable(model); //Таблица
    private JLabel searchParameter = new JLabel("Критерий поиска:");
    private JTextField searchBar = new JTextField(""); //Строка поиска

    public Window(){
        JFrame frame = new JFrame("Рабочий коллектив");
        frame.setBounds(300, 300, 655, 460);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);

        JScrollPane tableSP = new JScrollPane(table);
        tableSP.setBounds(10, 10, 400, 400);
        CustomTableRowSorter sorter = new CustomTableRowSorter(model);
        table.setRowSorter(sorter);

        JButton adder = new JButton("Добавить");
        adder.setBounds(420, 10, 100, 40);
        JButton deleter = new JButton("Удалить");
        deleter.setBounds(530, 10, 100, 40);
        searchParameter.setBounds(420, 82, 210, 30);
        searchBar.setBounds(420, 112, 210, 30);
        JButton search = new JButton("Поиск");
        search.setBounds(420, 147, 100, 40);
        JButton clear = new JButton("Очистить");
        clear.setBounds(530, 147, 100, 40);
        JButton fileSaver = new JButton("Сохранить файл (.txt)");
        fileSaver.setBounds(420, 233, 210, 40);
        JButton fileOpener = new JButton("Открыть файл (.txt)");
        fileOpener.setBounds(420, 283, 210, 40);
        JButton exit = new JButton("Выход");
        exit.setBounds(420, 368, 210, 40);

        frame.add(tableSP);
        frame.add(adder);
        frame.add(deleter);
        frame.add(searchParameter);
        frame.add(searchBar);
        frame.add(search);
        frame.add(clear);
        frame.add(fileSaver);
        frame.add(fileOpener);
        frame.add(exit);

        frame.setVisible(true);
        frame.setResizable(false);

        adder.addActionListener(new AddingWorker());
        deleter.addActionListener(new DeletingWorker());
        search.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                model.search(searchBar.getText(), workers);
                model.fireTableDataChanged();
                searchParameter.setText("Критерий поиска: "
                        + searchBar.getText());
            }
        });
        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchParameter.setText("Критерий поиска:");
                searchBar.setText("");
                model.search("", workers);
                model.fireTableDataChanged();
            }
        });
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        fileSaver.addActionListener(new FileSaving());
        fileOpener.addActionListener(new FileOpening());
    }

    class AddingWorker implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            JFrame frame = new JFrame("Добавление работника");
            frame.setBounds(475, 430, 305, 210);
            frame.setLocationRelativeTo(null);
            frame.setLayout(null);

            JTextField nameField= new JTextField("");
            JLabel nameLabel = new JLabel("ФИО работника: ");
            nameLabel.setBounds(15, 10, 100, 30);
            nameField.setBounds(125, 10, 150, 30);

            JTextField positionField= new JTextField("");
            JLabel positionLabel = new JLabel("Должность: ");
            positionLabel.setBounds(15, 50, 100, 30);
            positionField.setBounds(125, 50, 150, 30);

            JTextField yearField= new JTextField("");
            JLabel yearLabel = new JLabel("Год поступления: ");
            yearLabel.setBounds(15, 90, 120, 30);
            yearField.setBounds(125, 90, 150, 30);

            JButton apply = new JButton("Добавить");
            JButton cancel = new JButton("Отмена");
            apply.setBounds(15, 130, 125, 30);
            cancel.setBounds(150, 130, 125, 30);

            frame.add(nameLabel);
            frame.add(nameField);
            frame.add(positionLabel);
            frame.add(positionField);
            frame.add(yearLabel);
            frame.add(yearField);
            frame.add(apply);
            frame.add(cancel);

            frame.setVisible(true);
            frame.setResizable(false);

            apply.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e){
                    String yearStr = yearField.getText();
                    if (yearStr.matches(".*[a-zA-Zа-я-А-Я].*")) {
                        JOptionPane.showMessageDialog(null,
                                "Поле \"Год\" не должно содержать буквы",
                                "Ошибка ввода", JOptionPane.PLAIN_MESSAGE);
                    }
                    else if (Integer.parseInt(yearField.getText()) < 1900 ||
                            Integer.parseInt(yearField.getText()) > 2024){
                        JOptionPane.showMessageDialog(null,
                                "Поле \"Год\" " +
                                        "должно соответствовать " +
                                        "диапазону [1900-2024]",
                                "Ошибка ввода", JOptionPane.PLAIN_MESSAGE);
                    }
                    else {
                        Worker work = new Worker(nameField.getText(),
                                positionField.getText(),
                                Integer.parseInt(yearField.getText()));
                        workers.add(work);
                        model.rewrite(workers);
                        model.fireTableDataChanged();
                        searchParameter.setText("Критерий поиска:");
                        searchBar.setText("");
                        frame.dispose();
                    }
                }
            });

            cancel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frame.dispose();
                }
            });
        }
    }

    class DeletingWorker implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            if(model.getRowCount()==0) {
                JOptionPane.showMessageDialog(null,
                        "               Список работников пуст",
                        "Список пуст", JOptionPane.PLAIN_MESSAGE);
            }
            else if (table.getSelectedRow() >= 0){
                workers.remove(model.getIndex(table.getSelectedRow())-1);
                model.rewrite(workers);
                model.fireTableDataChanged();
                searchParameter.setText("Критерий поиска:");
                searchBar.setText("");
            }
            else {
                JOptionPane.showMessageDialog(null,
                        "                    Строка не выбрана",
                        "Ошибка удаления", JOptionPane.PLAIN_MESSAGE);
            }
        }
    }

    class FileSaving implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "Текстовые файлы (*.txt)", "txt");
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(filter);
            int result = fileChooser.showSaveDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                String filePath = file.getAbsolutePath();
                if (!filePath.endsWith(".txt")) {
                    file = new File(filePath + ".txt");
                }
                try {
                    FileWriter writer = new FileWriter(file);
                    writer.write("Coded_txt_file_KR_OOP_222402_23\n\n");
                    for(int i = 0; i < workers.size(); i++) {
                        writer.write(i + "\n");
                        writer.write(workers.get(i).getName() + "\n");
                        writer.write(workers.get(i).getPosition() + "\n");
                        writer.write(workers.get(i).getYear() + "\n\n");
                    }
                    writer.close();
                } catch (IOException er) {
                    System.out.println("Фатальная ошибка x_x");
                }
            }
        }
    }

    class FileOpening implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            if(model.getRowCount()!=0) {
                JOptionPane.showMessageDialog(null,
                        "При открытии текущие данные будут утеряны!",
                        "Внимание!", JOptionPane.WARNING_MESSAGE);
            }
            String information;
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    information = reader.readLine();
                    if(!information.equals("Coded_txt_file_KR_OOP_222402_23")) {
                        JOptionPane.showMessageDialog(null,
                                "           Выбран неподходящий файл",
                                "Ошибка открытия", JOptionPane.PLAIN_MESSAGE);
                    }
                    else {
                        workers.clear();
                        while((information = reader.readLine()) != null){
                            if(!information.isEmpty() &&
                                    !information.equals("Coded_txt_file_KR_OOP_222402_23")){
                                workers.add(new Worker(reader.readLine(), reader.readLine(),
                                        Integer.parseInt(reader.readLine())));
                            }
                        }
                        model.rewrite(workers);
                        model.fireTableDataChanged();
                    }
                } catch (IOException er) {
                    System.out.println("Фатальная ошибка x_x");
                }
            }
        }
    }
}