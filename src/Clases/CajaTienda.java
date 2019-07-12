package clases;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author OyarzunMatias
 */
public class CajaTienda {
    
    private int id;
    private final Semaphore caja;
    private static final int MAXCINTA = 30;
    private final ArrayBlockingQueue<Producto> cintaTransportadora;
    
    public CajaTienda(int id){
        this.id = id;
        this.caja = new Semaphore(1, true);
        this.cintaTransportadora = new ArrayBlockingQueue(MAXCINTA);
    }
    
    public void esperarCaja(String nombrePasajero){
        try {
            this.caja.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(CajaTienda.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("El pasajero: "+nombrePasajero+" ENTRO en la caja");
        
    }
    
    public void salirCaja(String nombrePasajero){
        System.out.println("El pasajero: "+nombrePasajero+" SALIO de la caja");
        this.caja.release();
    }
    
    public void ponerProductosCinta(ArrayBlockingQueue<Producto> carrito){
        int longCarro = carrito.size();
        for (int i = 0; i < longCarro; i++) {
            try {
                this.cintaTransportadora.put(carrito.take());
            } catch (InterruptedException ex) {
                Logger.getLogger(CajaTienda.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public Producto obtenerProductoCinta(){
        Producto producto = null;
        try {
            producto = this.cintaTransportadora.take();
        } catch (InterruptedException ex) {
            Logger.getLogger(CajaTienda.class.getName()).log(Level.SEVERE, null, ex);
        }
        return producto;
    }
    
    public void setId(int id){
        this.id = id;
    }
    
    public int getId(){
        return this.id;
    }
}
