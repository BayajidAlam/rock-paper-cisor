import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

/**
 * Game Panel - Main game interface
 * Handles the actual gameplay, countdown, input, and results
 */
public class GamePanel extends JPanel implements KeyListener {
    private RockPaperScissorsApp parent;
    private GameLogic gameLogic;
    
    // UI Components
    private JLabel countdownLabel;
    private JLabel roundLabel;
    private JLabel scoreLabel;
    private JLabel player1MoveLabel;
    private JLabel player2MoveLabel;
    private JLabel resultLabel;
    private JLabel instructionLabel;
    private JButton backButton;
    private JButton nextRoundButton;
    private JPanel movesPanel;
    
    // Game state
    private Timer countdownTimer;
    private int countdown;
    private boolean gameActive;
    private boolean roundInProgress;
    private Set<Character> pressedKeys;
    private long gameStartTime; // Track game duration
    
    public GamePanel(RockPaperScissorsApp parent) {
        this.parent = parent;
        this.pressedKeys = new HashSet<>();
        initializeUI();
        setupKeyListener();
    }
    
    /**
     * Initialize the game user interface
     */
    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(Constants.GAME_BACKGROUND);
        setFocusable(true);
        setRequestFocusEnabled(true);
        
        // Make sure this panel can receive focus
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                requestFocusInWindow();
            }
        });
        
        // Top panel with round and score info
        JPanel topPanel = createTopPanel();
        
        // Center panel with game display
        JPanel centerPanel = createCenterPanel();
        
        // Bottom panel with controls
        JPanel bottomPanel = createBottomPanel();
        
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Create top panel with round and score information
     */
    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(
            Constants.STANDARD_PADDING, Constants.STANDARD_PADDING, 
            Constants.SMALL_PADDING, Constants.STANDARD_PADDING
        ));
        
        roundLabel = new JLabel("Round 1 of 3", JLabel.LEFT);
        roundLabel.setFont(Constants.SECTION_FONT);
        roundLabel.setForeground(Constants.PRIMARY_TEXT);
        
        scoreLabel = new JLabel("Score: 0 - 0", JLabel.RIGHT);
        scoreLabel.setFont(Constants.SECTION_FONT);
        scoreLabel.setForeground(Constants.PRIMARY_TEXT);
        
        panel.add(roundLabel, BorderLayout.WEST);
        panel.add(scoreLabel, BorderLayout.EAST);
        
        return panel;
    }
    
    /**
     * Create center panel with main game display
     */
    private JPanel createCenterPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(
            Constants.STANDARD_PADDING, Constants.LARGE_PADDING, 
            Constants.STANDARD_PADDING, Constants.LARGE_PADDING
        ));
        
        // Countdown display
        countdownLabel = new JLabel("3", JLabel.CENTER);
        countdownLabel.setFont(Constants.COUNTDOWN_FONT);
        countdownLabel.setForeground(Constants.COUNTDOWN_COLOR);
        countdownLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Player moves display
        movesPanel = createMovesPanel();
        
        // Result display
        resultLabel = new JLabel("", JLabel.CENTER);
        resultLabel.setFont(Constants.RESULT_FONT);
        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Instructions
        instructionLabel = new JLabel(Constants.GET_READY + " (Click here if keys don't work)", JLabel.CENTER);
        instructionLabel.setFont(Constants.SUBTITLE_FONT);
        instructionLabel.setForeground(Constants.SECONDARY_TEXT);
        instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Make instruction label clickable to get focus
        instructionLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                requestFocusInWindow();
                System.out.println("Focus requested via instruction label click");
            }
        });
        
        panel.add(countdownLabel);
        panel.add(Box.createRigidArea(new Dimension(0, Constants.LARGE_SPACING)));
        panel.add(movesPanel);
        panel.add(Box.createRigidArea(new Dimension(0, Constants.STANDARD_PADDING)));
        panel.add(resultLabel);
        panel.add(Box.createRigidArea(new Dimension(0, Constants.STANDARD_PADDING)));
        panel.add(instructionLabel);
        
        return panel;
    }
    
    /**
     * Create panel to display player moves
     */
    private JPanel createMovesPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3, Constants.STANDARD_PADDING, 0));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(Constants.MOVES_PANEL_WIDTH, Constants.MOVES_PANEL_HEIGHT));
        
        // Player 1 move
        player1MoveLabel = new JLabel(Constants.UNKNOWN_SYMBOL, JLabel.CENTER);
        player1MoveLabel.setFont(Constants.MOVE_FONT);
        player1MoveLabel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Constants.PRIMARY_TEXT),
            "Player 1",
            0, 0, Constants.REGULAR_FONT
        ));
        player1MoveLabel.setBackground(Color.WHITE);
        player1MoveLabel.setOpaque(true);
        
        // VS label
        JLabel vsLabel = new JLabel("VS", JLabel.CENTER);
        vsLabel.setFont(new Font("Arial", Font.BOLD, 24));
        vsLabel.setForeground(Constants.PRIMARY_TEXT);
        
        // Player 2 move
        player2MoveLabel = new JLabel(Constants.UNKNOWN_SYMBOL, JLabel.CENTER);
        player2MoveLabel.setFont(Constants.MOVE_FONT);
        player2MoveLabel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Constants.PRIMARY_TEXT),
            "Player 2/Computer",
            0, 0, Constants.REGULAR_FONT
        ));
        player2MoveLabel.setBackground(Color.WHITE);
        player2MoveLabel.setOpaque(true);
        
        panel.add(player1MoveLabel);
        panel.add(vsLabel);
        panel.add(player2MoveLabel);
        
        return panel;
    }
    
    /**
     * Create bottom panel with control buttons
     */
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(
            Constants.SMALL_PADDING, Constants.STANDARD_PADDING, 
            Constants.STANDARD_PADDING, Constants.STANDARD_PADDING
        ));
        
        backButton = new JButton("Back to Menu");
        backButton.setFont(Constants.REGULAR_FONT);
        backButton.setPreferredSize(Constants.getSmallButtonDimension());
        backButton.addActionListener(e -> {
            stopCurrentGame();
            parent.showMainPage(); // Go back to main page instead of menu
        });
        
        nextRoundButton = new JButton("Next Round");
        nextRoundButton.setFont(Constants.SMALL_BUTTON_FONT);
        nextRoundButton.setBackground(Constants.BUTTON_COLOR);
        nextRoundButton.setForeground(Color.WHITE);
        nextRoundButton.setPreferredSize(Constants.getSmallButtonDimension());
        nextRoundButton.setFocusPainted(false);
        nextRoundButton.setBorderPainted(false);
        nextRoundButton.setVisible(false);
        nextRoundButton.addActionListener(e -> startNextRound());
        
        // Add hover effect to next round button
        nextRoundButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (nextRoundButton.isEnabled()) {
                    nextRoundButton.setBackground(Constants.BUTTON_HOVER);
                }
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                nextRoundButton.setBackground(Constants.BUTTON_COLOR);
            }
        });
        
        panel.add(backButton);
        panel.add(nextRoundButton);
        
        return panel;
    }
    
    /**
     * Set up key listener for game input
     */
    private void setupKeyListener() {
        addKeyListener(this);
        setFocusable(true);
        setRequestFocusEnabled(true);
    }
    
    /**
     * Start a new game with specified mode and rounds
     */
    public void startNewGame(GameMode mode, int rounds) {
        gameLogic = new GameLogic(mode, rounds);
        gameActive = true;
        gameStartTime = System.currentTimeMillis(); // Record start time
        updateGameUI();
        
        // Ensure keyboard focus
        requestFocusInWindow();
        grabFocus();
        
        startNextRound();
    }
    
    /**
     * Start the next round
     */
    private void startNextRound() {
        if (!gameLogic.hasMoreRounds()) {
            endGame();
            return;
        }
        
        gameLogic.startNewRound();
        roundInProgress = true;
        pressedKeys.clear();
        
        // Reset UI
        player1MoveLabel.setText(Constants.UNKNOWN_SYMBOL);
        player2MoveLabel.setText(Constants.UNKNOWN_SYMBOL);
        resetMoveLabels(); // Reset colors
        resultLabel.setText("");
        nextRoundButton.setVisible(false);
        
        updateGameUI();
        startCountdown();
    }
    
    /**
     * Start the 3-second countdown
     */
    private void startCountdown() {
        countdown = Constants.COUNTDOWN_SECONDS;
        countdownLabel.setText(String.valueOf(countdown));
        countdownLabel.setFont(Constants.COUNTDOWN_FONT);
        instructionLabel.setText(Constants.MAKE_MOVE + getControlsText());
        
        // Ensure keyboard focus during countdown - CRITICAL!
        SwingUtilities.invokeLater(() -> {
            requestFocusInWindow();
            grabFocus();
            System.out.println("Focus requested for game panel");
        });
        
        SoundManager.playSound(Constants.SOUND_COUNTDOWN);
        
        countdownTimer = new Timer(Constants.COUNTDOWN_TIMER_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                countdown--;
                if (countdown > 0) {
                    countdownLabel.setText(String.valueOf(countdown));
                    SoundManager.playSound(Constants.SOUND_COUNTDOWN);
                } else {
                    countdownLabel.setText("GO!");
                    countdownLabel.setForeground(Constants.WIN_COLOR);
                    SoundManager.playSound(Constants.SOUND_GO);
                    
                    Timer finalTimer = new Timer(Constants.FINISH_ROUND_DELAY, ev -> {
                        finishRound();
                        ((Timer)ev.getSource()).stop();
                    });
                    finalTimer.setRepeats(false);
                    finalTimer.start();
                    countdownTimer.stop();
                }
            }
        });
        countdownTimer.start();
    }
    
    /**
     * Get control instructions text based on game mode
     */
    private String getControlsText() {
        return Constants.getControlsText(gameLogic.getGameMode());
    }
    
    /**
     * Finish the current round
     */
    private void finishRound() {
        roundInProgress = false;
        countdownLabel.setText("");
        
        // Get computer move if needed
        if (gameLogic.getGameMode() == GameMode.PVC && gameLogic.getPlayer2Move() == null) {
            Move computerMove = gameLogic.getComputerMove();
            gameLogic.setPlayer2Move(computerMove);
        }
        
        // Check for cheating in PvP mode
        if (gameLogic.getGameMode() == GameMode.PVP) {
            boolean player1Cheated = gameLogic.isPlayer1Cheating();
            boolean player2Cheated = gameLogic.isPlayer2Cheating();
            
            if (player1Cheated || player2Cheated) {
                handleCheating(player1Cheated, player2Cheated);
                return;
            }
        }
        
        // Calculate result
        GameResult result = gameLogic.finishRound();
        
        // Display moves and result
        displayRoundResult(result);
        
        // Update UI for next round or game end
        if (gameLogic.hasMoreRounds()) {
            nextRoundButton.setVisible(true);
            instructionLabel.setText(Constants.NEXT_ROUND);
        } else {
            endGame();
        }
        
        updateGameUI();
    }
    
    /**
     * Handle cheating detection
     */
    private void handleCheating(boolean player1Cheated, boolean player2Cheated) {
        SoundManager.playSound(Constants.SOUND_CHEAT);
        
        if (player1Cheated && player2Cheated) {
            resultLabel.setText(Constants.BOTH_CHEATED);
            resultLabel.setForeground(Constants.CHEAT_COLOR);
        } else if (player1Cheated) {
            resultLabel.setText(Constants.PLAYER1_CHEATED);
            resultLabel.setForeground(Constants.CHEAT_COLOR);
            gameLogic.addWinToPlayer2();
        } else {
            resultLabel.setText(Constants.PLAYER2_CHEATED);
            resultLabel.setForeground(Constants.CHEAT_COLOR);
            gameLogic.addWinToPlayer1();
        }
        
        player1MoveLabel.setText(Constants.CHEAT_SYMBOL);
        player2MoveLabel.setText(Constants.CHEAT_SYMBOL);
        instructionLabel.setText(Constants.CHEATING_DETECTED);
        
        if (gameLogic.hasMoreRounds()) {
            nextRoundButton.setVisible(true);
        } else {
            endGame();
        }
        
        updateGameUI();
    }
    
    /**
     * Display the result of the round
     */
    private void displayRoundResult(GameResult result) {
        // Show moves
        Move p1Move = gameLogic.getPlayer1Move();
        Move p2Move = gameLogic.getPlayer2Move();
        
        System.out.println("Displaying result - P1: " + p1Move + ", P2: " + p2Move + ", Result: " + result);
        
        if (p1Move != null) {
            player1MoveLabel.setText(Constants.getMoveSymbol(p1Move));
            System.out.println("Set P1 symbol: " + Constants.getMoveSymbol(p1Move));
        } else {
            player1MoveLabel.setText("❌");
            System.out.println("P1 move was null, showing X");
        }
        
        if (p2Move != null) {
            player2MoveLabel.setText(Constants.getMoveSymbol(p2Move));
            System.out.println("Set P2 symbol: " + Constants.getMoveSymbol(p2Move));
        } else {
            player2MoveLabel.setText("❌");
            System.out.println("P2 move was null, showing X");
        }
        
        // Show result
        if (result != null) {
            resultLabel.setText(result.getName() + "!");
            resultLabel.setForeground(Constants.getResultColor(result));
            
            // Play appropriate sound
            SoundManager.playSound(result.getName().toLowerCase());
            System.out.println("Result displayed: " + result.getName());
        } else {
            resultLabel.setText("No Result");
            System.out.println("Result was null!");
        }
        
        // Update instruction
        String moveText = "";
        if (p1Move != null && p2Move != null) {
            moveText = p1Move.getName() + " vs " + p2Move.getName();
        } else {
            moveText = "Missing moves!";
        }
        instructionLabel.setText(moveText);
        System.out.println("Move text: " + moveText);
    }
    
    /**
     * End the game and show final results
     */
    private void endGame() {
        gameActive = false;
        roundInProgress = false;
        
        // Calculate game duration
        long gameDuration = System.currentTimeMillis() - gameStartTime;
        
        // Save game to history
        saveGameToHistory(gameDuration);
        
        String winner = gameLogic.getGameWinner();
        countdownLabel.setText(Constants.GAME_OVER);
        countdownLabel.setFont(Constants.GAME_OVER_FONT);
        countdownLabel.setForeground(Constants.PRIMARY_TEXT);
        
        resultLabel.setText(winner);
        resultLabel.setFont(Constants.FINAL_RESULT_FONT);
        
        if (winner.contains("Win")) {
            resultLabel.setForeground(Constants.WIN_COLOR);
            SoundManager.playSound(Constants.SOUND_WIN);
        } else if (winner.contains("Tie")) {
            resultLabel.setForeground(Constants.DRAW_COLOR);
            SoundManager.playSound(Constants.SOUND_DRAW);
        } else {
            resultLabel.setForeground(Constants.LOSE_COLOR);
            SoundManager.playSound(Constants.SOUND_LOSE);
        }
        
        instructionLabel.setText(Constants.GAME_FINISHED + gameLogic.getScoreText());
        nextRoundButton.setVisible(false);
        
        // Show game statistics if available
        try {
            GameStatistics stats = gameLogic.getStatistics();
            if (Constants.DEBUG_MODE) {
                System.out.println(stats.getSummary());
            }
        } catch (Exception e) {
            if (Constants.DEBUG_MODE) {
                System.out.println("Could not generate statistics: " + e.getMessage());
            }
        }
    }
    
    /**
     * Save completed game to history
     */
    private void saveGameToHistory(long durationMs) {
        try {
            GameMode mode = gameLogic.getGameMode();
            int rounds = gameLogic.getTotalRounds();
            int player1Score = gameLogic.getPlayer1Score();
            int player2Score = gameLogic.getPlayer2Score();
            String winner = gameLogic.getGameWinner();
            
            GameHistory.addGameRecord(mode, rounds, player1Score, player2Score, winner, durationMs);
        } catch (Exception e) {
            System.err.println("Error saving game to history: " + e.getMessage());
        }
    }
    
    /**
     * Stop the current game and clean up
     */
    private void stopCurrentGame() {
        gameActive = false;
        roundInProgress = false;
        
        if (countdownTimer != null && countdownTimer.isRunning()) {
            countdownTimer.stop();
        }
        
        // Reset UI to initial state
        countdownLabel.setText("");
        resultLabel.setText("");
        instructionLabel.setText(Constants.GET_READY);
        player1MoveLabel.setText(Constants.UNKNOWN_SYMBOL);
        player2MoveLabel.setText(Constants.UNKNOWN_SYMBOL);
        nextRoundButton.setVisible(false);
    }
    
    /**
     * Update all UI elements
     */
    private void updateGameUI() {
        if (gameLogic != null) {
            roundLabel.setText("Round " + gameLogic.getCurrentRound() + " of " + gameLogic.getTotalRounds());
            scoreLabel.setText("Score: " + gameLogic.getScoreText());
        }
        
        // Ensure the panel is focused for key input
        if (gameActive && roundInProgress) {
            requestFocusInWindow();
        }
    }
    
    // ===== KeyListener Implementation =====
    
    @Override
    public void keyPressed(KeyEvent e) {
        // Debug: Print key pressed
        System.out.println("Key pressed: " + e.getKeyChar() + " (Code: " + e.getKeyCode() + ")");
        System.out.println("Game state - Active: " + gameActive + ", Round in progress: " + roundInProgress + ", Countdown: " + countdown);
        
        if (!gameActive || !roundInProgress) {
            System.out.println("Game not ready for input - game not active or round not in progress");
            return;
        }
        
        // Allow input during countdown, not just after
        if (countdown > 0) {
            System.out.println("Input accepted during countdown");
        }
        
        char key = Character.toUpperCase(e.getKeyChar());
        System.out.println("Processing key: " + key);
        
        // Check if key was already pressed (for anti-cheating)
        if (pressedKeys.contains(key)) {
            System.out.println("Key already pressed: " + key);
            if (gameLogic.getGameMode() == GameMode.PVP) {
                // Mark as cheating
                if (Constants.isPlayer1Key(key)) {
                    gameLogic.setPlayer1Cheating(true);
                } else if (Constants.isPlayer2Key(key)) {
                    gameLogic.setPlayer2Cheating(true);
                }
            }
            return;
        }
        
        pressedKeys.add(key);
        System.out.println("Added key to pressed keys: " + key);
        
        // Process the key input
        Move move = null;
        
        if (gameLogic.getGameMode() == GameMode.PVC) {
            System.out.println("PvC mode - checking player 1 keys");
            // Player vs Computer - only check player 1 keys
            if (Constants.isPlayer1Key(key)) {
                move = Constants.getMoveFromPlayer1Key(key);
                System.out.println("Player 1 move: " + move);
                if (move != null) {
                    gameLogic.setPlayer1Move(move);
                    SoundManager.playSound(Constants.SOUND_SELECT);
                    
                    // Visual feedback
                    player1MoveLabel.setText("✓");
                    player1MoveLabel.setForeground(Constants.WIN_COLOR);
                    System.out.println("Set player 1 move: " + move);
                }
            } else {
                System.out.println("Key not valid for player 1: " + key);
            }
        } else {
            System.out.println("PvP mode - checking both player keys");
            // Player vs Player - check both player keys
            if (Constants.isPlayer1Key(key)) {
                move = Constants.getMoveFromPlayer1Key(key);
                System.out.println("Player 1 move: " + move);
                if (move != null) {
                    gameLogic.setPlayer1Move(move);
                    SoundManager.playSound(Constants.SOUND_SELECT);
                    
                    // Visual feedback
                    player1MoveLabel.setText("✓");
                    player1MoveLabel.setForeground(Constants.WIN_COLOR);
                }
            } else if (Constants.isPlayer2Key(key)) {
                move = Constants.getMoveFromPlayer2Key(key);
                System.out.println("Player 2 move: " + move);
                if (move != null) {
                    gameLogic.setPlayer2Move(move);
                    SoundManager.playSound(Constants.SOUND_SELECT);
                    
                    // Visual feedback
                    player2MoveLabel.setText("✓");
                    player2MoveLabel.setForeground(Constants.WIN_COLOR);
                }
            } else {
                System.out.println("Key not valid for any player: " + key);
            }
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        // Not used, but required by KeyListener interface
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        // Not used, but required by KeyListener interface
    }
    
    // ===== Helper Methods =====
    
    /**
     * Reset visual feedback colors
     */
    private void resetMoveLabels() {
        player1MoveLabel.setForeground(Color.BLACK);
        player2MoveLabel.setForeground(Color.BLACK);
    }
    
    /**
     * Get current game state for debugging
     */
    public String getGameState() {
        if (gameLogic == null) {
            return "No game active";
        }
        
        return String.format("Round %d/%d, Score: %s, Active: %s, In Progress: %s",
            gameLogic.getCurrentRound(),
            gameLogic.getTotalRounds(),
            gameLogic.getScoreText(),
            gameActive,
            roundInProgress
        );
    }
}