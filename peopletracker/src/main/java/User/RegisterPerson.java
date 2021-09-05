package main.java.User;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import main.java.Enum.PersonType;
import main.java.Estruturas.ArrayUnorderedList;
import main.java.Estruturas.NoComparableException;
import main.java.Hotel.Contacto;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Register a person on the txt file
 */
public class RegisterPerson {
    Gson gson = new Gson();
    Person[] registerdUsers = null;
    private String fileNamePath  = "./registeredPersons/registeredPersons.json";

    public boolean appendPersonToFile(Person p) throws IOException {
        Person person = p;
        Person[] persons = getRegisteredUsers();
        boolean alreadyRegisterd = false;
        Person[] tmp = new Person[persons.length + 1];

        for (int i = 0; i < persons.length; i++) {
            tmp[i] = persons[i];
            if (persons[i].getName().equals(person.getName())) {
                System.out.println("Pessoa já registrada");
                alreadyRegisterd = true;
            }
        }
        if (!alreadyRegisterd) {

            tmp[tmp.length - 1] = person;
            GsonBuilder builder = new GsonBuilder();
            builder.setPrettyPrinting();

            Gson gson = builder.create();

            FileWriter writer = new FileWriter(this.fileNamePath);

            gson.toJson(tmp, writer);

            writer.flush();
            writer.close();
        }
        return true;
    }

    public Person[] getRegisteredUsers() throws IOException {
        Gson gson = new Gson();

        Reader reader = Files.newBufferedReader(Paths.get(this.fileNamePath));

        Person[] persons = gson.fromJson(reader, Person[].class);

        reader.close();
        this.registerdUsers = persons;
        return persons;
    }

    public ArrayUnorderedList<Person> getAllFromTypeUsers(PersonType personType) throws IOException {

            Person[] people = this.getRegisteredUsers();
            ArrayUnorderedList<Person> personsFromOneType = new ArrayUnorderedList<>();

        for (int i = 0; i< people.length; i++){
            if (people[i].getRole() == personType){
                personsFromOneType.addToRear(people[i]);
            }
        }
        return personsFromOneType;
    }

    public Person getPersonById(int id) throws IOException {
        Person[] regPersons = this.getRegisteredUsers();

        for (int i = 0; i< regPersons.length; i++){
            if (regPersons[i].getIdPessoa() == id){
                return regPersons[i];
            }
        }
        Person notFound = new Person("NotFound", PersonType.HOSPEDE);
        PersonIdNotFoundGenerator personIdNotFoundGenerator = new PersonIdNotFoundGenerator();
        notFound.setRole(PersonType.NOTREGISTERED);
        notFound.setIdPessoa(personIdNotFoundGenerator.getNextId());

        return notFound;
    }
    public int getPersonIndex(int id) throws IOException {
        Person[] regPersons = this.getRegisteredUsers();

        for (int i = 0; i< regPersons.length; i++){
            if (regPersons[i].getIdPessoa() == id){
                return i;
            }
        }
        return -1;
    }

    public void updateContactos(int id, Contacto contacto) throws IOException, NoComparableException {
       int index =  getPersonIndex( id);

       if (index != -1){
           this.registerdUsers[index].setContactosId(contacto.getId(), contacto.getHora());
           updateJson();
       }

    }

    private void updateJson() throws IOException {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();

        FileWriter writer = new FileWriter(this.fileNamePath);

        gson.toJson(this.registerdUsers, writer);
        writer.flush();
        writer.close();
    }

    public void printAll() throws IOException {
        Person[] regPersons = this.getRegisteredUsers();

        for (int i = 0; i< regPersons.length; i++){
            Person p = regPersons[i];
            System.out.println("Name: "+ p.getName()+" id: "+p.getIdPessoa() + " role: "+p.getRole());
        }
    }
}
