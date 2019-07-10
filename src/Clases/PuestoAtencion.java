package Clases;

import Utiles.SoutColores;
import java.util.concurrent.Semaphore;

/**
 *
 * @author OyarzunMatias
 */
public class PuestoAtencion {
    
    private static final int MAXPUESTOATENCION = 20;
    private Semaphore filaVacia = new Semaphore(MAXPUESTOATENCION, true);
    private Semaphore filaLlena = new Semaphore(0, true);
    private Semaphore mutex = new Semaphore(1);
    private Guardia guardia;
    private String nombre;
    
    public PuestoAtencion(String nombrePuesto, Guardia guardia){
        this.nombre = nombrePuesto;
        this.guardia = guardia;
    }
    
    public void entrarFila(Pasajero pasajero) throws InterruptedException{
        this.filaVacia.acquire();
        this.mutex.acquire();
        System.out.println("\t\t\t\t\t\t"+SoutColores.BLUE+"El pasajero: "+pasajero.getNombre()+" entro a la fila el puesto de atencion...");
    }
    
    public void dejarPasar(){
        
    }
    
    public String getNombre(){
        return this.nombre;
    }
    
    public void setNombre(String nuevoNombre){
        this.nombre = nuevoNombre;
    }
    
    public Guardia getGuardia(){
        return this.guardia;
    }
    
    public void setGuardia(Guardia nuevoGuardia){
        this.guardia = nuevoGuardia;
    }
}
