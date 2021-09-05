package main.java.User;

import java.io.IOException;

public class PersonIdGenerator {
    private RegisterPerson registerPerson = new RegisterPerson();
    private int idCounter = registerPerson.getRegisteredUsers().length;

    public PersonIdGenerator() throws IOException {
    }

    public int getNextId(){
        return idCounter++;
    }
}
