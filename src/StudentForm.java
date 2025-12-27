import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class StudentForm extends JFrame implements ActionListener {

    JTextField tName, tAge, tCourse, tId;
    JTable studentTable;
    DefaultTableModel tableModel;
    JButton bAdd, bUpdate, bDelete, bView;
    
    // Modern Color Palette
    Color primaryColor = new Color(52, 152, 219); // Bright Blue
    Color successColor = new Color(46, 204, 113); // Emerald Green
    Color dangerColor = new Color(231, 76, 60);   // Alizarin Red
    Color bgColor = new Color(245, 247, 250);     // Soft Gray

    public StudentForm() {
        setTitle("Student Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        getContentPane().setBackground(bgColor);
        setLayout(new BorderLayout(20, 20));

        // --- Header Section ---
        JPanel header = new JPanel();
        header.setBackground(primaryColor);
        JLabel title = new JLabel("STUDENT REGISTRATION");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        header.setBorder(new EmptyBorder(15, 0, 15, 0));
        header.add(title);
        add(header, BorderLayout.NORTH);

        // --- Main Content Container ---
        JPanel mainContent = new JPanel(new BorderLayout(20, 20));
        mainContent.setOpaque(false);
        mainContent.setBorder(new EmptyBorder(0, 20, 20, 20));

        // 1. Input Panel (Left Side)
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Labels and TextFields
        addFormRow(leftPanel, "Student ID:", tId = new JTextField(15), gbc, 0);
        addFormRow(leftPanel, "Full Name:", tName = new JTextField(15), gbc, 1);
        addFormRow(leftPanel, "Age:", tAge = new JTextField(15), gbc, 2);
        addFormRow(leftPanel, "Course:", tCourse = new JTextField(15), gbc, 3);

        // 2. Buttons Panel (Bottom of Left Panel)
        JPanel buttonGrid = new JPanel(new GridLayout(2, 2, 10, 10));
        buttonGrid.setOpaque(false);
        
        bAdd = createStyledButton("Add", successColor);
        bUpdate = createStyledButton("Update", primaryColor);
        bDelete = createStyledButton("Delete", dangerColor);
        bView = createStyledButton("Refresh", new Color(108, 117, 125));

        buttonGrid.add(bAdd);
        buttonGrid.add(bUpdate);
        buttonGrid.add(bDelete);
        buttonGrid.add(bView);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 5, 5, 5);
        leftPanel.add(buttonGrid, gbc);

        mainContent.add(leftPanel, BorderLayout.WEST);

        // 3. Table Section (Right Side)
        String[] columns = {"ID", "Name", "Age", "Course"};
        tableModel = new DefaultTableModel(columns, 0);
        studentTable = new JTable(tableModel);
        studentTable.setRowHeight(30);
        studentTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        studentTable.setSelectionBackground(new Color(235, 245, 255));
        
        JScrollPane tableScroll = new JScrollPane(studentTable);
        tableScroll.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        mainContent.add(tableScroll, BorderLayout.CENTER);

        add(mainContent, BorderLayout.CENTER);

        // --- Event Listeners ---
        bAdd.addActionListener(this);
        bUpdate.addActionListener(this);
        bDelete.addActionListener(this);
        bView.addActionListener(this);

        setLocationRelativeTo(null);
        setVisible(true);
        refreshTable(); // Load data on startup
    }

    private void addFormRow(JPanel panel, String labelText, JTextField textField, GridBagConstraints gbc, int row) {
        gbc.gridy = row;
        gbc.gridx = 0; gbc.gridwidth = 1;
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        panel.add(lbl, gbc);
        
        gbc.gridx = 1;
        textField.setPreferredSize(new Dimension(150, 30));
        panel.add(textField, gbc);
    }

    private JButton createStyledButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        ArrayList<Student> list = StudentDAO.getAllStudents();
        for (Student s : list) {
            tableModel.addRow(new Object[]{s.getId(), s.getName(), s.getAge(), s.getCourse()});
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == bAdd) {
                Student s = new Student(Integer.parseInt(tId.getText()), tName.getText(), Integer.parseInt(tAge.getText()), tCourse.getText());
                StudentDAO.addStudent(s);
                refreshTable();
            } else if (e.getSource() == bUpdate) {
                Student s = new Student(Integer.parseInt(tId.getText()), tName.getText(), Integer.parseInt(tAge.getText()), tCourse.getText());
                StudentDAO.updateStudent(s);
                refreshTable();
            } else if (e.getSource() == bDelete) {
                StudentDAO.deleteStudent(Integer.parseInt(tId.getText()));
                refreshTable();
            } else if (e.getSource() == bView) {
                refreshTable();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "System Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
