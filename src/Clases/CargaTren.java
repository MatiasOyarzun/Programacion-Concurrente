package clases;

import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import utiles.SoutColores;

/**
 *
 * @author OyarzunMatias
 */
public class CargaTren {
    
    private int capacidadMaxTren;
    private final Semaphore subir;
    private final Semaphore bajar;
    private final Semaphore arrancar;
    private final Semaphore mutexCarga;
    
    public CargaTren(int capacidad){
        this.capacidadMaxTren = capacidad;
        this.subir = new Semaphore(this.capacidadMaxTren);
        this.bajar = new Semaphore(0);
        this.arrancar = new Semaphore(0);
        this.mutexCarga = new Semaphore(1);
    }
    
    public void setCapacidad(int capacidad){
        this.capacidadMaxTren = capacidad;
    }
    
    public int getCapacidad(){
        return this.capacidadMaxTren;
    }
    
    public void subirCargaTren(String nombrePasajero){
        try {
            this.subir.acquire();
            this.mutexCarga.acquire();
            System.out.println("\t\t\t\t\t\t\t"+SoutColores.BLUE_UNDERLINED+"El pasajero: "+nombrePasajero+" SUBIO a la carga del tren...");
            this.mutexCarga.release();
            this.arrancar.release();
        } catch (InterruptedException ex) {
            Logger.getLogger(CargaTren.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void bajarCargaTren(String nombrePasajero){
        try {
            this.mutexCarga.acquire();
            this.bajar.release();
            System.out.println("\t\t\t\t\t\t\t"+SoutColores.BLUE_UNDERLINED+"El pasajero: "+nombrePasajero+" BAJO de la carga del tren...");
            this.mutexCarga.release();
        } catch (InterruptedException ex) {
            Logger.getLogger(CargaTren.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void partirViaje(){
        try {
            this.arrancar.acquire(this.capacidadMaxTren);
            System.out.println("\t\t\t\t\t\t\t"+SoutColores.BLUE_UNDERLINED+"La carga esta LLENA, el tren puede partir viaje...");
        } catch (InterruptedException ex) {
            Logger.getLogger(CargaTren.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void volverViaje(){
        try {
            this.bajar.acquire();
            this.mutexCarga.acquire();
            System.out.println("\t\t\t\t\t\t\t"+SoutColores.BLUE_UNDERLINED+"La carga esta VACIA, cuando el tren llegue a la ultima terminal podra volver...");
            this.mutexCarga.release();
        } catch (InterruptedException ex) {
            Logger.getLogger(CargaTren.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void llegoOrigen(){
        try {
            this.mutexCarga.acquire();
            this.subir.release(this.capacidadMaxTren);
            this.mutexCarga.release();
        } catch (InterruptedException ex) {
            Logger.getLogger(CargaTren.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
