package Clases;

import java.util.Random;
import Utiles.SoutColores;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author OyarzunMatias, Clase que modela el Aeropuerto y sus funcionalidades.
 */
public class Aeropuerto {
    
    /*
    *   Variables:
    *   • aerolineas: array de aerolineas, las cuales estaran en el aeropuerto
    *   • terminales: array de terminales, las cuales estaran en el aeropuerto
    *   • tren: tren que llevara pasajeros entre las terminales
    *   • nombreAeropuerto: nombre del aeropuerto al que visitaran los pasajeros
    *   • esHoraAtencion: booleano, que representa si es el horario en que los puestos de atencion comienzan a trabajar
    *   • random: random, que permite utilizarlo en el momento en el que sean necesario, para crear valores aleatorios
    */
    private Aerolinea[] aerolineas;
    private Terminal[] terminales;
    private Tren tren;
    private String nombreAeropuerto;
    private boolean esHoraAtencion;
    private final Random random;
    
    //Constructor
    public Aeropuerto(String nombre, Terminal[] terminales, Aerolinea[] aerolineas, Tren tren){
        this.nombreAeropuerto = nombre;
        this.terminales = terminales;
        this.aerolineas = aerolineas;
        this.tren = tren;
        this.esHoraAtencion = false;
        this.random = new Random();
    }
    
    /*
    *   Metodo que permite que los pasajeros esperen mientras que todavia no sea el horario de atencion a los pasajeros (mediante el uso de monitores), luego le 
    *   devuelve el puesto de atencion de la aerolinea en la que sera atendido
    */
    public synchronized PuestoAtencion ingresarAeropuerto(Pasajero nuevoPasajero){
        //Declaracion variables
       Reserva reservaPasajero = nuevoPasajero.getReserva();
       Aerolinea aerolineaPasajero = reservaPasajero.getAerolinea();
       PuestoAtencion puesto = aerolineaPasajero.getPuestoAtencion();
       //Mientras que no sea el horario de atencion, el pasajero esperara
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
            Thread.sleep(100*(this.random.nextInt(3)+1));
        } catch (InterruptedException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
        }
        return puesto;
    }
    
    //Metodo sincronizado para que cuando comience el horario de atencion, les notifique a todos aquellos pasajeros bloqueados
    public synchronized void comenzarHorarioAtencion(){
        System.out.println("\n\t\t\t\t\t\t\t\t\t"+SoutColores.PURPLE+"║ EL AEROPUERTO COMENZO SU HORARIO DE ATENCION!... ║\n");
        this.esHoraAtencion = true;
        this.notifyAll();
    }
    
    //Metodo sincronizado para que finalice el horario de atencion del aeropuerto
    public synchronized void terminarHorarioAtencion(){
        this.esHoraAtencion = false;
        System.out.println("\n\t\t\t\t\t\t\t\t\t"+SoutColores.PURPLE+"║ EL AEROPUERTO FINALIZO SU HORARIO DE ATENCION!... ║\n");
    }

    /*
    *   Getters y Setters
    */
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