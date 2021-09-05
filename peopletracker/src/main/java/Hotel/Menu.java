package main.java.Hotel;

import DateTimeRegex.FormattedDateMatcher;
import main.java.Enum.PersonType;
import main.java.Estruturas.*;
import main.java.SensorLib.SensorManager;
import main.java.User.Person;
import main.java.User.RegisterPerson;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Iterator;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Menu {
    private String mapFileName = null;
    private String movimentsFileName = null;
    RegisterPerson registerPerson = new RegisterPerson();

    public void run() throws IOException, InvalidIndexException, EmptyException, NotFoundException, ParseException, NoComparableException {
        int choice = 0;
        System.out.println("Welcome to the Smart Tracking System");
        while (choice != 7) {

            choice = initialMenu();

            switch (choice) {
                case 1:
                    registerUserMenu();
                    break;
                case 2:
                    preloadMapAndMoviments();
                    break;
                case 3:
                    getContactosDosUtilizadores();
                    break;
                case 4:
                    printAllPersonsRegistered();
                    break;
                case 5:
                    prinSensorsAlerts();
                    break;
                case 6:
                    findPersonOnMap();
                    break;
                case 7:
                    printMap();
                    break;
                default:

            }
        }

    }

    /**
     * Print map divisions
     * @throws InvalidIndexException
     * @throws EmptyException
     * @throws NotFoundException
     * @throws IOException
     * @throws ParseException
     * @throws NoComparableException
     */
    private void printMap() throws InvalidIndexException, EmptyException, NotFoundException, IOException, ParseException, NoComparableException {
        isMapReadOrMoviments();
        Mapa m = new Mapa();
        m.lerJson(this.mapFileName);
        SensorManager sensorManager = new SensorManager(m, this.mapFileName);
        sensorManager.readMoves(this.movimentsFileName, false, m);
        sensorManager.printDivisions();
    }

    /**
     * find person on the map
     * @throws IOException
     * @throws InvalidIndexException
     * @throws EmptyException
     * @throws NotFoundException
     * @throws ParseException
     * @throws NoComparableException
     */
    private void findPersonOnMap() throws IOException, InvalidIndexException, EmptyException, NotFoundException, ParseException, NoComparableException {
        isMapReadOrMoviments();
        Scanner input = new Scanner(System.in);
        String choice;
        do {
            System.out.println("Please Select a User type");
            System.out.println("1- Hospede");
            System.out.println("2- Funcionario");
            choice = input.next();
        } while (!regexState(choice, 1, 2) /*Exit loop when choice is 2*/);

        if (choice.equals("1")) {
            findPerson(PersonType.HOSPEDE);
        } else if (choice.equals("2")) {
            findPerson(PersonType.FUNCIONARIO);
        }

    }

    /**
     * Find a person on the map
     * @param type
     * @throws IOException
     * @throws InvalidIndexException
     * @throws EmptyException
     * @throws NotFoundException
     * @throws ParseException
     * @throws NoComparableException
     */
    private void findPerson(PersonType type) throws IOException, InvalidIndexException, EmptyException, NotFoundException, ParseException, NoComparableException {

        ArrayUnorderedList<Person> persons = getPeopleFromType(type);
        Person p = chooseAPerson(persons);
        Mapa m = new Mapa();
        m.lerJson(this.mapFileName);
        SensorManager sensorManager = new SensorManager(m, this.mapFileName);
        sensorManager.readMoves(this.movimentsFileName, false, m);
        Divisao d = sensorManager.findPersonById(p.getIdPessoa(), this.movimentsFileName, m);
        System.out.println(" ");
        if (d != null) {

            System.out.println(" ");
            System.out.println("Pessoa encontrada: " + d.getNome());

            printBestPathToQuarentine(p.getIdPessoa());
        } else {
            System.out.println("Person not found in the hotel");
        }
    }

    /**
     * Print the best Path to the quarentine
     * @param idPessoa
     * @throws InvalidIndexException
     * @throws EmptyException
     * @throws NotFoundException
     * @throws IOException
     * @throws ParseException
     * @throws NoComparableException
     */
    private void printBestPathToQuarentine(int idPessoa) throws InvalidIndexException, EmptyException, NotFoundException, IOException, ParseException, NoComparableException {
        isMapReadOrMoviments();
        Scanner input = new Scanner(System.in);
        String choice;

        System.out.println("Do you want to get the best path to quarentine? Y/n");
        choice = input.next();
        if (choice.equals("Y")) {
            Mapa m = new Mapa();
            m.lerJson(this.mapFileName);
            SensorManager sensorManager = new SensorManager(m, this.mapFileName);
            sensorManager.findBestPAthToQuarentine(idPessoa, this.movimentsFileName, m);
        }
    }

    /**
     * Print sensors Alerts while reading moviments on the map
     * @throws InvalidIndexException
     * @throws EmptyException
     * @throws NotFoundException
     * @throws IOException
     * @throws ParseException
     * @throws NoComparableException
     */
    private void prinSensorsAlerts() throws InvalidIndexException, EmptyException, NotFoundException, IOException, ParseException, NoComparableException {
        isMapReadOrMoviments();

        Mapa m = new Mapa();
        m.lerJson(this.mapFileName);
        SensorManager sensorManager = new SensorManager(m, this.mapFileName);

        System.out.println(" ");
        System.out.println("Read moves and check Alerts");
        sensorManager.readMoves(this.movimentsFileName, true, m);
    }

    /**
     * Preload Mapa and Movimentos
     */
    private void preloadMapAndMoviments() {
        String mapFileName = readMap();
        String movesFileName = readMoviments();
        this.mapFileName = mapFileName;
        this.movimentsFileName = movesFileName;
    }

    /**
     * Verify if Map string name is load and moviments
     * @return
     */
    private boolean isMapReadOrMoviments() {
        boolean isread = false;
        if (this.mapFileName == null) {
            String mapFileName = readMap();

            this.mapFileName = mapFileName;
            isread = true;
        }
        if (this.movimentsFileName == null) {
            String movesFileName = readMoviments();

            this.movimentsFileName = movesFileName;
            isread = true;
        }

        return isread;
    }

    /**
     * Prnts all registered persons on the program to the cnsole
     * @throws IOException
     */
    private void printAllPersonsRegistered() throws IOException {
        RegisterPerson registerPerson = new RegisterPerson();
        System.out.println("Printing all registered persons");
        registerPerson.printAll();
    }

    public Integer initialMenu() {

        Scanner input = new Scanner(System.in);
        String choice;
        do {
            System.out.println("Choose from these choices");
            System.out.println("-------------------------\n");
            System.out.println("1 - Register User");
            System.out.println("2 - Preload Map and Moviments");
            System.out.println("3 - View user contacts");
            System.out.println("4 - View All registered persons");
            System.out.println("5 - View All Sensors Alerts");
            System.out.println("6 - Find Person On Map");
            System.out.println("7 - Visualize All Rooms");
            System.out.println("8 - Exit");

            choice = input.next();
        } while (!regexState(choice, 1, 8) /*Exit loop when choice is 4*/);
        return Integer.valueOf(choice);
    }

    public void getContactosDosUtilizadores() throws IOException, InvalidIndexException, EmptyException, NotFoundException, ParseException, NoComparableException {

        Scanner input = new Scanner(System.in);
        String choice;
        do {
            System.out.println("Please Select a main.java.User type");
            System.out.println("1- Hospede");
            System.out.println("2- Funcionario");
            choice = input.next();
        } while (!regexState(choice, 1, 2) /*Exit loop when choice is 4*/);

        if (choice.equals("1")) {
            getContactsFromPerson(PersonType.HOSPEDE);
        } else if (choice.equals("2")) {
            getContactsFromPerson(PersonType.FUNCIONARIO);
        }
    }

    /**
     *  Get all persons from one type
     * @param hospede
     * @throws IOException
     * @throws InvalidIndexException
     * @throws EmptyException
     * @throws NotFoundException
     * @throws ParseException
     * @throws NoComparableException
     */
    private void getContactsFromPerson(PersonType hospede) throws IOException, InvalidIndexException, EmptyException, NotFoundException, ParseException, NoComparableException {
        ArrayUnorderedList<Person> personsFromOneType = getPeopleFromType(hospede);
        Person p = chooseAPerson(personsFromOneType);
        FormattedDateMatcher dateMatcher = new FormattedDateMatcher();
        System.out.println("Person selected: " + p.getName());

        String choice = " ";

        do {
            choice = getDate();
        }while (regexHour(choice));
        isMapReadOrMoviments();

        System.out.println("Map choosed: " + this.mapFileName);
        System.out.println("Moviments choose: " + this.movimentsFileName);

        String mapPath = "mapa.json";

        Mapa m = new Mapa();
        m.lerJson(this.mapFileName);

        SensorManager sensorManager = new SensorManager(m, this.mapFileName);

        System.out.println(" ");
        System.out.println("Read moves and check Alerts");
        sensorManager.readMoves(this.movimentsFileName, false, m);

        String hora = "2020-07-14 14:40";
        int personId = p.getIdPessoa();

        m = new Mapa();
        m.lerJson(mapPath);
        sensorManager.getPersonContactsFromLastHours(hora, 2, m);


    }

    /**
     *  get date Sacnnner
     * @return String date
     */
    private String getDate() {
        String choice;
        Scanner input = new Scanner(System.in);
        System.out.println("Please select a date (YYY-MM-DD HH:MM) in this format");
        choice = input.next();
        return choice;
    }

    /**
     *  Regex String to verify if its a Datetime string
     * @param hora String
     * @return boolean
     */
    private boolean regexHour(String hora) {
        String pat = "[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1]) (2[0-3]|[01][0-9]):[0-5][0-9]";
        Pattern pattern = Pattern.compile(pat);
        Matcher matcher = pattern.matcher(hora);
        return matcher.find();
    }

    /**
     *  Print all file names in the movimentos directory
     * @return String fileName
     */
    private String readMoviments() {
        ArrayUnorderedList<String> results = new ArrayUnorderedList<>();
        int counter = 1;
        System.out.println("Choose a file");

        File[] files = new File("./movimentos").listFiles();
        //If this pathname does not denote a directory, then listFiles() returns null.
        for (File file : files) {
            if (file.isFile()) {
                results.addToRear(file.getName());
                System.out.println(counter + " - " + file.getName());
                counter++;
            }
        }

        int movesFile;
        Scanner inputMoves = new Scanner(System.in);

        movesFile = inputMoves.nextInt();
        if (movesFile <= results.size()) {
            System.out.println("Valid file!");
            System.out.println(results.getIndex(movesFile - 1));
        } else {
            System.out.println("Invalid File! PRESS X to leave");
            return "NotFound";
        }

        return results.getIndex(movesFile - 1);
    }

    /**
     * Prints all files names in the Maps directory
     * @return String map choosed
     */
    private String readMap() {
        ArrayUnorderedList<String> results = new ArrayUnorderedList<>();
        int counter = 1;
        System.out.println("Choose a file");

        File[] files = new File("./Maps").listFiles();
        //If this pathname does not denote a directory, then listFiles() returns null.

        for (File file : files) {
            if (file.isFile()) {
                results.addToRear(file.getName());
                System.out.println(counter + " - " + file.getName());
                counter++;
            }
        }

        int mapFile;
        Scanner inputMoves = new Scanner(System.in);

        mapFile = inputMoves.nextInt();
        if (mapFile <= results.size()) {
            System.out.println("Valid file!");
            System.out.println(results.getIndex(mapFile - 1));
        } else {
            return "NotFound";
        }
        return results.getIndex(mapFile - 1);
    }

    /**
     * Get list of all people registerd from one type
     * @param type
     * @return ArrayList of All people registerd from a given PersonType
     * @throws IOException
     */
    private ArrayUnorderedList<Person> getPeopleFromType(PersonType type) throws IOException {
        System.out.println("Printing All Hospedes on the Hotel");
        ArrayUnorderedList<Person> personsFromOneType = registerPerson.getAllFromTypeUsers(type);
        return personsFromOneType;
    }

    /**
     *  Choose a person from one type
     * @param personsFromOneType
     * @return Person p
     */
    private Person chooseAPerson(ArrayUnorderedList<Person> personsFromOneType) {

        Scanner input = new Scanner(System.in);
        String choice;
        int counter = 0;
        do {
            counter = 1;
            System.out.println();
            Iterator<Person> it = personsFromOneType.iterator();
            while (it.hasNext()) {
                Person p = it.next();
                System.out.println(" " + counter + " - Name: " + p.getName() + " id: " + p.getIdPessoa() + " role: " + p.getRole());
                counter++;
            }

            System.out.println("Please Select a User to view his contacts or press X to leave");
            choice = input.next();
        } while (!regexState(choice, 1, counter));
        Person p = personsFromOneType.getIndex(Integer.valueOf(choice) - 1);
        System.out.println("Person choosed: " + p.getName());
        return p;
    }

    /**
     *  Register one user
     * @throws IOException
     */
    public void registerUserMenu() throws IOException {
        boolean isDone = false;
        boolean isRoleOk = true;
        boolean saveIt = false;
        String name;
        String roleNumber;
        PersonType role = null;
        do {
            System.out.println("Input name");
            Scanner inputName = new Scanner(System.in);

            name = inputName.next();


            System.out.println("Chose Role");
            System.out.println("1 - FUNCIONARIO");
            System.out.println("2 - HOSPEDE");
            Scanner inputRole = new Scanner(System.in);

            roleNumber = inputRole.next();
            switch (roleNumber) {
                case "1":
                    role = PersonType.FUNCIONARIO;
                    break;
                case "2":
                    role = PersonType.HOSPEDE;
                    break;
                default:
                    isRoleOk = false;
            }

            System.out.println("Add user, name: " + name + ", role: " + role.toString());
            System.out.println("Save? Y/n or X to exit without saving");
            Scanner inputFinish = new Scanner(System.in);
            String isFinished;
            isFinished = inputFinish.next();

            switch (isFinished) {
                case "Y":

                    if (isRoleOk) {
                        isDone = true;
                        saveIt = true;
                    }
                    break;
                case "X":
                    return;
                default:
            }
        } while (!isDone);

        if (saveIt) {
            RegisterPerson registerPerson = new RegisterPerson();
            Person p = new Person(name, role);
            registerPerson.appendPersonToFile(p);
        }
    }

    /**
     * RegexState for dynamec list printing
     * @param choice
     * @param init
     * @param end
     * @return boolean
     */
    public boolean regexState(String choice, int init, int end) {

        String pat = "^[" + init + "-" + end + "]+$";
        Pattern pattern = Pattern.compile(pat);
        Matcher matcher = pattern.matcher(choice);
        return matcher.find();
    }
}
