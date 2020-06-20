// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2018T2, Assignment 2
 * Name: Matthew Corfiatis
 * Username: CorfiaMatt
 * ID: 300447277
 */
import ecs100.*;
import java.util.*;
import java.io.*;

/** Genealogy:
 * Prints out information from a genealogical database
 */

public class Genealogy  {

    // all the people:  key is a name,  value is a Person object
    private final Map<String, Person> database = new HashMap<String, Person>();

    private String selectedName = null;  //currently selected name.

    private boolean databaseHasBeenFixed = false;

    /**
     * Constructor
     */
    public Genealogy() {
        loadData();
        setupGUI();
    }

    /**
     * Buttons and text field for operations.
     */
    public void setupGUI(){
        UI.addButton("Print all names", this::printAllNames);
        UI.addButton("Print all details", this::printAllDetails);
        UI.addTextField("Name", this::selectPerson);
        UI.addButton("Parent details", this::printParentDetails);
        UI.addButton("Add child", this::addChild);
        UI.addButton("Find & print Children", this::printChildren);
        UI.addButton("Fix database", this::fixDatabase);
        UI.addButton("Print GrandChildren", this::printGrandChildren);
        UI.addButton("Print Missing", this::printMissing);
        UI.addButton("Check Entries", this::checkEntries);
        UI.addButton("Print Ascendants", this::printAscendants);
        UI.addButton("Print Descendants", this::printDescendants);
        UI.addButton("Clear Text", UI::clearText);
        UI.addButton("Reload Database", this::loadData);
        UI.addButton("Quit", UI::quit);
        UI.setDivider(1.0);
    }

    /** 
     *  Load the information from the file "database.txt".
     *        Each line of the file has information about one person:
     *        name, year of birth, mother's name, father's name
     *        (note: a '-' instead of a name means  the mother or father are unknown)
     *        For each line,
     *         - construct a new Person with the information, and
     *   - add to the database map.
     */
    public void loadData() {
        try{
            database.clear();
            Scanner sc = new Scanner(new File("database.txt")); //Create new scanner from the database file
            while(sc.hasNext()) //While there is data available
            {
                Scanner lnSc = new Scanner(sc.nextLine()); //Create new scanner for each line of the database file
                //Read the data for this row
                String name = lnSc.next();
                int birthYear = lnSc.nextInt();
                String mother = lnSc.next();
                String father = lnSc.next();

                //Create new person object
                Person person = new Person(name, birthYear, mother.equals("-") ? "" : mother, father.equals("-") ? "" : father);

                //Add person to the database
                database.put(name, person);
            }
            sc.close();
            UI.println("Loaded "+database.size()+" people into the database");
        }catch(IOException e){throw new RuntimeException("Loading database.txt failed" + e);}
    }

    /** Prints out names of all the people in the database */
    public void printAllNames(){
        for (String name : database.keySet()) { //Iterate over names and print them
            UI.println(name);
        }
        UI.println("-----------------");
    }

    /** Prints out details of all the people in the database */
    public void printAllDetails(){
        for (Person p : database.values()) { //Iterate over values and print the details
            p.printDetails();
            UI.println();
        }
        UI.println("-----------------");
    }

    /**
     * Store value (capitalised properly) in the selectedName field.
     * If there is a person with that name currently in people,
     *  then print out the details of that person,
     * Otherwise, offer to add the person:
     * If the user wants to add the person,
     *  ask for year of birth, mother, and father
     *  create the new Person,
     *  add to the database, and
     *  print out the details of the person.
     * Hint: it may be useful to make an askPerson(String name) method
     * Hint: remember to capitalise the names that you read from the user
     */
    public void selectPerson(String value){
        selectedName = capitalise(value);

        Person p = database.get(selectedName); //Get person from database
        if(p == null) //Check if person exists
        {
            UI.println("\nThat person doesn't exist!");
            UI.println("Would you like to add them? (Y/N)");
            String result = UI.nextLine(); //Get the entered Y/N answer
            if(result.equalsIgnoreCase("Y")) //if the user wants to add the user
            {
                Person newP = askPerson(capitalise(selectedName)); //Create new person object
                database.put(newP.getName(), newP); //Add person to database
                UI.println();
                newP.printDetails(); //Print user info
            }
        }
        else //Person exists, print the details
            p.printDetails();
        UI.println("-----------------");
    }

