package Clases;

import Utiles.SoutColores;
import java.util.concurrent.Semaphore;

/**
 *
 * @author OyarzunMatias
 */
public class PuestoAtencion {

    private static final int MAXPUESTOATENCION = 10;
    private Semaphore puestoVacio = new Semaphore(MAXPUESTOATENCION, true);
    private Semaphore mutex = new Semaphore(1);
    private Guardia guardia;
    private String nombre;

    public PuestoAtencion(String nombrePuesto, Guardia guardia) {
        this.nombre = nombrePuesto;
        this.guardia = guardia;
    }

    public void entrarFila(Pasajero pasajero) throws InterruptedException {
        System.out.println("\t\t\t\t\t\t" + SoutColores.BLUE + "El pasajero: " + pasajero.getNombre() + " se encuentra en el hall central en espera...");
        this.puestoVacio.acquire();
        this.mutex.acquire();
        System.out.println("\t\t\t\t\t\t" + SoutColores.BLUE + "El pasajero: " + pasajero.getNombre() + " entro a la fila el puesto de atencion...");
        this.mutex.release();
    }
    
    public void salirFila(Pasajero pasajero) throws InterruptedException{
        this.mutex.acquire();
        System.out.println("\t\t\t\t\t\t" + SoutColores.BLUE + "El pasajero: " + pasajero.getNombre() + " ya fue atendido va a salir del puesto de atencion...");
        
    }

    public void dejarPasar() {

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
