package clases;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author OyarzunMatias
 */
public class ControlDia implements Runnable{
    
    private static final int HORARIO_INICIO_ATENCION = 6;
    private static final int HORARIO_FIN_ATENCION = 22;
    private static final int HORARIO_COMIENZO_NUEVO_DIA = 24;
    private AtomicInteger hora;
    private Aeropuerto aeropuerto;
    
    public ControlDia(AtomicInteger hora, Aeropuerto aeropuerto){
        this.hora = hora;
        this.aeropuerto = aeropuerto;
    }
    
    @Override
    public void run(){
        while(true){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ControlDia.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.hora.addAndGet(1);
            if(this.hora.get() == HORARIO_INICIO_ATENCION){
                this.aeropuerto.comenzarHorarioAtencion();
            }else{
                if(this.hora.get() == HORARIO_FIN_ATENCION){
                    this.aeropuerto.terminarHorarioAtencion();
                }else{
                    if(this.hora.get() == HORARIO_COMIENZO_NUEVO_DIA){
                        this.hora.set(0);
                    }
                }
            }
            System.out.println("CONTROL DEL DIA, ES LA HORA: "+this.hora.get()+"hs.");
        }
    }
}