    /**
     * Ask the user to enter required information to create a person object
     * @param name Name of new person
     * @return Person object containing user entered info
     */
    public Person askPerson(String name)
    {
        UI.println("\nNew person: " + name);
        UI.println("Enter DOB:");
        int dob = Integer.parseInt(UI.nextLine()); //Parse the DOB to an integer
        UI.println("Enter mother:");
        String mother = capitalise(UI.nextLine());
        UI.println("Enter father:");
        String father = capitalise(UI.nextLine());

        //Create new person from the info
        return new Person(name, dob, mother, father); //
    }

    /**
     * Print all the details of the mother and father of the person
     * with selectedName (if there is one).
     * (If there is no person with the current name, print "no person called ...")
     * If the mother or father's names are unknown, print "unknown".
     * If the mother or father names are known but they are not in
     *  the database, print "...: No details known".
     */
    public void printParentDetails(){
        if(selectedName == null) {
            UI.println("Selected person does not exist.");
            return;
        }

        Person person = database.get(selectedName); //Get person object

        UI.println("\nPrinting parent details for: " + person.getName());

        if(!person.getMother().equals("")) //If mother is present
        {
            //Print details for mother
            Person mother = database.get(person.getMother());
            UI.println("\nMother:");
            mother.printDetails();
        }

        if(!person.getFather().equals("")) //If father is present
        {
            //Print details for father
            Person father = database.get(person.getFather());
            UI.println("\nFather:");
            father.printDetails();
        }

        UI.println("-----------------");
    }

    /**
     * Add a child to the person with selectedName (if there is one).
     * If there is no person with the selectedName,
     *   print "no person called ..." and return
     * Ask for the name of a child of the selectedName
     *  (remember to capitalise the child's name)
     * If the child is already recorded as a child of the person,
     *  print a message
     * Otherwise, add the child's name to the current person.
     * If the child's name is not in the current database,
     *   offer to add the child's details to the current database.
     *   Check that the selectedName is either the mother or the father.
     */
    public void addChild(){

        if(selectedName == null) {
            UI.println("Selected person does not exist.");
            return;
        }

        Person p = database.get(selectedName); //Get person object

        UI.println("\nEnter the name of the child to add:");
        String child = capitalise(UI.nextLine()); //Get formatted name for child

        if(!database.containsKey(child)) //If child does not exist
        {
            UI.println("\nThat person doesn't exist!");
            UI.println("Would you like to add them? (Y/N)");
            String result = UI.nextLine();
            if(result.equalsIgnoreCase("Y")) {
                Person c = askPerson(child);

                //Check if either the mother or father match the new child
                if(!c.getMother().equals(p.getName()) && !c.getFather().equals(p.getName()))
                {
                    UI.println("The child must have the selected person as their mother or father.");
                    UI.println("Child not added.");
                    UI.println("-----------------");
                    return;
                }
                database.put(child, c);
                p.addChild(child);
                UI.println("Child added");
            }
        }
        else { //If child exists
            if(p.getChildren().contains(child))
            {
                UI.println(child + " is already a child of " + selectedName);
            }
            else {
                p.addChild(child);
                UI.println("Child added");
            }
        }
        UI.println("-----------------");
    }

    /**
     * Print the number of children of the selectedName and their names (if any)
     * Find the children by searching the database for people with
     * selectedName as a parent.
     * Hint: Use the findChildren method (which is needed for other methods also)
     */
    public void printChildren(){
        if(selectedName == null) {
            UI.println("Selected person does not exist.");
            return;
        }
        Set<String> children = findChildren(capitalise(selectedName)); //Find children of the selected name

        UI.println("\n" + selectedName + " has " + children.size() + " children.");

        //Iterate over children and print the names
        for(String child : children)
            UI.println("Child: " + child);
        UI.println("-----------------");
    }

    /**
     * Find (and return) the set of all the names of the children of
     * the given person by searching the database for every person 
     * who has a mother or father equal to the person's name.
     * If there are no children, then return an empty Set
     */
    public Set<String> findChildren(String name){
        Set<String> children = new HashSet<>(); //Set used so children are only printed once

        //Iterate over every person
        for(Person person : database.values())
            if(person.getMother().equals(name) || person.getFather().equals(name)) //If the person's mother or father matches the name
                children.add(person.getName());

        return children;
    }

