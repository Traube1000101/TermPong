package org.traube;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

import java.util.HashMap;

public class KeyListener implements NativeKeyListener {

    // HashMap to keep track of which keys are pressed
    public static HashMap<Integer, Boolean> isPressed = new HashMap<>();

    public void nativeKeyPressed(NativeKeyEvent e) {
        int keyCode = e.getKeyCode();
        //if (!isPressed.containsKey(keyCode) || !isPressed.get(keyCode)) {
        //    System.out.println("Key Pessed: " + NativeKeyEvent.getKeyText(keyCode) + " - Key Code: " + keyCode);
        //}
        isPressed.put(keyCode, true);

        if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
            try {
                GlobalScreen.unregisterNativeHook();
                Main.exit=true;
            } catch (NativeHookException nativeHookException) {
                nativeHookException.printStackTrace();
            }
        }
    }

    public void nativeKeyReleased(NativeKeyEvent e) {
        int keyCode = e.getKeyCode();
        isPressed.put(keyCode, false);
        //System.out.println("Key Released: " + NativeKeyEvent.getKeyText(keyCode) + " - Key Code: " + keyCode);
    }

    public static void initializeKeyHook() {
        try {
            GlobalScreen.registerNativeHook();
        }
        catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());

            System.exit(1);
        }
        GlobalScreen.addNativeKeyListener(new KeyListener());
    }
}