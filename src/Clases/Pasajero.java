package clases;

import Utiles.SoutColores;
import java.util.ArrayList;
import java.util.Random;
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
            if (((this.hora.get() + 2 <= horaVuelo) || (this.hora.get() > horaVuelo)) && (this.verTienda)) {
                System.out.println("\t\t\t" + SoutColores.PURPLE + "El pasajero: "+this.nombre+" esta viendo la tienda de la terminal: "+terminal.getLetra()+"...");
                Thread.sleep(2000*3);
            }else{
                System.out.println("\t\t\t" + SoutColores.PURPLE + "El pasajero: "+this.nombre+" NO VA A ver, y por lo tanto TAMPOCO VA A comprar en la tienda de la terminal: "+terminal.getLetra()+" (ya sea por falta de tiempo, o porque no tenia ganas)");
            }
            if (((this.hora.get() + 3 <= horaVuelo) || (this.hora.get() > horaVuelo)) && (this.comprarTienda && this.verTienda)) {
                tiendaTerminal.entrarTienda(this.nombre);
                ArrayList<Producto> carro = new ArrayList<>();
                tiendaTerminal.seleccionarProductos(this.nombre, carro);
                Thread.sleep(500);
                CajaTienda caja = tiendaTerminal.irCaja(this.nombre);
                caja.esperarCaja(this.nombre);
                caja.ponerProductosCinta(carro);
                Thread.sleep(500);
                caja.verificarCinta();
                caja.salirCaja(this.nombre);
                tiendaTerminal.salirTienda(this.nombre);
            }
            terminal.esperarVuelo(this.nombre, this.reserva);
            terminal.abordarVueloEmbarque(this.nombre, this.reserva);
        } catch (InterruptedException ex) {
            Logger.getLogger(Pasajero.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