    /**
     * Check consistency of entries in the database for various things such as parents age difference, parents vs child DOB and if a user is an ancestor of themselves
     */
    public void checkEntries()
    {
        UI.println("\nChecking entries for consistency...");
        for(Person person : database.values()) //Iterate over each person in the database
        {
            //Get parents
            Person mother = database.get(person.getMother());
            Person father = database.get(person.getFather());

            //Check that the person is not born before either of their parents
            if(mother != null && person.getDOB() < mother.getDOB())
                UI.println(person.getName() + " is born before their mother.");
            if(father != null && person.getDOB() < father.getDOB())
                UI.println(person.getName() + " is born before their father.");

            //Check that the age difference between parents is not greather than 90
            if(mother != null && father != null && Math.abs(mother.getDOB() - father.getDOB()) > 90)
                UI.println(person.getName() + " has parents that have birth dates more than 90 years apart.");

            //Ensure the user is not an ancestor of themselves
            Stack<String> recursiveAncestorStack = new Stack<>();
            if(mother != null)
                recursiveAncestorStack.push(mother.getName());
            if(father != null)
                recursiveAncestorStack.push(father.getName());

            //Process stack
            while(recursiveAncestorStack.size() > 0)
            {
                String ancestor = recursiveAncestorStack.pop(); //Get the next ascendant
                if(ancestor.equalsIgnoreCase(person.getName())) //Check that the ascendant is not the user themselves
                {
                    UI.println(person.getName() + " is their own ancestor");
                    break; //Exit loop to avoid infinite loop
                }

                Person p = database.get(ancestor); //Get person object

                //Add parents to stack if they are present
                if(p.getMother().trim().length() > 0)
                    recursiveAncestorStack.push(p.getMother());
                if(p.getFather().trim().length() > 0)
                    recursiveAncestorStack.push(p.getFather());
            }
        }
        UI.println("-----------------");
    }

    /**
     * Recursively print parents of selected person
     */
    public void printAscendants()
    {
        if(selectedName == null) {
            UI.println("Selected person does not exist.");
            return;
        }

        UI.println("Ascendants of " + selectedName + "\n");

        Person person = database.get(selectedName); //Get the person object

        //Find the parents
        Person mother = database.get(person.getMother());
        Person father = database.get(person.getFather());

        //Use a stack of tuples containing the information to emulate recursive behaviour without actual recursion
        //Tuple has Name, Depth and IsMother
        Stack<Tuple<String, Integer, Boolean>> recursiveAncestorStack = new Stack<>();

        //Add the current parents to be processed at depth 0 with mother/father boolean
        if(mother != null)
            recursiveAncestorStack.push(new Tuple<>(mother.getName(), 0, true));
        if(father != null)
            recursiveAncestorStack.push(new Tuple<>(father.getName(), 0, false));

        //Process the stack
        while(recursiveAncestorStack.size() > 0)
        {
            Tuple<String, Integer, Boolean> ancestor = recursiveAncestorStack.pop(); //Get the next ascendant on the recursive stack

            int depth = ancestor.y; //Get the depth from the tuple

            Person a = database.get(ancestor.x); //Get person object from the current ascendant

            //Indent the text by the depth
            for(int i = 0; i < depth; i++)
                UI.print("   ");

            //Print the ascendant
            UI.println((ancestor.z ? "M: " : "F: ") + a.getName() + " (" + a.getDOB() + ")");

            Person p = database.get(ancestor.x); //Get the person object for the current ascendant

            //Add the parents of this ascendant to the stack with +1 depth if they exist
            if(p.getMother().trim().length() > 0)
                recursiveAncestorStack.push(new Tuple<>(p.getMother(), depth + 1, true));
            if(p.getFather().trim().length() > 0)
                recursiveAncestorStack.push(new Tuple<>(p.getFather(), depth + 1, false));
        }
        UI.println("-----------------");
    }

