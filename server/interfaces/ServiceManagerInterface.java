package server.interfaces;
import java.io.PrintWriter;



/*
 * VERSIÓN DE Santiago Rodenas Herráiz, para PSP 23-24
 * 
 * ESTA CLASE, DEFINE LOS SERVICIOS QUE NOS OFRECE LA REST.
 * 
 * La idea es tener una Interfaz, que marqué el método execute que cada uno de los servicios deberá implementar.
 * 
 * PrintWriter pw (Flujo de salida para el intercambio de datos) con el cliente.
 * String[] args (argumentos del comando que manda el cliente). 
 * ObjectOperations  (recurso compartido). 
 * Thread context (Hilo que atiende al cliente).
 * 
 */

public interface ServiceManagerInterface {
    /*
     *  @param pw (flujo salida), args (argumentos del comando), context (hilo que atiende al cliente)
     *  @return boolean true (correcto), false(no correcto)
     * 
     */
    public boolean execute(PrintWriter pw, String [] args, ObjectOperationsInterface manager, Thread context);
}
