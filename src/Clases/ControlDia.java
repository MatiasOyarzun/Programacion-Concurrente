package Clases;

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
                Thread.sleep(10000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ControlDia.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.hora.addAndGet(1);
            if(this.hora.get() == 6){
            }else{
                if(this.hora.get() == 22){
                }else{
                    if(this.hora.get() == 24){
                        this.hora.set(0);
                    }
                }
            }
            System.out.println("CONTROL DEL DIA, ES LA HORA: "+this.hora.get()+"hs.");
        }
    }
}
