import javax.sound.sampled.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Enhanced Sound Manager with distinct sound effects for each action
 * Provides unique audio feedback for different game events
 */
public class EnhancedSoundManager {
    private static Map<String, Clip> soundClips = new HashMap<>();
    private static boolean soundEnabled = true;
    private static ExecutorService soundExecutor = Executors.newCachedThreadPool();
    
    // Enhanced sound mappings with distinct effects
    private static final Map<String, SoundConfig> SOUND_CONFIGS = new HashMap<String, SoundConfig>() {{
        // Menu sounds
        put("menu_click", new SoundConfig("click", 800, 100, 0.5f));
        put("menu_hover", new SoundConfig("hover", 1000, 50, 0.3f));
        put("start_game", new SoundConfig("start", 600, 300, 0.7f));
        put("back_button", new SoundConfig("back", 400, 150, 0.4f));
        
        // Game sounds
        put("countdown_tick", new SoundConfig("tick", 1200, 80, 0.6f));
        put("countdown_final", new SoundConfig("final", 800, 200, 0.8f));
        put("move_select", new SoundConfig("select", 1500, 100, 0.5f));
        put("move_rock", new SoundConfig("rock", 300, 150, 0.6f));
        put("move_paper", new SoundConfig("paper", 800, 120, 0.5f));
        put("move_scissors", new SoundConfig("scissors", 1200, 140, 0.6f));
        
        // Result sounds
        put("round_win", new SoundConfig("win", 880, 400, 0.7f));
        put("round_lose", new SoundConfig("lose", 220, 300, 0.6f));
        put("round_draw", new SoundConfig("draw", 660, 250, 0.5f));
        put("game_victory", new SoundConfig("victory", 523, 800, 0.8f));
        put("game_defeat", new SoundConfig("defeat", 196, 600, 0.7f));
        put("game_tie", new SoundConfig("tie", 440, 500, 0.6f));
        
        // Special sounds
        put("cheat_detected", new SoundConfig("cheat", 150, 200, 0.9f));
        put("theme_switch", new SoundConfig("switch", 1000, 150, 0.4f));
        put("export_success", new SoundConfig("success", 880, 300, 0.6f));
        put("error", new SoundConfig("error", 200, 400, 0.7f));
        put("achievement", new SoundConfig("achievement", 1760, 500, 0.8f));
        
        // Animation sounds
        put("text_animation", new SoundConfig("animation", 440, 100, 0.3f));
        put("fade_in", new SoundConfig("fade", 660, 200, 0.2f));
        put("celebration", new SoundConfig("celebrate", 1320, 600, 0.7f));
    }};
    
    /**
     * Sound configuration class
     */
    private static class SoundConfig {
        String name;
        int frequency;
        int duration;
        float volume;
        
        SoundConfig(String name, int frequency, int duration, float volume) {
            this.name = name;
            this.frequency = frequency;
            this.duration = duration;
            this.volume = volume;
        }
    }
    
    /**
     * Initialize enhanced sound system
     */
    public static void initialize() {
        // Pre-generate commonly used sounds
        preloadSound("menu_click");
        preloadSound("countdown_tick");
        preloadSound("move_select");
        preloadSound("round_win");
        preloadSound("round_lose");
        preloadSound("round_draw");
    }
    
    /**
     * Play a sound by name with enhanced effects
     */
    public static void playSound(String soundName) {
        if (!soundEnabled) return;
        
        soundExecutor.submit(() -> {
            try {
                SoundConfig config = SOUND_CONFIGS.get(soundName);
                if (config != null) {
                    playTone(config.frequency, config.duration, config.volume);
                } else {
                    // Fallback for unknown sounds
                    playTone(800, 100, 0.5f);
                }
            } catch (Exception e) {
                System.out.println("Error playing sound: " + soundName);
            }
        });
    }
    
    /**
     * Play sound with custom parameters
     */
    public static void playSound(String soundName, float volume) {
        if (!soundEnabled) return;
        
        SoundConfig config = SOUND_CONFIGS.get(soundName);
        if (config != null) {
            soundExecutor.submit(() -> {
                try {
                    playTone(config.frequency, config.duration, volume);
                } catch (Exception e) {
                    System.out.println("Error playing sound: " + soundName);
                }
            });
        }
    }
    
