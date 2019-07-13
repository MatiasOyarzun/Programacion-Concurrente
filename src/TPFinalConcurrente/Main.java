package tpfinalconcurrente;

import clases.CargaTren;
import clases.Tienda;
import clases.Terminal;
import clases.CajaTienda;
import clases.Guardia;
import clases.PuestoAtencion;
import clases.Tren;
import clases.Aerolinea;
import clases.Pasajero;
import clases.Aeropuerto;
import clases.CajeraTienda;
import clases.ControlDia;
import clases.Reserva;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author OyarzunMatias
 */
public class Main {
    
    private static final int CANTTERMINALES = 3;
    private static final int CANTCARGATREN = 5;
    private static final int CANTAEROLINEAS = 3;
    private static final Aerolinea[] AEROLINEAS = new Aerolinea[CANTAEROLINEAS];
    private static final String[] NOMBRESAEROLINEAS = {"AEROLINEAS ARGENTINAS", "LAN", "LATAM"};
    private static final int CANTCAJASXTIENDA = 3;
    private static final int CANTPUESTOSTERMINAL = 7;
    private static final Terminal[] TERMINALES = new Terminal[CANTTERMINALES];
    private static final char[] LETRASTERMINALES = {'A', 'B', 'C'};
    private static final AtomicInteger HORA = new AtomicInteger(0);
    private static final Random RANDOM = new Random();
    private static final int CANTPASAJEROS = CANTCARGATREN*(RANDOM.nextInt(10)+3);
    
    public static void crearTerminales(){
        int numPuesto = 1;
        for (int i = 0; i < CANTTERMINALES; i++) {
            int[] puestosTerminal = new int[CANTPUESTOSTERMINAL];
            Tienda nuevaTienda;
            for (int j = 0; j < CANTPUESTOSTERMINAL; j++) {
                puestosTerminal[j] = numPuesto;
                numPuesto++;
            }
            nuevaTienda = crearTienda(LETRASTERMINALES[i]);
            TERMINALES[i] = new Terminal(LETRASTERMINALES[i], puestosTerminal, nuevaTienda);
        }
    }
    
    public static Tienda crearTienda(char letraTerminal){
        CajaTienda[] cajas = new CajaTienda[CANTCAJASXTIENDA];
        Tienda tienda;
        for (int i = 0; i < CANTCAJASXTIENDA; i++) {
            cajas[i] = new CajaTienda(i);
            CajeraTienda cajera = new CajeraTienda(i, cajas[i]);
            Thread nuevaCajera = new Thread(cajera, ("CAJERA: "+i+" - TIENDA TERMINAL: "+letraTerminal));
            nuevaCajera.start();
        }
        tienda = new Tienda(cajas, "TIENDA TERMINAL: "+letraTerminal);
        return tienda;
    }
    
    public static void crearAerolineas(){
        for (int i = 0; i < CANTAEROLINEAS; i++) {
            Guardia nuevoGuardia = new Guardia((i+1), "Guardia: "+(i+1));
            Thread guardia = new Thread(nuevoGuardia);
            PuestoAtencion nuevoPuesto = new PuestoAtencion("Puesto: "+NOMBRESAEROLINEAS[i], nuevoGuardia);
            nuevoGuardia.setPuesto(nuevoPuesto);
            guardia.start();
            AEROLINEAS[i] = new Aerolinea(NOMBRESAEROLINEAS[i], nuevoPuesto);
        }
    }
    
    public static void crearPasajeros(Aeropuerto viajeBonito){
        int indiceAerolinea, horaViaje, indiceTerminal, indicePuesto, puesto;
        Aerolinea aerolineaPasajero;
        Terminal terminalPasajero;
        Boolean verTienda, comprarTienda;
        int i = 0;
        while (i < CANTPASAJEROS) {
            indiceAerolinea = RANDOM.nextInt(CANTAEROLINEAS);
            aerolineaPasajero = AEROLINEAS[indiceAerolinea];
            horaViaje = RANDOM.nextInt(24);
            indiceTerminal = RANDOM.nextInt(CANTTERMINALES);
            terminalPasajero = TERMINALES[indiceTerminal];
            indicePuesto = RANDOM.nextInt(CANTPUESTOSTERMINAL);
            puesto = (terminalPasajero.getPuestos())[indicePuesto];
            Reserva nuevaReserva = new Reserva(aerolineaPasajero, horaViaje, terminalPasajero, puesto);
            verTienda = RANDOM.nextBoolean();
            comprarTienda = RANDOM.nextBoolean();
            Pasajero nuevoPasajero = new Pasajero((i+1), "Pasajero: "+(i+1), nuevaReserva, viajeBonito, verTienda, comprarTienda, HORA);
            System.out.println("___---____---__--_-EL PASAJERO: "+(i+1)+" VER TIENDA: "+verTienda+" - COMPRAR TIENDA: "+comprarTienda+"-_--__---___---___");
            Thread pasajero = new Thread(nuevoPasajero, "Pasajero: "+(i+1));
            pasajero.start();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            i++;
        }
    }

    public static void main(String[] args) {
        crearTerminales();
        crearAerolineas();
        CargaTren carga = new CargaTren(CANTCARGATREN, CANTTERMINALES);
        Tren trenInterno = new Tren("Tren Interno", TERMINALES, carga, CANTTERMINALES);
        Thread tren = new Thread(trenInterno, "Tren Interno");
        tren.start();
        Aeropuerto viajeBonito = new Aeropuerto("Viaje Bonito", TERMINALES, AEROLINEAS, trenInterno);
        ControlDia pasoDia = new ControlDia(HORA, viajeBonito);
        Thread control = new Thread(pasoDia, "Simulador Tiempo");
        control.start();
        crearPasajeros(viajeBonito);
    }
}