/**
 * 3D Vector class for storing and manipulating points in a 3D space.
 * @author Matthew Corfiatis
 */
public class Vector3 {

    public float x, y, z; //Position

    public Vector3() { }

    /**
     * Constructor
     * @param x X axis position
     * @param y Y axis position
     * @param z Z axis position
     */
    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Adds two vectors together
     * @param a Vector A
     * @param b Vector B
     * @return Output of vector calculation
     */
    public static Vector3 add(Vector3 a, Vector3 b) {
        return new Vector3(a.x + b.x, a.y + b.y, a.z + b.z);
    }

    /**
     * Subtracts a vector from another vector
     * @param a Vector A
     * @param b Vector B
     * @return Output of vector calculation
     */
    public static Vector3 sub(Vector3 a, Vector3 b) {
        return new Vector3(a.x - b.x, a.y - b.y, a.z - b.z);
    }

    /**
     * Multiplies two vectors together
     * @param a Vector A
     * @param b Vector B
     * @return Output of vector calculation
     */
    public static Vector3 mult(Vector3 a, Vector3 b) {
        return new Vector3(a.x * b.x, a.y * b.y, a.z * b.z);
    }

    /**
     * Divides a vector by another vector
     * @param a Vector A
     * @param b Vector B
     * @return Output of vector calculation
     */
    public static Vector3 div(Vector3 a, Vector3 b) {
        return new Vector3(a.x / b.x, a.y / b.y, a.z / b.z);
    }

    /**
     * Divides a vector by a scalar value
     * @param a Vector A
     * @param b Scalar float to divide by
     * @return Output of vector calculation
     */
    public static Vector3 div(Vector3 a, float b) {
        return new Vector3(a.x / b, a.y / b, a.z / b);
    }

    /**
     * @return vector in string format (x, y, z)
     */
    @Override
    public String toString()
    {
        return "(" + x + ", " + y + ", " + z + ")";
    }
}
