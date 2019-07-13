/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import Utiles.SoutColores;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author OyarzunMatias
 */
public class CajeraTienda extends Persona implements Runnable{
    
    private CajaTienda caja;
    
    public CajeraTienda(int id, CajaTienda caja){
        super(id);
        this.caja = caja;
    }
    
    public void setCaja(CajaTienda caja){
        this.caja = caja;
    }
    
    public CajaTienda getCaja(){
        return this.caja;
    }
    
    @Override
    public void run(){
        while(true){
            try {
                Producto producto = this.caja.obtenerProductoCinta();
                System.out.println("\t\t" + SoutColores.PURPLE +"La cajera: "+this.id+" esta PROCESANDO un producto...");
                Thread.sleep(200);
                producto.restarStock();
            } catch (InterruptedException ex) {
                Logger.getLogger(CajeraTienda.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
