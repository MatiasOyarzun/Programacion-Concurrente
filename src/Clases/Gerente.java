package Clases;

import Utiles.SoutColores;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author OyarzunMatias, Clase que modela un gerente del aeropuerto que repondra productos de las tiendas de esta, implementa la interfaz Runnable y hereda de Persona.
 */
public class Gerente extends Persona implements Runnable{
    
    /*
    *   Variables: (y todas aquellas heredadas de la clase Persona, nombre e id)
    *   • HORA_REPONER: entero que representa a la hora que debera reponer productos en las tiendas.
    *   • tiendas: arreglo de tiendas, a las cuales luego debera reponer los productos.
    *   • hora: AtomicInteger que representa la hora actual.
    */
    
    private static final int HORA_REPONER = 0;
    private Tienda[] tiendas;
    private final AtomicInteger hora;
    
    //Constructor
    public Gerente(int id, String nombre, Tienda[] tiendas, AtomicInteger hora){
        super(id, nombre);
        this.tiendas = tiendas;
        this.hora = hora;
    }
    
    /*
    *   Getters y Setters
    */
    public Tienda[] getTienda(){
        return this.tiendas;
    }
    
    public void setTienda(Tienda[] tiendas){
        this.tiendas = tiendas;
    }
    
    @Override
    //Metodo redefinido que ejecutara el hilo del gerente
    public void run(){
        while(true){
            this.reponerProductosTiendas();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Gerente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    //Metodo que permite verificar si el gerente ya puede reponer productos en las tiendas del aeropuerto
    private synchronized void reponerProductosTiendas(){
        while (!this.hora.compareAndSet(HORA_REPONER, this.hora.addAndGet(0))) {
            try {
                this.wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(Gerente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        int cantTiendas = this.tiendas.length;
        System.out.println("\t\t\t" + SoutColores.PURPLE + "El gerente: "+this.nombre+" REPONDRA productos de las tiendas del aeropuerto");
        for (int i = 0; i < cantTiendas; i++) {
            tiendas[i].reponerProductos();
        }
    }
    
    //Metodo que sirve para que el gerente verifique la hora, para ver si ya es necesario reponer productos
    public synchronized void actualizarHora(){
        this.notify();
    }
}
