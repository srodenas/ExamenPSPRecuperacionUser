package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/*
 * VERSIÓN DE Santiago Rodenas Herráiz, para PSP 22-23
 * Cliente que solicita servicios a una servidor.
 * launch.json  --> localhost, 3000
 */
public class Client {
    public static Socket socket;
    public static void main(String[] args) {
       
        int port;
        InetAddress serverIp;
        String line;

        if (args.length < 2){
            System.out.println("Error en numero de argumentos, son 2 (ip-server y puerto)");
            System.exit(1);
        }

        port = Integer.parseInt(args[1]);
        try{
            socket = new Socket(args[0], port);
            serverIp=socket.getInetAddress();
            System.out.printf("Cliente conectado con servidor %s...%n",serverIp.getHostAddress());
            
            //Flujo de entrada del socket que conecta con el flujo de salida del servidor.
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
           
            //Flujo de salida del socket que conecta con el flujo de entrada del servidor.
            PrintWriter pw = new PrintWriter(socket.getOutputStream());
           
            //para la entrada de los comandos de la shell del cliente
            Scanner input = new Scanner(System.in);

            //El cliente no ha salido todavía
            boolean exit=false;
            String cmd="shell cliente>";

            //String para recoger la respuesta que le manda el servidor.
            String response;
            
            do{
                System.out.print(cmd + " ");

                //leemos la línea desde la shell.
                line = input.nextLine();

                //la mandamos al servidor por medio de su flujo de salida.
                pw.println(line);  

                //limpiamos el buffer para que se haga efectivo.
                pw.flush();

                //comprobamos si el cliente quiere salir.
                if (line.equals("fin")){
                    exit=true;  //no esperamos respuesta del servidor. Cerramos todo lo que no está escrito.
                    socket.close();
                    pw.close();
                    input.close();
                }else
                    do { 
                        //leemos del flujo de entrada. Es lo que le manda el servidor.
                        response = reader.readLine();
                        System.out.println(response);

                        //leemos mientras el buffer de entrada, tenga datos para leer y esté listo.
                        //Con reader.ready() a true, nos garantiza que la próxima lectura, no bloqueará la entrada
                      } 
                      while (reader.ready() && response != null);
                    
            }while(!exit);
            
            

        } //fin try
        catch (UnknownHostException ex){
            System.out.printf("Servidor desconocido %s%n", args[0]);
            ex.printStackTrace();
            System.exit(2);
        } 
        catch (IOException e){
            System.out.println("Error en flujo de E/S");
            e.printStackTrace();
            System.exit(3);    

        }

    } //fin main
} //fin clase
