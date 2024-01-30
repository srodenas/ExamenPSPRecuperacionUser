package server.services.class_services;

import java.io.PrintWriter;

import server.interfaces.ObjectOperationsInterface;
import server.interfaces.ServiceManagerInterface;
import server.logic.User;
import server.ppal.ClientServiceThread;

/*
 * VERSIÓN DE Santiago Rodenas Herráiz, para PSP 22-23
 */
public class UserData implements ServiceManagerInterface{

    /*
     *  @param pw (flujo salida), args (argumentos del comando), context (hilo que atiende al cliente)
     *  @return boolean true (correcto), false(no correcto)
     */
    @Override
    public boolean execute(PrintWriter pw, String[] args, ObjectOperationsInterface sharedResource, Thread context) {

        if (args.length < 1){
            pw.println("Debes pasar el email");
            pw.flush();
            return false;
        }
/*
 * Sólo se puede invocar si está registrado.
 */
        if (!((ClientServiceThread)context).isLogged()){
            pw.println("Acción no permitidq. Debes estar registrado!!");  //Se lo mandamos el cliente.  --> cliente
            pw.flush();
            return false;
        }

        User u = (User)sharedResource
        .FindByEmail(args[0]);  //Buscamos por email

        if (u == null){
            pw.println("Ese usuario no exixte ");    //Se lo mandamos el cliente.  --> cliente
        }
        else
            pw.println("Datos del usuario: " + u.toString());  //Se lo mandamos el cliente.  --> cliente

        pw.flush();
        return true;
    }
    
}
