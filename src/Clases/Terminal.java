package Clases;

import Utiles.SoutColores;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author OyarzunMatias, Clase que modela una Terminal y su funcionamiento
 */
public class Terminal {
    
    /*
    *   Variables:
    *   • puestosEmbarque: representa los puestos en los que los pasajeros, subiran a su vuelo
    *   • tienda: representa la tienda que hay en cada terminal, donde los pasajeros podran comprar Productos
    *   • letraTerminal: nombre de la terminal del aeropuerto
    *   • hora: representa la hora del dia en la terminal, para verificar si los pasajeros ya pueden subir a su vuelo
    */
    private int[] puestosEmbarque;
    private Tienda tienda;
    private char letraTerminal;
    private final AtomicInteger hora;
    
    //Constructor
    public Terminal(char letra, int[] puestos, Tienda tienda, AtomicInteger hora){
        this.letraTerminal = letra;
        this.puestosEmbarque = puestos;
        this.tienda = tienda;
        this.hora = hora;
    }
    
    /*
    *   Getters y Setters
    */
    public int[] getPuestos(){
        return this.puestosEmbarque;
    }
    
    public void setPuestos(int[] puestos){
        this.puestosEmbarque = puestos;
    }
    
    public char getLetra(){
        return this.letraTerminal;
    }
    
    public void setLetra(char letraTerminal){
        this.letraTerminal = letraTerminal;
    }
    
    public void setTienda(Tienda tienda){
        this.tienda = tienda;
    }
    
    public Tienda getTienda(){
        return this.tienda;
    }
    
    //Metodo que permite a los pasajeros esperar por su vuelo en la terminal (mediante el uso de monitores)
    public synchronized void esperarVuelo(String nombrePasajero, Reserva reservaPasajero){
        //Mientras no sea la hora del viaje del pasajero, este debera esperar
        while(!(this.hora.compareAndSet(reservaPasajero.getHoraVuelo(), this.hora.addAndGet(0)))){
            try {
                this.wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(Terminal.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    //Metodo que permite mostrar por pantalla que el pasajero ya se pudo subir a su vuelo
    public void abordarVueloEmbarque(String nombrePasajero, Reserva reservaPasajero){
        System.out.println("\t\t"+SoutColores.GREEN+" El pasajero: "+nombrePasajero+" esta abordando vuelo de la terminal: "+reservaPasajero.getTerminal().getLetra()+" en el embarque: "+reservaPasajero.getPuestoEmbarque()+", ya que esta por partir...");
    }
    
    //Metodo que permite actualizar la hora de la terminal, notificando a todos los pasajeros que esten bloqueados, para que verifiquen si ya deben subir a su vuelo
    public synchronized void actualizarHoraTerminal(){
        this.notifyAll();
    }
}
