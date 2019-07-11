package clases;

import java.util.Random;
import utiles.SoutColores;
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
    private boolean esHoraAtencion;
    private final Random random;
    
    public Aeropuerto(String nombre, Terminal[] terminales, Aerolinea[] aerolineas, Tren tren){
        this.nombreAeropuerto = nombre;
        this.terminales = terminales;
        this.aerolineas = aerolineas;
        this.tren = tren;
        this.esHoraAtencion = false;
        this.random = new Random();
    }
    
    public synchronized PuestoAtencion ingresarAeropuerto(Pasajero nuevoPasajero){
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
        try {
            Thread.sleep(100*(this.random.nextInt(5)+1));
        } catch (InterruptedException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
        }
        return puesto;
    }
    
    public synchronized void comenzarHorarioAtencion(){
        System.out.println("\n\t\t\t\t\t\t\t\t\t"+SoutColores.PURPLE+"║ EL AEROPUERTO COMENZO SU HORARIO DE ATENCION!... ║\n");
        this.esHoraAtencion = true;
        this.notifyAll();
    }
    
    public synchronized void terminarHorarioAtencion(){
        this.esHoraAtencion = false;
        System.out.println("\n\t\t\t\t\t\t\t\t\t"+SoutColores.PURPLE+"║ EL AEROPUERTO FINALIZO SU HORARIO DE ATENCION!... ║\n");
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
    
}
