package clases;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * @author OyarzunMatias, clase que modela el paso de las horas durante los dias
 */
public class ControlDia implements Runnable {

    /*
    *   Variables:
    *   • HORARIO_INICIO_ATENCION: constante que representa el horario en el que se da inicio al horario de atencion
    *   • HORARIO_FIN_ATENCION: constante que representa el horario en el que se da fin al horario de atencion
    *   • HORARIO_COMIENZO_NUEVO_DIA: constante que representa cuando comienza un nuevo dia
    *   • hora: representa la hora actual del dia
    *   • aeropuerto: representa el aeropuerto que debera controlar el horario durante el paso de los dias
    */
    private static final int HORARIO_INICIO_ATENCION = 6;
    private static final int HORARIO_FIN_ATENCION = 22;
    private static final int HORARIO_COMIENZO_NUEVO_DIA = 24;
    private final AtomicInteger hora;
    private final Aeropuerto aeropuerto;

    //Constructor
    public ControlDia(AtomicInteger hora, Aeropuerto aeropuerto) {
        this.hora = hora;
        this.aeropuerto = aeropuerto;
    }

    @Override
    //Metodo redefinido que invocaran aquellos hilos de ControlDia
    public void run() {
        //Declaracion variables
        int i = 1;
        Terminal[] terminales = this.aeropuerto.getTerminales();
        int cantTerminales = terminales.length;
        Tienda tienda;
        
        while (true) {
            try {
                Thread.sleep(2000);
                this.hora.addAndGet(1);
                //Comienza horario atencion
                if (this.hora.get() == HORARIO_INICIO_ATENCION) {
                    this.aeropuerto.comenzarHorarioAtencion();
                    Thread.sleep(200);
                } else {
                    //Finaliza horario atencion
                    if (this.hora.get() == HORARIO_FIN_ATENCION) {
                        this.aeropuerto.terminarHorarioAtencion();
                        Thread.sleep(200);
                    } else {
                        //Comienza nuevo dia y se reponen productos en las tiendas de las terminales
                        if (this.hora.get() == HORARIO_COMIENZO_NUEVO_DIA) {
                            this.hora.set(0);
                            System.out.println("\n\n--------DIA: " + i + " FINALIZADO!!.--------\n\n");
                            i++;
                            for (int j = 0; j < cantTerminales; j++) {
                                tienda = terminales[j].getTienda();
                                tienda.reponerProductos();
                            }
                            System.out.println("\n\n--------DIA: " + i + " COMIENZA!!.--------\n\n");
                        }
                    }

                }
                System.out.println("\n\nCONTROL DEL DIA, ES LA HORA: " + this.hora.get() + "hs.\n\n");
                //Notifica la hora a las terminales
                this.notificarTerminalesHora(terminales);
            } catch (InterruptedException ex) {
                Logger.getLogger(ControlDia.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    //Metodo que permite notificar a las terminales la hora actual
    private void notificarTerminalesHora(Terminal[] terminales){
        int cantTerminales = terminales.length;
        for (int i = 0; i < cantTerminales; i++) {
            terminales[i].actualizarHoraTerminal();
        }
    }
}
