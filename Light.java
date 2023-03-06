package Traffic;

import java.util.Random;

public class Light {
    public enum State {
        started, stopSoon, stopped;

        public State getNext() {
            int i = (ordinal() + 1) % values().length;
            return values()[i];
        }

        public static State getRandom(Random rand) {
            int i = rand.nextInt(values().length);
            return values()[i];
        }
    }

    private State state;
    private int seconds;
    public final int x;

    public Light(int x, State initialState) {
        this.x = x;
        setState(initialState);
    }

    public void tick() {
        if (seconds >= 1)
            seconds -= 1;
        else setState(state.getNext());
    }

    public int getSeconds() {
        return seconds;
    }

    public void setState(State state) {
        this.state = state;

        seconds = switch (state) {
            case stopped -> 15;
            case started -> 10;
            default -> 3;
        };
    }

    public State getState() {
        return state;
    }

}