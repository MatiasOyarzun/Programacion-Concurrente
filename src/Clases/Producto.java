package clases;

/**
 *
 * @author OyarzunMatias 
 * Clase que modela un producto de la Tienda con su
 * id, cantidad del mismo en el supermercado (Stock) y precio.
 */
public class Producto {

    /**
     * Variables: 
     * id: Codigo unico de producto
     * stock: Cantidad del producto en el supermecado
     * precio: Precio del producto
     */
    private int id;
    private int stock;
    private double precio;

    /**
     * Constructor
     */
    public Producto(int id, int stock, double precio) {
        this.id = id;
        this.stock = stock;
        this.precio = precio;
    }

    /**
     * Getters y setters
     *
     */
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public synchronized int getStock() {
        return this.stock;
    }

    public synchronized void setStock(int stock) {
        this.stock = stock;
    }


    public double getPrecio() {
        return this.precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    /**
     *  Resta uno al stock del producto
     **/
    public synchronized void restarStock() {
        this.stock -= 1;
    }
    
    /**
    *   Agrego una cantidad ingresada por parametro al stock del producto
    **/
    public synchronized void agregarStock(int cantidad) {
        this.stock += cantidad;
    }
}