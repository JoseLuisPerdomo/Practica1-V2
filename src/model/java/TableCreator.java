import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TableCreator {

    public static List<String> days = DataGetter.days;

    public static void tablesCreation() {
        String dbPath = "CanaryWeatherTables.db";
        DataGetter.weatherGetter();
        try(Connection connection = connect(dbPath)) {
            Statement statement = connection.createStatement();
            dropTable(statement, islas);
            createTables(statement, islas);
        } catch (SQLException e) {
            e.getCause();
        }
    }
    public static void tablesupdates(){
        String dbPath = "CanaryWeatherTables.db";
        DataGetter.weatherGetter();
        try(Connection connection = connect(dbPath)) {
            Statement statement = connection.createStatement();
            update(statement);
        } catch (SQLException e) {
            e.getCause();
        }
    }


    public static List<String> islas = new ArrayList<>(Arrays.asList("Gran_Canaria", "Tenerife", "La_Gomera", "Fuerteventura", "La_Palma", "Lanzarote", "El_Hierro", "La_Graciosa"));

    public static void createTables(Statement statement, List islas) throws SQLException {
        for (int i = 0; i <8; i++){
            statement.execute("CREATE TABLE IF NOT EXISTS " + islas.get(i) +  " (day INTEGER PRIMARY KEY, temperature Double, precipitations Double, humidity Double, clouds Double, windSpeed Double);");
        }
    }

    public static void dropTable(Statement statement, List islas) throws SQLException {
        for (int i = 0; i < 8; i++) {
            statement.execute("DROP TABLE IF EXISTS " + islas.get(i) + " ;\n");
        }
    }

    public static void update(Statement statement) throws SQLException {
        List<List<String>> weatherData = DataGetter.WeatherData;
        List<Integer> lista = new ArrayList<>();
        lista.add(1);
        lista.add(2);
        lista.add(3);
        lista.add(4);
        lista.add(5);
        lista.add(6);
        for (int i = 0; i < 8;i++) {
            for (int j = 0; j < 5; j++) {
                statement.execute(String.format("INSERT OR REPLACE INTO " +  islas.get(i) + " (day, temperature, precipitations, humidity, clouds, windSpeed) VALUES (%s, %s, %s, %s, %s, %s);", days.get(j), weatherData.get(i).get(0), weatherData.get(i).get(1), weatherData.get(i).get(2), weatherData.get(i).get(3), weatherData.get(i).get(4)));
                weatherData.get(i).subList(0, 5).clear();
            }
        }
        System.out.println("Table products updated");
    }

    public static Connection connect(String dbPath) {
        Connection conn = null;
        try {
            String url = "jdbc:sqlite:" + dbPath;
            conn = DriverManager.getConnection(url);
            System.out.println("Connection to SQLite has been established.");
            return conn;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
}

