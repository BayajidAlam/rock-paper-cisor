import java.awt.*;

/**
 * Constants - Game configuration and settings
 * Contains all game constants, key bindings, and configuration values
 */
public class Constants {
    
    // ===== GAME SETTINGS =====
    
    /** Default countdown time in seconds */
    public static final int COUNTDOWN_SECONDS = 3;
    
    /** Timer delay for countdown in milliseconds */
    public static final int COUNTDOWN_TIMER_DELAY = 1000;
    
    /** Delay after "GO!" before finishing round in milliseconds */
    public static final int FINISH_ROUND_DELAY = 500;
    
    /** Maximum number of rounds allowed */
    public static final int MAX_ROUNDS = 10;
    
    /** Default number of rounds */
    public static final int DEFAULT_ROUNDS = 3;
    
    // ===== KEY BINDINGS =====
    
    /** Player 1 key bindings */
    public static final char PLAYER1_ROCK = 'A';
    public static final char PLAYER1_PAPER = 'S';
    public static final char PLAYER1_SCISSORS = 'D';
    
    /** Player 2 key bindings (for PvP mode) */
    public static final char PLAYER2_ROCK = 'J';
    public static final char PLAYER2_PAPER = 'K';
    public static final char PLAYER2_SCISSORS = 'L';
    
    // ===== UI COLORS =====
    
    /** Background color for main window */
    public static final Color BACKGROUND_COLOR = new Color(240, 248, 255); // Alice Blue
    
    /** Game panel background color */
    public static final Color GAME_BACKGROUND = new Color(245, 245, 220); // Beige
    
    /** Primary text color */
    public static final Color PRIMARY_TEXT = new Color(25, 25, 112); // Midnight Blue
    
    /** Secondary text color */
    public static final Color SECONDARY_TEXT = new Color(70, 70, 70); // Dark Gray
    
    /** Win result color */
    public static final Color WIN_COLOR = new Color(34, 139, 34); // Forest Green
    
    /** Lose result color */
    public static final Color LOSE_COLOR = new Color(220, 20, 60); // Crimson
    
    /** Draw result color */
    public static final Color DRAW_COLOR = new Color(255, 140, 0); // Dark Orange
    
    /** Countdown text color */
    public static final Color COUNTDOWN_COLOR = new Color(220, 20, 60); // Crimson
    
    /** Button background color */
    public static final Color BUTTON_COLOR = new Color(34, 139, 34); // Forest Green
    
    /** Button hover color */
    public static final Color BUTTON_HOVER = new Color(50, 205, 50); // Lime Green
    
    /** Cheating detection color */
    public static final Color CHEAT_COLOR = Color.RED;
    
    // ===== UI FONTS =====
    
