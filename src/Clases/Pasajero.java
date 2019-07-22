package clases;

import Utiles.SoutColores;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * @author OyarzunMatias, clase que modela un Pasajero y su funcionamiento, hereda de Persona e implementa la interfaz de Runnable
 */
public class Pasajero extends Persona implements Runnable{
    
    /*
    *   Variables: (incluyendo las variables heredadas de Persona, como nombre e id)
    *   • random: utilizado en el caso que se desee algun valor aleatorio
    *   • reserva: representa la reserva que el pasajero saco en algun momento
    *   • aeropuerto: representa el aeropuerto que el pasajero visito
    *   • verTienda: booleano que representa si un pasajero vera las vidrieras de la tienda
    *   • comprarTienda: booleano que representa si un pasajero comprara en la tienda de la terminal (siempre y cuando "verTienda" sea verdadero tambien)
    *   • hora: representa la hora actual del dia
    */
    private final Random random;
    private Reserva reserva;
    private Aeropuerto aeropuerto;
    private boolean verTienda;
    private boolean comprarTienda;
    private final AtomicInteger hora;
    
    //Constructor
    public Pasajero(int id, String nombre, Reserva reserva, Aeropuerto aeropuerto, boolean verTienda, boolean comprarTienda, AtomicInteger hora){
        super(id, nombre);
        this.reserva = reserva;
        this.aeropuerto = aeropuerto;
        this.random = new Random();
        this.verTienda = verTienda;
        this.comprarTienda = comprarTienda;
        this.hora = hora;
    }
    
    /*
    *   Getters y Setters
    */
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
    //Metodo redefinido que invocaran los hilos de los pasajeros
    public void run(){
        try {
            //Declaracion variables
            Tren trenInterno = this.aeropuerto.getTren();
            CargaTren cargaTren = trenInterno.getCarga();
            PuestoAtencion puesto = this.aeropuerto.ingresarAeropuerto(this);
            Terminal terminal = this.reserva.getTerminal();
            Tienda tiendaTerminal = terminal.getTienda();
            int horaVuelo = this.reserva.getHoraVuelo();
            Thread.sleep(100*(this.random.nextInt(4)+1));
            //Verifica si puede entrar en la fila del aeropuerto
            puesto.entrarFilaPuesto(this.nombre);
            Thread.sleep(100*(this.random.nextInt(5)+1));
            //Verifica si puede entrar al puesto de atencion de la terminal
            puesto.entrarPuestoAtencion(this.nombre);
            Thread.sleep(100*(this.random.nextInt(3)+1));
            //Sale del puesto de atencion
            puesto.salirPuestoAtencion(this.nombre);
            Thread.sleep(100*(this.random.nextInt(5)+1));
            //Subirse a la carga del tren (vagones)
            cargaTren.subirCargaTren(this.nombre, (this.reserva.getTerminal().getLetra()));
            //Espera hasta que llegue a su terminal
            trenInterno.trasladarATerminal(this.reserva.getTerminal().getLetra());
            //Se baja del tren
            cargaTren.bajarCargaTren(this.nombre);
            //Si tiene tiempo y si "verTienda" es true, vera la tienda
            if (((this.hora.get() + 2 <= horaVuelo) || (this.hora.get() > horaVuelo)) && (this.verTienda)) {
                System.out.println("\t\t\t" + SoutColores.PURPLE + "El pasajero: "+this.nombre+" esta viendo la tienda de la terminal: "+terminal.getLetra()+"...");
                Thread.sleep(2000*3);
            }else{
                System.out.println("\t\t\t" + SoutColores.PURPLE + "El pasajero: "+this.nombre+" NO VA A ver, y por lo tanto TAMPOCO VA A comprar en la tienda de la terminal: "+terminal.getLetra()+" (ya sea por falta de tiempo, o porque no tenia ganas)");
            }
            //Si tiene tiempo y si "comprarTienda" y "verTienda" son true, comprara en la tienda
            if (((this.hora.get() + 3 <= horaVuelo) || (this.hora.get() > horaVuelo)) && (this.comprarTienda && this.verTienda)) {
                //Espera en el caso de que la tienda este llena
                tiendaTerminal.entrarTienda(this.nombre);
                //Creo carro de productos
                ArrayList<Producto> carro = new ArrayList<>();
                //Elije productos a comprar de la tienda
                tiendaTerminal.seleccionarProductos(this.nombre, carro);
                Thread.sleep(500);
                //Va a una caja de la tienda
                CajaTienda caja = tiendaTerminal.irCaja(this.nombre);
                //Espera si la caja esta ocupada
                caja.esperarCaja(this.nombre);
                //Pone los productos en la cinta transportadora de la caja
                caja.ponerProductosCinta(carro);
                Thread.sleep(500);
                //Espera hasta que la cajera haya chequeado todos sus productos
                caja.verificarCinta();
                //Sale de la caja
                caja.salirCaja(this.nombre);
                //Sale de la tienda
                tiendaTerminal.salirTienda(this.nombre);
            }
            //Espera a que sea la hora de la reserva de su vuelo
            terminal.esperarVuelo(this.nombre, this.reserva);
            //Se sube a su vuelo
            terminal.abordarVueloEmbarque(this.nombre, this.reserva);
        } catch (InterruptedException ex) {
            Logger.getLogger(Pasajero.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}