import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

// Extend JFrame for Swing functionality
public class StudentForm extends JFrame implements ActionListener {

    JTextField tName, tAge, tCourse, tId;
    JTextArea output;
    JButton bAdd, bUpdate, bDelete, bView;
    JScrollPane scrollPane; // To make the output area scrollable

    public StudentForm() {
        setTitle("Student Management System");
        // Simple way to ensure the application closes
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(650, 500);
        setLayout(new BorderLayout(15, 15)); // Use BorderLayout for main frame

        // ----------------- INPUT & BUTTONS PANEL (Top Section) -----------------
        JPanel topPanel = new JPanel(new BorderLayout());

        // 1. Input Panel: Uses GridLayout for neat input/label arrangement
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Student Details"));
        inputPanel.setBackground(new Color(240, 240, 240)); // Light gray background

        inputPanel.add(new JLabel("ID (for Update/Delete):", SwingConstants.RIGHT));
        tId = new JTextField(10);
        inputPanel.add(tId);

        inputPanel.add(new JLabel("Name:", SwingConstants.RIGHT));
        tName = new JTextField(20);
        inputPanel.add(tName);

        inputPanel.add(new JLabel("Age:", SwingConstants.RIGHT));
        tAge = new JTextField(5);
        inputPanel.add(tAge);

        inputPanel.add(new JLabel("Course:", SwingConstants.RIGHT));
        tCourse = new JTextField(15);
        inputPanel.add(tCourse);
        
        topPanel.add(inputPanel, BorderLayout.NORTH);

        // 2. Buttons Panel: Uses FlowLayout to center the buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        
        bAdd = new JButton("Add Student");
        bUpdate = new JButton("Update Student");
        bDelete = new JButton("Delete Student");
        bView = new JButton("View All Students");

        // Simple professional styling for buttons
        bAdd.setBackground(new Color(46, 204, 113)); // Emerald Green
        bAdd.setForeground(Color.WHITE);
        bUpdate.setBackground(new Color(52, 152, 219)); // Peter River Blue
        bUpdate.setForeground(Color.WHITE);
        bDelete.setBackground(new Color(231, 76, 60)); // Alizarin Red
        bDelete.setForeground(Color.WHITE);
        bView.setBackground(new Color(149, 165, 166)); // Concrete Gray
        bView.setForeground(Color.WHITE);
        
        buttonPanel.add(bAdd);
        buttonPanel.add(bUpdate);
        buttonPanel.add(bDelete);
        buttonPanel.add(bView);

        topPanel.add(buttonPanel, BorderLayout.CENTER);
        
        add(topPanel, BorderLayout.NORTH); // Add the combined input/button panel to the top

        // ----------------- OUTPUT AREA (Center Section) -----------------
        output = new JTextArea(10, 50);
        output.setEditable(false);
        output.setFont(new Font("Monospaced", Font.PLAIN, 12));
        output.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Add padding
        scrollPane = new JScrollPane(output); // Make output scrollable

        add(scrollPane, BorderLayout.CENTER);

        // ----------------- Listeners -----------------
        bAdd.addActionListener(this);
        bUpdate.addActionListener(this);
        bDelete.addActionListener(this);
        bView.addActionListener(this);
        
        // Final setup
        setLocationRelativeTo(null); // Center the window on the screen
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == bAdd) {
            Student s = new Student(
            Integer.parseInt(tId.getText()),
            tName.getText(),
            Integer.parseInt(tAge.getText()),
            tCourse.getText()
    );
        StudentDAO.addStudent(s);
         output.setText("Added Successfully!\n" + output.getText());
         // Clear the ID field after successful addition
         tId.setText(""); 
} else if (e.getSource() == bUpdate) {
                // Input validation for ID
                if (tId.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter an ID to update.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Student s = new Student(
                    Integer.parseInt(tId.getText()),
                    tName.getText(),
                    Integer.parseInt(tAge.getText()),
                    tCourse.getText()
                );
                StudentDAO.updateStudent(s);
                output.setText("Updated Successfully!\n" + output.getText());

            } else if (e.getSource() == bDelete) {
                 // Input validation for ID
                if (tId.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter an ID to delete.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int id = Integer.parseInt(tId.getText());
                StudentDAO.deleteStudent(id);
                output.setText("Deleted Successfully!\n" + output.getText());

            } else if (e.getSource() == bView) {
                ArrayList<Student> list = StudentDAO.getAllStudents();
                StringBuilder sb = new StringBuilder("ID | Name            | Age | Course\n" + "--------------------------------------------------------\n");
                for (Student st : list) {
                    // Use String.format for clean, column-aligned output
                    sb.append(String.format("%-2s | %-15s | %-3s | %s%n", st.getId(), st.getName(), st.getAge(), st.getCourse()));
                }
                output.setText(sb.toString());
            }

        } catch (NumberFormatException nfe) {
            // Catching all number format errors and showing a friendly dialog
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for ID and Age.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            // Catching database errors
            JOptionPane.showMessageDialog(this, "A database error occurred: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}