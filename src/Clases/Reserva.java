package Clases;

/**
 *
 * @author OyarzunMatias
 */
public class Reserva {
    
    public Aerolinea aerolinea;
    public int horaVuelo;
    private Terminal terminal;
    private int puestoEmbarque;
    
    public Reserva(Aerolinea aerolinea, int hora, Terminal terminal, int puesto){
        this.aerolinea = aerolinea;
        this.horaVuelo = hora;
        this.terminal = terminal;
        this.puestoEmbarque = puesto;
    }
    
    public Aerolinea getAerolinea(){
        return this.aerolinea;
    }
    
    public void setAerolinea(Aerolinea aerolinea){
        this.aerolinea = aerolinea;
    }
    
    public Terminal getTerminal(){
        return this.terminal;
    }
    
    public void setTerminal(Terminal terminal){
        this.terminal = terminal;
    }
    
    public void setPuestoEmbarque(int puesto){
        this.puestoEmbarque = puesto;
    }
    
    public int getPuestoEmbarque(){
        return this.puestoEmbarque;
    }
}
