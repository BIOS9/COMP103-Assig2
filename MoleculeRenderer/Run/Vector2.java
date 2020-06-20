/**
 * 2D vector class for storing and manipulating points in a 2D space.
 * @author Matthew Corfiatis
 */
public class Vector2 {

    public float x, y; //Position

    public Vector2() { }

    /**
     * Constructor
     * @param x X axis position
     * @param y Y axis position
     */
    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Adds two vectors together
     * @param a Vector A
     * @param b Vector B
     * @return Output of vector calculation
     */
    public static Vector2 add(Vector2 a, Vector2 b) {
        return new Vector2(a.x + b.x, a.y + b.y);
    }

    /**
     * Subtracts a vector from another vector
     * @param a Vector A
     * @param b Vector B
     * @return Output of vector calculation
     */
    public static Vector2 sub(Vector2 a, Vector2 b) {
        return new Vector2(a.x - b.x, a.y - b.y);
    }

    /**
     * Multiplies two vectors together
     * @param a Vector A
     * @param b Vector B
     * @return Output of vector calculation
     */
    public static Vector2 mult(Vector2 a, Vector2 b) {
        return new Vector2(a.x * b.x, a.y * b.y);
    }

    /**
     * Divides a vector by another vector
     * @param a Vector A
     * @param b Vector B
     * @return Output of vector calculation
     */
    public static Vector2 div(Vector2 a, Vector2 b) {
        return new Vector2(a.x / b.x, a.y / b.y);
    }

    /**
     * Adds a scalar value to a vector
     * @param a Vector to add to
     * @param b Scalar value
     * @return Resulting vector
     */
    public static Vector2 add(Vector2 a, float b) {
        return new Vector2(a.x + b, a.y + b);
    }

    /**
     * Subtracts a scalar value from a vector
     * @param a Vector to subtract from
     * @param b Scalar value
     * @return Resulting vector
     */
    public static Vector2 sub(Vector2 a, float b) {
        return new Vector2(a.x - b, a.y - b);
    }

    /**
     * Multiplies a vector by a scalar value
     * @param a Vector to multiply
     * @param b Scalar value
     * @return Resulting vector
     */
    public static Vector2 mult(Vector2 a, float b) {
        return new Vector2(a.x * b, a.y * b);
    }

    /**
     * Divides a vector by a scalar value
     * @param a Vector to divide
     * @param b Scalar value
     * @return Resulting vector
     */
    public static Vector2 div(Vector2 a, float b) {
        return new Vector2(a.x / b, a.y / b);
    }

    /**
     * @return Vector in string format (x, y)
     */
    @Override
    public String toString()
    {
        return "(" + x + ", " + y + ")";
    }
}
