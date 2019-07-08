package Clases;

/**
 *
 * @author OyarzunMatias
 */
public class Terminal {
    
    private int[] puestosEmbarque;
    private Tienda tienda;
    private char LetraTerminal;
    
    public Terminal(char letra, int[] puestos, Tienda tienda){
        this.LetraTerminal = letra;
        this.puestosEmbarque = puestos;
        this.tienda = tienda;
    }
}
