package Clases;

import Utiles.SoutColores;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author OyarzunMatias, Clase que modela una Caja de una tienda y su funcionamiento
 */
public class CajaTienda {
    
    /*
    *   Variables:
    *   • id: simula el numero de la caja en la tienda
    *   • caja: semaforo que permite que los demas hilos esperen hasta que esta se desocupe (permite verificar si esta ocupada o libre la caja)
    *   • cantActualCinta: variable entera que permite indicar la cantidad actual de los productos de la cinta de la caja
    *   • lock: ReentrantLock que permite la exclusion mutua a la seccion critica
    *   • esperaCajera: Condition, que permite bloquear a la cajera en cuanto no hayan items en la cinta
    *   • esperaPasajero: Condition, que permite bloquear al pasajero mientras que hayan items en la cinta
    *   • cintaTransportadora: ArrayList que simula la cinta, para almacenar los productos en esta
    */
    private int id;
    private final Semaphore caja;
    private int cantActualCinta;
    private final Lock lock;
    private final Condition esperaCajera;
    private final Condition esperaPasajero;
    private final ArrayList<Producto> cintaTransportadora;

    //Constructor
    public CajaTienda(int id) {
        this.id = id;
        this.caja = new Semaphore(1, true);
        this.cintaTransportadora = new ArrayList<>();
        this.lock = new ReentrantLock();
        this.esperaCajera = this.lock.newCondition();
        this.esperaPasajero = this.lock.newCondition();
        this.cantActualCinta = 0;
    }
    
    //Metodo que permite al pasajero esperar por la caja, cuando esta este ocupada, adquiriendo un permiso del semaforo "caja"
    public void esperarCaja(String nombrePasajero) {
        try {
            this.caja.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(CajaTienda.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("\t\t\t" + SoutColores.PURPLE + "El pasajero: " + nombrePasajero + " ENTRO en la caja: " + this.id + "...");

    }

    //Metodo que permite al pasajero liberar la caja, liberando un permiso del semaforo "caja", permitiendo que otros pasajeros, que se encuentren esperando ingresen
    public void salirCaja(String nombrePasajero) {
        System.out.println("\t\t\t" + SoutColores.PURPLE + "El pasajero: " + nombrePasajero + " SALIO de la caja: " + this.id + "...");
        this.caja.release();
    }
    
    //Metodo que recibe el carrito del pasajero y pone los productos en la cinta transportadora, y luego cuando termina, le avisa a la cajera que ya puede comenzar a atender
    public void ponerProductosCinta(ArrayList<Producto> carrito) {
        this.lock.lock();
        try {
            int longCarro = carrito.size();
            this.cantActualCinta = longCarro;
            for (int i = 0; i < longCarro; i++) {
                this.cintaTransportadora.add((carrito.get(i)));
            }
            carrito.clear();
            //Ya puso todos los productos en la cinta, ahora despierta al hilo de la cajera
            this.esperaCajera.signal();
        } finally {
            this.lock.unlock();
        }
    }
    
    //Metodo que ejecuta el pasajero, para esperar a que la cinta transportadora sea vaciada (todos sus productos sean chequeados por la cajera)
    public void verificarCinta() {
        this.lock.lock();
        try {
            //Mientras que la cantidad actual de productos en la cinta sea mayor a cero, el pasajero espera
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
    
    /*
    *   Metodo que ejecuta la cajera, para obtener un producto de la cinta transportadora, en el caso que no haya de primeras, la cajera es bloqueada,
    *   si hay productos, la cajera quita el primer elemento de la cinta, luego, si el producto que saco era el ultimo, avisa al pasajero que se puede ir.
    */
    public Producto obtenerProductoCinta() {
        this.lock.lock();
        Producto producto = null;
        try {  
            //Mientras que no haya productos en la cinta, la cajera espera
            while (this.cantActualCinta == 0) {
                this.esperaCajera.await();
            }
            producto = this.cintaTransportadora.get(0);
            this.cantActualCinta--;
            this.cintaTransportadora.remove(0);
            //Cuando saca ultimo producto la cajera, despierta al hilo del pasajero
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

    /*
    *   Getters y Setters
    */
    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }
    
    public int getCantActualCinta() {
        return this.cantActualCinta;
    }

    public void setCantActualCinta(int cant) {
        this.cantActualCinta = cant;
    }
}