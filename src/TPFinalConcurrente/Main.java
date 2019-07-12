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
    private static final Random NUMRANDOM = new Random();
    private static final int CANTPASAJEROS = CANTCARGATREN*(NUMRANDOM.nextInt(10)+3);
    
    public static void crearTerminales(){
        int numPuesto = 1;
        for (int i = 0; i < CANTTERMINALES; i++) {
            int[] puestosTerminal = new int[CANTPUESTOSTERMINAL];
            Tienda nuevaTienda;
            for (int j = 0; j < CANTPUESTOSTERMINAL; j++) {
                puestosTerminal[j] = numPuesto;
                numPuesto++;
            }
            nuevaTienda = crearTiendas();
            TERMINALES[i] = new Terminal(LETRASTERMINALES[i], puestosTerminal, nuevaTienda);
        }
    }
    
    public static Tienda crearTiendas(){
        CajaTienda[] cajas = new CajaTienda[CANTCAJASXTIENDA];
        Tienda tienda;
        for (int i = 0; i < CANTCAJASXTIENDA; i++) {
            cajas[i] = new CajaTienda(i);
            Thread nuevaCaja = new Thread(cajas[i]);
            nuevaCaja.start();
        }
        tienda = new Tienda(cajas);
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
        int i = 0;
        while (i < CANTPASAJEROS) {
            indiceAerolinea = NUMRANDOM.nextInt(CANTAEROLINEAS);
            aerolineaPasajero = AEROLINEAS[indiceAerolinea];
            horaViaje = NUMRANDOM.nextInt(24);
            indiceTerminal = NUMRANDOM.nextInt(CANTTERMINALES);
            terminalPasajero = TERMINALES[indiceTerminal];
            indicePuesto = NUMRANDOM.nextInt(CANTPUESTOSTERMINAL);
            puesto = (terminalPasajero.getPuestos())[indicePuesto];
            Reserva nuevaReserva = new Reserva(aerolineaPasajero, horaViaje, terminalPasajero, puesto);
            Pasajero nuevoPasajero = new Pasajero((i+1), "Pasajero: "+(i+1), nuevaReserva, viajeBonito);
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