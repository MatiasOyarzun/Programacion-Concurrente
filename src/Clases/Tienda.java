package clases;

import Utiles.SoutColores;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author OyarzunMatias
 */
public class Tienda {

    private static final int CANTMAXTIENDA = 5;
    private CajaTienda[] cajasTienda;
    private final Producto[] productos;
    private final Random random;
    private final Semaphore clientesTienda;
    private final Semaphore mutexProductos;
    private int cantProductos;
    private String nombreTienda;

    public Tienda(CajaTienda[] cajas, String nombre) {
        this.nombreTienda = nombre;
        this.cajasTienda = cajas;
        this.random = new Random();
        this.cantProductos = (this.random.nextInt(11) + 10);
        int stockProductos = (this.random.nextInt(11) + 20);
        int precioRandom;
        this.productos = new Producto[this.cantProductos];
        for (int i = 0; i < this.cantProductos; i++) {
            precioRandom = (this.random.nextInt(100) + 20);
            this.productos[i] = new Producto(i, stockProductos, precioRandom);
        }
        this.clientesTienda = new Semaphore(CANTMAXTIENDA);
        this.mutexProductos = new Semaphore(1);
    }
    
    public void setNombreTienda(String nombreTienda){
        this.nombreTienda = nombreTienda;
    }
    
    public String getNombreTienda(){
        return this.nombreTienda;
    }

    public void setCantProductos(int cantProductos) {
        this.cantProductos = cantProductos;
    }

    public int getCantProductos() {
        return this.cantProductos;
    }

    public void setCajasTienda(CajaTienda[] nuevasCajas) {
        this.cajasTienda = nuevasCajas;
    }

    public CajaTienda[] getCajasTienda() {
        return this.cajasTienda;
    }

    public void entrarTienda(String nombrePasajero) {
        try {
            this.clientesTienda.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(Tienda.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("\t\t\t" + SoutColores.PURPLE + "El pasajero: " + nombrePasajero + " esta ENTRANDO a la tienda: "+this.nombreTienda+"...");
    }

    public void seleccionarProductos(String nombrePasajero, ArrayList<Producto> carroCompra) {
        try {
            this.mutexProductos.acquire();
            int cantProductosRandom = (this.random.nextInt(6) + 5);
            int prodRandom;
            Producto producto;
            System.out.println("\t\t\t" + SoutColores.PURPLE + "El pasajero: " + nombrePasajero + " esta LLENANDO su carro de compras en la tienda: "+this.nombreTienda+"...");
            for (int i = 0; i < cantProductosRandom; i++) {
                prodRandom = this.random.nextInt(this.cantProductos);
                producto = this.productos[prodRandom];
                synchronized (producto) {
                    if (producto.getStock() > 0) {
                        System.out.println("\t\t\t" + SoutColores.PURPLE + "El pasajero: " + nombrePasajero + " AGREGO un producto a su carro en la tienda: "+this.nombreTienda+"...");
                        carroCompra.add(this.productos[prodRandom]);
                    } else {
                        System.out.println("\t\t\t" + SoutColores.PURPLE + "El pasajero: " + nombrePasajero + " queria AGREGAR a su carro de compras un producto, pero ya no hay en la tienda: "+this.nombreTienda+"...");
                    }
                }
            }
            this.mutexProductos.release();
        } catch (InterruptedException ex) {
            Logger.getLogger(Tienda.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public CajaTienda irCaja(String nombrePasajero) {
        int cantCajas = this.cajasTienda.length;
        int cajaRandom = this.random.nextInt(cantCajas);
        CajaTienda caja = this.cajasTienda[cajaRandom];
        System.out.println("\t\t\t" + SoutColores.PURPLE + "El pasajero: " + nombrePasajero + " esta YENDO a la caja: " + caja.getId() + "...");
        return caja;
    }

    public void salirTienda(String nombrePasajero) {
        System.out.println("\t\t\t" + SoutColores.PURPLE + "El pasajero: " + nombrePasajero + " esta SALIENDO de la tienda: "+this.nombreTienda+"...");
        this.clientesTienda.release();
    }

    public void reponerProductos() {
        int cantStock;
        System.out.println("\t\t\t" + SoutColores.PURPLE + "Se estan REPONIENDO productos de la tienda: "+this.nombreTienda+"...");
        for (int i = 0; i < this.cantProductos; i++) {
            cantStock = (this.random.nextInt(11) + 20);
            this.productos[i].agregarStock(cantStock);
        }
    }
}
