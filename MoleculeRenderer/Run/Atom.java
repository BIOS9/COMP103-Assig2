// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2018T2, Assignment 2
 * Name: Matthew Corfiatis
 * Username: CorfiaMatt
 * ID: 300447277
 */

import ecs100.*;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.*;
import java.util.List;

/** 
 *  Represents information about an atom in a molecule.
 *  
 *  The information includes
 *   - the kind of atom
 *   - the 3D position of the atom
 *     x is measured from the left to the right
 *     y is measured from the top to the bottom
 *     z is measured from the front to the back.
 *   
 */

public class Atom implements Comparable<Atom> {
    private String kind;    // the kind of atom.
    // coordinates of center of atom, relative to top left front corner
    public Vector3 position;

    public boolean hasExternalBonds; //If the atom is bonded to another atom, but the bond is not stored here

    public Vector3 globalPosition; //Position in the 3d graphics space after all transformations have been applied
    public Vector2 position2d = null; //Position in the 2d projected plane after all transformations have been applied

    private List<Atom> bonds = new ArrayList<>(); //Bonds to other atoms

    /**
     * Getter method for getting the kind of atom
     */
    public String getKind()
    {
        return kind;
    }

    /** Constructor: requires the position and kind  */
    public Atom (Vector3 position, String kind) {
        this.position = position;
        this.kind = kind;
    }

    /**
     * Adds an atom to the bond list
     */
    public void addBond(Atom atom)
    {
        bonds.add(atom);
    }

    /**
     * Set the local 2d position to the specified value
     */
    public void update2dPos(Vector2 pos)
    {
        position2d = pos;
    }

    /** 
     *  Renders the atom on the graphics pane at the position (u, v).
     *  using a circle of the size and color specified in the elementInfo map.
     *  The circle should also have a black border
     */
    public void render(Graphics graph, Vector2 pos, Map<String, ElementInfo> elementInfo) {
        boolean hasBonds = hasExternalBonds || bonds.size() > 0; //If a molecule with bonds is being drawn

        ElementInfo info = elementInfo.get(kind); //Get element info for the atom
        Graphics2D g2 = UI.getGraphics(); //Get the 2d graphics canvas from the UI class
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //Set rendering mode to smooth
        float radius = (float)(info.getRadius() * graph.sizeMultiplier); //Radius of the atom to draw
        float x = pos.x - radius; //Calculate x position of the corner of the ball
        float y = pos.y - radius; //Calculate y pos of the corner of the ball
        float size = radius * 2; //Calculate total width of the atom

        if(hasBonds) size/=1.2f; //If the atom has bonds, draw it smaller

        Color gradientStart = info.getColor().darker().darker().darker(); //Create a darker shadow color for the underside of the atom
        Color gradientEnd = info.getColor().brighter().brighter().brighter(); //Create a lighter highlight color for the topside of the atom

        position2d = pos; //Update the new 2d position

        if(hasBonds) { //If the atom has bonds, draw the bond and redraw the bonded atom on top of the line
            UI.setLineWidth(5);
            UI.setColor(Color.white);
            for (Atom atom : bonds) {
                if (atom.position2d == null) continue;
                UI.drawLine(pos.x, pos.y, atom.position2d.x, atom.position2d.y);
                atom.render(graph, atom.position2d, elementInfo);
            }
        }
        UI.setLineWidth(1);
        //Create a new gradient paint brush to draw the shaded atom
        GradientPaint gradPaint = new GradientPaint(pos.x,pos.y + size, gradientStart,pos.x + size + graph.shaderOffset, pos.y - graph.shaderOffset, gradientEnd); //new GradientPaint(x, y, info.getColor(), size / 2, size / 2, Color.white);
        g2.setPaint(gradPaint); //Set the paint of the canvas
        Ellipse2D ellipse = new Ellipse2D.Float(x, y, size, size); //Create a new ellipse object representing the atom
        g2.fill(ellipse); 
        g2.setColor(Color.black);
        g2.draw(ellipse); //Draw the atom
    }

    /**
     * compareTo returns
     *   -1 if this comes before other
     *    0 if this is the same as other
     *    1 if this comes after other
     * The default ordering is by the z position (back to front)
     * so atoms with large z come before atoms with small z
     */
    public int compareTo(Atom other){
        if(other.globalPosition.z > globalPosition.z) return 1;
        else if(other.globalPosition.z < globalPosition.z) return -1;
        return 0;
    }

}
