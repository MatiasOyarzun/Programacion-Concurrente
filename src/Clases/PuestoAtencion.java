package clases;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import Utiles.SoutColores;
import java.util.ArrayList;

/**
 *
 * @author OyarzunMatias, Clase que simula un puesto de atencion y su funcionamiento
 */
public class PuestoAtencion {

    /*
    *   Variables:
    *   • MAXPUESTOATENCION: constante que indica la cantidad maxima que pueden haber en el puesto de atencion
    *   • colaPrioridad: array list que permite seguir el orden de llegada a la fila del puesto de atencion
    *   • mutexPuesto: semaforo que garantiza la exclusion mutua de la seccion critica
    *   • lock: ReentrantLock que permite la exclusion mutua de la seccion critica
    *   • esperaHall: Condition que permite bloquear a los pasajeros, cuando ya no haya espacio en el puesto de atencion
    *   • esperaFila: Condition que permite bloquear a los pasajeros, cuando estan esperando en la fila, hasta que sean llamados
    *   • guardiaPuesto: Condition que permite bloquear al guardia, mientras que no haya alcanzado el maximo de gente en el puesto o no haya nadie esperando
    *   • guardia: Guardia que sera el encargado de controlar a los pasajeros, para que pasen a la fila
    *   • nombre: nombre del puesto de atencion
    */
    private static final int MAXPUESTOATENCION = 2;
    private int cantActualPuesto = 0, cantEspera = 0;
    private final ArrayList<String> colaPrioridad;
    private final Semaphore mutexPuesto = new Semaphore(1);
    private final Lock lock;
    private final Condition esperaHall;
    private final Condition esperaFila;
    private final Condition guardiaPuesto;
    private Guardia guardia;
    private String nombre;

    //Constructor
    public PuestoAtencion(String nombrePuesto, Guardia guardia) {
        this.nombre = nombrePuesto;
        this.guardia = guardia;
        this.colaPrioridad = new ArrayList();
        this.lock = new ReentrantLock(true);
        this.esperaHall = this.lock.newCondition();
        this.esperaFila = this.lock.newCondition();
        this.guardiaPuesto = this.lock.newCondition();
    }
    
    /*
    *   Metodo que permite a un pasajero entrar a la fila del puesto de atencion, en caso de que no haya espacio, se queda esperando en el hall
    *   luego es añadido a la cola de prioridad, para luego obtenerlos en un orden adecuado de llegada
    */
    public void entrarFilaPuesto(String nombrePasajero) {
        this.lock.lock();
        try {
            //Aumenta la cantidad de pasajeros que estan esperando
            this.cantEspera++;
            //Si la cantidad de pasajeros que estan esperando es uno, se le avisa al guardia para que verifique si puede hacer pasar a alguien a la fila
            if(this.cantEspera == 1){
                this.guardiaPuesto.signal();
            }
            //Mientras que la fila este llena el pasajero esperara en el hall
            while (this.cantActualPuesto == MAXPUESTOATENCION){
                try {
                    System.out.println("\t\t\t\t\t"+SoutColores.RED+"El pasajero: "+nombrePasajero+" esta ESPERANDO en el [HALL DE ESPERA] del puesto de atencion: "+this.nombre+"...");
                    this.esperaHall.await();
                } catch (InterruptedException ex) {
                    Logger.getLogger(PuestoAtencion.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            //Se desocupo la fila, entonces ingresa en la fila y se agrega en la cola de prioridad, por lo tanto, se disminuye uno porque no esta esperando mas, y se aumenta la cantidad que estan en el puesto
            System.out.println("\t\t\t\t\t"+SoutColores.RED+"El pasajero: "+nombrePasajero+" COMENZO a hacer fila en el puesto de atencion: "+this.nombre+"...");
            this.colaPrioridad.add(nombrePasajero);
            this.cantEspera--;
            this.cantActualPuesto++;
        } finally {
            this.lock.unlock();
        }
    }
    
    /*
    *   Metodo que permite a un pasajero entrar al puesto de atencion, siempre y cuando sea el primero en la cola de prioridad
    */
    public void entrarPuestoAtencion(String nombrePasajero){
        this.lock.lock();
        try {
            //Si no esta primero en la fila espera
            while(!(this.colaPrioridad.get(0).equals(nombrePasajero))){
                this.esperaFila.await();
            }
            //Lo remuevo de la fila
            this.colaPrioridad.remove(0);
            this.mutexPuesto.acquire();
            System.out.println("\t\t\t\t\t"+SoutColores.RED+"El pasajero: "+nombrePasajero+" comenzo a realizar el CHECK-IN en el puesto de atencion: "+this.nombre+"...");
        } catch (InterruptedException ex) {
            Logger.getLogger(PuestoAtencion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //Metodo que permite a un pasajero salir del puesto de atencion, y notifica a los que estan en la fila para que puedan pasar y al guardia para que le de paso a alguien mas
    public void salirPuestoAtencion(String nombrePasajero) {
        try {
            System.out.println("\t\t\t\t\t"+SoutColores.RED+"El pasajero: "+nombrePasajero+" termino de realizar el CHECK-IN en el puesto de atencion: "+this.nombre+"...");
            //Salio alguien del puesto, por lo tanto disminuye
            this.cantActualPuesto--;
            //Notifica a los que estan en la fila
            this.esperaFila.signal();
            //Notifica al guardia, porque se libero espacio de la fila
            this.guardiaPuesto.signal();
            this.mutexPuesto.release();
        } finally {
            this.lock.unlock();
        }
    }
    
    /*
    *   Metodo que permite al guardia verificar el puesto de atencion, para  ver si puede hacer pasar a algun pasajero del hall central
    */
    public void verificarPuesto() {
        this.lock.lock();
        try {
            //Mientras que no haya nadie esperando o este lleno el puesto, el guardia espera
            while(this.cantEspera == 0 || this.cantActualPuesto == MAXPUESTOATENCION){
                try {
                    this.guardiaPuesto.await();
                } catch (InterruptedException ex) {
                    Logger.getLogger(PuestoAtencion.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            System.out.println("\t\t"+SoutColores.PURPLE+"El guardia: "+this.guardia.getNombre()+" del puesto: "+this.nombre+" dejara pasar un pasajero...");
            this.esperaHall.signal();
        } finally {
            this.lock.unlock();
        }
    }
    
    /*
    *   Getters y Setters
    */
    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nuevoNombre) {
        this.nombre = nuevoNombre;
    }

    public Guardia getGuardia() {
        return this.guardia;
    }

    public void setGuardia(Guardia nuevoGuardia) {
        this.guardia = nuevoGuardia;
    }
}