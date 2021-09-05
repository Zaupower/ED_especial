package main.java.Hotel;

/**
 * Contacto
 */
public class Contacto {
    private int id;
    private int hora;

    /**
     * Contacto Constructor
     * @param id
     * @param hora
     */
    public Contacto(int id, int hora) {
        this.id = id;
        this.hora = hora;
    }

    /**
     * Get id
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * Set id
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Get hora
     * @return
     */
    public int getHora() {
        return hora;
    }

    /**
     * Set hora
     * @param hora
     */
    public void setHora(int hora) {
        this.hora = hora;
    }

    /**
     * toString method
     * @return
     */
    @Override
    public String toString() {
        return "Contacto{" +
                "id=" + id +
                ", hora=" + hora +
                '}';
    }
}
