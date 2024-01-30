package server.services.class_services;

import java.io.PrintWriter;
import java.util.List;

import server.interfaces.ObjectOperationsInterface;
import server.interfaces.ServiceManagerInterface;
import server.logic.User;
import server.ppal.ClientServiceThread;

/*
 * VERSIÓN DE Santiago Rodenas Herráiz, para PSP 22-23
 */
public class UsersList implements ServiceManagerInterface {

    /*
     *  @param pw (flujo salida), args (argumentos del comando), context (hilo que atiende al cliente)
     *  @return boolean true (correcto), false(no correcto)
     */
    @Override
    public boolean execute(PrintWriter pw, String[] args, ObjectOperationsInterface sharedResource, Thread context) {

        /*
         * Sólo se puede invocar a este servicio si está logueado.
         */
        if (!((ClientServiceThread)context).isLogged()){
            pw.println("Acción no permitidq. Debes estar registrado!!");
            pw.flush();
            return false;
        }

        /*
         * Recuperamos todos los usuarios y recuperamos todos los datos
         * de una forma sencilla.
         */
        List <User> list = sharedResource.GetAll();  //Ya tengo todos los usuarios

        String msg = ""; //Preparamos en un mismo String, todos los datos de todos los usuarios. A lo bestia. Esto podría ser problemático con un buen tamaño.
        for (User user : list )
            msg+="Id: " + user.getId()
                + " Nombre: " + user.getName()
                + " Email: " + user.getEmail()
                + " Passw: " + user.getPasswd()
                + "\n";
       
        
       
        pw.println(msg);        //Se lo mandamos el cliente.  --> cliente
        pw.flush();
        return true;
    }
    
}
