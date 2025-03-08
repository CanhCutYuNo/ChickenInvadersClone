package debug;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author hp
 */
/**
 * How to use: --> Call only 1 function UPSCounter.Update() to get UPS value
 * every 1 second in STDOUT
 *
 */
public abstract class UPSCounter {

    private static long lastTime;
    private static int frames;
    private static int fps;

    public UPSCounter() {
        lastTime = System.nanoTime();
        frames = 0;
        fps = 0;
    }

    public static void Update() {
        frames++;
        long currentTime = System.nanoTime();
        if (currentTime - lastTime >= 1_000_000_000) { // 1 second
            fps = frames;
            frames = 0;
            lastTime = currentTime;
            System.out.println("UPS: " + fps);
        }
    }

}
