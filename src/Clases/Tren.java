package Clases;

/**
 *
 * @author OyarzunMatias
 */
public class Tren implements Runnable{
    
    private static final int CAPACIDADMAX = 20;
    private String nombreTren;
    
    public Tren(String nombre){
        this.nombreTren = nombre;
    }
    
    @Override
    public void run(){
        
    }
    
    public void setNombre(String nombre){
        this.nombreTren = nombre;
    }
    
    public String getNombre(){
        return this.nombreTren;
    }
}
