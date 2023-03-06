package Traffic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class SimulatorFrame extends JFrame {
    private enum State { stopped, paused, running }
    private State state = State.stopped;

    private final JButton btnStart = new JButton("Start");
    private final JButton btnPause = new JButton("Pause");

    private final Map<Light.State, Color> stateColors = Map.of(
        Light.State.started, Color.green,
        Light.State.stopSoon, Color.yellow,
        Light.State.stopped, Color.red
    );

    private final Timer t = new Timer(100, this::clockTicks);
    private Simulator sim;

    SimulatorFrame() {
        btnPause.setEnabled(false);
        btnPause.addActionListener(this::pauseClicked);
        btnStart.addActionListener(this::startClicked);

        JPanel jp = new JPanel();
        jp.setLayout(new BorderLayout());
        JPanel south = new JPanel();
        south.setLayout(new FlowLayout());
        south.add(btnStart);
        south.add(btnPause);
        jp.add(south, BorderLayout.SOUTH);
        jp.add(new Road(), BorderLayout.CENTER);
        getContentPane().add(jp);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setVisible(true);
    }

    private void pauseClicked(ActionEvent event) {
        state = switch (state) {
            case running -> {
                btnPause.setText("Continue");
                t.stop();
                yield State.paused;
            }
            case paused -> {
                btnPause.setText("Pause");
                t.start();
                yield State.running;
            }
            default -> throw new IllegalStateException();
        };
    }

    private void startClicked(ActionEvent event) {
        state = switch (state) {
            case stopped -> {
                btnStart.setText("Stop");
                btnPause.setEnabled(true);
                sim = new Simulator();
                t.start();
                yield State.running;
            }
            case running, paused -> {
                btnStart.setText("Start");
                btnPause.setText("Pause");
                btnPause.setEnabled(false);
                t.stop();
                sim = null;
                yield State.stopped;
            }
        };
    }

    private void clockTicks(ActionEvent event) {
        sim.tick();
        getContentPane().repaint();
    }

    private class Road extends JPanel {
        private record TextExtent(
            int y, int x1, int x2
        ) { }

        private static final int
            LIGHT_RADIUS = 20,
            LIGHT_DIAMETER = 2*LIGHT_RADIUS,
            LIGHT_Y = 350,
            LIGHT_STICK_HEIGHT = 60,
            CAR_Y = 400,
            CAR_TEXT_Y = 470;

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (sim == null) return;

            for (Light light: sim.getLights())
                paintLight(g, light);

            FontMetrics metrics = g.getFontMetrics();
            List<TextExtent> lines = new ArrayList<>();

            for (Car car: sim.getCars())
                paintCar(g, metrics, car, lines);
        }

        private void paintLight(Graphics g, Light light) {
            g.setColor(Color.BLACK);
            g.drawString(Integer.toString(light.getSeconds()), light.x, LIGHT_Y - 60);
            Color c = stateColors.get(light.getState());
            g.setColor(c);
            g.fillOval(light.x, LIGHT_Y, LIGHT_DIAMETER, LIGHT_DIAMETER);
            g.drawLine(
                light.x + LIGHT_RADIUS, LIGHT_Y + LIGHT_RADIUS,
                light.x + LIGHT_RADIUS, LIGHT_Y + LIGHT_STICK_HEIGHT
            );
        }

        private void paintCar(Graphics g, FontMetrics metrics, Car car, List<TextExtent> lines) {
            Color c = car.getColor();
            g.setColor(c);
            int x = (int)car.getX();
            g.fillRect(x, CAR_Y, 20, 20);

            String desc = "(%.0f, 0) %.2f km/h".formatted(
                car.getX(), car.getVelocity()
            );
            int w = metrics.stringWidth(desc),
                x2 = x + w,
                y;

            for (y = 0;; y++) {
                boolean found = false;
                for (TextExtent text: lines) {
                    if (
                        y == text.y
                            && x < text.x2
                            && x2 > text.x1
                    ) {
                        found = true;
                        break;
                    }
                }
                if (!found) break;
            }

            lines.add(new TextExtent(y, x, x2));

            g.setColor(Color.black);
            g.drawString(
                desc, x,
                CAR_TEXT_Y + metrics.getHeight()*y
            );
        }
    }



}