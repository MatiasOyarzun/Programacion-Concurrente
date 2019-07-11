package clases;

import utiles.SoutColores;

/**
 *
 * @author OyarzunMatias
 */
public class Tren implements Runnable{
    
    private Terminal[] terminalesPorRecorrer;
    private String nombreTren;
    private CargaTren carga;
    
    public Tren(String nombre, Terminal[] terminales, CargaTren carga){
        this.nombreTren = nombre;
        this.terminalesPorRecorrer = terminales;
        this.carga = carga;
    }
    
    @Override
    public void run(){
        while(true){
            this.carga.partirViaje();
            System.out.println("\t\t\t\t\t\t\t"+SoutColores.BLUE_UNDERLINED+"El tren: "+this.nombreTren+" COMENZO RECORRIDO...");
        }
    }
    
    public CargaTren getCarga(){
        return this.carga;
    }
    
    public void setCarga(CargaTren carga){
        this.carga = carga;
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
