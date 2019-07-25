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
    
    //Constante que representa la cantidad de terminales que habra en el aeropuerto
    private static final int CANTTERMINALES = 3;
    //Constante que representa la cantidad de personas que podran estar en el tren
    private static final int CANTCARGATREN = 5;
    //Constante que representa la cantidad de aerolineas en el aeropuerto
    private static final int CANTAEROLINEAS = 3;
    //Creo array de Aerolineas del tamaño antes declarado
    private static final Aerolinea[] AEROLINEAS = new Aerolinea[CANTAEROLINEAS];
    //Creo array, con sus valores ya inicializado de los nombres de aerolineas
    private static final String[] NOMBRESAEROLINEAS = {"AEROLINEAS ARGENTINAS", "LAN", "LATAM"};
    //Constante que representa la cantidad de cajas que habra por tienda
    private static final int CANTCAJASXTIENDA = 2;
    //Constante que representa la cantidad de puestos por terminal
    private static final int CANTPUESTOSTERMINAL = 7;
    //Creo array de Terminales del tamaño antes declarado
    private static final Terminal[] TERMINALES = new Terminal[CANTTERMINALES];
    //Creo array, con sus valores ya inicializados de los nombres de las terminales
    private static final char[] LETRASTERMINALES = {'A', 'B', 'C'};
    //Creo AtomicInteger que representara el paso de las horas durante los dias
    private static final AtomicInteger HORA = new AtomicInteger(0);
    //Creo Random para utilizarlo en caso necesario
    private static final Random RANDOM = new Random();
    //Constante que representa la cantidad de pasajeros que ingresaran al aeropuerto (debe ser multiplo de la carga del tren para que nunca se bloquee)
    private static final int CANTPASAJEROS = CANTCARGATREN*(RANDOM.nextInt(10)+3);
    
    
    /*
    *   Metodo que permite crear los puestos de cada terminal, y la tienda de cada terminal, y finalmente crear la terminal
    *   seteandole su letra, puestos de la terminal, tienda, y la hora.
    */
    public static void crearTerminales(){
        //Declaracion de variables
        int numPuesto = 1;
        
        //Repetitiva que permite crear las terminales, los puestos de las terminales, y la tienda de la terminal
        for (int i = 0; i < CANTTERMINALES; i++) {
            int[] puestosTerminal = new int[CANTPUESTOSTERMINAL];
            Tienda nuevaTienda;
            for (int j = 0; j < CANTPUESTOSTERMINAL; j++) {
                puestosTerminal[j] = numPuesto;
                numPuesto++;
            }
            nuevaTienda = crearTienda(LETRASTERMINALES[i]);
            TERMINALES[i] = new Terminal(LETRASTERMINALES[i], puestosTerminal, nuevaTienda, HORA);
        }
    }
    
    /*
    *   Metodo que permite crear las cajas de las tiendas, con sus cajeras y sus respectivos hilos,  y luego
    *   setearle estos al momento de la creacion de la tienda.
    */
    public static Tienda crearTienda(char letraTerminal){
        //Declaracion de variables
        CajaTienda[] cajas = new CajaTienda[CANTCAJASXTIENDA];
        Tienda tienda;
        
        //Repetitiva que permite crear las cajas y las cajeras de estas, y sus respectivos hilos
        for (int i = 0; i < CANTCAJASXTIENDA; i++) {
            cajas[i] = new CajaTienda(i);
            CajeraTienda cajera = new CajeraTienda(i, cajas[i]);
            Thread nuevaCajera = new Thread(cajera, ("CAJERA: "+i+" - TIENDA TERMINAL: "+letraTerminal));
            nuevaCajera.start();
        }
        
        //Creo la tienda, con las cajas que tendra y su respectivo nombre
        tienda = new Tienda(cajas, "TIENDA TERMINAL: "+letraTerminal);
        return tienda;
    }
    
    /*
    *   Metodo que permite crear las aerolineas, con sus guardias, puestos de atencion, y sus respectivos hilos
    */
    public static void crearAerolineas(){
        
        //Repetitiva que permite crear los guardias que daran paso en cada puesto de aerolinea, los puestos, y finalmente la aerolinea
        for (int i = 0; i < CANTAEROLINEAS; i++) {
            Guardia nuevoGuardia = new Guardia((i+1), "Guardia: "+(i+1));
            Thread guardia = new Thread(nuevoGuardia);
            PuestoAtencion nuevoPuesto = new PuestoAtencion("Puesto: "+NOMBRESAEROLINEAS[i], nuevoGuardia);
            nuevoGuardia.setPuesto(nuevoPuesto);
            guardia.start();
            AEROLINEAS[i] = new Aerolinea(NOMBRESAEROLINEAS[i], nuevoPuesto);
        }
    }
    
    /*
    *   Metodo que permite crear los pasajeros, crear la reserva de forma aleatoria del pasajero, con su aerolinea, hora de viaje, terminal y el puesto de terminal,
    *   ademas permite indicar con un booleano, si el pasajero comprara o no en la tienda de la terminal, siempre y cuando tenga tiempo.
    */
    public static void crearPasajeros(Aeropuerto viajeBonito){
        
        //Declaracion de variables
        int indiceAerolinea, horaViaje, indiceTerminal, indicePuesto, puesto;
        Aerolinea aerolineaPasajero;
        Terminal terminalPasajero;
        Boolean verTienda, comprarTienda;
        int i = 0;
        
        /*  
        *   Repetitiva que permite crear las reservas de los pasajeros, con su aerolinea, hora de viaje, terminal y puesto de terminal, luego de forma random
        *   setear a las variables booleanas "verTienda" y "comprarTienda", valores booleanos para ver si solo veran o si veran y compraran en la tienda de la terminal,
        *   finalmente crear el hilo respectivo del pasajero y darle inicio a la ejecucion.
        */
        while (i < CANTPASAJEROS) {
            indiceAerolinea = RANDOM.nextInt(CANTAEROLINEAS);
            aerolineaPasajero = AEROLINEAS[indiceAerolinea];
            horaViaje = RANDOM.nextInt(24);
            indiceTerminal = RANDOM.nextInt(CANTTERMINALES);
            terminalPasajero = TERMINALES[indiceTerminal];
            indicePuesto = RANDOM.nextInt(CANTPUESTOSTERMINAL);
            puesto = (terminalPasajero.getPuestos())[indicePuesto];
            Reserva nuevaReserva = new Reserva(aerolineaPasajero, horaViaje, terminalPasajero, puesto);
            //Solo si ambos ("verTienda" y "comprarTienda") son verdaderos el pasajero ingresara a comprar.
            verTienda = RANDOM.nextBoolean();
            comprarTienda = RANDOM.nextBoolean();
            System.out.println("Pasajero: "+(i+1)+" VER TIENDA: "+verTienda+" COMPRAR TIENDA: "+comprarTienda+" hora vuelo: "+nuevaReserva.getHoraVuelo());
            Pasajero nuevoPasajero = new Pasajero((i+1), "Pasajero: "+(i+1), nuevaReserva, viajeBonito, verTienda, comprarTienda, HORA);
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

    /*
    *   Metodo main, que invoca los metodos anteriores, en el se crea el tren, junto con su hilo, el aeropuerto, y la clase "ControlDia" que se encargara de controlar
    *   el paso de los dias (las horas).
    */
    public static void main(String[] args) {
        
        //Declaracion de variables
        CargaTren carga;
        Tren trenInterno;
        Aeropuerto viajeBonito;
        ControlDia pasoDia;
        
        crearTerminales();
        crearAerolineas();
        carga = new CargaTren(CANTCARGATREN, CANTTERMINALES);
        trenInterno = new Tren("Tren Interno", TERMINALES, carga, CANTTERMINALES);
        Thread tren = new Thread(trenInterno, "Tren Interno");
        tren.start();
        viajeBonito = new Aeropuerto("Viaje Bonito", TERMINALES, AEROLINEAS, trenInterno);
        pasoDia = new ControlDia(HORA, viajeBonito);
        Thread control = new Thread(pasoDia, "Simulador Tiempo");
        control.start();
        System.out.println("---------------------------SE CREARAN: "+CANTPASAJEROS+" PASAJEROS-------------------------");
        crearPasajeros(viajeBonito);
    }
}