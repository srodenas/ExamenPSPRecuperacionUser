package server.services.class_services;

import java.io.PrintWriter;

import server.interfaces.ObjectOperationsInterface;
import server.interfaces.ServiceManagerInterface;
import server.logic.User;
import server.ppal.ClientServiceThread;

/*
 * VERSIÓN DE Santiago Rodenas Herráiz, para PSP 22-23
 */
public class LoginService implements ServiceManagerInterface{
   
    /*
     *  @param pw (flujo salida), args (argumentos del comando), context (hilo que atiende al cliente)
     *  @return boolean true (correcto), false(no correcto)
     */
    @Override
    public boolean execute(PrintWriter pw, String[] args, ObjectOperationsInterface sharedResource,  Thread context) {

        if (args.length < 2){
            pw.println("Debes pasar el email y passw");  //Se lo mandamos el cliente.  --> cliente
            pw.flush();
            return false;
        }


        User user = (User)sharedResource
                .FindByEmailAndPassw(args[0], args[1]);  //Buscamos por email

        
        if(user == null){
            pw.println("Usuario no encontrado");  //Se lo mandamos el cliente.  --> cliente
            pw.flush();
            return false;
        }
        else{
            pw.println("Usuario logueado correctamente ");  //Se lo mandamos el cliente.  --> cliente
            pw.flush();
            ((ClientServiceThread)context).setLogged(true);
            return true;
        }
               
        
    }
    
}