    /** Main title font */
    public static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 42);
    
    /** Subtitle font */
    public static final Font SUBTITLE_FONT = new Font("Arial", Font.PLAIN, 16);
    
    /** Large countdown font */
    public static final Font COUNTDOWN_FONT = new Font("Arial", Font.BOLD, 120);
    
    /** Game over font */
    public static final Font GAME_OVER_FONT = new Font("Arial", Font.BOLD, 48);
    
    /** Result display font */
    public static final Font RESULT_FONT = new Font("Arial", Font.BOLD, 36);
    
    /** Final result font */
    public static final Font FINAL_RESULT_FONT = new Font("Arial", Font.BOLD, 28);
    
    /** Section header font */
    public static final Font SECTION_FONT = new Font("Arial", Font.BOLD, 18);
    
    /** Regular text font */
    public static final Font REGULAR_FONT = new Font("Arial", Font.PLAIN, 14);
    
    /** Large button font */
    public static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 20);
    
    /** Small button font */
    public static final Font SMALL_BUTTON_FONT = new Font("Arial", Font.PLAIN, 14);
    
    /** Move symbol font */
    public static final Font MOVE_FONT = new Font("Arial", Font.PLAIN, 72);
    
    /** Instructions font */
    public static final Font INSTRUCTION_FONT = new Font("Arial", Font.PLAIN, 12);
    
    // ===== UI DIMENSIONS =====
    
    /** Main window width */
    public static final int WINDOW_WIDTH = 800;
    
    /** Main window height */
    public static final int WINDOW_HEIGHT = 600;
    
    /** Standard button width */
    public static final int BUTTON_WIDTH = 200;
    
    /** Standard button height */
    public static final int BUTTON_HEIGHT = 50;
    
    /** Small button height */
    public static final int SMALL_BUTTON_HEIGHT = 30;
    
    /** Combo box width */
    public static final int COMBO_WIDTH = 200;
    
    /** Small combo box width */
    public static final int SMALL_COMBO_WIDTH = 100;
    
    /** Moves panel max width */
    public static final int MOVES_PANEL_WIDTH = 600;
    
    /** Moves panel height */
    public static final int MOVES_PANEL_HEIGHT = 100;
    
    // ===== SPACING AND PADDING =====
    
    /** Standard padding */
    public static final int STANDARD_PADDING = 20;
    
    /** Large padding */
    public static final int LARGE_PADDING = 50;
    
    /** Small padding */
    public static final int SMALL_PADDING = 10;
    
    /** Component spacing */
    public static final int COMPONENT_SPACING = 20;
    
    /** Large component spacing */
    public static final int LARGE_SPACING = 40;
    
    /** Small component spacing */
    public static final int SMALL_SPACING = 10;
    
    // ===== GAME LOGIC CONSTANTS =====
    
    /** Maximum allowed key presses per round (for anti-cheat) */
    public static final int MAX_KEY_PRESSES = 1;
    
    /** AI learning window size (number of recent moves to consider) */
    public static final int AI_LEARNING_WINDOW = 5;
    
    /** Probability thresholds for AI strategies */
    public static final double AI_RANDOM_THRESHOLD = 0.7;
    public static final double AI_PATTERN_THRESHOLD = 0.4;
    public static final double AI_FREQUENCY_THRESHOLD = 0.5;
    public static final double AI_ANTI_FREQUENCY_THRESHOLD = 0.67;
    
    // ===== MOVE SYMBOLS =====
    
    /** Rock symbol */
    public static final String ROCK_SYMBOL = "✊";
    
    /** Paper symbol */
    public static final String PAPER_SYMBOL = "✋";
    
    /** Scissors symbol */
    public static final String SCISSORS_SYMBOL = "✌️";
    
    /** Unknown move symbol */
    public static final String UNKNOWN_SYMBOL = "?";
    
    /** Cheat detection symbol */
    public static final String CHEAT_SYMBOL = "❌";
    
    // ===== GAME MESSAGES =====
    
    /** Welcome messages */
    public static final String WELCOME_TITLE = "ROCK PAPER SCISSORS";
    public static final String WELCOME_SUBTITLE = "Choose your game mode and start playing!";
    
    /** Game mode descriptions */
    public static final String PVC_DESCRIPTION = "Player vs Computer";
    public static final String PVP_DESCRIPTION = "Player vs Player";
    
    /** Control instructions */
    public static final String PVC_CONTROLS = "A=Rock, S=Paper, D=Scissors";
    public static final String PVP_CONTROLS = "P1: A/S/D | P2: J/K/L";
    
    /** Game state messages */
    public static final String GET_READY = "Get ready...";
    public static final String MAKE_MOVE = "Make your move! ";
    public static final String GAME_OVER = "GAME OVER";
    public static final String NEXT_ROUND = "Click 'Next Round' to continue";
    public static final String GAME_FINISHED = "Game finished! Final score: ";
    
    /** Result messages */
    public static final String YOU_WIN = "You Win!";
    public static final String COMPUTER_WINS = "Computer Wins!";
    public static final String PLAYER1_WINS = "Player 1 Wins!";
    public static final String PLAYER2_WINS = "Player 2 Wins!";
    public static final String ITS_A_TIE = "It's a Tie!";
    
    /** Cheating messages */
    public static final String BOTH_CHEATED = "Both players cheated! Round void.";
    public static final String PLAYER1_CHEATED = "Player 1 cheated! Player 2 wins round.";
    public static final String PLAYER2_CHEATED = "Player 2 cheated! Player 1 wins round.";
    public static final String CHEATING_DETECTED = "Cheating detected! No multiple key presses allowed.";
    
    // ===== SOUND EFFECT NAMES =====
    
    /** Sound effect identifiers */
    public static final String SOUND_COUNTDOWN = "countdown";
    public static final String SOUND_GO = "go";
    public static final String SOUND_SELECT = "select";
    public static final String SOUND_WIN = "win";
    public static final String SOUND_LOSE = "lose";
    public static final String SOUND_DRAW = "draw";
    public static final String SOUND_CHEAT = "cheat";
    public static final String SOUND_START = "start";
    
    // ===== FILE PATHS =====
    
    /** Resource directory paths */
    public static final String SOUNDS_PATH = "resources/sounds/";
    public static final String IMAGES_PATH = "resources/images/";
    
    /** Specific sound files */
    public static final String COUNTDOWN_SOUND = SOUNDS_PATH + "countdown.wav";
    public static final String WIN_SOUND = SOUNDS_PATH + "win.wav";
    public static final String LOSE_SOUND = SOUNDS_PATH + "lose.wav";
    public static final String DRAW_SOUND = SOUNDS_PATH + "draw.wav";
    public static final String SELECT_SOUND = SOUNDS_PATH + "select.wav";
    public static final String CHEAT_SOUND = SOUNDS_PATH + "cheat.wav";
    public static final String START_SOUND = SOUNDS_PATH + "start.wav";
    
    /** Image files */
    public static final String ROCK_IMAGE = IMAGES_PATH + "rock.png";
    public static final String PAPER_IMAGE = IMAGES_PATH + "paper.png";
    public static final String SCISSORS_IMAGE = IMAGES_PATH + "scissors.png";
    public static final String APP_ICON = IMAGES_PATH + "question.png";
    
    // ===== VALIDATION CONSTANTS =====
    
    /** Minimum number of rounds */
    public static final int MIN_ROUNDS = 1;
    
    /** Valid round options */
    public static final Integer[] ROUND_OPTIONS = {1, 3, 5, 7, 9};
    
    // ===== DEBUG AND LOGGING =====
    
    /** Enable debug output */
    public static final boolean DEBUG_MODE = false;
    
    /** Enable sound system debug */
    public static final boolean DEBUG_SOUND = false;
    
    /** Enable AI debug output */
    public static final boolean DEBUG_AI = false;
    
    // ===== UTILITY METHODS =====
    
    /**
     * Get the display text for a game mode
     */
    public static String getGameModeText(GameMode mode) {
        switch (mode) {
            case PVC: return PVC_DESCRIPTION;
            case PVP: return PVP_DESCRIPTION;
            default: return "Unknown Mode";
        }
    }
    
    /**
     * Get the controls text for a game mode
     */
    public static String getControlsText(GameMode mode) {
        switch (mode) {
            case PVC: return PVC_CONTROLS;
            case PVP: return PVP_CONTROLS;
            default: return "";
        }
    }
    
    /**
     * Get the symbol for a move
     */
    public static String getMoveSymbol(Move move) {
        if (move == null) return UNKNOWN_SYMBOL;
        
        switch (move) {
            case ROCK: return ROCK_SYMBOL;
            case PAPER: return PAPER_SYMBOL;
            case SCISSORS: return SCISSORS_SYMBOL;
            default: return UNKNOWN_SYMBOL;
        }
    }
    
    /**
     * Get the color for a game result
     */
    public static Color getResultColor(GameResult result) {
        if (result == null) return SECONDARY_TEXT;
        
        switch (result) {
            case WIN: return WIN_COLOR;
            case LOSE: return LOSE_COLOR;
            case DRAW: return DRAW_COLOR;
            default: return SECONDARY_TEXT;
        }
    }
    
    /**
     * Check if a character is a valid player 1 key
     */
    public static boolean isPlayer1Key(char key) {
        char upperKey = Character.toUpperCase(key);
        return upperKey == PLAYER1_ROCK || upperKey == PLAYER1_PAPER || upperKey == PLAYER1_SCISSORS;
    }
    
    /**
     * Check if a character is a valid player 2 key
     */
    public static boolean isPlayer2Key(char key) {
        char upperKey = Character.toUpperCase(key);
        return upperKey == PLAYER2_ROCK || upperKey == PLAYER2_PAPER || upperKey == PLAYER2_SCISSORS;
    }
    
    /**
     * Get move from player 1 key
     */
    public static Move getMoveFromPlayer1Key(char key) {
        char upperKey = Character.toUpperCase(key);
        switch (upperKey) {
            case PLAYER1_ROCK: return Move.ROCK;
            case PLAYER1_PAPER: return Move.PAPER;
            case PLAYER1_SCISSORS: return Move.SCISSORS;
            default: return null;
        }
    }
    
    /**
     * Get move from player 2 key
     */
    public static Move getMoveFromPlayer2Key(char key) {
        char upperKey = Character.toUpperCase(key);
        switch (upperKey) {
            case PLAYER2_ROCK: return Move.ROCK;
            case PLAYER2_PAPER: return Move.PAPER;
            case PLAYER2_SCISSORS: return Move.SCISSORS;
            default: return null;
        }
    }
    
    /**
     * Validate number of rounds
     */
    public static boolean isValidRounds(int rounds) {
        return rounds >= MIN_ROUNDS && rounds <= MAX_ROUNDS;
    }
    
    /**
     * Get default dimension for components
     */
    public static Dimension getButtonDimension() {
        return new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT);
    }
    
    /**
     * Get small button dimension
     */
    public static Dimension getSmallButtonDimension() {
        return new Dimension(BUTTON_WIDTH, SMALL_BUTTON_HEIGHT);
    }
    
    /**
     * Get combo box dimension
     */
    public static Dimension getComboDimension() {
        return new Dimension(COMBO_WIDTH, SMALL_BUTTON_HEIGHT);
    }
    
    /**
     * Get small combo box dimension
     */
    public static Dimension getSmallComboDimension() {
        return new Dimension(SMALL_COMBO_WIDTH, SMALL_BUTTON_HEIGHT);
    }
}