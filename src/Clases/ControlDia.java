package clases;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author OyarzunMatias
 */
public class ControlDia implements Runnable {

    private static final int HORARIO_INICIO_ATENCION = 6;
    private static final int HORARIO_FIN_ATENCION = 22;
    private static final int HORARIO_COMIENZO_NUEVO_DIA = 24;
    private final AtomicInteger hora;
    private final Aeropuerto aeropuerto;

    public ControlDia(AtomicInteger hora, Aeropuerto aeropuerto) {
        this.hora = hora;
        this.aeropuerto = aeropuerto;
    }

    @Override
    public void run() {
        int i = 1;
        Terminal[] terminales = this.aeropuerto.getTerminales();
        int cantTerminales = terminales.length;
        Tienda tienda;
        while (true) {
            try {
                Thread.sleep(2000);
                this.hora.addAndGet(1);
                if (this.hora.get() == HORARIO_INICIO_ATENCION) {
                    this.aeropuerto.comenzarHorarioAtencion();
                    Thread.sleep(200);
                } else {
                    if (this.hora.get() == HORARIO_FIN_ATENCION) {
                        this.aeropuerto.terminarHorarioAtencion();
                        Thread.sleep(200);
                    } else {
                        if (this.hora.get() == HORARIO_COMIENZO_NUEVO_DIA) {
                            System.out.println("\n\n--------DIA: " + i + " FINALIZADO!!.--------\n\n");
                            i++;
                            for (int j = 0; j < cantTerminales; j++) {
                                tienda = terminales[j].getTienda();
                                tienda.reponerProductos();
                            }
                            this.hora.set(0);
                        }
                    }

                }
                System.out.println("\n\nCONTROL DEL DIA, ES LA HORA: " + this.hora.get() + "hs.\n\n");
                this.notificarTerminalesHora(terminales);
            } catch (InterruptedException ex) {
                Logger.getLogger(ControlDia.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void notificarTerminalesHora(Terminal[] terminales){
        int cantTerminales = terminales.length;
        for (int i = 0; i < cantTerminales; i++) {
            terminales[i].actualizarHoraTerminal();
        }
    }
}
