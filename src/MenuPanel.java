import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Menu Panel - Main menu for game mode selection
 * Allows players to choose between PvP/PvC and number of rounds
 */
public class MenuPanel extends JPanel {
    private RockPaperScissorsApp parent;
    private JComboBox<GameMode> gameModeCombo;
    private JComboBox<Integer> roundsCombo;
    
    public MenuPanel(RockPaperScissorsApp parent) {
        this.parent = parent;
        initializeUI();
    }
    
    /**
     * Set up the menu user interface
     */
    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 248, 255)); // Light blue background
        
        // Create main content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        // Title
        JLabel titleLabel = new JLabel("ROCK PAPER SCISSORS");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 42));
        titleLabel.setForeground(new Color(25, 25, 112)); // Dark blue
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Subtitle
        JLabel subtitleLabel = new JLabel("Choose your game mode and start playing!");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(70, 70, 70));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Game mode selection
        JPanel gameModePanel = createGameModePanel();
        
        // Rounds selection
        JPanel roundsPanel = createRoundsPanel();
        
        // Start button
        JButton startButton = createStartButton();
        
        // Back button
        JButton backButton = createBackButton();
        
        // Instructions
        JPanel instructionsPanel = createInstructionsPanel();
        
        // Add components with spacing
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(subtitleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        contentPanel.add(gameModePanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(roundsPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        contentPanel.add(startButton);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        contentPanel.add(backButton);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        contentPanel.add(instructionsPanel);
        
        add(contentPanel, BorderLayout.CENTER);
    }
    
    /**
     * Create game mode selection panel
     */
    private JPanel createGameModePanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setOpaque(false);
        
        JLabel label = new JLabel("Game Mode:");
        label.setFont(new Font("Arial", Font.BOLD, 18));
        
        gameModeCombo = new JComboBox<>(GameMode.values());
        gameModeCombo.setFont(new Font("Arial", Font.PLAIN, 16));
        gameModeCombo.setPreferredSize(new Dimension(200, 30));
        
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(20, 0)));
        panel.add(gameModeCombo);
        
        return panel;
    }
    
    /**
     * Create rounds selection panel
     */
    private JPanel createRoundsPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setOpaque(false);
        
        JLabel label = new JLabel("Number of Rounds:");
        label.setFont(new Font("Arial", Font.BOLD, 18));
        
        Integer[] roundOptions = {1, 3, 5, 7};
        roundsCombo = new JComboBox<>(roundOptions);
        roundsCombo.setSelectedIndex(1); // Default to 3 rounds
        roundsCombo.setFont(new Font("Arial", Font.PLAIN, 16));
        roundsCombo.setPreferredSize(new Dimension(100, 30));
        
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(20, 0)));
        panel.add(roundsCombo);
        
        return panel;
    }
    
    /**
     * Create start game button
     */
    private JButton createStartButton() {
        JButton startButton = new JButton("START GAME");
        startButton.setFont(new Font("Arial", Font.BOLD, 20));
        startButton.setPreferredSize(new Dimension(200, 50));
        startButton.setBackground(new Color(34, 139, 34)); // Forest green
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false);
        startButton.setBorderPainted(false);
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Add hover effect
        startButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                startButton.setBackground(new Color(50, 205, 50)); // Lime green
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                startButton.setBackground(new Color(34, 139, 34)); // Forest green
            }
        });
        
        // Add click action
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });
        
        return startButton;
    }
    
    /**
     * Create back to main button
     */
    private JButton createBackButton() {
        JButton backButton = new JButton("🏠 Back to Main");
        backButton.setFont(new Font("Arial", Font.PLAIN, 14));
        backButton.setPreferredSize(new Dimension(150, 35));
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        backButton.addActionListener(e -> parent.showMainPage());
        
        return backButton;
    }
    
    /**
     * Create instructions panel
     */
    private JPanel createInstructionsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            "Controls",
            0, 0,
            new Font("Arial", Font.BOLD, 16)
        ));
        
        String[] instructions = {
            "Player vs Computer:",
            "• Use A (Rock), S (Paper), D (Scissors)",
            "",
            "Player vs Player:",
            "• Player 1: A (Rock), S (Paper), D (Scissors)",
            "• Player 2: J (Rock), K (Paper), L (Scissors)",
            "",
            "• Press your key once during the 3-second countdown",
            "• Multiple key presses in PvP mode = cheating!"
        };
        
        for (String instruction : instructions) {
            JLabel label = new JLabel(instruction);
            if (instruction.isEmpty()) {
                label.setText(" ");
            }
            label.setFont(new Font("Arial", Font.PLAIN, 12));
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(label);
        }
        
        return panel;
    }
    
    /**
     * Start the game with selected options
     */
    private void startGame() {
        GameMode selectedMode = (GameMode) gameModeCombo.getSelectedItem();
        Integer selectedRounds = (Integer) roundsCombo.getSelectedItem();
        
        // Play sound effect if available
        SoundManager.playSound("start");
        
        // Start the game
        parent.showGame(selectedMode, selectedRounds);
    }
}