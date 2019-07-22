package clases;

/**
 * @author OyarzunMatias, Clase que modela una Reserva en el aeropuerto y su funcionalidad
 */
public class Reserva {
    
    /*
    *   Variables: 
    *   • aerolinea: representa la aerolinea que el pasajero contrato para viajar
    *   • horaVuelo: representa la hora del vuelo que el pasajero reservo
    *   • terminal: representa la terminal a la que el pasajero debera trasladarse para encontrar su vuelo
    *   • puestoEmbarque: representa el puesto de embarque de la terminal, a la que debera esperar el vuelo
    */
    public Aerolinea aerolinea;
    public int horaVuelo;
    private Terminal terminal;
    private int puestoEmbarque;
    
    //Constructor
    public Reserva(Aerolinea aerolinea, int hora, Terminal terminal, int puesto){
        this.aerolinea = aerolinea;
        this.horaVuelo = hora;
        this.terminal = terminal;
        this.puestoEmbarque = puesto;
    }
    
    /*
    *   Getters y Setters
    */
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
    
    public int getHoraVuelo(){
        return this.horaVuelo;
    }
    
    public void setHoraVuelo(int hora){
        this.horaVuelo = hora;
    }
    
    public void setPuestoEmbarque(int puesto){
        this.puestoEmbarque = puesto;
    }
    
    public int getPuestoEmbarque(){
        return this.puestoEmbarque;
    }
}
