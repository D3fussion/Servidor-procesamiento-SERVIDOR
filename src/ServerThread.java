import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
/**
 * @author Angel Laureano Borquez Fimbres (221210626)
 * @author Juan Diego Quijada Castillo (222206011)
 * @author Aaron Velez Coronado (222206601)
 */
public class ServerThread extends Thread {
    private final Socket socket;
    private final ArrayList<ServerThread> threadList;
    private final String nombre_thread;
    private PrintWriter output;
    
    public ServerThread(Socket socket, ArrayList<ServerThread> threads, String nombre_thread) {
        this.socket = socket;
        this.threadList = threads;
        this.nombre_thread = nombre_thread;
    }
    @Override
    public void run() {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Con este recibe los mensajes
            output = new PrintWriter(socket.getOutputStream(),true); // Con este envia los mensajes
            currentThread().setName(nombre_thread);
            output.println(nombre_thread); //Aqui le manda su nombre al cliente
            output.println(threadList.size()); //Aqui le manda el numero de maquinas al cliente

            String respuesta = input.readLine(); // Recibe la respuesta del cliente
            if(respuesta.equals("y")) {
                System.out.println(">>>>>>>>>>Marcador 1");
                printToAllClients(nombre_thread);
            }
            String s = input.readLine();
            System.out.println(">>>>>>>>>>Marcador 2 " + s);
            buscarUsuario(s + " Listo");
            if(respuesta.equals("y")){
                for (int i = 0; i < threadList.size(); i++) {
                    String recibido = input.readLine(); // Recibe los urls para cada cliente
                    buscarUsuario(recibido);
                }
            }

            StringBuffer listaDePalabrasRepetidas = new StringBuffer();
            String linea;
            while ( (linea= input.readLine()) !=null ) {
                printToAllClients(linea);
                listaDePalabrasRepetidas.append("\n").append(linea);
            }
        } catch (Exception e) {
            System.out.println("Se a desconectado el cliente" + nombre_thread);
        }
    }
    private void buscarUsuario(String urls) {
        for (int j = 0; j < threadList.size(); j++) {
            String[] respuesta_array = urls.split(" "); // Convierte la respuesta en un array
            StringBuilder mensaje = new StringBuilder();

            for (int i = 1; i < respuesta_array.length; i++) {
                mensaje.append(respuesta_array[i]).append(" ");
            }

            for (ServerThread serverThread : threadList) {
                if (serverThread.getName().equals(respuesta_array[0])) {
                    serverThread.output.println(mensaje);
                }
            }
        }
    }
    private void printToAllClients(String outputString) {
        for (ServerThread serverThread : threadList) {
            serverThread.output.println(outputString);
        }
    }

}
