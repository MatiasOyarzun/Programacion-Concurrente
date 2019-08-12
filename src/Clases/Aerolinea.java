package Clases;

import java.util.Objects;

/**
 * @author OyarzunMatias, Clase que simula una Aerolinea y su funcionamiento
 */
public class Aerolinea {
    
    /*
    *   Variables:
    *   • puestoAtencion: puesto de atencion, en el cual seran atendidos aquellos pasajeros, que sean enviados a esta terminal
    *   • nombreAerolinea: nombre de la aerolinea, ya sea LAN, LATAM, etc.
    */
    private PuestoAtencion puestoAtencion;
    private String nombreAerolinea;
    
    // Metodo constructor
    public Aerolinea(String nombreAerolinea, PuestoAtencion puesto){
        this.nombreAerolinea = nombreAerolinea;
        this.puestoAtencion = puesto;
    }

    /*
    * Getters y Setters
    */
    public PuestoAtencion getPuestoAtencion() {
        return this.puestoAtencion;
    }

    public void setPuestoAtencion(PuestoAtencion puestoAtencion) {
        this.puestoAtencion = puestoAtencion;
    }

    public String getNombreAerolinea() {
        return this.nombreAerolinea;
    }

    public void setNombreAerolinea(String nombreAerolinea) {
        this.nombreAerolinea = nombreAerolinea;
    }
    
    /*
    *   Metodos redefinidos, equals y toString.
    */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Aerolinea other = (Aerolinea) obj;
        if (!Objects.equals(this.nombreAerolinea, other.nombreAerolinea)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString(){
        return this.nombreAerolinea;
    }
    
}