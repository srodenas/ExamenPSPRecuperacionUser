package server.ppal;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import server.interfaces.ObjectOperationsInterface;
import server.logic.UserSharedResource;
import server.services.ServiceManager;

/*
 * VERSIÓN DE Santiago Rodenas Herráiz, para PSP 23-24
 * 
 * 1.- Creará el administrador de Servicios (service) de nuestra Rest (Lo que puede hacer el cliente) 
 * 2.- Creará el recurso compartido (userManager) a todos los hilos. (Las operaciones que podemos hacer con los objetos a tratar)
 * 3.- Creará un hilo por petición de cliente. Cuando se atienda la conexión, nuevo hilo.
 */


public class Server {
    private static ServiceManager service;  //Gestor de servicio rest. (Lo que puede hacer el cliente)
    int port;   //puerto a la escucha.


    public static void main(String[] args) {
        int port = -1; 


        //Probamos que se le pase un parámetro puerto.
        if (args.length == 0) {
            System.out.println("Debes pasar el puerto a escuchar");
            System.exit(1);  //Cerramos conexión con errores.
        }


        //Comprobamos que el puerto sea un entero.
        try{
            port = Integer.parseInt(args[0]);
        }catch(NumberFormatException e){
            System.out.println("Error en el tipo puerto");
            System.exit(2);  //Cerramos conexión con errores.
        }

        Server server = new Server();  //Creamos nuestro Servidor.
        server.configure(port);         //Configuramos el servidor.
        server.clientListen();          //Lo ponemos a la escucha.
       
    }



    public void configure(int port){

        this.port = port; //nos quedamos con el puerto.
        final ObjectOperationsInterface USER_MANAGER = new UserSharedResource<>(); //Recurso compartido. Métodos de manipulación con los usuarios.
        service = new ServiceManager(USER_MANAGER);  //Servicios que el cliente puede solicitar. Necesitamos pasarle el recurso compartido.

    }





    public void clientListen(){
        System.out.println("Servidor a la escucha del puerto  " + port);
        System.out.println("Esperando conexión ......");
    
        try (ServerSocket serverSocket = new ServerSocket(port)) { //Creamos nuestro socket de servidor, para servir a clientes.
            while (true) {  
                Socket socketClient = serverSocket.accept(); //Aceptamos conexión con cliente
                System.out.printf("Establecida conexión con %s:%d%n",
                    socketClient.getInetAddress(),
                    socketClient.getPort()
                );  //información del cliente (su ip y puerto origen) para contestarle)
        
                new ClientServiceThread(socketClient,  service).start(); //Aquí creamos el hilo de ejecución y atiende al cliente.
            }

        } catch (IOException e) {
             e.printStackTrace();
        }

    }
 
}
