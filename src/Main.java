import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
/**
 * @author Angel Laureano Borquez Fimbres (221210626)
 * @author Juan Diego Quijada Castillo (222206011)
 * @author Aaron Velez Coronado (222206601)
 */
public class Main {
    public static void main(String[] args) {
        // Aqui empiezan a conectarse los clientes
        ArrayList<ServerThread> threadList = new ArrayList<>();
        try (ServerSocket serversocket = new ServerSocket(5000)) {
            System.out.println("Server IP: 127.0.0.1");
            System.out.println("Server IP: " + conseguirIP() + "\n");
            serversocket.setSoTimeout(5000); // Establece un tiempo de espera de 5 segunditos
            int i = 0;
            while(true) {
                try {
                    i++;
                    System.out.println("Esperando conexión...");
                    Socket socket = serversocket.accept();
                    ServerThread serverThread = new ServerThread(socket, threadList, "Maquina"+i);
                    threadList.add(serverThread);
                    System.out.println("Se ha conectado un cliente.");
                } catch (SocketTimeoutException e) {
                    System.out.println("No se ha establecido ninguna conexión en los últimos 5 segundos. Cerrando la entrada de mas clientes.");
                    break;
                }
            }
            if(threadList.isEmpty()) {
                System.out.println("No se ha conectado ningun cliente. Cerrando el servidor.");
                System.exit(0);
            }
            for (ServerThread serverThread : threadList) {
                serverThread.start();
            }
        } catch (Exception e) {
            System.out.println("Main. Error:" + e.getMessage());
        }
    }

    private static String conseguirIP() {
        String ipPublica = "";
        try {
            URL url = new URL("https://httpbin.org/ip");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (linea.contains("\"origin\":")) {
                    int inicio = linea.indexOf("\"") + 1;
                    int fin = linea.lastIndexOf("\"");
                    ipPublica = linea.substring(inicio, fin);
                    break;
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ipPublica.substring(10);
    }
}