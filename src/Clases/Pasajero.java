package Clases;

/**
 *
 * @author OyarzunMatias
 */
public class Pasajero extends Persona implements Runnable{
    
    private Reserva reserva;
    
    public Pasajero(int id, String nombre, Reserva reserva){
        super(id, nombre);
        this.reserva = reserva;
    }
    
    public void setReserva(Reserva reserva){
        this.reserva = reserva;
    }
    
    public Reserva getReserva(){
        return this.reserva;
    }
    
    @Override
    public void run(){
    }
}
