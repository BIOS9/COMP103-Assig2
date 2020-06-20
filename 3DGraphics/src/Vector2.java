public class Vector2 {

    public float x, y;

    public Vector2() { }

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public static Vector2 add(Vector2 a, Vector2 b) {
        return new Vector2(a.x + b.x, a.y + b.y);
    }

    public static Vector2 sub(Vector2 a, Vector2 b) {
        return new Vector2(a.x - b.x, a.y - b.y);
    }

    public static Vector2 mult(Vector2 a, Vector2 b) {
        return new Vector2(a.x * b.x, a.y * b.y);
    }

    public static Vector2 div(Vector2 a, Vector2 b) {
        return new Vector2(a.x / b.x, a.y / b.y);
    }

    public static Vector2 add(Vector2 a, float b) {
        return new Vector2(a.x + b, a.y + b);
    }

    public static Vector2 sub(Vector2 a, float b) {
        return new Vector2(a.x - b, a.y - b);
    }

    public static Vector2 mult(Vector2 a, float b) {
        return new Vector2(a.x * b, a.y * b);
    }

    public static Vector2 div(Vector2 a, float b) {
        return new Vector2(a.x / b, a.y / b);
    }

    @Override
    public String toString()
    {
        return "{ " + x + ", " + y + " }";
    }
}
