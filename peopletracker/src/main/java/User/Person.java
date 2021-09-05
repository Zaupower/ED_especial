package main.java.User;

import main.java.Enum.PersonType;
import main.java.Estruturas.ArrayUnorderedList;
import main.java.Estruturas.NoComparableException;
import main.java.Hotel.Contacto;

import java.io.IOException;

public class Person {
    private String name;
    private int idPessoa;
    private PersonType role;
    private ArrayUnorderedList<Contacto> contactos = new ArrayUnorderedList<>();;
    private transient PersonIdGenerator personIdGenerator = new PersonIdGenerator();

    public Person(String name, PersonType role) throws IOException {
        this.name = name;
        this.idPessoa = personIdGenerator.getNextId();
        this.role = role;
    }

    public void setIdPessoa(int idPessoa) {
        this.idPessoa = idPessoa;
    }

    public void setRole(PersonType role) {
        this.role = role;
    }

    public int getIdPessoa() {
        return idPessoa;
    }

    public String getName() {
        return name;
    }

    public PersonType getRole() {
        return role;
    }

    public ArrayUnorderedList getContactosId() {
        return contactos;
    }

    public ArrayUnorderedList<Contacto> getContactos() {
        return contactos;
    }

    public void addContactos(Contacto contactos) {
        if (this.contactos == null){
            this.contactos = new ArrayUnorderedList<>();
        }
        this.contactos.addToFront(contactos);
    }

    public void setContactosId(int contactosId, int hora) throws NoComparableException, IOException {
        if (contactosId!= this.idPessoa){

            Contacto contacto = new Contacto(contactosId, hora);
           // registerPerson.updateContactos( this.idPessoa,  contacto);
            this.addContactos(contacto);
        }
    }

    @Override
    public String toString() {
        return "Person{" +
                "idPessoa=" + idPessoa +
                ", role=" + role +
                ", contactosId=" + contactos +
                '}';
    }
}
