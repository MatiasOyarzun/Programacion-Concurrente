package Clases;

/**
 *
 * @author OyarzunMatias
 */
public class Guardia extends Persona implements Runnable{
    
    private PuestoAtencion puesto;
    
    public Guardia(int id){
        super(id);
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
            
        }
    }
}
