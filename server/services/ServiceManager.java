package server.services;

import java.io.PrintWriter;
import java.util.HashMap;

import server.interfaces.ObjectOperationsInterface;
import server.interfaces.ServiceManagerInterface;
import server.ppal.ClientServiceThread;
import server.services.class_services.GetHashService;
import server.services.class_services.LoginService;
import server.services.class_services.LogoutService;
import server.services.class_services.RegisterService;
import server.services.class_services.UserData;
import server.services.class_services.UsersList;


/*
 *  VERSIÓN DE Santiago Rodenas Herráiz, para PSP 22-23
 * 
 * CLASE GESTOR DE OPERACIONES REST (servicios)
 * 'Esta clase es la gestora de todas las operaciones rest. Contendrá cada uno de
 * los objetos que represente una de las operaciones REST.'
 * Se encargará de llamar a las diferentes operaciones mediante objetos que implementen el
 * servicio a emular.
 * 
 * Los objetos de operaciones Rest, los contiene dentro de un HashMap, cuya clave será el Verbo.
 * 
 * Esta clase contiene un único método llamado execute, que se encargará de seleccionar el objeto
 * que emulará el servicio REST, dependiendo del verbo. Para llevarlo a cabo, necesita tener acceso al recurso compartido,
 * y el contexto del hilo que invoca a este objeto.
 */

public class ServiceManager {
    
    private final HashMap<String, ServiceManagerInterface> servicesMap;  //Contendrá los objetos operacion a ejecutar.
    private final ObjectOperationsInterface sharedResource; //Recurso compartido por todos los hilos de ejecución.



/*
 * Crea cada uno de los objetos que implementen los servicios.
 * Por cada servicio, un objeto.
 */
    public ServiceManager(ObjectOperationsInterface sharedResourceManager){
        servicesMap = new HashMap<>();
        servicesMap.put("reg" , new RegisterService());   //Creamos el servicio que registra.
        servicesMap.put("log", new LoginService());       //Creamos el servicio que loguea.
        servicesMap.put("datu", new UserData());          //Creamos el servicio que devuelve un usuario
        servicesMap.put("list", new UsersList());         //Creamos el servicio que devuelve la lista de usuarios
        servicesMap.put("hash5", new GetHashService());   //Creamos el servicio que devuelve el hash de un usuario
        servicesMap.put("fin", new LogoutService());      //Creamos el servicio que nos desloguea.
        this.sharedResource = sharedResourceManager;            //Aquí recibo el recurso compartido común para todos los hilos.
        
      
    }




    /*
     * Dependiendo de la línea recibida por el Cliente, deberá sacar el verbo
     * y los argumentos en un array. Despues llamar a la operación solicitada.
     * PrintWriter pw (el flujo de salida)
     * String line (la línea recibida con el vervo y los argumentos)
     * UserDataThread (Es el contexto o el hilo que invoca a este this)
     * Sería conveniente utilizar expresiones regex, en vez de split, pero en este ejemplo me puede valer.
     */
    public boolean executeService(PrintWriter pw, String line, ClientServiceThread context){
        String [] args = line.split(" ");  //separamos en líneas, ejemplo:  reg | <nombre> | <email> | <contraseña>
        
        try{
            String verb = args[0]; //extraemos el verbo del servicio.  reg, log, datu, etc..
            ServiceManagerInterface service = servicesMap.get(verb);  //Seleccionamos el Servicio

            /*
             * Comprobamos si existe un servicio invocado, en caso contrario informamos.
             */
            if (service == null){
                pw.println("Error, debes pasar un comando válido");  //Se lo mandamos el cliente.  --> cliente
                pw.flush();
                return false;
            }

            /*
             * Separamos tanto el verbo (servicio), como sus argumentos.
             */
            String [] operationsArgs = new String[args.length - 1];  //copiamos sólo argumentos.
            System.arraycopy(args, 1,  operationsArgs, 0, args.length - 1);

            /*
             * Invocamos al objeto cuyo verbo corresponde con el servicio, pasándole tanto el flujo de salida,
             * como los parámetros, el recurso compartido y el contexto de quien invoca el servicio.
             * Devolvemos true/false, dependiendo de si se ha obtenido un resultado OK.
             * Podríamos haberlo complicado algo más, con objetos de código 400 o de código 200.
             * 
             * Aplico polimorfismo.
             */
            return(service.execute(pw, operationsArgs, getSharedResource(), context));


        }catch(ArrayIndexOutOfBoundsException e){
            pw.println("Error, debes pasar la acción");  //Se lo mandamos el cliente.  --> cliente
            pw.flush();
            return false;
        }
       
    }

    private ObjectOperationsInterface getSharedResource() {
        return sharedResource;
    }
}
