package Clases;
/**
 *
 * @author OyarzunMatias
 */
public class PuestoAtencion {
    
    private static final int MAXPUESTOATENCION = 20;
    private Guardia guardia;
    private String nombre;
    
    public PuestoAtencion(String nombrePuesto, Guardia guardia){
        this.nombre = nombrePuesto;
        this.guardia = guardia;
    }
    
    public String getNombre(){
        return this.nombre;
    }
    
    public void setNombre(String nuevoNombre){
        this.nombre = nuevoNombre;
    }
    
    public Guardia getGuardia(){
        return this.guardia;
    }
    
    public void setGuardia(Guardia nuevoGuardia){
        this.guardia = nuevoGuardia;
    }
}
