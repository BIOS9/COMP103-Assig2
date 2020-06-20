// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2018T2, Assignment 2
 * Name: Matthew Corfiatis
 * Username: CorfiaMatt
 * ID: 300447277
 */

import ecs100.*;
import java.awt.Color;
import java.util.*;
import java.io.*;

/** 
 *  Renders a molecule on the graphics pane from different positions.
 *  
 *  A molecule consists of a collection of molecule elements, i.e., atoms.
 *  Each atom has a type or element (eg, Carbon, or Hydrogen, or Oxygen, ..),
 *  and a three dimensional position in the molecule (x, y, z).
 *
 *  Each molecule is described in a file by a list of atoms and their positions.
 *  The molecule is rendered by drawing a colored circle for each atom.
 *  The size and color of each atom is determined by the type of the atom.
 * 
 *  The description of the size and color for rendering the different types of
 *  atoms is stored in the file "element-info.txt" which should be read and
 *  stored in a Map.  When an atom is rendered, the element type should be looked up in
 *  the map to find the size and color.
 * 
 *  A molecule can be rendered from different perspectives, and the program
 *  provides buttons to control the perspective of the rendering.
 *  
 */

public class MoleculeRenderer {

    long millis = System.currentTimeMillis();

    public static final float CANVAS_WIDTH = 900;
    public static final float CANVAS_HEIGHT = 700;
    public static final float FOV = 3000;
    public boolean allowMouse = false;
    public boolean hasBonds = false;

    Graphics graph = null;

    // Map containing info about the size and color of each element type.
    private Map<String, ElementInfo> elements = new HashMap<>();

    // The collection of the atoms in the current molecule
    private List<Atom> molecule = new ArrayList<Atom>();  

    // Constructor:
    /** 
     * Sets up the Graphical User Interface and reads the file of element data of
     * each possible type of atom into a Map: where the type is the key
     * and an ElementInfo object is the value (containing size and color).
     */
    public MoleculeRenderer() {
        setupGUI();
        readElementInfo();    //  Read the atom info
    }

    public void setupGUI(){
        UI.addButton("Molecule", this::loadMolecule);
        UI.addButton("FromFront", this::showFromFront);
        UI.addButton("FromBack", this::showFromBack);
        UI.addButton("FromRight", this::showFromRight);
        UI.addButton("FromLeft", this::showFromLeft);
        UI.addButton("FromTop", this::showFromTop);
        UI.addButton("FromBot", this::showFromBot);
        UI.addSlider("X Angle", -180, 180, 0, this::xSlider);
        UI.addSlider("Y Angle", -180, 180, 0, this::ySlider);
        UI.addButton("Mouse On", () -> allowMouse = true);
        UI.addButton("Mouse Off", () -> allowMouse = false);
        UI.addButton("Quit", UI::quit);
        UI.setMouseMotionListener(this::mouseMotion);
        UI.setDivider(0.0);
    }

    /** 
     * Reads the file "element-info.txt" which contains radius and color
     * information about each type of element:
     *   element name, a radius, and red, green, blue, components of the color (integers)
     * Stores the info in a Map in the elements field.
     * The element name is the key.
     */
    private void readElementInfo() {
        UI.printMessage("Reading the element information...");
        try {
            Scanner scan = new Scanner(new File("element-info.txt")); //Open file
            while(scan.hasNext()) //While there is data left in the file
            {
                Scanner lnSc = new Scanner(scan.nextLine()); //Create new scanner from one line of the file 
                //Scan data
                String element = lnSc.next();
                float size = lnSc.nextFloat();
                int red = lnSc.nextInt();
                int green = lnSc.nextInt();
                int blue = lnSc.nextInt();
                elements.put(element, new ElementInfo(element, size, new Color(red, green, blue))); //Add new data to the elements list
            }

            UI.printMessage("Reading element information: " + elements.size() + " elements found.");
        }
        catch (IOException ex) {UI.printMessage("Reading element information FAILED."+ex);}
    }