    /**
     * Recursively print children of selected person
     */
    public void printDescendants()
    {
        if (!databaseHasBeenFixed) { UI.println("Database must be fixed first!"); return; }
        if(selectedName == null) {
            UI.println("Selected person does not exist.");
            return;
        }

        UI.println("Descendants of " + selectedName + "\n");

        Person person = database.get(selectedName); //Get person object

        //Use a stack of pairs containing the information to emulate recursive behaviour without actual recursion
        //Pair has Named and Depth
        Stack<Pair<String, Integer>> recursiveDescendantStack = new Stack<>();

        //Add current children with a depth of 0
        for(String child : person.getChildren())
            recursiveDescendantStack.add(new Pair<>(child, 0));

        //Process stack
        while(recursiveDescendantStack.size() > 0) {
            Pair<String, Integer> descendant = recursiveDescendantStack.pop(); //Get the next descendant

            int depth = descendant.y; //Get the depth from the pair

            //Indent the text by the depth
            for(int i = 0; i < depth; i++)
                UI.print("   ");

            //Get the person object for the current descendant
            Person d = database.get(descendant.x);

            //Print the descendant
            UI.println(d.getName() + " (" + d.getDOB() + ")");

            //Add the children of this descendant to the stack with +1 depth
            for(String child : d.getChildren())
                recursiveDescendantStack.add(new Pair<>(child, depth + 1));
        }

        UI.println("-----------------");
    }

    //Tuple class taken from stack overflow
    public class Tuple<X, Y, Z> {
        public final X x;
        public final Y y;
        public final Z z;
        public Tuple(X x, Y y, Z z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    public class Pair<X, Y> {
        public final X x;
        public final Y y;
        public Pair(X x, Y y) {
            this.x = x;
            this.y = y;
        }
    }

    /**
     * When the database is first loaded, none of the Persons will
     * have any children recorded in their children field. 
     * Fix the database so every Person's children includes all the
     * people that have that Person as a parent.
     * Hint: use the findChildren method
     */
    public void fixDatabase(){
        for(Map.Entry<String, Person> entry : database.entrySet()) //Iterate over every entry in the DB
        {
            Set<String> children = findChildren(entry.getKey()); //Find the children for the current entry
            for(String child : children) //Add the children to the persons children set
            {
                entry.getValue().addChild(child);
            }
        }
        databaseHasBeenFixed = true;
        UI.println("Found children of each person in database\n-----------------");
    }


    /**
     * Print out all the grandchildren of the selectedName (if any)
     * Assume that the database has been "fixed" so that every Person
     * contains a set of all their children.
     * If the selectedName is not in the database, print "... is not known"
     */
    public void printGrandChildren(){
        if (!databaseHasBeenFixed) { UI.println("Database must be fixed first!");}
        if (!database.containsKey(selectedName)){
            UI.println("That person is not known");
            return;
        }

        Person person = database.get(selectedName); //Get selected person

        for(String childStr : person.getChildren()) //Iterate over each child
        {
            Person child = database.get(childStr); //Get child object

            for(String grandChildStr : child.getChildren()) //Iterate over the child's children
            {
                Person grandChild = database.get(grandChildStr); //Get the grandchild object
                grandChild.printDetails(); //Print their details
                UI.println();
            }
        }

        UI.println("------------------");
    }

    /**
     * Print out all the names that are in the database but for which
     * there is no Person in the database. Do not print any name twice.
     * These will be names of parents or children of Persons in the Database
     * for which a Person object has not been created.
     */
    public void printMissing(){
        UI.println("Missing names:");

        Set<String> missing = new HashSet<>(); //Set to only print names once

        for(Person person : database.values()) //Iterate over each value in the database
        {
            //If the father is present and not in the database, add them to the set
            if(!database.containsKey(person.getFather()) && person.getFather().trim().length() > 0)
                missing.add(person.getFather());

            //If the mother is present and not in the database, add them to the set
            if(!database.containsKey(person.getMother()) && person.getMother().trim().length() > 0)
                missing.add(person.getMother());

            //Iterate over the children
            for(String child : person.getChildren())
            {
                if(!database.containsKey(child) && child.trim().length() > 0) //If the child is present but not in the database, add them to the set
                    missing.add(child);
            }
        }

        UI.println(missing.size() + " missing people: "); //Print missing people count
        for(String missingPerson : missing) //Print missing people
        {
            UI.println(missingPerson);
        }
        UI.println("------------------");
    }

    /**
     * Return a capitalised version of a string
     */
    public String capitalise(String s){
        return s.substring(0,1).toUpperCase() + s.substring(1).toLowerCase();
    }


    public static void main(String[] args) throws IOException {
        new Genealogy();
    }
}
