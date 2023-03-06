package Traffic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class Simulator {
    private final ArrayList<Light> lights = new ArrayList<>();
    private final ArrayList<Car> cars = new ArrayList<>();
    private final Random rand = new Random();
    private int ticks = 0;

    public Simulator() {
        int nLights = rand.nextInt(3, 5),
            distance = 1000 / nLights;

        for (int i = 0; i < nLights; i++) {
            int x = (i + 1) * distance;
            Light l = new Light(x, Light.State.getRandom(rand));
            lights.add(l);
        }

        for (int i = 0; i < 3; i++) {
            int x = rand.nextInt(500);
            cars.add(new Car(x, rand));
        }
    }

    public List<Light> getLights() {
        return Collections.unmodifiableList(lights);
    }

    public List<Car> getCars() {
        return Collections.unmodifiableList(cars);
    }

    private Light getLight(double x) {
        for (Light light: lights) {
            double diff = light.x - x;
            if (diff < 5 && diff > -1)
                return light;
        }
        return null;
    }

    public void tick() {
        boolean newCar = rand.nextInt(100) < 1;
        if (newCar) {
            int x = 50;
            cars.add(new Car(x, rand));
        }

        List<Car> toRemove = new ArrayList<>();
        for (Car car: cars) {
            double x = car.getX();
            Light light = getLight(x);
            car.setLight(light);
            car.tick();
            if (car.expired())
                toRemove.add(car);
        }
        for (Car car: toRemove)
            cars.remove(car);

        ticks++;
        if (ticks % 10 == 0) {
            for (Light light: lights)
                light.tick();
        }
    }
}