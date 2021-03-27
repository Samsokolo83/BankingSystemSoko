package banking;

import java.sql.*;

public class DB {
    private static String url = "jdbc:sqlite:card.s3db";
    public static void createNewDataBase(String fileName) {

        String url = "jdbc:sqlite:" + fileName;

        try (Connection connection = DriverManager.getConnection(url)) {
            DatabaseMetaData meta = connection.getMetaData();
            System.out.println("The driver name is " + meta.getDriverName());
            System.out.println("A new database has been created.");
        } catch (SQLException e) {

            System.out.println(e.getMessage());
        }
    }

    public static void createNewTable() {
        // SQLite connection string


        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS card (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	number text NOT NULL,\n"
                + "	pin text NOT NULL,\n"
                + " balance integer default 0\n"
                + ");";

        try (Connection connection = DriverManager.getConnection(url);
             Statement stmt = connection.createStatement()) {
            // create a new table
            stmt.execute(sql);
            connection.commit();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void insertNewCard(String cardNo, String cardPin) {
        String sql = "INSERT INTO card(number,pin) VALUES(?,?)";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
             preparedStatement.setString(1, cardNo);
             preparedStatement.setString(2, cardPin);
             preparedStatement.executeUpdate();
             conn.commit();

        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }

    }

    public static boolean isValidCard(String cardNo, String cardPin) {
        String sql = "SELECT number,pin FROM card WHERE number = ? and pin = ?";
        Card card = new Card();
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,cardNo);
            preparedStatement.setString(2,cardPin);
            ResultSet resultSet = preparedStatement.executeQuery();
            card.setCardNo(resultSet.getString("number"));
            card.setPin(resultSet.getString("pin"));
            if(card.getCardNo().equals(cardNo) && card.getPin().equals(cardPin)) {
                connection.close();
                return true;
            }

        } catch (SQLException e) {
            return false;
        }

        return  false;
    }

    public static void addIncome(int income, String cardNo) {
        int total = Integer.parseInt(getBalance(cardNo)) + income;
        String sql = "UPDATE card SET balance = " + String.valueOf(total) + " WHERE number = " + cardNo;
        try {
            Connection connection = DriverManager.getConnection(url);
            Statement statement = connection.createStatement();
            statement.execute(sql);
            connection.commit();
            statement.close();
            connection.close();

        } catch (SQLException e) {

            System.out.println(e.getMessage());
        }


    }

    public static String getBalance(String cardNo) {
        String sql = "SELECT balance FROM card where number = " + cardNo;

        try {
            Connection connection = DriverManager.getConnection(url);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            String balance = resultSet.getString("balance");
            connection.close();
            return balance;


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return "";

    }

    public static boolean isRegisteredCard(String cardNo)  {
        String sql = "SELECT number FROM card where number = " + cardNo;
        ResultSet resultSet = null;
        try {
            Connection connection = DriverManager.getConnection(url);
            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            if (cardNo.equals(resultSet.getString("number"))) {
                resultSet.close();
                statement.close();
                connection.close();
                return true;
            }



        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false;

    }

    public static void deleteAccount(String account) {
        String sql = "DELETE FROM card WHERE number = " + account;
        try {
            Connection connection = DriverManager.getConnection(url);
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            connection.commit();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    }

