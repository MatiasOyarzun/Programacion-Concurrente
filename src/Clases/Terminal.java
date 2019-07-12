package clases;

/**
 *
 * @author OyarzunMatias
 */
public class Terminal {
    
    private int[] puestosEmbarque;
    private final Tienda tienda;
    private char letraTerminal;
    
    public Terminal(char letra, int[] puestos, Tienda tienda){
        this.letraTerminal = letra;
        this.puestosEmbarque = puestos;
        this.tienda = tienda;
    }
    
    public int[] getPuestos(){
        return this.puestosEmbarque;
    }
    
    public void setPuestos(int[] puestos){
        this.puestosEmbarque = puestos;
    }
    
    public char getLetra(){
        return this.letraTerminal;
    }
    
    public void setLetra(char letraTerminal){
        this.letraTerminal = letraTerminal;
    }
}
