package server.services.class_services;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import server.interfaces.ObjectOperationsInterface;
import server.interfaces.ServiceManagerInterface;
import server.ppal.ClientServiceThread;
/*
 * VERSIÓN DE Santiago Rodenas Herráiz, para PSP 22-23
 * 
 * Esta clase, crea un proceso que devolverá el hash de un fichero
 * previamente existente.
 */
public class GetHashService implements ServiceManagerInterface{

    /*
     *  @param pw (flujo salida), args (argumentos del comando), context (hilo que atiende al cliente)
     *  @return boolean true (correcto), false(no correcto)
     */
    @Override
    public boolean execute(PrintWriter pw, String[] args, ObjectOperationsInterface sharedResource, Thread context) {

        // CertUtil -hashfile file.ext MD5
        if (args.length < 1){
            pw.println("Debes pasar el nombre del usuario");
            pw.flush();
            return false;
        }

        /*
         * Este servicio, sólo se puede invocar si el usuario está logueado
         * Esto lo controlamos mediante el contexto, es decir el hilo.
         */
        if (!((ClientServiceThread)context).isLogged()){
            pw.println("Acción no permitidq. Debes estar registrado!!");
            pw.flush();
            return false;
        }

      //  final String path="22_23/recuperacion_examen_final_socket_marzo_23/files/";  
      //  File file = new File(path + args[0] + ".dat");  //args[0] contiene el nombre del usuario
      
        File file = new File("server/files", args[0] + ".dat");
        final String absolutePathFile = file.getAbsolutePath();

        /*
         * Si el fichero existe y no es un Directorio, ejecuta el comando
         * que devuelve el hash de ese fichero.
         */
        if (file.exists() && !file.isDirectory()){
            final String [] cmd = {"CertUtil", "-hashfile", absolutePathFile, "MD5"};
           
            String  msg = "";
            ProcessBuilder pb = new ProcessBuilder(cmd);
    
            try{
                Process p = pb.start();
                
                try{
                    int codRet = p.waitFor();
                    InputStream is = p.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader br = new BufferedReader(isr);
                    String lineReciber = "";
                    while ( (lineReciber = br.readLine()) != null){
                        msg+=lineReciber;
                    }
                } catch(InterruptedException ex) {
                    pw.println("Error inesperado. Finalización interrumpida");  //Se lo mandamos el cliente.  --> cliente
                    pw.flush();
                    return false;
                }
            }
           
            catch (IOException e){
                e.printStackTrace();
                pw.println("Error de E/S.");  //Se lo mandamos el cliente.  --> cliente
                pw.flush();
                return false;
            }
    
            String [] hash = msg.split(":");  //separamos lo que nos devuelve el hash en array utilizando una separación de :
            String [] hash1 = hash[2].split("CertUtil"); //Separamos en dos partes a partir de CertUtil
            
            //Se lo mandamos el cliente.  --> cliente
            pw.println("Codigo Hash MD5 " + hash1[0]);  //Nos quedamos con la primera parte que contiene el hash y lo mandamos al flujo de salida.
            pw.flush();
            return true;
        }
        pw.println("No se ha encontrado el fichero");  //Se lo mandamos el cliente.  --> cliente
        pw.flush();
        return false;
       
    }

   
    
}
