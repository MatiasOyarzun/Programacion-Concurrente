package clases;

import utiles.SoutColores;
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
               System.out.println("\t\t\t"+SoutColores.GREEN+"El pasajero: "+nuevoPasajero.getNombre()+" esta ESPERANDO horario atencion...");
               this.wait();
           } catch (InterruptedException ex) {
               Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
           }
        }
        System.out.println("\t\t\t"+SoutColores.GREEN+"El pasajero: "+nuevoPasajero.getNombre()+" INGRESO al puesto de informes del aeropuerto...");
    }
    
    public synchronized void comenzarHorarioAtencion(){
        System.out.println("\t"+SoutColores.RED+"EL AEROPUERTO COMENZO SU HORARIO DE ATENCION...");
        this.esHoraAtencion = true;
        this.notifyAll();
    }
    
    public synchronized void terminarHorarioAtencion(){
        this.esHoraAtencion = false;
        System.out.println("\t"+SoutColores.RED+"EL AEROPUERTO FINALIZO SU HORARIO DE ATENCION...");
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
