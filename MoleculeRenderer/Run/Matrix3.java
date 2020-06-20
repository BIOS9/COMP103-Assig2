/**
 * 3D matrix class for storing and manipulating mathematical matrices in Java
 * @author Matthew Corfiatis
 */
public class Matrix3 {
    public float
            x1, y1, z1,
            x2, y2, z2,
            x3, y3, z3;

    public Matrix3() {
        x1 = y2 = z3 = 1;
    }

    /**
     * Constructor using floating point matrix values
     */
    public Matrix3(
            float x1, float y1, float z1,
            float x2, float y2, float z2,
            float x3, float y3, float z3)
    {
        this.x1 = x1;
        this.y1 = y1;
        this.z1 = z1;

        this.x2 = x2;
        this.y2 = y2;
        this.z2 = z2;

        this.x3 = x3;
        this.y3 = y3;
        this.z3 = z3;
    }

    /**
     * Constructor using double precision floating point matrix values.
     * Values converted to float internally.
     */
    public Matrix3(
            double x1, double y1, double z1,
            double x2, double y2, double z2,
            double x3, double y3, double z3)
    {
        this.x1 = (float)x1;
        this.y1 = (float)y1;
        this.z1 = (float)z1;

        this.x2 = (float)x2;
        this.y2 = (float)y2;
        this.z2 = (float)z2;

        this.x3 = (float)x3;
        this.y3 = (float)y3;
        this.z3 = (float)z3;
    }

    /**
     * Multiply a vector by this matrix
     * @param vec Vector3 to multiply
     * @return Output Vector3 of the calculation
     */
    public Vector3 multiplyVector(Vector3 vec)
    {
        return new Vector3(
                (x1 * vec.x) + (y1 * vec.y) + (z1 * vec.z),
                (x2 * vec.x) + (y2 * vec.y) + (z2 * vec.z),
                (x3 * vec.x) + (y3 * vec.y) + (z3 * vec.z));
    }

    /**
     * Multiply two matrices together to combine linear transformations.
     * @param a Matrix A
     * @param b Matrix B
     * @return Result of the matrix multiplication
     */
    public static Matrix3 mult(Matrix3 a, Matrix3 b)
    {
        return new Matrix3( //Multiplies matrix values together. Order matters
                (a.x1 * b.x1) + (a.y1 * b.x2) + (a.z1 * b.x3), (a.x1 * b.y1) + (a.y1 * b.y2) + (a.z1 * b.y3), (a.x1 * b.z1) + (a.y1 * b.z2) + (a.z1 * b.z3),
                (a.x2 * b.x1) + (a.y2 * b.x2) + (a.z2 * b.x3), (a.x2 * b.y1) + (a.y2 * b.y2) + (a.z2 * b.y3), (a.x2 * b.z1) + (a.y2 * b.z2) + (a.z2 * b.z3),
                (a.x3 * b.x1) + (a.y3 * b.x2) + (a.z3 * b.x3), (a.x3 * b.y1) + (a.y3 * b.y2) + (a.z3 * b.y3), (a.x3 * b.z1) + (a.y3 * b.z2) + (a.z3 * b.z3));
    }

    /**
     * Generate a matrix for rotation in the X Euler axis
     * @param angle Angle in radians to produce the matrix for
     * @return The resulting rotation matrix
     */
    public static Matrix3 xRotation(float angle)
    {
        return new Matrix3(
                1, 0, 0,
                0, Math.cos(angle), -Math.sin(angle),
                0, Math.sin(angle), Math.cos(angle)
        );
    }

    /**
     * Generate a matrix for rotation in the Y Euler axis
     * @param angle Angle in radians to produce the matrix for
     * @return The resulting rotation matrix
     */
    public static Matrix3 yRotation(float angle)
    {
        return new Matrix3(
                Math.cos(angle), 0, Math.sin(angle),
                0, 1, 0,
                -Math.sin(angle), 0, Math.cos(angle)
        );
    }

    /**
     * Generate a matrix for rotation in the Z Euler axis
     * @param angle Angle in radians to produce the matrix for
     * @return The resulting rotation matrix
     */
    public static Matrix3 zRotation(float angle)
    {
        return new Matrix3(
                Math.cos(angle), -Math.sin(angle), 0,
                Math.sin(angle), Math.cos(angle), 0,
                0, 0, 1
        );
    }
}
