package Clases;

import Utiles.SoutColores;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * @author OyarzunMatias, clase que modela la Tienda de una terminal
 */
public class Tienda {

    /*
    *   Variables:
    *   • CANTMAXTIENDA: representa la cantidad maxima de pasajeros que puede haber en la tienda en un mismo momento.
    *   • cajasTienda: array de CajaTienda que representa las cajas que hay en la tienda
    *   • productos: array de productos que representa los productos que se venden en la tienda
    *   • random: utilizado para generar valores aleatorios cuando sea necesario
    *   • clientesTienda: semaforo utilizado para bloquear a los clientes que quieren acceder a la tienda
    *   • mutexProductos: seamforo que permite la exclusion mutua de la seccion critica de los productos
    *   • cantProductos: representa la cantidad de productos que hay en la tienda para vender
    *   • nombreTienda: representa el nombre que tendra la tienda de la terminal
    */
    private static final int CANTMAXTIENDA = 3;
    private CajaTienda[] cajasTienda;
    private final Producto[] productos;
    private final Random random;
    private final Semaphore clientesTienda;
    private final Semaphore mutexProductos;
    private int cantProductos;
    private String nombreTienda;

    //Constructor
    public Tienda(CajaTienda[] cajas, String nombre) {
        this.nombreTienda = nombre;
        this.cajasTienda = cajas;
        this.random = new Random();
        this.cantProductos = (this.random.nextInt(11) + 10);
        int stockProductos = (this.random.nextInt(11) + 20);
        int precioRandom;
        this.productos = new Producto[this.cantProductos];
        //Creo arreglo de productos
        for (int i = 0; i < this.cantProductos; i++) {
            precioRandom = (this.random.nextInt(100) + 20);
            this.productos[i] = new Producto(i, stockProductos, precioRandom);
        }
        this.clientesTienda = new Semaphore(CANTMAXTIENDA);
        this.mutexProductos = new Semaphore(1);
    }
    
    /*
    *   Getters y Setters
    */
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

    //Metodo que permite verificar si un pasajero puede ingresar en la tienda o no, siempre y cuando haya los permisos necesarios
    public void entrarTienda(String nombrePasajero) {
        try {
            this.clientesTienda.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(Tienda.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("\t\t\t" + SoutColores.PURPLE + "El pasajero: " + nombrePasajero + " esta ENTRANDO a la tienda: "+this.nombreTienda+"...");
    }

    //Metodo que permite que un pasajero seleccione los productos que desea comprar, siempre y cuando tengan stock
    public void seleccionarProductos(String nombrePasajero, ArrayList<Producto> carroCompra) {
        try {
            this.mutexProductos.acquire();
            //Declaracion variables
            int cantProductosRandom = (this.random.nextInt(6) + 5);
            int prodRandom;
            Producto producto;
            
            System.out.println("\t\t\t" + SoutColores.PURPLE + "El pasajero: " + nombrePasajero + " esta LLENANDO su carro de compras en la tienda: "+this.nombreTienda+"...");
            
            //Repetitiva que permite seleccionar a productos de la tienda
            for (int i = 0; i < cantProductosRandom; i++) {
                prodRandom = this.random.nextInt(this.cantProductos);
                producto = this.productos[prodRandom];
                //Bloque sincronizado para verificar el stock de un producto
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

    //Metodo que permite que un pasajero vaya a una caja aleatoria de la tienda
    public CajaTienda irCaja(String nombrePasajero) {
        int cantCajas = this.cajasTienda.length;
        int cajaRandom = this.random.nextInt(cantCajas);
        CajaTienda caja = this.cajasTienda[cajaRandom];
        System.out.println("\t\t\t" + SoutColores.PURPLE + "El pasajero: " + nombrePasajero + " esta YENDO a la caja: " + caja.getId() + "...");
        return caja;
    }

    //Metodo que permite que un pasajero salga de la tienda despues de realizar la compra
    public void salirTienda(String nombrePasajero) {
        System.out.println("\t\t\t" + SoutColores.PURPLE + "El pasajero: " + nombrePasajero + " esta SALIENDO de la tienda: "+this.nombreTienda+"...");
        this.clientesTienda.release();
    }

    //Metodo que permite reponer productos que se venden en la tienda
    public void reponerProductos() {
        int cantStock;
        System.out.println("\t\t\t" + SoutColores.PURPLE + "Se estan REPONIENDO productos de la tienda: "+this.nombreTienda+"...");
        for (int i = 0; i < this.cantProductos; i++) {
            //Agrega una cantidad aleatoria de stock a un producto
            cantStock = (this.random.nextInt(11) + 10);
            this.productos[i].agregarStock(cantStock);
        }
    }
}