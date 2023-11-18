import com.google.gson.JsonArray;
import org.jsoup.Jsoup;
import org.jsoup.Connection;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.util.stream.StreamSupport;


public class DataGetter {

    public static List<String> days = new ArrayList<>();
    public static List<List<String>> WeatherData;

    public static List<List<String>> coordinates;

    static {
        try {
            coordinates = CoordenadasReader.leerCoordenadas("src/control/resources//islandsCoordinates");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        weatherGetter();
    }

    public static void weatherGetter() {
        WeatherData = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            String Url = String.format("https://api.openweathermap.org/data/2.5/forecast?lat=%s&lon=%s&unit=metric&appid=036defba6c31cb2bbe3259bbfb66dc75", coordinates.get(i).get(0), coordinates.get(i).get(1));

            try {
                Connection.Response response = Jsoup.connect(Url)
                        .ignoreContentType(true)
                        .execute();

                String jsonResponse = response.body();
                JsonObject weatherJson = JsonParser.parseString(jsonResponse).getAsJsonObject();
                WeatherData.add(dataCleaner(jsonListSelector(weatherJson).subList(0, 5)));
            } catch (IOException e) {
                e.getCause();
            }
        }
    }

    public static List<String> dataCleaner(List<JsonObject> weatherJson) {
        List<String> islandData = new ArrayList<>(5);

        for (int i = 0; i < 5;i++) {
            if (weatherJson != null) {
                String temperature = weatherJson.get(i).getAsJsonObject("main").get("temp").getAsString();
                String precipitation = "0.0";
                String humidity = weatherJson.get(i).getAsJsonObject("main").get("humidity").getAsString();
                String clouds = weatherJson.get(i).getAsJsonObject("clouds").get("all").getAsString();
                String windSpeed = weatherJson.get(i).getAsJsonObject("wind").get("speed").getAsString();
                islandData.add(String.valueOf(temperature).replace(',', '.'));
                islandData.add(precipitation);
                islandData.add(String.valueOf(humidity).replace(',', '.'));
                islandData.add(String.valueOf(clouds).replace(',', '.'));
                islandData.add(String.valueOf(windSpeed).replace(',', '.'));
            } else {
                System.out.println("Error: No se ha obtenido el JSON del tiempo.");
            }
        }
        return islandData;
        }

    public static List<JsonObject> jsonListSelector(JsonObject weatherJson){
        JsonObject jsonObject = JsonParser.parseString(String.valueOf(weatherJson)).getAsJsonObject();

        JsonArray listArray = jsonObject.getAsJsonArray("list");

        return StreamSupport.stream(listArray.spliterator(), false)
                .map(JsonObject.class::cast)
                .filter(item -> item.getAsJsonPrimitive("dt_txt").getAsString().endsWith("12:00:00"))
                .peek(item -> {
                    String day = obtenerDia(item.getAsJsonPrimitive("dt_txt").getAsString());
                    item.addProperty("day", day);
                    days.add(day);})
                .toList();

        }
    public static String obtenerDia(String dt_txt) {
        return dt_txt.substring(8, 10);
    }
    }

