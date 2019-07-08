package Clases;

/**
 *
 * @author OyarzunMatias
 */
public class Aerolinea {
    private PuestoAtencion puestoAtencion;
    private String nombreAerolinea;
    
    public Aerolinea(String nombreAerolinea, PuestoAtencion puesto){
        this.nombreAerolinea = nombreAerolinea;
        this.puestoAtencion = puesto;
    }

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
    
    
}
