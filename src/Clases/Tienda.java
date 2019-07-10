package clases;

/**
 *
 * @author OyarzunMatias
 */
public class Tienda {
    
    private static final int CANTMAXTIENDA = 5;
    public CajaTienda[] cajasTienda;
    
    public Tienda(CajaTienda[] cajas){
        this.cajasTienda = cajas;
    }
    
    public void setCajasTienda(CajaTienda[] nuevasCajas){
        this.cajasTienda = nuevasCajas;
    }
    
    public CajaTienda[] getCajasTienda(){
        return this.cajasTienda;
    }
}
