package clases;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author OyarzunMatias
 */
public class Pasajero extends Persona implements Runnable{
    
    private final Random random;
    private Reserva reserva;
    private Aeropuerto aeropuerto;
    
    public Pasajero(int id, String nombre, Reserva reserva, Aeropuerto aeropuerto){
        super(id, nombre);
        this.reserva = reserva;
        this.aeropuerto = aeropuerto;
        this.random = new Random();
    }
    
    public void setReserva(Reserva reserva){
        this.reserva = reserva;
    }
    
    public Reserva getReserva(){
        return this.reserva;
    }
    
    public void setAeropuerto(Aeropuerto aeropuerto){
        this.aeropuerto = aeropuerto;
    }
    
    public Aeropuerto getAeropuerto(){
        return this.aeropuerto;
    }
    
    @Override
    public void run(){
        try {
            Tren trenInterno = this.aeropuerto.getTren();
            PuestoAtencion puesto = this.aeropuerto.ingresarAeropuerto(this);
            Thread.sleep(100*(this.random.nextInt(10)+1));
            puesto.entrarFilaPuesto(this.nombre);
            Thread.sleep(200*(this.random.nextInt(10)+1));
            puesto.entrarPuestoAtencion(this.nombre);
            Thread.sleep(100*(this.random.nextInt(5)+1));
            puesto.salirPuestoAtencion(this.nombre);
        } catch (InterruptedException ex) {
            Logger.getLogger(Pasajero.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
