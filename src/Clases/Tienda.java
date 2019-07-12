package clases;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
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

    public Tienda(CajaTienda[] cajas) {
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
        System.out.println("El pasajero: " + nombrePasajero + " esta ENTRANDO a la tienda...");
    }

    public ArrayBlockingQueue<Producto> seleccionarProductos(String nombrePasajero) {
        ArrayBlockingQueue<Producto> carroCompra = null;
        try {
            this.mutexProductos.acquire();
            int cantProductosRandom = (this.random.nextInt(6) + 5);
            int prodRandom;
            Producto producto;
            carroCompra = new ArrayBlockingQueue(cantProductosRandom);
            System.out.println("El pasajero: " + nombrePasajero + " esta LLENANDO su carro de compras...");
            for (int i = 0; i < cantProductosRandom; i++) {
                prodRandom = this.random.nextInt(this.cantProductos);
                producto = this.productos[prodRandom];
                synchronized (producto) {
                    if (producto.getStock() > 0) {
                        System.out.println("El pasajero: "+nombrePasajero+" AGREGO un producto a su carro...");
                        carroCompra.add(this.productos[prodRandom]);
                    }else{
                        System.out.println("El pasajero: "+nombrePasajero+" queria AGREGAR a su carro de compras un producto, pero ya no hay...");
                    }
                }
            }
            this.mutexProductos.release();

            return carroCompra;
        } catch (InterruptedException ex) {
            Logger.getLogger(Tienda.class.getName()).log(Level.SEVERE, null, ex);
        }
        return carroCompra;
    }

    public CajaTienda irCaja(String nombrePasajero) {
        int cantCajas = this.cajasTienda.length;
        int cajaRandom = this.random.nextInt(cantCajas);
        CajaTienda caja = this.cajasTienda[cajaRandom];
        System.out.println("El pasajero: " + nombrePasajero + " esta YENDO a la caja...");
        return caja;
    }

    public void salirTienda(String nombrePasajero) {
        System.out.println("El pasajero: " + nombrePasajero + " esta SALIENDO de la tienda...");
        this.clientesTienda.release();
    }
    
    public void reponerProductos(){
        int cantStock;
        System.out.println("Se estan REPONIENDO productos de la tienda...");
        for (int i = 0; i < this.cantProductos; i++) {
            cantStock = (this.random.nextInt(11) + 20);
            this.productos[i].agregarStock(cantStock);
        }
    }
}
