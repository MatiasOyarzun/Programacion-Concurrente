package clases;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import utiles.SoutColores;

/**
 *
 * @author OyarzunMatias
 */
public class PuestoAtencion {

    private static final int MAXPUESTOATENCION = 10;
    private int cantActualPuesto = 0, cantEspera = 0;
    private final Semaphore mutexPuesto = new Semaphore(1, true);
    private final Lock lock = new ReentrantLock(true);
    private final Condition esperaHall = this.lock.newCondition();
    private final Condition guardiaPuesto = this.lock.newCondition();
    private Guardia guardia;
    private String nombre;

    public PuestoAtencion(String nombrePuesto, Guardia guardia) {
        this.nombre = nombrePuesto;
        this.guardia = guardia;
    }

    public void entrarFilaPuesto(Pasajero pasajero) {
        this.lock.lock();
        try {
            this.cantEspera++;
            if(this.cantEspera == 1){
                this.guardiaPuesto.signal();
            }
            while (this.cantActualPuesto == MAXPUESTOATENCION){
                try {
                    System.out.println("t\t\t"+SoutColores.RED+"El pasajero: "+pasajero.getNombre()+" esta ESPERANDO en el hall de espera del puesto de atencion: "+this.nombre+"...");
                    this.esperaHall.await();
                } catch (InterruptedException ex) {
                    Logger.getLogger(PuestoAtencion.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            System.out.println("t\t\t"+SoutColores.RED+"El pasajero: "+pasajero.getNombre()+" COMENZO a hacer fila en el puesto de atencion: "+this.nombre+"...");
            this.cantEspera--;
            this.cantActualPuesto++;
        } finally {
            this.lock.unlock();
        }
    }
    
    public void entrarPuestoAtencion(Pasajero pasajero){
        try {
            this.mutexPuesto.acquire();
            System.out.println("\t\t\t"+SoutColores.RED+"El pasajero: "+pasajero.getNombre()+" comenzo a realizar el CHECK-IN en el puesto de atencion: "+this.nombre+"...");
        } catch (InterruptedException ex) {
            Logger.getLogger(PuestoAtencion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void salirPuestoAtencion(Pasajero pasajero) {
        this.lock.lock();
        try {
            System.out.println("\t\t\t"+SoutColores.RED+"El pasajero: "+pasajero.getNombre()+" termino de realizar el CHECK-IN en el puesto de atencion: "+this.nombre+"...");
            this.cantActualPuesto--;
            this.guardiaPuesto.signal();
            this.mutexPuesto.release();
        } finally {
            this.lock.unlock();
        }
    }

    public void verificarPuesto() {
        this.lock.lock();
        try {
            while(this.cantEspera == 0 || this.cantActualPuesto <= MAXPUESTOATENCION){
                try {
                    this.guardiaPuesto.await();
                } catch (InterruptedException ex) {
                    Logger.getLogger(PuestoAtencion.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            System.out.println("\t\t"+SoutColores.PURPLE+"El guardia: "+this.guardia.getNombre()+" dejara pasar un pasajero...");
            this.esperaHall.signal();
        } finally {
            this.lock.unlock();
        }
    }

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
