package Traffic;

import java.awt.Color;
import java.util.Random;


public class Car {
    private static final Color[] colors = {
        Color.blue, Color.lightGray, Color.cyan, Color.magenta
    };
    private final double velocity;
    private final Color color;
    private double x;
    private Light light;

    public Car(double x, double velocity, Color color) {
        this.x = x;
        this.velocity = velocity;
        this.color = color;
    }

    public Car(double x, Random rand) {
        this(x, randomVelocity(rand), randomColor(rand));
    }

    private static double randomVelocity(Random rand) {
        return rand.nextDouble(0, 25);
    }

    private static Color randomColor(Random rand) {
        return colors[rand.nextInt(colors.length)];
    }

    public double getX() {
        return x;
    }

    public double getVelocity() {
        if (!isStopped()) {
            return velocity;
        }
        return 0;
    }

    public Color getColor() {
        return color;
    }

    private boolean isStopped() {
        return light != null && light.getState() == Light.State.stopped;
    }

    public void tick() {
        x += getVelocity() * 0.1;
    }

    public void setLight(Light light) {
        this.light = light;
    }

    public boolean expired() {
        return x >= 1200;
    }
}