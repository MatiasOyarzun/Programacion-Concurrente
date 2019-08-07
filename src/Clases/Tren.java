package Clases;

import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import Utiles.SoutColores;

/**
 * @author OyarzunMatias, Clase que modela un tren y su funcionamiento, implementa la interfaz Runnable
 */
public class Tren implements Runnable {

    /*
    *   Variables:
    *   • terminalesPorRecorrer: array de Terminales, que representa a las terminales que va a recorrer el tren
    *   • cantTerminales: valor entero que representa la cantidad de terminales que hay para recorrer
    *   • nombreTren: representa el nombre del tren del aeropuerto
    *   • carga: representa los vagones del tren donde se subiran los pasajeros
    *   • semaforosTerminal: array de semaforos, para bloquear a los pasajeros mientra que todavia no hayan llegado a su terminal de destino
    *   • INICIO_ASCII: constante para representar el inicio del codigo ascii, para representar las letras de las terminales, utilizado al parsear un int a char
    */
    private Terminal[] terminalesPorRecorrer;
    private int cantTerminales;
    private String nombreTren;
    private CargaTren carga;
    private final Semaphore[] semaforosTerminal;
    private static final int INICIO_ASCII = 65;

    //Constructor
    public Tren(String nombre, Terminal[] terminales, CargaTren carga, int cantTerminales) {
        this.nombreTren = nombre;
        this.terminalesPorRecorrer = terminales;
        this.carga = carga;
        this.cantTerminales = cantTerminales;   
        this.semaforosTerminal = new Semaphore[this.cantTerminales];
        for (int i = 0; i < this.cantTerminales; i++) {
            this.semaforosTerminal[i] = new Semaphore(0);
        }
    }

    @Override
    //Metodo redefinido, que ejecutara el hilo del tren
    public void run() {
        while (true) {
            try {
                char letraTerminal;
                //Permite partir viaje cuando este lleno la cantidad maxima de pasajeros
                this.carga.partirViaje();
                Thread.sleep(1000);
                System.out.println("\n\t\t\t\t\t\t\t" + SoutColores.BLUE_UNDERLINED + "El tren: " + this.nombreTren + " COMENZO RECORRIDO...\n");
                //Repetitiva que permite pasar por cada terminal del aeropuerto
                for (int i = 0; i < this.cantTerminales; i++) {
                    letraTerminal = this.terminalesPorRecorrer[i].getLetra();
                    System.out.println("\t\t\t\t\t\t\t" + SoutColores.BLUE_UNDERLINED + "El tren: " + this.nombreTren + " esta PASANDO por la terminal: " + letraTerminal);
                    this.carga.pasarTerminal(letraTerminal, this.semaforosTerminal);
                    Thread.sleep(2500);
                }
                //Permite volver del viaje al tren en cuanto se hayan bajado todos los pasajeros
                this.carga.volverViaje();
                Thread.sleep(2500*this.cantTerminales);
                System.out.println("\n\t\t\t\t\t\t\t" + SoutColores.BLUE_UNDERLINED + "El tren: " + this.nombreTren + " ya regreso al punto de origen...\n");
                //Avisa que el tren ya llego al origen para partir un nuevo viaje
                this.carga.llegoOrigen();
            } catch (InterruptedException ex) {
                Logger.getLogger(Tren.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    //Metodo invocado por el pasajero para que pueda notificar a la terminal a la que debera ser trasladado por el tren, se queda bloqueado hasta que llega
    public void trasladarATerminal(char letraTerminal) {
        int cantTotal = this.cantTerminales+INICIO_ASCII;
        for (int i = INICIO_ASCII; i < cantTotal; i++) {
            if (letraTerminal == ((char) i)){
                try {
                    this.semaforosTerminal[(i-INICIO_ASCII)].acquire();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Tren.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /*
    *   Getters y Setters
    */
    public int getCantTerminales() {
        return this.cantTerminales;
    }

    public void setCantTerminales(int cantTerminales) {
        this.cantTerminales = cantTerminales;
    }

    public CargaTren getCarga() {
        return this.carga;
    }

    public void setCarga(CargaTren carga) {
        this.carga = carga;
    }

    public Terminal[] getTerminales() {
        return this.terminalesPorRecorrer;
    }

    public void setTerminales(Terminal[] terminales) {
        this.terminalesPorRecorrer = terminales;
    }

    public void setNombre(String nombre) {
        this.nombreTren = nombre;
    }

    public String getNombre() {
        return this.nombreTren;
    }
}