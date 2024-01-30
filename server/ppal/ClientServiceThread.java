package server.ppal;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

import server.services.ServiceManager;

/*
 * VERSIÓN DE Santiago Rodenas Herráiz, para PSP 23-24
 * 
 * El hilo se encargará de invocar a los servicios que pida el cliente.
 * Deberá tener acceso al Recurso compartido.
 */
public class ClientServiceThread extends Thread{

    private Socket socket;  //Socker que ha conectado con el cliente.
    private PrintWriter pw; //Será el flujo de salida del hilo hacia el cliente  servidor --> cliente 
    private Scanner sc;     //Será el flujo de entrada del hilo desde el cliente  servidor <-- cliente
    private ServiceManager service;  //Administrador del servicio que invoca el cliente.
    private boolean logged  = false;  //El usuario por defecto no está logueado. 
    private boolean exit = false;  //Para el estado de cierre de conexión



    public ClientServiceThread (Socket socket, ServiceManager serviceManager){
        this.socket = socket;   //recibimos el socket desde el Server
        this.service = serviceManager;  //Gestor de servicios Rest
    }



    @Override
    public void run() {
        try{
           
            /*
             * Creamos los flujos de E/S para conectar con Cliente.
             */
            pw = new PrintWriter(socket.getOutputStream());
            sc = new Scanner(socket.getInputStream());
            System.out.println("Esperando respuesta cliente");


            while (sc.hasNextLine()) { //Mientras el cliente mande comandos...
              String line = sc.nextLine();
              InetAddress address = socket.getInetAddress();  //Sacamos información del cliente. El servidor recibirá muchos comandos.
              System.out.printf("Recibo comando desde %s:%d: %s%n", address.getHostAddress(), socket.getPort(), line);
      
              
              service.executeService(pw, line, this);  //Ejecutamos el comando recibido del cliente.  

              if (isExit()){ //En caso de que se haya cerrado la conexión por parte del cliente. Finalizamos el hilo.
                socket.close();
                pw.close();
                sc.close();
                System.exit(0);  //cerramos conexión sin errores.
              }
             
            }

        }
        catch (Exception e) {
            e.printStackTrace();
            try {
                socket.close();
            } catch (IOException ex) {
                System.out.println("Error inesperado de E/S por el servidor");
            }
        }
         
    }


    /*
     * Método que setea cuando se loguea el usuario.  Controlaremos el logueo desde el hilo, no desde el Usuario.
     */
    public boolean isLogged() {
        return this.logged;
    }

    /*
     * Método que devuelve si un usuario está logueado o no.
     */
    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    public boolean isExit(){
        return this.exit;
    }

    public void setExit(){
        this.exit = true;
    }
}