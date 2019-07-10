package TPFinalConcurrente;

import Clases.*;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author OyarzunMatias
 */
public class Main {
    
    private static final int CANTTERMINALES = 3;
    private static final int CANTPASAJEROS = 15;
    private static final int CANTAEROLINEAS = 3;
    private static final Aerolinea[] AEROLINEAS = new Aerolinea[CANTAEROLINEAS];
    private static final String[] NOMBRESAEROLINEAS = {"AEROLINEAS ARGENTINAS", "LAN", "LATAM"};
    private static final int CANTCAJASXTIENDA = 3;
    private static final int CANTPUESTOSTERMINAL = 7;
    private static final Terminal[] TERMINALES = new Terminal[CANTTERMINALES];
    private static final char[] LETRASTERMINALES = {'A', 'B', 'C'};
    private static final AtomicInteger HORA = new AtomicInteger(0);
    private static final Random NUMRANDOM = new Random();
    
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
            Guardia nuevoGuardia = new Guardia((i+1));
            Thread guardia = new Thread(nuevoGuardia);
            guardia.start();
            PuestoAtencion nuevoPuesto = new PuestoAtencion("Puesto: "+NOMBRESAEROLINEAS[i], nuevoGuardia);
            AEROLINEAS[i] = new Aerolinea(NOMBRESAEROLINEAS[i], nuevoPuesto);
        }
    }
    
    public static void crearPasajeros(){
        int indiceAerolinea, horaViaje, indiceTerminal, indicePuesto, puesto;
        Aerolinea aerolineaPasajero;
        Terminal terminalPasajero;
        for (int i = 0; i < CANTPASAJEROS; i++) {
            indiceAerolinea = NUMRANDOM.nextInt(CANTAEROLINEAS);
            aerolineaPasajero = AEROLINEAS[indiceAerolinea];
            horaViaje = NUMRANDOM.nextInt(24);
            indiceTerminal = NUMRANDOM.nextInt(CANTTERMINALES);
            terminalPasajero = TERMINALES[indiceTerminal];
            indicePuesto = NUMRANDOM.nextInt(CANTPUESTOSTERMINAL);
            puesto = (terminalPasajero.getPuestos())[indicePuesto];
            Reserva nuevaReserva = new Reserva(aerolineaPasajero, horaViaje, terminalPasajero, puesto);
            Pasajero nuevoPasajero = new Pasajero((i+1), "Pasajero: "+(i+1), nuevaReserva);
            Thread pasajero = new Thread(nuevoPasajero, "Pasajero: "+(i+1));
            pasajero.start();
        }
    }

    public static void main(String[] args) {
        crearTerminales();
        System.out.println("");
        crearAerolineas();
        Tren trenInterno = new Tren("Tren Interno", TERMINALES);
        Thread tren = new Thread(trenInterno, "Tren Interno");
        tren.start();
        crearPasajeros();
        Aeropuerto viajeBonito = new Aeropuerto("Viaje Bonito", TERMINALES, AEROLINEAS, trenInterno, HORA);
        ControlDia pasoDia = new ControlDia(HORA, viajeBonito);
        Thread control = new Thread(pasoDia, "Simulador Tiempo");
        control.start();
    }
}
