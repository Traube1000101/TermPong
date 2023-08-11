package org.traube;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

@FunctionalInterface
interface Function {
    void apply();
}


public class Main {
    void executeFunctionOnKey(Function function, int keyCode) {
        if (KeyListener.isPressed.containsKey(keyCode) && KeyListener.isPressed.get(keyCode)) {
            function.apply();
        }
    }
    static void executeFunctionOnKey(Function function, int keyCode, int keyCode2) {
        if ((KeyListener.isPressed.containsKey(keyCode) && KeyListener.isPressed.get(keyCode)) || (KeyListener.isPressed.containsKey(keyCode2) && KeyListener.isPressed.get(keyCode2))) {
            function.apply();
        }
    }

    public static boolean exit = false;
    public static void main(String... args) throws Exception {
        String characters = " `.-':_,^=;><+!rc*/z?sLTv)J7(|Fi{C}fI31tlu[neoZ5Yxjya]2ESwqkP6h9d4VpOGbUAKXHm8RD#$Bg0MNWQ%&@";
        char[] charsSortedByBrightness = characters.toCharArray();
        double[] brightnessValues = {0.0, 0.0751, 0.0829, 0.0848, 0.1227, 0.1403, 0.1559, 0.185, 0.2183, 0.2417, 0.2571, 0.2852, 0.2902, 0.2919, 0.3099, 0.3192, 0.3232, 0.3294, 0.3384, 0.3609, 0.3619, 0.3667, 0.3737, 0.3747, 0.3838, 0.3921, 0.396, 0.3984, 0.3993, 0.4075, 0.4091, 0.4101, 0.42, 0.423, 0.4247, 0.4274, 0.4293, 0.4328, 0.4382, 0.4385, 0.442, 0.4473, 0.4477, 0.4503, 0.4562, 0.458, 0.461, 0.4638, 0.4667, 0.4686, 0.4693, 0.4703, 0.4833, 0.4881, 0.4944, 0.4953, 0.4992, 0.5509, 0.5567, 0.5569, 0.5591, 0.5602, 0.5602, 0.565, 0.5776, 0.5777, 0.5818, 0.587, 0.5972, 0.5999, 0.6043, 0.6049, 0.6093, 0.6099, 0.6465, 0.6561, 0.6595, 0.6631, 0.6714, 0.6759, 0.6809, 0.6816, 0.6925, 0.7039, 0.7086, 0.7235, 0.7302, 0.7332, 0.7602, 0.7834, 0.8037, 0.9999};
        double[] brightnessValuesInverted = new double[brightnessValues.length];
        for (int i = 0; i < brightnessValues.length; i++) {
            brightnessValuesInverted[i] = 1 - brightnessValues[i];
        }


        double circleHeight = 5, circleWidth = 10;
        int canvasHeight = (int) (circleHeight * 7), canvasWidth = (int) (circleWidth * 10);
        int circleCenterX = canvasWidth / 2, circleCenterY = canvasHeight / 2;

        double[][] pixels = new double[canvasWidth][canvasHeight];
        long beginningTime, endTime;
        int totalFrames = 1;

        KeyListener.initializeKeyHook();

        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(java.io.FileDescriptor.out), StandardCharsets.UTF_8), 512); //"ASCII"
        out.write("\033[H\033[2J");
        out.flush();

        double ellipsis = 0.0;
        AtomicReference<Double> horizontal = new AtomicReference<>(0.0);
        AtomicReference<Double> vertical = new AtomicReference<>(0.0);

        Timer timer = new Timer();
        long sleep = (long) (Timer.ONE_SECOND / 60);
        try {
            while(!exit) {
                // Tick and print fps
                long lastTick = System.nanoTime();
                timer.tick();
                out.write("FPS: ");
                out.write(Integer.toString(timer.getFPS()));
                out.write("\t\tUse WASD or the arrow keys to move the circle!");
                out.flush();

                Function moveLeft = () -> horizontal.updateAndGet(v -> Double.valueOf(v - 0.0075));
                executeFunctionOnKey(moveLeft, 30, 57419);

                Function moveRight = () -> horizontal.updateAndGet(v -> Double.valueOf(v + 0.0075));
                executeFunctionOnKey(moveRight, 32, 57421);

                Function moveUp = () -> vertical.updateAndGet(v -> Double.valueOf(v - 0.015));
                executeFunctionOnKey(moveUp, 17, 57416);

                Function moveDown = () -> vertical.updateAndGet(v -> Double.valueOf(v + 0.015));
                executeFunctionOnKey(moveDown, 31, 57424);


                // Fills the array with a circle
                for (int x = 0; x < canvasWidth; x++) {
                    for (int y = 0; y < canvasHeight; y++) {
                        ellipsis = (Math.pow(x - ((circleCenterX - 0.5) * (horizontal.get() + 1.0)), 2) / Math.pow(circleWidth, 2)) + (Math.pow(y - ((circleCenterY - 0.5) * (vertical.get() + 1.0)), 2) / Math.pow(circleHeight, 2));
                        pixels[x][y] = ellipsis;
                    }
                }

                out.write("\033[H"); // Moves cursor to top left
                for (int y = 0; y < canvasHeight; y++) {
                    for (int x = 0; x < canvasWidth; x++)
                        out.write(charsSortedByBrightness[indexOfClosestInArray(brightnessValuesInverted, pixels[x][y])]);
                    out.newLine();
                }
                out.flush();

                // Sleep to retain Frame Rate
                long wake = lastTick + sleep;
                do {Thread.sleep(0);}
                while (System.nanoTime() < wake);
            }
        } catch (InterruptedException e) {
            // Got interrupted
        }
    }


    static int indexOfClosestInArray(double[] array, double value) {
        double distance = Math.abs(array[0] - value);
        int idx = 0;
        for (int c = 1; c < array.length; c++) {
            double cdistance = Math.abs(array[c] - value);
            if (cdistance < distance) {
                idx = c;
                distance = cdistance;
            }
        }
        return idx;
    }
}