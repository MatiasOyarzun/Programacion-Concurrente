package clases;

import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import Utiles.SoutColores;

/**
 *
 * @author OyarzunMatias
 */
public class CargaTren {

    private int capacidadMaxTren;
    private final Semaphore subir;
    private final Semaphore bajar;
    private final Semaphore arrancar;
    private final Semaphore mutexCarga;
    private final int[] cantPasajerosXTerminal;
    private int cantTerminales;

    public CargaTren(int capacidad, int cantTerminales) {
        this.capacidadMaxTren = capacidad;
        this.subir = new Semaphore(this.capacidadMaxTren);
        this.bajar = new Semaphore(0);
        this.arrancar = new Semaphore(0);
        this.mutexCarga = new Semaphore(1);
        this.cantTerminales = cantTerminales;
        this.cantPasajerosXTerminal = new int[this.cantTerminales];
    }

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

    public void subirCargaTren(String nombrePasajero, char letraTerminal) {
        try {
            this.subir.acquire();
            this.mutexCarga.acquire();
            System.out.println("\t\t\t\t\t\t\t" + SoutColores.BLUE_UNDERLINED + "El pasajero: " + nombrePasajero + " SUBIO a la carga del tren...");
            verificarCarga(letraTerminal);
            this.mutexCarga.release();
            this.arrancar.release();
        } catch (InterruptedException ex) {
            Logger.getLogger(CargaTren.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void verificarCarga(char letra) {
        int cantTotal = this.cantTerminales + 65, i = 65;
        boolean seguir = true;
        while (seguir && i < cantTotal) {
            if (letra == ((char) i)) {
                this.cantPasajerosXTerminal[(i-65)]++;
                seguir = false;
            }
            i++;
        }
    }

    public void bajarCargaTren(String nombrePasajero) {
        try {
            this.mutexCarga.acquire();
            this.bajar.release();
            System.out.println("\t\t\t\t\t\t\t" + SoutColores.BLUE_UNDERLINED + "El pasajero: " + nombrePasajero + " BAJO de la carga del tren...");
            this.mutexCarga.release();
        } catch (InterruptedException ex) {
            Logger.getLogger(CargaTren.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void pasarTerminal(char letraTerminal, Semaphore[] semaforosTerminal) {
        try {
            this.mutexCarga.acquire();
            int cantTotal = this.cantTerminales+65, i = 65;
            int pasajerosTerminalActual;
            boolean seguir = true;
            while (seguir && i < cantTotal) {
                if (letraTerminal == ((char) i)){
                    pasajerosTerminalActual = this.cantPasajerosXTerminal[(i-65)];
                    semaforosTerminal[(i-65)].release(pasajerosTerminalActual);
                    this.cantPasajerosXTerminal[(i-65)] = 0;
                    seguir = false;
                }
                i++;
            }
            this.mutexCarga.release();
        } catch (InterruptedException ex) {
            Logger.getLogger(CargaTren.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void partirViaje() {
        try {
            this.arrancar.acquire(this.capacidadMaxTren);
            System.out.println("\t\t\t\t\t\t\t" + SoutColores.BLUE_UNDERLINED + "La carga esta LLENA, el tren puede partir viaje...");
        } catch (InterruptedException ex) {
            Logger.getLogger(CargaTren.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void volverViaje() {
        try {
            this.bajar.acquire();
            this.mutexCarga.acquire();
            System.out.println("\t\t\t\t\t\t\t" + SoutColores.BLUE_UNDERLINED + "La carga esta VACIA, el tren ya podra volver...");
            this.mutexCarga.release();
        } catch (InterruptedException ex) {
            Logger.getLogger(CargaTren.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void llegoOrigen() {
        try {
            this.mutexCarga.acquire();
            this.subir.release(this.capacidadMaxTren);
            this.mutexCarga.release();
        } catch (InterruptedException ex) {
            Logger.getLogger(CargaTren.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
