package Clases;

import Utiles.SoutColores;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private boolean esHoraAtencion;
    
    public Aeropuerto(String nombre, Terminal[] terminales, Aerolinea[] aerolineas, Tren tren, AtomicInteger hora){
        this.nombreAeropuerto = nombre;
        this.terminales = terminales;
        this.aerolineas = aerolineas;
        this.tren = tren;
        this.hora = hora;
        this.esHoraAtencion = false;
    }
    
    public synchronized void ingresarAeropuerto(Pasajero nuevoPasajero){
       Reserva reservaPasajero = nuevoPasajero.getReserva();
       Aerolinea aerolineaPasajero = reservaPasajero.getAerolinea();
       PuestoAtencion puesto = aerolineaPasajero.getPuestoAtencion();
        while(!this.esHoraAtencion){
           try {
               this.wait();
           } catch (InterruptedException ex) {
               Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
           }
        }
        System.out.println("\t\t\t"+SoutColores.GREEN+"El pasajero: "+nuevoPasajero.getNombre()+" fue atendido en el aeropuerto...");
        puesto.
    }
    
    public synchronized void comenzarHorarioAtencion(){
        this.esHoraAtencion = true;
        this.notifyAll();
    }
    
    public synchronized void terminarHorarioAtencion(){
        this.esHoraAtencion = false;
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
