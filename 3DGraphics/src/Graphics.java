import ecs100.*;

import java.awt.*;

public class Graphics {
    public static final float CANVAS_WIDTH = 1100;
    public static final float CANVAS_HEIGHT = 800;

    Matrix3 camMatrix = new Matrix3(
            1, 0, 0,
            0, 1, 0,
            0, 0, 1);

    Vector3 cubeOffset = new Vector3(0, 0, 10);
    Matrix3 pointMatrix = new Matrix3(
            1, 0, 0,
            0, 1, 0,
            0, 0, 1);

    Vector3[] cube = new Vector3[] {
            new Vector3(-1, -1, 1),
            new Vector3(-1, 1, 1),
            new Vector3(1, 1, 1),
            new Vector3(1, -1, 1),
            new Vector3(-1, -1, -3),
            new Vector3(-1, 1, -3),
            new Vector3(1, 1, -3),
            new Vector3(1, -1, -3)
    };

    int[][] cubeLineMap = new int[][] {
        {0,1},
        {1,2},
        {2,3},
        {3,0}, //Front face

        {4,5},
        {5,6},
        {6,7},
        {7,4}, //Back face

        {0,4},
        {1,5},
        {2,6},
        {3,7}, //Face connectors
    };

    float angleX = 0;
    float angleY = 0;
    float angleZ = 0;
    float cameraAngle = 0;

    public Graphics()
    {
        setupGUI();
    }

    public void setupGUI(){
        UI.initialise();
        UI.addButton("Calc", this::draw);
        UI.setKeyListener(this::doKey);
        while(true)
        {
            draw();
            UI.sleep(16);
        }
    }

    private void draw()
    {
        UI.setColor(Color.black);
        UI.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        angleX +=0.01;
        angleY +=0.005;
        angleZ +=0.02;
        pointMatrix = Matrix3.mult(Matrix3.mult(Matrix3.xRotation(angleX), Matrix3.yRotation(angleY)), Matrix3.zRotation(angleZ));
        Vector2[] cube2d = new Vector2[cube.length];
        for(int i = 0; i < cube.length; i++) {
            Vector3 point = cube[i];
            Vector3 newPoint = Vector3.add(pointMatrix.multiplyVector(point), cubeOffset);
            Vector2 vec = project2D(newPoint, CANVAS_HEIGHT);
            cube2d[i] = vec;
            float size = 200 / newPoint.z;
            UI.setColor(Color.red);
            UI.fillOval(vec.x - (size/2), vec.y - (size/2), size, size);
        }
        for(int[] lineMap : cubeLineMap)
        {
            Vector2 a = cube2d[lineMap[0]];
            Vector2 b = cube2d[lineMap[1]];
            UI.setColor(Color.cyan);
            UI.drawLine(a.x, a.y, b.x, b.y);
        }
    }

    public Vector2 project2D(Vector3 vect, float fov) {
        vect = camMatrix.multiplyVector(vect);
        Vector2 polygon = Vector2.div(new Vector2(vect.x, vect.y), vect.z);
        polygon = Vector2.mult(polygon, fov);
        return Vector2.add(polygon, new Vector2(CANVAS_WIDTH / 2, CANVAS_HEIGHT / 2));
    }

    private void doKey(String key)
    {
        switch(key)
        {
            case "Left":
                angleY+=0.01;
                //camMatrix = Matrix3.xRotation(cameraAngle);
                break;
            case "Right":
                angleY-=0.01;
                //camMatrix = Matrix3.xRotation(cameraAngle);
                break;
        }
    }

    public static void main(String[] args) {
        new Graphics();
    }
}
