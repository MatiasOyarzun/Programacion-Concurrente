package Clases;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author OyarzunMatias
 */
public class ControlDia implements Runnable{
    
    private static final int HORARIO_INICIO_ATENCION = 6;
    private static final int HORARIO_FIN_ATENCION = 22;
    private AtomicInteger hora;
    private Aeropuerto aeropuerto;
    
    public ControlDia(AtomicInteger hora){
        this.hora = hora;
    }
    
    @Override
    public void run(){
        while(true){
            
        }
    }
}
