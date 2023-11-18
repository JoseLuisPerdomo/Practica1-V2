import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CoordenadasReader {

    public static void main(String[] args) {
        String archivo = "src/control/resources//islandsCoordinates";

        try {
            List<List<String>> coordenadas = leerCoordenadas(archivo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<List<String>> leerCoordenadas(String archivo) throws IOException {
        List<List<String>> coordenadas = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                List<String> coordenada = new ArrayList<>();
                for (String parte : partes) {
                    coordenada.add(parte.trim());
                }
                coordenadas.add(coordenada);
            }
        } catch (IOException e){
            e.printStackTrace();
        }

        return coordenadas;
    }
    }
