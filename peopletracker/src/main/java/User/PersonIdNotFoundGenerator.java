package main.java.User;

import java.io.IOException;

public class PersonIdNotFoundGenerator {
    private RegisterPerson registerPerson = new RegisterPerson();
    private int idCounter = 999;

    public PersonIdNotFoundGenerator() throws IOException {
    }

    public int getNextId(){
        return idCounter++;
    }
}
