package Clases;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author OyarzunMatias
 */
public class Aeropuerto {
    
    private Aerolinea[] aerolineas;
    private Terminal[] terminales;
    
    private Tren tren;
    private String nombreAeropuerto;
    private AtomicInteger hora;
    
    public Aeropuerto(String nombre, Terminal[] terminales, Aerolinea[] aerolineas, Tren tren, AtomicInteger hora){
        this.nombreAeropuerto = nombre;
        this.terminales = terminales;
        this.aerolineas = aerolineas;
        this.tren = tren;
        this.hora = hora;
    }
    
    public void ingresarAeropuerto(){
        
    }

    public Aerolinea[] getAerolineas() {
        return aerolineas;
    }

    public void setAerolineas(Aerolinea[] aerolineas) {
        this.aerolineas = aerolineas;
    }

    public Terminal[] getTerminales() {
        return this.terminales;
    }

    public void setTerminales(Terminal[] terminales) {
        this.terminales = terminales;
    }

    public Tren getTren() {
        return this.tren;
    }

    public void setTren(Tren tren) {
        this.tren = tren;
    }

    public String getNombreAeropuerto() {
        return this.nombreAeropuerto;
    }

    public void setNombreAeropuerto(String nombreAeropuerto) {
        this.nombreAeropuerto = nombreAeropuerto;
    }

    public AtomicInteger getHora() {
        return this.hora;
    }

    public void setHora(AtomicInteger hora) {
        this.hora = hora;
    }
    
}