    /**
     * Ask the user to choose a file that contains info about a molecule,
     * load the information, and render it on the graphics pane.
     */
    public void loadMolecule(){
        String filename = UIFileChooser.open();
        if(filename == null) return;
        graph = new Graphics(CANVAS_WIDTH, CANVAS_HEIGHT);
        molecule.clear();
        readMoleculeFile(filename);
        fixMoleculeOrigin(molecule);
        showFromFront();
    }

    /**
     * x rotation slider moved handler
     * @param val Value of slider
     */
    private void xSlider(double val)
    {
        if(molecule.size() == 0) return;
        allowMouse = false;
        double radians = val * Math.PI/180;
        graph.eulerAngles = new Vector3(0, (float)radians, 0);
        delayRender();
    }

    /**
     * y rotation slider moved handler
     * @param val Value of slider
     */
    private void ySlider(double val)
    {
        if(molecule.size() == 0) return;
        allowMouse = false;
        double radians = val * Math.PI/180;
        graph.eulerAngles = new Vector3((float)radians, 0, 0);
        delayRender();
    }

    /**
     * Mouse movement handler for use with mouse controlled rotation
     * @param action Action of mouse
     * @param x X co-ordinate
     * @param y Y co-ordinate
     */
    private void mouseMotion(String action, double x, double y)
    {
        try {
            if (molecule.size() == 0 || !allowMouse) return;
            if (x < 0 || x > CANVAS_WIDTH || y < 0 || y > CANVAS_HEIGHT) return;
            double xDegrees = ((x / (double) CANVAS_WIDTH) * 2 * Math.PI) - Math.PI;
            double yDegrees = ((y / (double) CANVAS_HEIGHT) * 2 * Math.PI) - Math.PI;
            graph.eulerAngles = new Vector3((float)yDegrees, (float) -xDegrees, 0);
            delayRender();
        }
        catch(Exception e){}
    }

    /**
     * Render at a max frame rate of 30fps, or 32ms delay between frames
     */
    public void delayRender()
    {
        if(System.currentTimeMillis() - millis > 32) {
            millis = System.currentTimeMillis();
            render();
        }
    }

    /** 
     * Reads the molecule data from a file containing one line for
     *  each atom in the molecule.
     * Each line contains an atom type and the 3D coordinates of the atom.
     * For each atom, the method constructs a Atom object,
     * and adds it to the List of molecule elements in the molecule.
     * To obtain the color and the size of each atom, it has to look up the name
     * of the atom in the Map of ElementInfo objects.
     */
    public void readMoleculeFile(String fname) {
        try {
            hasBonds = false;
            File file = new File(fname);
            Scanner sc = new Scanner(file);
            while(sc.hasNext())
            {
                Scanner lineSc = new Scanner(sc.nextLine());
                String val = lineSc.next();
                if(isNumeric(val)) //Bond map
                {
                    hasBonds = true;
                    Atom atom1 = molecule.get(Integer.parseInt(val));
                    Atom atom2 = molecule.get(lineSc.nextInt());
                    atom1.addBond(atom2);
                    atom2.hasExternalBonds = true;
                }
                else //Atom
                {
                    float x = lineSc.nextFloat();
                    float y = lineSc.nextFloat();
                    float z = lineSc.nextFloat();
                    molecule.add(new Atom(new Vector3(x, y, z), val));
                }
            }
        }
        catch(IOException ex) {
            UI.println("Reading molecule file " + fname + " failed."+ex);
        }
    }

    /**
     * Render the molecule using the current graphics settings and currently loaded molecule
     */
    public void render()
    {
        if(molecule.size() == 0) return; //If molecule is empty, skip
        UI.setColor(Color.black);
        UI.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        graph.offset = new Vector3(0, 0, 200); //Move object away from camera to avoid divide by zero error with weak perspective projection
        graph.moleculeMatrix = Matrix3.mult(Matrix3.mult(Matrix3.xRotation(graph.eulerAngles.x), Matrix3.yRotation(graph.eulerAngles.y)), Matrix3.zRotation(graph.eulerAngles.z)); //Set molecule matrix to rotation matrices multiplied together

        //Scale the atom by 0.05 to maker it fit in the window
        Matrix3 scaleMatrix = new Matrix3(
                0.05, 0, 0,
                0, 0.05, 0,
                0, 0, 0.05
        );

        //Apply scale matrix to the molecule
        graph.moleculeMatrix = Matrix3.mult(graph.moleculeMatrix, scaleMatrix);

        List<Atom> orderList = new ArrayList<>(); //New list to store temporarily sorted atoms
        for(Atom atom : molecule) {
            atom.globalPosition = Vector3.add(graph.moleculeMatrix.multiplyVector(atom.position), graph.offset); //Calculate the transformed position of the atom
            orderList.add(atom); //Add it to the list
        }

        Collections.sort(orderList); //Sort the list

        for(Atom atom : orderList) //Draw atoms in order of Z according to the sorted list
        {
            Vector2 vec = graph.project2D(atom.globalPosition, FOV); //Project 3d point onto 2d plane
            if(atom.hasExternalBonds) //Skip atoms that are managed by bonded atoms
                atom.update2dPos(vec);
            else
                atom.render(graph, vec, elements); //Draw
        }
    }

