package server.services.class_services;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import server.interfaces.ObjectOperationsInterface;
import server.interfaces.ServiceManagerInterface;
import server.logic.User;
import server.ppal.ClientServiceThread;

/*
 * VERSIÓN DE Santiago Rodenas Herráiz, para PSP 22-23
 */
public class RegisterService implements ServiceManagerInterface{

   
    /*
     * Registra los datos recibidos como parámetro.
     */
    /*
     *  @param pw (flujo salida), args (argumentos del comando), context (hilo que atiende al cliente)
     *  @return boolean true (correcto), false(no correcto)
     */

    @Override
    public boolean execute(PrintWriter pw, String[] args, ObjectOperationsInterface sharedResource, Thread context) {
        /*
        Deberá comprar si existe ese usuario y en caso negativo, insertarlo.
        Debe comprobar que el número de argumentos sea al menos:
        nombre, email, passwd
        */

        if (args.length < 3){
            pw.println("Debes pasar el nombre, email y passw");
            pw.flush();
            return false;
        }

        /*
         * Verificamos si el usuario ya esta registrado comparando su email.
         */
        User u = (User)sharedResource
                .FindByEmail(args[1]);  //Buscamos por email

        if (u != null){
            pw.println("Ese usuario ya está registrado ");
            pw.flush();
            return false;
        }

        sharedResource.Add(new User(args));  //registramos el usuario   

        /*
         * Debemos de añadir su fichero con sus datos. 
         */
        try{


           // final String  path="22_23/recuperacion_examen_final_socket_marzo_23/files/";  
         //  final String path="server/files/";
         //   File file = new File(path + args[0] + ".dat");
          //  final String absolutePathFile = file.getAbsolutePath();
            File file = new File("server/files", args[0] + ".dat");
            
            FileWriter userFile = new FileWriter(file); //Creamos nuestro fichero con la ruta y nombre del usuario.dat
            PrintWriter pwf = new PrintWriter(userFile);        //utilizamos un PrintWriter sobre el fichero creado.
            String info = "Nombre: " +
                args[0] + ", Email: " +
                args[1] + ", Passwd: " +
                args[2];

            pwf.println("Datos del usuario " + info);  //escribimos la información en fichero
            pwf.close();
        }catch (Exception e){
           e.printStackTrace();
          //  pw.println("Error al crear fichero del usuario en registro");  //Se lo mandamos el cliente.  --> cliente
          //  pw.flush();
            return false;
        }

        pw.println ("Usuario registrado correctamente...");  //Se lo mandamos el cliente.  --> cliente
        pw.flush();
        ((ClientServiceThread)context).setLogged(true); //No tengo porqué loguearme después
      
        return true;
        
    }
    
}
