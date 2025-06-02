import javax.sound.sampled.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Sound Manager - Handles all audio effects in the game
 * Provides easy sound loading and playing capabilities
 */
public class SoundManager {
    private static Map<String, Clip> soundClips = new HashMap<>();
    private static boolean soundEnabled = true;
    
    // Sound file mappings
    private static final Map<String, String> SOUND_FILES = new HashMap<String, String>() {{
        put("countdown", "resources/sounds/countdown.wav");
        put("go", "resources/sounds/countdown.wav");
        put("select", "resources/sounds/select.wav");
        put("win", "resources/sounds/win.wav");
        put("lose", "resources/sounds/lose.wav");
        put("draw", "resources/sounds/draw.wav");
        put("cheat", "resources/sounds/win.wav");
        put("start", "resources/sounds/win.wav");
    }};
    
    /**
     * Initialize sound system and preload common sounds
     */
    public static void initialize() {
        // Preload frequently used sounds
        loadSound("countdown");
        loadSound("select");
        loadSound("win");
        loadSound("lose");
        loadSound("draw");
    }
    
    /**
     * Load a sound file into memory
     */
    private static void loadSound(String soundName) {
        if (!soundEnabled) return;
        
        try {
            String filename = SOUND_FILES.get(soundName);
            if (filename == null) {
                System.out.println("Sound file mapping not found: " + soundName);
                return;
            }
            
            // Try to load from file system first
            File soundFile = new File(filename);
            AudioInputStream audioInputStream;
            
            if (soundFile.exists()) {
                audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            } else {
                // Try to load from resources (when running from JAR)
                InputStream resourceStream = SoundManager.class.getResourceAsStream("/" + filename);
                if (resourceStream == null) {
                    // Create a simple beep sound as fallback
                    createBeepSound(soundName);
                    return;
                }
                audioInputStream = AudioSystem.getAudioInputStream(resourceStream);
            }
            
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            soundClips.put(soundName, clip);
            
            audioInputStream.close();
            
        } catch (Exception e) {
            System.out.println("Could not load sound: " + soundName + " - " + e.getMessage());
            // Create fallback beep sound
            createBeepSound(soundName);
        }
    }
    
    /**
     * Create a simple beep sound as fallback when audio files are not available
     */
    private static void createBeepSound(String soundName) {
        try {
            // Create a simple tone
            int sampleRate = 16000;
            int duration = 200; // milliseconds
            
            if (soundName.equals("countdown")) duration = 100;
            else if (soundName.equals("win")) duration = 500;
            else if (soundName.equals("lose")) duration = 300;
            
            byte[] buffer = new byte[sampleRate * duration / 1000];
            
            for (int i = 0; i < buffer.length; i++) {
                double angle = 2.0 * Math.PI * i * 800 / sampleRate; // 800 Hz tone
                buffer[i] = (byte) (Math.sin(angle) * 127);
            }
            
            AudioFormat format = new AudioFormat(sampleRate, 8, 1, true, false);
            InputStream inputStream = new ByteArrayInputStream(buffer);
            AudioInputStream audioInputStream = new AudioInputStream(inputStream, format, buffer.length);
            
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            soundClips.put(soundName, clip);
            
        } catch (Exception e) {
            System.out.println("Could not create fallback sound: " + soundName);
        }
    }
    
    /**
     * Play a sound by name
     */
    public static void playSound(String soundName) {
        if (!soundEnabled) return;
        
        // Load sound if not already loaded
        if (!soundClips.containsKey(soundName)) {
            loadSound(soundName);
        }
        
        Clip clip = soundClips.get(soundName);
        if (clip != null) {
            try {
                // Stop the clip if it's already playing
                if (clip.isRunning()) {
                    clip.stop();
                }
                
                // Rewind to beginning
                clip.setFramePosition(0);
                
                // Play the sound
                clip.start();
                
            } catch (Exception e) {
                System.out.println("Error playing sound: " + soundName + " - " + e.getMessage());
            }
        }
    }
    
    /**
     * Play sound with volume control (0.0 to 1.0)
     */
    public static void playSound(String soundName, float volume) {
        if (!soundEnabled) return;
        
        if (!soundClips.containsKey(soundName)) {
            loadSound(soundName);
        }
        
        Clip clip = soundClips.get(soundName);
        if (clip != null) {
            try {
                // Set volume
                FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
                volumeControl.setValue(dB);
                
                // Stop and play
                if (clip.isRunning()) {
                    clip.stop();
                }
                clip.setFramePosition(0);
                clip.start();
                
            } catch (Exception e) {
                // Fallback to normal play if volume control fails
                playSound(soundName);
            }
        }
    }
    
    /**
     * Enable or disable sound effects
     */
    public static void setSoundEnabled(boolean enabled) {
        soundEnabled = enabled;
        
        if (!enabled) {
            // Stop all currently playing sounds
            stopAllSounds();
        }
    }
    
    /**
     * Check if sound is enabled
     */
    public static boolean isSoundEnabled() {
        return soundEnabled;
    }
    
    /**
     * Stop all currently playing sounds
     */
    public static void stopAllSounds() {
        for (Clip clip : soundClips.values()) {
            if (clip != null && clip.isRunning()) {
                clip.stop();
            }
        }
    }
    
    /**
     * Clean up resources
     */
    public static void cleanup() {
        stopAllSounds();
        for (Clip clip : soundClips.values()) {
            if (clip != null) {
                clip.close();
            }
        }
        soundClips.clear();
    }
    
    /**
     * Preload all sounds for better performance
     */
    public static void preloadAllSounds() {
        for (String soundName : SOUND_FILES.keySet()) {
            if (!soundClips.containsKey(soundName)) {
                loadSound(soundName);
            }
        }
    }
    
    /**
     * Test if audio system is working
     */
    public static boolean testAudioSystem() {
        try {
            // Try to get the audio system
            Mixer.Info[] mixers = AudioSystem.getMixerInfo();
            return mixers.length > 0;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Get available sound effects
     */
    public static String[] getAvailableSounds() {
        return SOUND_FILES.keySet().toArray(new String[0]);
    }
    
    /**
     * Play a sequence of sounds with delays
     */
    public static void playSoundSequence(String[] soundNames, int delayMs) {
        Thread soundThread = new Thread(() -> {
            for (String soundName : soundNames) {
                playSound(soundName);
                try {
                    Thread.sleep(delayMs);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        soundThread.setDaemon(true);
        soundThread.start();
    }
}