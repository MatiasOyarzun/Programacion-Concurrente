package clases;

import Utiles.SoutColores;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author OyarzunMatias
 */
public class CajaTienda {

    private int id;
    private final Semaphore caja;
    private int cantActualCinta;
    private final Lock lock = new ReentrantLock();
    private final Condition esperaCajera = this.lock.newCondition();
    private final Condition esperaPasajero = this.lock.newCondition();
    private final ArrayList<Producto> cintaTransportadora;

    public CajaTienda(int id) {
        this.id = id;
        this.caja = new Semaphore(1, true);
        this.cintaTransportadora = new ArrayList<>();
        this.cantActualCinta = 0;
    }

    public int getCantActualCinta() {
        return this.cantActualCinta;
    }

    public void setCantActualCinta(int cant) {
        this.cantActualCinta = cant;
    }

    public void esperarCaja(String nombrePasajero) {
        try {
            this.caja.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(CajaTienda.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("\t\t\t" + SoutColores.PURPLE + "El pasajero: " + nombrePasajero + " ENTRO en la caja: " + this.id + "...");

    }

    public void salirCaja(String nombrePasajero) {
        System.out.println("\t\t\t" + SoutColores.PURPLE + "El pasajero: " + nombrePasajero + " SALIO de la caja: " + this.id + "...");
        this.caja.release();
    }

    public void ponerProductosCinta(ArrayList<Producto> carrito) {
        this.lock.lock();
        try {
            int longCarro = carrito.size();
            this.cantActualCinta = longCarro;
            for (int i = 0; i < longCarro; i++) {
                this.cintaTransportadora.add((carrito.get(i)));
            }
            carrito.clear();
            this.esperaCajera.signal();
        } finally {
            this.lock.unlock();
        }
    }

    public void verificarCinta() {
        this.lock.lock();
        try {
            while(this.cantActualCinta > 0){
                try {
                    this.esperaPasajero.await();
                } catch (InterruptedException ex) {
                    Logger.getLogger(CajaTienda.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } finally {
            this.lock.unlock();
        }
    }

    public Producto obtenerProductoCinta() {
        this.lock.lock();
        Producto producto = null;
        try {  
            while (this.cantActualCinta == 0) {
                this.esperaCajera.await();
            }
            producto = this.cintaTransportadora.get(0);
            this.cantActualCinta--;
            this.cintaTransportadora.remove(0);
            if(this.cantActualCinta == 0){
                this.esperaPasajero.signal();
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(CajaTienda.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            this.lock.unlock();
        }
        return producto;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }
}
