package clases;

import Utiles.SoutColores;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author OyarzunMatias
 */
public class Pasajero extends Persona implements Runnable{
    
    private final Random random;
    private Reserva reserva;
    private Aeropuerto aeropuerto;
    private boolean verTienda;
    private boolean comprarTienda;
    private final AtomicInteger hora;
    
    public Pasajero(int id, String nombre, Reserva reserva, Aeropuerto aeropuerto, boolean verTienda, boolean comprarTienda, AtomicInteger hora){
        super(id, nombre);
        this.reserva = reserva;
        this.aeropuerto = aeropuerto;
        this.random = new Random();
        this.verTienda = verTienda;
        this.comprarTienda = comprarTienda;
        this.hora = hora;
    }
    
    public void setReserva(Reserva reserva){
        this.reserva = reserva;
    }
    
    public Reserva getReserva(){
        return this.reserva;
    }
    
    public void setVerTienda(boolean verTienda){
        this.verTienda = verTienda;
    }
    
    public boolean getVerTienda(){
        return this.verTienda;
    }
    
    public void setComprarTienda(boolean comprarTienda){
        this.comprarTienda = comprarTienda;
    }
    
    public boolean getComprarTienda(){
        return this.comprarTienda;
    }
    
    public void setAeropuerto(Aeropuerto aeropuerto){
        this.aeropuerto = aeropuerto;
    }
    
    public Aeropuerto getAeropuerto(){
        return this.aeropuerto;
    }
    
    @Override
    public void run(){
        try {
            Tren trenInterno = this.aeropuerto.getTren();
            CargaTren cargaTren = trenInterno.getCarga();
            PuestoAtencion puesto = this.aeropuerto.ingresarAeropuerto(this);
            Terminal terminal = this.reserva.getTerminal();
            Tienda tiendaTerminal = terminal.getTienda();
            int horaVuelo = this.reserva.getHoraVuelo();
            Thread.sleep(100*(this.random.nextInt(4)+1));
            puesto.entrarFilaPuesto(this.nombre);
            Thread.sleep(100*(this.random.nextInt(5)+1));
            puesto.entrarPuestoAtencion(this.nombre);
            Thread.sleep(100*(this.random.nextInt(3)+1));
            puesto.salirPuestoAtencion(this.nombre);
            Thread.sleep(100*(this.random.nextInt(5)+1));
            cargaTren.subirCargaTren(this.nombre, (this.reserva.getTerminal().getLetra()));
            trenInterno.trasladarATerminal(this.reserva.getTerminal().getLetra());
            cargaTren.bajarCargaTren(this.nombre);
            if ((this.hora.get() + 3 <= horaVuelo) && (this.verTienda)) {
                System.out.println("\t\t\t\t\t\t\t\t\t"+SoutColores.GREEN_BACKGROUND_BRIGHT+"El pasajero: "+this.nombre+" esta viendo la tienda...");
                Thread.sleep(2000*3);
            }
            if ((this.hora.get() + 2 <= horaVuelo) && (this.comprarTienda)) {
                tiendaTerminal.entrarTienda(this.nombre);
                ArrayBlockingQueue carro = tiendaTerminal.seleccionarProductos(this.nombre);
                Thread.sleep(500);
                CajaTienda caja = tiendaTerminal.irCaja(this.nombre);
                caja.esperarCaja(this.nombre);
                caja.ponerProductosCinta(carro);
                tiendaTerminal.salirTienda(this.nombre);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Pasajero.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
