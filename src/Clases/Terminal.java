package clases;

import Utiles.SoutColores;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author OyarzunMatias
 */
public class Terminal {
    
    private int[] puestosEmbarque;
    private Tienda tienda;
    private char letraTerminal;
    private final AtomicInteger hora;
    
    public Terminal(char letra, int[] puestos, Tienda tienda, AtomicInteger hora){
        this.letraTerminal = letra;
        this.puestosEmbarque = puestos;
        this.tienda = tienda;
        this.hora = hora;
    }
    
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
    
    public synchronized void esperarVuelo(String nombrePasajero, Reserva reservaPasajero){
        while(this.hora.get() != reservaPasajero.getHoraVuelo()){
            try {
                this.wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(Terminal.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void abordarVueloEmbarque(String nombrePasajero, Reserva reservaPasajero){
        System.out.println("\t\t"+SoutColores.GREEN+" El pasajero: "+nombrePasajero+" esta abordando vuelo de la terminal: "+reservaPasajero.getTerminal().getLetra()+" en el embarque: "+reservaPasajero.getPuestoEmbarque()+", ya que esta por partir...");
    }
    
    public synchronized void actualizarHoraTerminal(){
        this.notifyAll();
    }
}