    /**
     * Renders the molecule from the front.
     * Sorts the Atoms in the List by their z value, back to front
     * Uses the default ordering of the Atoms
     * Then renders each atom at the position (MIDX+x,y)
     */
    public void showFromFront() {
        viewFromDirection(0, 0);
    }

    /**
     * Renders the molecule from the back.
     * Sorts the Atoms in the List by their z value (front to back)
     * Then renders each atom at (MIDX-x,y) position
     */
    public void showFromBack() {
        viewFromDirection(Math.PI, 0);
    }

    /**
     * Renders the molecule from the left.
     * Sorts the Atoms in the List by their x value (larger x first)
     * Then renders each atom at (MIDZ-z,y) position
     */
    public void showFromLeft() {
        viewFromDirection(3 * Math.PI / 2, 0);
    }

    /**
     * Renders the molecule from the right.
     * Sorts the Atoms in the List by their x value (smaller x first)
     * Then renders each atom at (MIDZ+z,y) position
     */
    public void showFromRight() {
        viewFromDirection(Math.PI / 2, 0);
    }

    /**
     * Renders the molecule from the top.
     * Sorts the Atoms in the List by their y value (larger y first)
     * Then renders each atom at (MIDX+x, MIDZ-z) position
     */
    public void showFromTop() {
        viewFromDirection(0, Math.PI / 2);
    }

    /**
     * Changes euler angles of the molecule and redraws it
     * @param theta Horizontal rotation
     * @param phi Vertical rotation
     */
    public void viewFromDirection(double theta, double phi)
    {
        if(molecule.size() == 0) return;
        allowMouse = false;
        graph.eulerAngles = new Vector3((float)phi, (float)theta, 0);
        render();
    }

    /**
     * Renders the molecule from the bottom.
     * Sorts the Atoms in the List by their y value (smaller y first)
     * Then renders each atom at (MIDX+x, MIDZ+z) position
     */
    public void showFromBot() {
        if(molecule.size() == 0) return;
        allowMouse = false;
        graph.eulerAngles = new Vector3(3 * (float)Math.PI / 2, 0, 0);
        render();
    }

    //Method taken from stackoverflow
    public static boolean isNumeric(String str)
    {
        for (char c : str.toCharArray())
            if (!Character.isDigit(c)) return false;
        return true;
    }

    public static void main(String args[]) {
        new MoleculeRenderer();
    }

    /**
     * Moves the origin of the molecule to the centre
     * @param molecule
     */
    public static void fixMoleculeOrigin(List<Atom> molecule)
    {
        Vector3 centreOffset = findMidPoint(molecule); //Find the centre of the molecule
        for(Atom a : molecule)
            a.position = Vector3.sub(a.position, centreOffset); //Shift atom by offset
    }

    /**
     * Finds centre position of molecule by averaging positons of atoms
     * @param points Molecule to find the centre of
     * @return 3D vector representing the local centre of the molecule
     */
    public static Vector3 findMidPoint(List<Atom> points)
    {
        Vector3 total = points.get(0).position; //Add all positions to this value
        for(int i = 1; i < points.size(); ++i) //Add each position
            total = Vector3.add(total, points.get(i).position);
        total = Vector3.div(total, points.size()); //Divide total by atom count to complete average
        return total;
    }
}
