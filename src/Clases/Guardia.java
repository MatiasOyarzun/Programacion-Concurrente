package Clases;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author OyarzunMatias, clase que modela un Guardia, hereda de Persona e implementa la interfaz Runnable
 */
public class Guardia extends Persona implements Runnable{
    
    /*
    *   Variables: (incluidas las de persona, nombre e id)
    *   • puesto: representa el puesto en el que el guardia debera controlar el paso de los pasajeros
    */
    private PuestoAtencion puesto;
    
    //Constructor
    public Guardia(int id, String nombre){
        super(id, nombre);
    }
    
    @Override
    //Metodo redefinido, que ejecutaran los hilos de guardia, cuando sean ejecutados
    public void run(){
        while(true){
            try {
                //Verifica puesto para indicar si puede pasar otro pasajero
                this.puesto.hacerPasarPasajero();
                Thread.sleep(200);
            } catch (InterruptedException ex) {
                Logger.getLogger(Guardia.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /*
    *   Getters y Setters
    */
    public PuestoAtencion getPuesto(){
        return this.puesto;
    }
    
    public void setPuesto(PuestoAtencion puesto){
        this.puesto = puesto;
    }
}
