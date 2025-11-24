import java.sql.*;
import java.util.ArrayList;

public class StudentDAO {


public static void addStudent(Student s) {
    String sql = "INSERT INTO Student(id, name, age, course) VALUES (?,?,?,?)"; // <-- Included 'id' column
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, s.getId());      
        stmt.setString(2, s.getName());
        stmt.setInt(3, s.getAge());
        stmt.setString(4, s.getCourse());

        stmt.executeUpdate();
        System.out.println("Student Added!");

    } catch (Exception e) {
        e.printStackTrace();
    }
}

    public static void updateStudent(Student s) {
        // FIX: Changed table name from Stdnet to Student
        String sql = "UPDATE Student SET name=?, age=?, course=? WHERE id=?"; 
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, s.getName());
            stmt.setInt(2, s.getAge());
            stmt.setString(3, s.getCourse());
            stmt.setInt(4, s.getId());

            stmt.executeUpdate();
            System.out.println("Student Updated!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteStudent(int id) {
        // FIX: Changed table name from Stdnet to Student
        String sql = "DELETE FROM Student WHERE id=?"; 
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Student Deleted!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Student> getAllStudents() {
        ArrayList<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM Student"; 

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("course")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}