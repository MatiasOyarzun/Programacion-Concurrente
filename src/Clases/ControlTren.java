package Clases;

import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import Utiles.SoutColores;

/*
 * @author OyarzunMatias, Clase que modela la carga o vagones del tren
 */
public class ControlTren {

    /*
    *   Variables:
    *   • capacidadMaxTren: representa la capacidad maxima de pasajeros que se pueden subir
    *   • subir: semaforo que representa si se pueden o no subir los pasajeros
    *   • bajar: semaforo que representa si se pueden o no bajar los pasajeros
    *   • arrancar: semaforo que representa si el tren puede arrancar su recorrido o no
    *   • mutexCarga: semaforo que permite la exclusion mutua de las secciones criticas
    *   • cantPasajerosXTerminal: array que representa la cantidad de pasajeros que iran a cada terminal
    *   • cantTerminales: representa la cantidad de terminales que hay en el aeropuerto
    *   • INICIO_ASCII: constante que representa el inicio de codigo ascii, al momento de parsear un int a char
    */
    private int capacidadMaxTren;
    private final Semaphore subir;
    private final Semaphore bajar;
    private final Semaphore arrancar;
    private final Semaphore mutexCarga;
    private final int[] cantPasajerosXTerminal;
    private int cantTerminales;
    private static final int INICIO_ASCII = 65;

    //Constructor
    public ControlTren(int capacidad, int cantTerminales) {
        this.capacidadMaxTren = capacidad;
        this.subir = new Semaphore(this.capacidadMaxTren);
        this.bajar = new Semaphore(0);
        this.arrancar = new Semaphore(0);
        this.mutexCarga = new Semaphore(1);
        this.cantTerminales = cantTerminales;
        this.cantPasajerosXTerminal = new int[this.cantTerminales];
    }
    
    /*
    *   Getters y Setters
    */
    public void setCapacidad(int capacidad) {
        this.capacidadMaxTren = capacidad;
    }

    public void setCantTerminales(int cantTerminales) {
        this.cantTerminales = cantTerminales;
    }

    public int getCantTerminales() {
        return this.cantTerminales;
    }

    public int getCapacidad() {
        return this.capacidadMaxTren;
    }

    //Metodo que permite que un pasajero se pueda subir a la carga del tren
    public void subirATren(Pasajero pasajero, char letraTerminal) {
        try {
            this.subir.acquire();
            this.mutexCarga.acquire();
            System.out.println("\t\t\t\t\t\t\t" + SoutColores.BLUE_UNDERLINED + "El pasajero: " + pasajero.getNombre() + " se SUBIO al tren...");
            //Aumenta en uno la cantidad de pasajeros que se bajaran en esa terminal
            verificarCarga(letraTerminal);
            this.mutexCarga.release();
            this.arrancar.release();
        } catch (InterruptedException ex) {
            Logger.getLogger(ControlTren.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //Metodo privado que aumenta la cantidad de pasajeros que bajaran en una terminal indicada
    private void verificarCarga(char letra) {
        int cantTotal = this.cantTerminales + INICIO_ASCII, i = INICIO_ASCII;
        boolean seguir = true;
        while (seguir && i < cantTotal) {
            if (letra == ((char) i)) {
                this.cantPasajerosXTerminal[(i-INICIO_ASCII)]++;
                seguir = false;
            }
            i++;
        }
    }

    //Metodo que permite a un pasajero bajar del tren
    public void bajarDeTren(Pasajero pasajero) {
        try {
            this.mutexCarga.acquire();
            this.bajar.release();
            System.out.println("\t\t\t\t\t\t\t" + SoutColores.BLUE_UNDERLINED + "El pasajero: " + pasajero.getNombre() + " BAJO del tren...");
            this.mutexCarga.release();
        } catch (InterruptedException ex) {
            Logger.getLogger(ControlTren.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Metodo que permite que el tren pase de una terminal a otra
    public void pasarTerminal(char letraTerminal, Semaphore[] semaforosTerminal) {
        try {
            this.mutexCarga.acquire();
            int cantTotal = this.cantTerminales+INICIO_ASCII, i = INICIO_ASCII;
            int pasajerosTerminalActual;
            boolean seguir = true;
            while (seguir && i < cantTotal) {
                if (letraTerminal == ((char) i)){
                    //Obtengo la cantidad de pasajeros que se quieren bajar en la terminal actual
                    pasajerosTerminalActual = this.cantPasajerosXTerminal[(i-INICIO_ASCII)];
                    //Libero la cantidad de permisos deseados segun la cantidad de pasajeros que se quieren bajar en esta terminal
                    semaforosTerminal[(i-INICIO_ASCII)].release(pasajerosTerminalActual);
                    //Seteo nuevamente en 0 la cantidad de pasajeros que se bajaran en esa terminal
                    this.cantPasajerosXTerminal[(i-INICIO_ASCII)] = 0;
                    seguir = false;
                }
                i++;
            }
            this.mutexCarga.release();
        } catch (InterruptedException ex) {
            Logger.getLogger(ControlTren.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Metodo que permite al tren arrancar el recorrido, siempre y cuando se hayan subido todos los pasajeros necesarios
    public void partirViaje() {
        try {
            this.arrancar.acquire(this.capacidadMaxTren);
            System.out.println("\t\t\t\t\t\t\t" + SoutColores.BLUE_UNDERLINED + "El tren esta LLENO, puede partir viaje...");
        } catch (InterruptedException ex) {
            Logger.getLogger(ControlTren.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Metodo que permite que el tren vuelva del recorrido, para iniciar uno nuevo
    public void volverViaje() {
        try {
            this.bajar.acquire(this.capacidadMaxTren);
            this.mutexCarga.acquire();
            System.out.println("\t\t\t\t\t\t\t" + SoutColores.BLUE_UNDERLINED + "El tren esta VACIO, ya podra volver...");
            this.mutexCarga.release();
        } catch (InterruptedException ex) {
            Logger.getLogger(ControlTren.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Metodo que permite notificar que llego a origen nuevamente, y libera permisos para que se puedan subir nuevos pasajeros
    public void llegoOrigen() {
        try {
            this.mutexCarga.acquire();
            this.subir.release(this.capacidadMaxTren);
            this.mutexCarga.release();
        } catch (InterruptedException ex) {
            Logger.getLogger(ControlTren.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}