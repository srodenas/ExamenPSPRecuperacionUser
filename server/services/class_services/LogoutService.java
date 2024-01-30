package server.services.class_services;

import java.io.PrintWriter;

import server.interfaces.ObjectOperationsInterface;
import server.interfaces.ServiceManagerInterface;
import server.ppal.ClientServiceThread;

/*
 * VERSIÓN DE Santiago Rodenas Herráiz, para PSP 22-23
 */
public class LogoutService implements ServiceManagerInterface{
    /*
     *  @param pw (flujo salida), args (argumentos del comando), context (hilo que atiende al cliente)
     *  @return boolean true (correcto), false(no correcto)
     */
    @Override
    public boolean execute(PrintWriter pw, String[] args, ObjectOperationsInterface sharedResource, Thread context) {
        
        if (!((ClientServiceThread)context).isLogged()){
            pw.println("Acción no permitidq. Debes estar registrado!!");
            pw.flush();
            return false;
        }


        //modificamos el contexto con el login a falso.
        ((ClientServiceThread)context).setLogged(false);
        ((ClientServiceThread)context).setExit();
        return true;
    }
    
}
