package Clases;

import Utiles.SoutColores;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author OyarzunMatias, Clase que modela una Cajera, y su funcionamiento, implementa la interfaz de Runnable y extiende de Persona
 */
public class CajeraTienda extends Persona implements Runnable{
    
    /*
    *   Variables: (y todas aquellas heredadas de la clase Persona, nombre e id)
    *   • caja: representa la caja, en la que atendera la cajera
    */
    private CajaTienda caja;
    
    //Constructor
    public CajeraTienda(int id, CajaTienda caja){
        super(id);
        this.caja = caja;
    }
    
    @Override
    //Metodo redefinido que ejecutara el hilo de la cajera
    public void run(){
        while(true){
            try {
                //Obtiene un producto de la cinta de la caja, y lo procesa
                Producto producto = this.caja.obtenerProductoCinta();
                Pasajero cliente = this.caja.getClienteActual();
                System.out.println("\t\t" + SoutColores.PURPLE +"La cajera: "+this.id+" de la caja: "+ caja.getId() +" esta PROCESANDO un producto del cliente: "+ cliente.getNombre() +"...");
                Thread.sleep(300);
                //Resta stock del producto
                producto.restarStock();
            } catch (InterruptedException ex) {
                Logger.getLogger(CajeraTienda.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /*
    *   Getters y Setters
    */
    public void setCaja(CajaTienda caja){
        this.caja = caja;
    }
    
    public CajaTienda getCaja(){
        return this.caja;
    }
}
