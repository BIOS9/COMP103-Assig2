/**
 * Stores information about current scene and performs calculations using that information.
 * @author Matthew Corfiatis
 */
public class Graphics {
    public final float CANVAS_WIDTH; //Width of the viewable canvas
    public final float CANVAS_HEIGHT; //Height of the viewable canvas

    public float shaderOffset = 0; //Moves shader position up or down to make atoms darker or lighter
    public float sizeMultiplier = 0.5f; //Size of atoms

    public Vector2 offset2D; //Offset to add to all projected 2d points
    public Vector3 offset = new Vector3(); //Offset to add to all unprojected 3d points
    public Vector3 eulerAngles = new Vector3(); //Angles to rotate the model by

    /**
     * Constructor
     * Specify dimentions of canvas
     */
    public Graphics(float canvasWidth, float canvasHeight) {
        CANVAS_WIDTH = canvasWidth;
        CANVAS_HEIGHT = canvasHeight;
        offset2D = new Vector2(CANVAS_WIDTH / 2, CANVAS_HEIGHT / 2); //Set offset to middle of canvas
    }

    /**
     * Camera matrix to apply to the projected points
     */
    public Matrix3 camMatrix = new Matrix3(
            1, 0, 0,
            0, 1, 0,
            0, 0, 1);

    /**
     * Matrix to apply to all points in the molecule
     */
    public Matrix3 moleculeMatrix = new Matrix3(
            1, 0, 0,
            0, 1, 0,
            0, 0, 1);

    /**
     * Project the 3d points onto a 2d plane using weak perspective projection
     * @param vect Vector to project
     * @param fov Field of view
     */
    public Vector2 project2D(Vector3 vect, float fov) {
        vect = camMatrix.multiplyVector(vect); //Multiply the vector by the camera matrix
        Vector2 polygon = Vector2.div(new Vector2(vect.x, vect.y), vect.z); //Project using scale by global Z (Weak perspective)
        polygon = Vector2.mult(polygon, fov); //Multiply the point by the fov
        return Vector2.add(polygon, offset2D); //Return new 2d point
    }
}
