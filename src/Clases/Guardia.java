package clases;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author OyarzunMatias
 */
public class Guardia extends Persona implements Runnable{
    
    private PuestoAtencion puesto;
    
    public Guardia(int id, String nombre){
        super(id, nombre);
    }
    
    public PuestoAtencion getPuesto(){
        return this.puesto;
    }
    
    public void setPuesto(PuestoAtencion puesto){
        this.puesto = puesto;
    }
    
    @Override
    public void run(){
        while(true){
            try {
                this.puesto.verificarPuesto();
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(Guardia.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
