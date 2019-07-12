package clases;

import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import Utiles.SoutColores;

/**
 *
 * @author OyarzunMatias
 */
public class Tren implements Runnable {

    private Terminal[] terminalesPorRecorrer;
    private int cantTerminales;
    private String nombreTren;
    private CargaTren carga;
    private final Semaphore[] semaforosTerminal;

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
    public void run() {
        while (true) {
            try {
                char letraTerminal;
                this.carga.partirViaje();
                Thread.sleep(1000);
                System.out.println("\t\t\t\t\t\t\t" + SoutColores.BLUE_UNDERLINED + "El tren: " + this.nombreTren + " COMENZO RECORRIDO...");
                for (int i = 0; i < this.cantTerminales; i++) {
                    letraTerminal = this.terminalesPorRecorrer[i].getLetra();
                    System.out.println("\t\t\t\t\t\t\t" + SoutColores.BLUE_UNDERLINED + "El tren: " + this.nombreTren + " esta PASANDO por la terminal: " + letraTerminal);
                    this.carga.pasarTerminal(letraTerminal, this.semaforosTerminal);
                    Thread.sleep(5000);
                }
                this.carga.volverViaje();
                Thread.sleep(2000*this.cantTerminales);
                this.carga.llegoOrigen();
                System.out.println("\t\t\t\t\t\t\t" + SoutColores.BLUE_UNDERLINED + "El tren: " + this.nombreTren + " ya regreso al punto de origen...");
            } catch (InterruptedException ex) {
                Logger.getLogger(Tren.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void trasladarATerminal(char letraTerminal) {
        int cantTotal = this.cantTerminales+65;
        for (int i = 65; i < cantTotal; i++) {
            if (letraTerminal == ((char) i)){
                try {
                    this.semaforosTerminal[(i-65)].acquire();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Tren.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

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