    /**
     * Play a sequence of sounds for complex effects
     */
    public static void playSoundSequence(String[] soundNames, int delayMs) {
        if (!soundEnabled) return;
        
        soundExecutor.submit(() -> {
            for (String soundName : soundNames) {
                playSound(soundName);
                try {
                    Thread.sleep(delayMs);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
    }
    
    /**
     * Play celebration sound sequence
     */
    public static void playCelebration() {
        String[] sequence = {"achievement", "celebration", "game_victory"};
        playSoundSequence(sequence, 200);
    }
    
    /**
     * Play countdown sequence
     */
    public static void playCountdownSequence() {
        soundExecutor.submit(() -> {
            // Three ticks followed by final sound
            for (int i = 0; i < 3; i++) {
                playSound("countdown_tick");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    break;
                }
            }
            playSound("countdown_final");
        });
    }
    
    /**
     * Generate and play a tone
     */
    private static void playTone(int frequency, int duration, float volume) {
        try {
            int sampleRate = 16000;
            int samples = (sampleRate * duration) / 1000;
            byte[] buffer = new byte[samples];
            
            // Generate sine wave
            for (int i = 0; i < samples; i++) {
                double angle = 2.0 * Math.PI * i * frequency / sampleRate;
                buffer[i] = (byte) (Math.sin(angle) * 127 * volume);
            }
            
            // Apply envelope for smoother sound
            applyEnvelope(buffer);
            
            // Play the sound
            AudioFormat format = new AudioFormat(sampleRate, 8, 1, true, false);
            InputStream inputStream = new ByteArrayInputStream(buffer);
            AudioInputStream audioInputStream = new AudioInputStream(inputStream, format, buffer.length);
            
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
            
            // Clean up after playing
            new Thread(() -> {
                try {
                    Thread.sleep(duration + 100);
                    clip.close();
                } catch (Exception e) {
                    // Ignore cleanup errors
                }
            }).start();
            
        } catch (Exception e) {
            System.out.println("Error generating tone: " + e.getMessage());
        }
    }
    
    /**
     * Apply envelope to make sound smoother
     */
    private static void applyEnvelope(byte[] buffer) {
        int fadeLength = Math.min(buffer.length / 10, 1000);
        
        // Fade in
        for (int i = 0; i < fadeLength; i++) {
            float factor = (float) i / fadeLength;
            buffer[i] = (byte) (buffer[i] * factor);
        }
        
        // Fade out
        for (int i = buffer.length - fadeLength; i < buffer.length; i++) {
            float factor = (float) (buffer.length - i) / fadeLength;
            buffer[i] = (byte) (buffer[i] * factor);
        }
    }
    
    /**
     * Preload a sound for better performance
     */
    private static void preloadSound(String soundName) {
        // Just initialize the sound config
        SoundConfig config = SOUND_CONFIGS.get(soundName);
        if (config != null) {
            // Sound will be generated when first played
        }
    }
    
    /**
     * Play move-specific sound
     */
    public static void playMoveSound(Move move) {
        if (move == null) return;
        
        switch (move) {
            case ROCK:
                playSound("move_rock");
                break;
            case PAPER:
                playSound("move_paper");
                break;
            case SCISSORS:
                playSound("move_scissors");
                break;
        }
    }
    
    /**
     * Play result-specific sound
     */
    public static void playResultSound(GameResult result, boolean isGameEnd) {
        if (result == null) return;
        
        if (isGameEnd) {
            // Game end sounds
            switch (result) {
                case WIN:
                    playCelebration();
                    break;
                case LOSE:
                    playSound("game_defeat");
                    break;
                case DRAW:
                    playSound("game_tie");
                    break;
            }
        } else {
            // Round sounds
            switch (result) {
                case WIN:
                    playSound("round_win");
                    break;
                case LOSE:
                    playSound("round_lose");
                    break;
                case DRAW:
                    playSound("round_draw");
                    break;
            }
        }
    }
    
    /**
     * Play UI interaction sounds
     */
    public static void playUISound(String action) {
        switch (action) {
            case "click":
                playSound("menu_click");
                break;
            case "hover":
                playSound("menu_hover");
                break;
            case "start":
                playSound("start_game");
                break;
            case "back":
                playSound("back_button");
                break;
            case "theme":
                playSound("theme_switch");
                break;
            case "export":
                playSound("export_success");
                break;
            case "error":
                playSound("error");
                break;
        }
    }
    
    /**
     * Enable or disable sound effects
     */
    public static void setSoundEnabled(boolean enabled) {
        soundEnabled = enabled;
        if (enabled) {
            playSound("theme_switch");
        }
    }
    
    /**
     * Check if sound is enabled
     */
    public static boolean isSoundEnabled() {
        return soundEnabled;
    }
    
    /**
     * Stop all sounds and cleanup
     */
    public static void cleanup() {
        soundEnabled = false;
        if (soundExecutor != null && !soundExecutor.isShutdown()) {
            soundExecutor.shutdown();
        }
        
        for (Clip clip : soundClips.values()) {
            if (clip != null && clip.isRunning()) {
                clip.stop();
                clip.close();
            }
        }
        soundClips.clear();
    }
}