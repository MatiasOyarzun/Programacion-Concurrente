package Clases;

import java.util.Objects;

/**
 * @author OyarzunMatias, Clase abstracta Persona para hacer buen uso de la
 * programacion orientada a objetos
 */
public abstract class Persona {

    /*
    * Variables: 
    * • nombre: Nombre de la persona
    * • id: Codigo unico que permitira identificar a la persona
     */
    protected String nombre;
    protected int id;

    /*
    * Constructor que solo usaran las clases hijas
     */
    public Persona(int id) {
        this.id = id;
    }

    /*
    * Constructor que solo usaran las clases hijas
     */
    public Persona(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    /*
    * Getters y Setters
     */
    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /*
    *   Metodos equals y toString redefinidos
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
        final Persona other = (Persona) obj;
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Persona: "+this.nombre+" su id es: "+this.id;
    }
}
