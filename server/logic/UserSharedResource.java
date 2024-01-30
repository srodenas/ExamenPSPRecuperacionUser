package server.logic;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import server.interfaces.ObjectOperationsInterface;
/*
 * *VERSIÓN DE Santiago Rodenas Herráiz, para PSP 23-24
 * 
 * CLASE SAFE.
 * Esta clase, ya implementa de las operaciones sobre un tipo de objeto en particular
 * Ese objeto ya lo identificamos a partir de User. Por tanto el genérico será de User.
 * SERÁ EL RECURSO COMPARTIDO ENTRE TODOS LOS HILOS QUE ATIENDAN A LOS DIFERENTES CLIENTES.
 * SI QUISIÉRAMOS EN VEZ DE USUARIOS OTRO TIPO DE OBJETOS, PODRÍAMOS INTEGRARLOS EN LOS MÉTODOS
 * DE LA FORMA: if (instanceof User){}else if (instanceof Client){} else ....
 * 
 * 1.- Necesitamos una lista de Usuarios.
 * 2.- Necesitamos contar los id de los nuevos usuarios.
 */


public class UserSharedResource<T> implements ObjectOperationsInterface<T>{
 
    private AtomicInteger automaticId; // Necesitamos una clave para el User
    private List<User> userList; //Aquí tenemos nuestra lista de Users.


    /*
     * Constructor que creará nuestro objeto AtomicInteger, para que nos dé una clave diferente.
     * Nuestra lista será un ArrayList de Usuarios.
     */
    public UserSharedResource(){

        automaticId = new AtomicInteger(0);
        userList = new ArrayList<User>();  //Nos creamos la lista de Usuarios.
     //   initialize();
     
    }




    /*
     * Implementamos nuestra operación de insertar un usuario. Al trabajar con genéricos, debemos
     * de castear a usuarios, siempre y cuando sea una instancia del mismo. Una mejora, sería tratar
     * una excepción en caso de que no fuera un User.
     * Deberá estar sincronizado, para asegurar la EXCLUSIÓN MÚTUA al recurso compartido.
     */
    @Override
    synchronized public void Add(T u) {

       if (u instanceof User){  //Como es una instancia de usuario, puedo hacer el casteo sin problemas.
        User user = (User) u;
        user.setId(automaticId.incrementAndGet());  //pido un nuevo id único.
        userList.add(user); //inserto en nuestra lista de este recurso compartido el nuevo usuario.
       }    

    }




    /*
     * A partir de un entero, devolvemos el User.
     * Es importante darse cuenta, que filtraremos por id, encontrando
     * la primera instancia que coincida y en caso de que no se encuentre, 
     * devolver un null. Utilizamos una expresión lambda. El método debe
     * devolver un T, por tanto lo generalizamos a T.
     * Aseguramos la EXCLUSIÓN MÚTUA.
     */

    @Override
    synchronized  public T Get(int i) {

        User user = userList
            .stream()
            .filter(u -> u.getId()==i)
            .findFirst()
            .orElse(null);
        return (T)user;

    }




    /*
     * Devolvemos todos los usuarios que tengamos.
     * Aseguramos la EXCLUSIÓN MÚTUA
     */
    @Override
    synchronized  public List<T> GetAll() {
       return (List<T>) userList;
    }





    /*
     * Devolvemos un User, donde coincida su email.
     * Utilizamos expresión lambda.
     * Aseguramos la EXCLUSIÓN MÚTUA.
     */
    @Override
    synchronized  public T FindByEmail(String email) {

        User user = userList
            .stream()
            .filter(u -> u.getEmail().equals(email))
            .findFirst()
            .orElse(null);
        
        return (T) user;

    }





    /*
     * Lo mismo que el anterio método, sólo que añadimos una operación
     * and en la expresión lambda
     */
    @Override
    synchronized  public T FindByEmailAndPassw(String email, String pass) {

        User user = userList.stream()
        .filter(
            (u) -> u.getEmail().equals(email) && 
                    u.getPasswd().equals(pass))
        .findFirst()
        .orElse(null);

        return (T) user;

    }

    
    


    /*
     * Para pruebas
     */

   /*  private void initialize(){
        userList.add(new User("Santi", "srodenashe@gmail.com", "santi"));
        userList.add(new User("Sonia", "smenadel@gmail.com", "sonia"));
        userList.add(new User("David", "drodehe@gmail.com", "david"));
    }
*/
    
    
}
