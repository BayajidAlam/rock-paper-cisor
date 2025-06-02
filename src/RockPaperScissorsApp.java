import javax.swing.*;
import java.awt.*;

/**
 * Rock Paper Scissors Game Application
 * Main class with enhanced UI, themes, and game history
 */
public class RockPaperScissorsApp extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private MainPagePanel mainPagePanel;
    private MenuPanel menuPanel;
    private GamePanel gamePanel;
    private HistoryPanel historyPanel;
    private ThemeManager themeManager;
    
    public RockPaperScissorsApp() {
        themeManager = new ThemeManager();
        initializeWindow();
        createMenuBar();
        initializePanels();
        showMainPage();
    }
    
    /**
     * Set up the main window properties
     */
    private void initializeWindow() {
        setTitle("Rock Paper Scissors Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        setResizable(true);
        setMinimumSize(new Dimension(800, 600));
    }
    
    /**
     * Create the menu bar
     */
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // Game Menu
        JMenu gameMenu = new JMenu("Game");
        JMenuItem newGame = new JMenuItem("New Game");
        JMenuItem viewHistory = new JMenuItem("View History");
        JMenuItem clearHistory = new JMenuItem("Clear History");
        JMenuItem exit = new JMenuItem("Exit");
        
        newGame.addActionListener(e -> showGameMenu());
        viewHistory.addActionListener(e -> showHistory());
        clearHistory.addActionListener(e -> {
            GameHistory.clearHistory();
            JOptionPane.showMessageDialog(this, "Game history cleared!");
        });
        exit.addActionListener(e -> System.exit(0));
        
        gameMenu.add(newGame);
        gameMenu.add(viewHistory);
        gameMenu.addSeparator();
        gameMenu.add(clearHistory);
        gameMenu.addSeparator();
        gameMenu.add(exit);
        
        // Theme Menu
        JMenu themeMenu = new JMenu("Theme");
        JMenuItem lightTheme = new JMenuItem("Light Theme");
        JMenuItem darkTheme = new JMenuItem("Dark Theme");
        
        lightTheme.addActionListener(e -> {
            themeManager.setLightTheme();
            updateTheme();
        });
        darkTheme.addActionListener(e -> {
            themeManager.setDarkTheme();
            updateTheme();
        });
        
        themeMenu.add(lightTheme);
        themeMenu.add(darkTheme);
        
        // Help Menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem controls = new JMenuItem("Controls");
        JMenuItem about = new JMenuItem("About");
        
        controls.addActionListener(e -> showControls());
        about.addActionListener(e -> showAbout());
        
        helpMenu.add(controls);
        helpMenu.add(about);
        
        menuBar.add(gameMenu);
        menuBar.add(themeMenu);
        menuBar.add(helpMenu);
        
        setJMenuBar(menuBar);
    }
    
    /**
     * Initialize all panels and set up card layout
     */
    private void initializePanels() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        // Create panels
        mainPagePanel = new MainPagePanel(this);
        menuPanel = new MenuPanel(this);
        gamePanel = new GamePanel(this);
        historyPanel = new HistoryPanel(this);
        
        // Add panels to card layout
        mainPanel.add(mainPagePanel, "MAIN");
        mainPanel.add(menuPanel, "MENU");
        mainPanel.add(gamePanel, "GAME");
        mainPanel.add(historyPanel, "HISTORY");
        
        add(mainPanel);
    }
    
    /**
     * Show the main page
     */
    public void showMainPage() {
        cardLayout.show(mainPanel, "MAIN");
    }
    
    /**
     * Show the game menu (second page)
     */
    public void showGameMenu() {
        cardLayout.show(mainPanel, "MENU");
    }
    
    /**
     * Show the game screen
     */
    public void showGame(GameMode mode, int rounds) {
        gamePanel.startNewGame(mode, rounds);
        cardLayout.show(mainPanel, "GAME");
    }
    
    /**
     * Show game history
     */
    public void showHistory() {
        historyPanel.refreshHistory();
        cardLayout.show(mainPanel, "HISTORY");
    }
    
    /**
     * Update theme for all components
     */
    private void updateTheme() {
        themeManager.applyTheme(this);
        themeManager.applyTheme(mainPagePanel);
        themeManager.applyTheme(menuPanel);
        themeManager.applyTheme(gamePanel);
        themeManager.applyTheme(historyPanel);
        repaint();
    }
    
    /**
     * Get current theme manager
     */
    public ThemeManager getThemeManager() {
        return themeManager;
    }
    
    /**
     * Show controls dialog
     */
    private void showControls() {
        String controls = "Game Controls:\n\n" +
                "Player vs Computer:\n" +
                "• A = Rock\n" +
                "• S = Paper\n" +
                "• D = Scissors\n\n" +
                "Player vs Player:\n" +
                "• Player 1: A (Rock), S (Paper), D (Scissors)\n" +
                "• Player 2: J (Rock), K (Paper), L (Scissors)\n\n" +
                "Rules:\n" +
                "• Press your key once during the 3-second countdown\n" +
                "• Multiple key presses in PvP mode = cheating!\n" +
                "• Rock beats Scissors\n" +
                "• Paper beats Rock\n" +
                "• Scissors beats Paper";
        
        JOptionPane.showMessageDialog(this, controls, "Game Controls", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Show about dialog
     */
    private void showAbout() {
        String about = "Rock Paper Scissors Game\n\n" +
                "Version 2.0\n" +
                "Features:\n" +
                "• Player vs Player and Player vs Computer modes\n" +
                "• Smart AI that learns from your moves\n" +
                "• Anti-cheating system\n" +
                "• Game history with CSV export\n" +
                "• Light and Dark themes\n" +
                "• Sound effects\n" +
                "• Multi-round games\n\n" +
                "Created for Java Programming Course\n" +
                "Final Project 2024-2025";
        
        JOptionPane.showMessageDialog(this, about, "About", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Main method - entry point of the application
     */
    public static void main(String[] args) {
        // Initialize game history
        GameHistory.initialize();
        
        // Create and show the application
        SwingUtilities.invokeLater(() -> {
            new RockPaperScissorsApp().setVisible(true);
        });
    }
}

/**
 * Enumeration for different game modes
 */
enum GameMode {
    PVP("Player vs Player"),
    PVC("Player vs Computer");
    
    private final String displayName;
    
    GameMode(String displayName) {
        this.displayName = displayName;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}

/**
 * Enumeration for game moves
 */
enum Move {
    ROCK("Rock", "✊", 'R'),
    PAPER("Paper", "✋", 'P'), 
    SCISSORS("Scissors", "✌️", 'S');
    
    private final String name;
    private final String symbol;
    private final char shortcut;
    
    Move(String name, String symbol, char shortcut) {
        this.name = name;
        this.symbol = symbol;
        this.shortcut = shortcut;
    }
    
    public String getName() { return name; }
    public String getSymbol() { return symbol; }
    public char getShortcut() { return shortcut; }
    
    /**
     * Compare this move against another move
     * @param other The opponent's move
     * @return The result from this move's perspective
     */
    public GameResult compare(Move other) {
        if (this == other) {
            return GameResult.DRAW;
        }
        
        switch (this) {
            case ROCK:
                return (other == SCISSORS) ? GameResult.WIN : GameResult.LOSE;
            case PAPER:
                return (other == ROCK) ? GameResult.WIN : GameResult.LOSE;
            case SCISSORS:
                return (other == PAPER) ? GameResult.WIN : GameResult.LOSE;
            default:
                return GameResult.DRAW;
        }
    }
    
    /**
     * Get a random move (for computer player)
     */
    public static Move getRandomMove() {
        Move[] moves = values();
        return moves[(int)(Math.random() * moves.length)];
    }
    
    /**
     * Get move from character (for keyboard input)
     */
    public static Move fromChar(char c) {
        for (Move move : values()) {
            if (Character.toUpperCase(c) == move.shortcut) {
                return move;
            }
        }
        return null;
    }
}

/**
 * Enumeration for game results
 */
enum GameResult {
    WIN("Win", Color.GREEN),
    LOSE("Lose", Color.RED),
    DRAW("Draw", Color.ORANGE);
    
    private final String name;
    private final Color color;
    
    GameResult(String name, Color color) {
        this.name = name;
        this.color = color;
    }
    
    public String getName() { return name; }
    public Color getColor() { return color; }
    
    @Override
    public String toString() {
        return name;
    }
}