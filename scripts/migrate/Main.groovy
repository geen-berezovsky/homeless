import java.sql.*;
import java.util.Calendar;
import java.util.Map;

public class Main {

    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    private void givenCertToBasicDocumentRegistry() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        connect = DriverManager.getConnection("jdbc:mysql://localhost/homeless?"
                + "user=homeless&password=homeless");

        statement = connect.createStatement();
        resultSet = statement.executeQuery("select * from GivenCertificate");

        while (resultSet.next()) {

            int id = resultSet.getInt("id");
            Date date = resultSet.getDate("date");
            String docNum = resultSet.getString("num");
            int type = resultSet.getInt("type");
            int client = resultSet.getInt("client");
            int worker = resultSet.getInt("worker");
            System.out.println(id);

            preparedStatement = connect.prepareStatement("insert into BasicDocumentRegistry values (?, ?, ?, ?, ? , ?, ?, ?, ?)");
            preparedStatement.setInt(1, id);
            preparedStatement.setInt(2, type);
            preparedStatement.setString(3, docNum);
            preparedStatement.setInt(4, client);
            preparedStatement.setInt(5, 0);
            preparedStatement.setDate(6, date);
            preparedStatement.setDate(7, null);
            preparedStatement.setInt(8, worker);
            java.util.Date d = new java.util.Date();
            int y = d.getYear();
            int m = d.getMonth();
            int day = d.getDay();
            preparedStatement.setDate(9, new java.sql.Date(y,m,day));
            preparedStatement.executeUpdate();
        }
    }


    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        Main m = new Main();
        m.givenCertToBasicDocumentRegistry();
/*
        for (Map.Entry e : System.getProperties().entrySet()) {
            System.out.println(e.getKey()+"="+e.getValue());
        }
*/
    }
}
