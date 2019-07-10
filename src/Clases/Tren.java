package Clases;

/**
 *
 * @author OyarzunMatias
 */
public class Tren implements Runnable{
    
    private static final int CAPACIDADMAX = 5;
    private Terminal[] terminalesPorRecorrer;
    private String nombreTren;
    
    public Tren(String nombre, Terminal[] terminales){
        this.nombreTren = nombre;
        this.terminalesPorRecorrer = terminales;
    }
    
    @Override
    public void run(){
        
    }
    
    public Terminal[] getTerminales(){
        return this.terminalesPorRecorrer;
    }
    
    public void setTerminales(Terminal[] terminales){
        this.terminalesPorRecorrer = terminales; 
    }
    
    public void setNombre(String nombre){
        this.nombreTren = nombre;
    }
    
    public String getNombre(){
        return this.nombreTren;
    }
}
