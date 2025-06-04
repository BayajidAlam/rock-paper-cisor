import javax.swing.*;
import java.awt.*;

/**
 * Main Page Panel - Landing page with all main options
 * New main page with game mode, rounds, history, and theme options
 */
public class MainPagePanel extends JPanel {
    private RockPaperScissorsApp parent;
    private JComboBox<GameMode> gameModeCombo;
    private JComboBox<Integer> roundsCombo;
    private JButton startGameButton;
    private JButton viewHistoryButton;
    private JToggleButton themeToggle;
    private JLabel statsLabel;
    private Timer discoTimer; // For title animation
    
    public MainPagePanel(RockPaperScissorsApp parent) {
        this.parent = parent;
        initializeUI();
        updateStats();
        startTitleAnimation();
    }
    
    /**
     * Initialize the main page UI
     */
    private void initializeUI() {
        setLayout(new BorderLayout());
        
        // Create main content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));
        
        // Title section
        JPanel titlePanel = createTitlePanel();
        
        // Quick stats section
        JPanel statsPanel = createStatsPanel();
        
        // Game setup section
        JPanel gameSetupPanel = createGameSetupPanel();
        
        // Action buttons section
        JPanel actionPanel = createActionPanel();
        
        // Theme controls section
        JPanel themePanel = createThemePanel();
        
        // Add all sections
        contentPanel.add(titlePanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(statsPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        contentPanel.add(gameSetupPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        contentPanel.add(actionPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(themePanel);
        contentPanel.add(Box.createVerticalGlue());
        
        add(contentPanel, BorderLayout.CENTER);
    }
    
    /**
     * Create title panel (without animations)
     */
    private JPanel createTitlePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("🎮 ROCK PAPER SCISSORS");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("✨ Ultimate Gaming Experience ✨");
        subtitleLabel.setFont(new Font("Arial", Font.ITALIC, 18));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(subtitleLabel);
        
        return panel;
    }
    
    /**
     * Start title animation (disabled)
     */
    private void startTitleAnimation() {
        // Animation disabled for better UX
        // EnhancedSoundManager.playSound("text_animation");
    }
    
    /**
     * Create stats panel
     */
    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createTitledBorder("Quick Stats"));
        
        statsLabel = new JLabel("Games Played: 0 | Total Wins: 0");
        statsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        panel.add(statsLabel);
        
        return panel;
    }
    
    /**
     * Create game setup panel with improved styling
     */
    private JPanel createGameSetupPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(),
            "Game Setup",
            0, 0,
            new Font("Arial", Font.BOLD, 18)
        ));
        
        // Game mode selection with improved styling
        JPanel modePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        modePanel.setOpaque(false);
        
        JLabel modeLabel = new JLabel("Game Mode:");
        modeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        gameModeCombo = new JComboBox<>(GameMode.values());
        gameModeCombo.setFont(new Font("Arial", Font.PLAIN, 16));
        gameModeCombo.setPreferredSize(new Dimension(220, 40)); // Increased size
        gameModeCombo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        // Custom renderer for better text display
        gameModeCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setFont(new Font("Arial", Font.PLAIN, 16));
                setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
                return c;
            }
        });
        
        modePanel.add(modeLabel);
        modePanel.add(Box.createRigidArea(new Dimension(20, 0)));
        modePanel.add(gameModeCombo);
        
        // Rounds selection with improved styling
        JPanel roundsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        roundsPanel.setOpaque(false);
        
        JLabel roundsLabel = new JLabel("Number of Rounds:");
        roundsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        Integer[] roundOptions = {1, 3, 5, 7, 9};
        roundsCombo = new JComboBox<>(roundOptions);
        roundsCombo.setSelectedIndex(1); // Default to 3 rounds
        roundsCombo.setFont(new Font("Arial", Font.PLAIN, 16));
        roundsCombo.setPreferredSize(new Dimension(120, 40)); // Increased size
        roundsCombo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        // Custom renderer for rounds combo
        roundsCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setFont(new Font("Arial", Font.PLAIN, 16));
                setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
                setHorizontalAlignment(JLabel.CENTER);
                return c;
            }
        });
        
        roundsPanel.add(roundsLabel);
        roundsPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        roundsPanel.add(roundsCombo);
        
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(modePanel);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(roundsPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        return panel;
    }
    
    /**
     * Create action buttons panel
     */
    private JPanel createActionPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        
        // Start game button
        startGameButton = new JButton("🚀 START NEW GAME");
        startGameButton.setFont(new Font("Arial", Font.BOLD, 20));
        startGameButton.setPreferredSize(new Dimension(300, 50));
        startGameButton.setMaximumSize(new Dimension(300, 50));
        startGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startGameButton.setBackground(new Color(34, 139, 34));
        startGameButton.setForeground(Color.WHITE);
        startGameButton.setFocusPainted(false);
        startGameButton.setBorderPainted(false);
        
        // View history button
        viewHistoryButton = new JButton("📊 VIEW GAME HISTORY");
        viewHistoryButton.setFont(new Font("Arial", Font.BOLD, 16));
        viewHistoryButton.setPreferredSize(new Dimension(250, 40));
        viewHistoryButton.setMaximumSize(new Dimension(250, 40));
        viewHistoryButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        viewHistoryButton.setBackground(new Color(70, 130, 180));
        viewHistoryButton.setForeground(Color.WHITE);
        viewHistoryButton.setFocusPainted(false);
        viewHistoryButton.setBorderPainted(false);
        
        // Add hover effects
        addHoverEffect(startGameButton, new Color(34, 139, 34), new Color(50, 205, 50));
        addHoverEffect(viewHistoryButton, new Color(70, 130, 180), new Color(100, 149, 237));
        
        // Add action listeners (removed sound effects)
        startGameButton.addActionListener(e -> startGame());
        viewHistoryButton.addActionListener(e -> parent.showHistory());
        
        panel.add(startGameButton);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(viewHistoryButton);
        
        return panel;
    }
    
    /**
     * Create theme panel
     */
    private JPanel createThemePanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createTitledBorder("Appearance"));
        
        JLabel themeLabel = new JLabel("Theme:");
        themeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        themeToggle = new JToggleButton("🌙 Dark Mode");
        themeToggle.setFont(new Font("Arial", Font.PLAIN, 12));
        themeToggle.setPreferredSize(new Dimension(120, 30));
        themeToggle.setFocusPainted(false);
        
        themeToggle.addActionListener(e -> toggleTheme());
        
        panel.add(themeLabel);
        panel.add(Box.createRigidArea(new Dimension(10, 0)));
        panel.add(themeToggle);
        
        return panel;
    }
    
    /**
     * Add hover effect to button
     */
    private void addHoverEffect(JButton button, Color normalColor, Color hoverColor) {
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (button.isEnabled()) {
                    button.setBackground(hoverColor);
                    // Removed hover sound
                }
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(normalColor);
            }
            
            public void mousePressed(java.awt.event.MouseEvent evt) {
                // Removed click sound
            }
        });
    }
    
    /**
     * Start the game with selected options
     */
    private void startGame() {
        GameMode selectedMode = (GameMode) gameModeCombo.getSelectedItem();
        Integer selectedRounds = (Integer) roundsCombo.getSelectedItem();
        
        // Removed sound effect
        // Start the game
        parent.showGame(selectedMode, selectedRounds);
    }
    
    /**
     * Start title animation
     */
    
    /**
     * Stop animations when leaving panel
     */
    public void stopAnimations() {
        if (discoTimer != null && discoTimer.isRunning()) {
            discoTimer.stop();
        }
    }
    
    /**
     * Toggle between light and dark theme
     */
    private void toggleTheme() {
        EnhancedThemeManager themeManager = parent.getThemeManager();
        
        if (themeToggle.isSelected()) {
            themeManager.setDarkTheme();
            themeToggle.setText("☀️ Light Mode");
        } else {
            themeManager.setLightTheme();
            themeToggle.setText("🌙 Dark Mode");
        }
        
        // Apply theme to entire application
        themeManager.applyTheme(parent);
        themeManager.applyTheme(this);
        parent.repaint();
    }
    
    /**
     * Update stats display
     */
    public void updateStats() {
        int totalGames = GameHistory.getTotalGames();
        int totalWins = GameHistory.getTotalWins();
        
        statsLabel.setText(String.format("Games Played: %d | Total Wins: %d", totalGames, totalWins));
    }
    
    /**
     * Refresh the panel (called when returning to main page)
     */
    public void refresh() {
        updateStats();
    }
}