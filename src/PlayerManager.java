import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * Player Manager - Handles player names, profiles, and leaderboard
 * Manages player data and statistics for the leaderboard system
 */
public class PlayerManager {
    private static final String PLAYERS_FILE = "players.dat";
    private static Map<String, PlayerProfile> players = new HashMap<>();
    private static String currentPlayer1Name = "Player 1";
    private static String currentPlayer2Name = "Player 2";
    
    /**
     * Initialize player management system
     */
    public static void initialize() {
        loadPlayersFromFile();
    }
    
    /**
     * Show player name input dialog
     */
    public static boolean showPlayerNameDialog(Component parent, GameMode gameMode) {
        System.out.println("Showing player name dialog for mode: " + gameMode);
        
        JDialog dialog = new JDialog((Window) SwingUtilities.getWindowAncestor(parent), "Enter Player Names", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(parent);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        mainPanel.setBackground(new Color(248, 250, 252));
        
        // Title
        JLabel titleLabel = new JLabel("🎮 Player Setup", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(30, 41, 59));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        // Input panel with better spacing
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(new Color(248, 250, 252));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Player 1 input with larger text field
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel p1Label = new JLabel("Your Name:");
        p1Label.setFont(new Font("Arial", Font.BOLD, 16));
        p1Label.setForeground(new Color(30, 41, 59));
        inputPanel.add(p1Label, gbc);
        
        JTextField player1Field = new JTextField(currentPlayer1Name, 20); // Increased width
        player1Field.setFont(new Font("Arial", Font.PLAIN, 16));
        player1Field.setPreferredSize(new Dimension(250, 35)); // Set explicit size
        player1Field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(203, 213, 225), 2),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(player1Field, gbc);
        
        // Player 2 input (only for PvP) - declare as final
        final JTextField player2Field;
        if (gameMode == GameMode.PVP) {
            gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
            JLabel p2Label = new JLabel("Opponent Name:");
            p2Label.setFont(new Font("Arial", Font.BOLD, 16));
            p2Label.setForeground(new Color(30, 41, 59));
            inputPanel.add(p2Label, gbc);
            
            player2Field = new JTextField(currentPlayer2Name, 20); // Increased width
            player2Field.setFont(new Font("Arial", Font.PLAIN, 16));
            player2Field.setPreferredSize(new Dimension(250, 35)); // Set explicit size
            player2Field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225), 2),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
            ));
            gbc.gridx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            inputPanel.add(player2Field, gbc);
        } else {
            player2Field = null; // Initialize to null for PvC mode
        }
        
        // Recent players section with better styling
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(25, 15, 10, 15);
        JLabel recentLabel = new JLabel("Recent Players:");
        recentLabel.setFont(new Font("Arial", Font.BOLD, 14));
        recentLabel.setForeground(new Color(30, 41, 59));
        inputPanel.add(recentLabel, gbc);
        
        String[] recentPlayers = getRecentPlayerNames();
        JComboBox<String> recentCombo = new JComboBox<>(recentPlayers);
        recentCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        recentCombo.setPreferredSize(new Dimension(250, 30));
        gbc.gridy = 3;
        gbc.insets = new Insets(5, 15, 10, 15);
        inputPanel.add(recentCombo, gbc);
        
        // Use recent player button
        JButton useRecentButton = new JButton("Use Selected");
        useRecentButton.setFont(new Font("Arial", Font.PLAIN, 12));
        useRecentButton.setBackground(new Color(59, 130, 246));
        useRecentButton.setForeground(Color.WHITE);
        useRecentButton.setFocusPainted(false);
        useRecentButton.setBorderPainted(false);
        useRecentButton.setPreferredSize(new Dimension(120, 30));
        useRecentButton.addActionListener(e -> {
            String selected = (String) recentCombo.getSelectedItem();
            if (selected != null && !selected.equals("No recent players")) {
                player1Field.setText(selected);
            }
        });
        gbc.gridy = 4;
        gbc.insets = new Insets(5, 15, 15, 15);
        inputPanel.add(useRecentButton, gbc);
        
        // Button panel with better styling
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(248, 250, 252));
        
        final boolean[] confirmed = {false};
        
        JButton okButton = new JButton("✓ Start Game");
        okButton.setBackground(new Color(34, 197, 94));
        okButton.setForeground(Color.WHITE);
        okButton.setFont(new Font("Arial", Font.BOLD, 16));
        okButton.setPreferredSize(new Dimension(150, 40));
        okButton.setFocusPainted(false);
        okButton.setBorderPainted(false);
        
        okButton.addActionListener(e -> {
            System.out.println("OK button clicked");
            String p1Name = player1Field.getText().trim();
            if (p1Name.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please enter your name!", "Name Required", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            currentPlayer1Name = p1Name;
            System.out.println("Player 1 name set to: " + currentPlayer1Name);
            
            if (gameMode == GameMode.PVP && player2Field != null) {
                String p2Name = player2Field.getText().trim();
                if (p2Name.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Please enter opponent's name!", "Name Required", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                currentPlayer2Name = p2Name;
                System.out.println("Player 2 name set to: " + currentPlayer2Name);
            } else {
                currentPlayer2Name = "Computer";
                System.out.println("Player 2 name set to: Computer");
            }
            
            // Create or update player profiles
            getOrCreatePlayer(currentPlayer1Name);
            if (gameMode == GameMode.PVP) {
                getOrCreatePlayer(currentPlayer2Name);
            }
            
            confirmed[0] = true;
            System.out.println("Dialog confirmed, closing...");
            dialog.dispose();
        });
        
        JButton cancelButton = new JButton("✗ Cancel");
        cancelButton.setFont(new Font("Arial", Font.PLAIN, 14));
        cancelButton.setPreferredSize(new Dimension(120, 40));
        cancelButton.setBackground(new Color(107, 114, 128));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        cancelButton.setBorderPainted(false);
        cancelButton.addActionListener(e -> {
            System.out.println("Cancel button clicked");
            dialog.dispose();
        });
        
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(inputPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.add(mainPanel);
        
        // Focus on first text field
        SwingUtilities.invokeLater(() -> player1Field.requestFocusInWindow());
        
        System.out.println("About to show dialog...");
        dialog.setVisible(true);
        
        System.out.println("Dialog closed, confirmed: " + confirmed[0]);
        return confirmed[0];
    }
    
    /**
     * Get or create player profile
     */
    private static PlayerProfile getOrCreatePlayer(String name) {
        PlayerProfile player = players.get(name);
        if (player == null) {
            player = new PlayerProfile(name);
            players.put(name, player);
            savePlayersToFile();
        }
        return player;
    }
    
    /**
     * Update player statistics after game
     */
    public static void updatePlayerStats(String playerName, GameMode gameMode, boolean won, int roundsPlayed) {
        PlayerProfile player = getOrCreatePlayer(playerName);
        
        player.gamesPlayed++;
        player.roundsPlayed += roundsPlayed;
        
        if (won) {
            player.gamesWon++;
        }
        
        if (gameMode == GameMode.PVC) {
            player.pvcGames++;
            if (won) player.pvcWins++;
        } else {
            player.pvpGames++;
            if (won) player.pvpWins++;
        }
        
        player.lastPlayed = new Date();
        savePlayersToFile();
    }
    
    /**
     * Get current player names
     */
    public static String getPlayer1Name() {
        return currentPlayer1Name;
    }
    
    public static String getPlayer2Name() {
        return currentPlayer2Name;
    }
    
    /**
     * Get leaderboard data
     */
    public static List<PlayerProfile> getLeaderboard() {
        List<PlayerProfile> leaderboard = new ArrayList<>(players.values());
        
        // Sort by win rate, then by games won
        leaderboard.sort((p1, p2) -> {
            double winRate1 = p1.getWinRate();
            double winRate2 = p2.getWinRate();
            
            if (Math.abs(winRate1 - winRate2) < 0.01) {
                return Integer.compare(p2.gamesWon, p1.gamesWon);
            }
            
            return Double.compare(winRate2, winRate1);
        });
        
        return leaderboard;
    }
    
    /**
     * Get recent player names for dropdown
     */
    private static String[] getRecentPlayerNames() {
        List<PlayerProfile> recentPlayers = new ArrayList<>(players.values());
        recentPlayers.sort((p1, p2) -> p2.lastPlayed.compareTo(p1.lastPlayed));
        
        if (recentPlayers.isEmpty()) {
            return new String[]{"No recent players"};
        }
        
        String[] names = new String[Math.min(recentPlayers.size(), 10)];
        for (int i = 0; i < names.length; i++) {
            names[i] = recentPlayers.get(i).name;
        }
        
        return names;
    }
    
    /**
     * Show leaderboard dialog
     */
    public static void showLeaderboard(Component parent) {
        JDialog dialog = new JDialog((Window) SwingUtilities.getWindowAncestor(parent), "🏆 Leaderboard", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(600, 500);
        dialog.setLocationRelativeTo(parent);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("🏆 LEADERBOARD", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        
        // Create table
        String[] columnNames = {"Rank", "Player", "Games", "Wins", "Win Rate", "PvC W/L", "PvP W/L", "Last Played"};
        
        List<PlayerProfile> leaderboard = getLeaderboard();
        Object[][] data = new Object[leaderboard.size()][];
        
        for (int i = 0; i < leaderboard.size(); i++) {
            PlayerProfile player = leaderboard.get(i);
            String rank = String.valueOf(i + 1);
            
            // Add medal emojis for top 3
            if (i == 0) rank = "🥇 1";
            else if (i == 1) rank = "🥈 2";
            else if (i == 2) rank = "🥉 3";
            
            data[i] = new Object[]{
                rank,
                player.name,
                player.gamesPlayed,
                player.gamesWon,
                String.format("%.1f%%", player.getWinRate()),
                String.format("%d/%d", player.pvcWins, player.pvcGames - player.pvcWins),
                String.format("%d/%d", player.pvpWins, player.pvpGames - player.pvpWins),
                formatDate(player.lastPlayed)
            };
        }
        
        JTable table = new JTable(data, columnNames);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setRowHeight(25);
        table.setSelectionBackground(new Color(59, 130, 246));
        table.setSelectionForeground(Color.WHITE);
        
        // Set column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(60);   // Rank
        table.getColumnModel().getColumn(1).setPreferredWidth(120);  // Player
        table.getColumnModel().getColumn(2).setPreferredWidth(60);   // Games
        table.getColumnModel().getColumn(3).setPreferredWidth(60);   // Wins
        table.getColumnModel().getColumn(4).setPreferredWidth(80);   // Win Rate
        table.getColumnModel().getColumn(5).setPreferredWidth(80);   // PvC
        table.getColumnModel().getColumn(6).setPreferredWidth(80);   // PvP
        table.getColumnModel().getColumn(7).setPreferredWidth(100);  // Last Played
        
        JScrollPane scrollPane = new JScrollPane(table);
        
        // Stats panel
        JPanel statsPanel = new JPanel(new FlowLayout());
        JLabel statsLabel = new JLabel(String.format("Total Players: %d | Total Games: %d", 
            players.size(), 
            players.values().stream().mapToInt(p -> p.gamesPlayed).sum()));
        statsPanel.add(statsLabel);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        JButton refreshButton = new JButton("🔄 Refresh");
        refreshButton.addActionListener(e -> {
            dialog.dispose();
            showLeaderboard(parent);
        });
        
        JButton closeButton = new JButton("✗ Close");
        closeButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(refreshButton);
        buttonPanel.add(closeButton);
        
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(statsPanel, BorderLayout.SOUTH);
        mainPanel.add(buttonPanel, BorderLayout.PAGE_END);
        
        dialog.add(mainPanel);
        dialog.setVisible(true);
    }
    
    /**
     * Format date for display
     */
    private static String formatDate(Date date) {
        if (date == null) return "Never";
        
        long diff = new Date().getTime() - date.getTime();
        long days = diff / (24 * 60 * 60 * 1000);
        
        if (days == 0) return "Today";
        if (days == 1) return "Yesterday";
        if (days < 7) return days + " days ago";
        if (days < 30) return (days / 7) + " weeks ago";
        
        return new java.text.SimpleDateFormat("MM/dd/yy").format(date);
    }
    
    /**
     * Save players to file
     */
    private static void savePlayersToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(PLAYERS_FILE))) {
            oos.writeObject(players);
        } catch (IOException e) {
            System.err.println("Error saving players: " + e.getMessage());
        }
    }
    
    /**
     * Load players from file
     */
    @SuppressWarnings("unchecked")
    private static void loadPlayersFromFile() {
        File file = new File(PLAYERS_FILE);
        if (!file.exists()) {
            return;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            players = (Map<String, PlayerProfile>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading players: " + e.getMessage());
            players = new HashMap<>();
        }
    }
    
    /**
     * Clear all player data
     */
    public static void clearAllPlayers() {
        players.clear();
        savePlayersToFile();
    }
}

/**
 * Player Profile class
 */
class PlayerProfile implements Serializable {
    private static final long serialVersionUID = 1L;
    
    String name;
    int gamesPlayed = 0;
    int gamesWon = 0;
    int roundsPlayed = 0;
    int pvcGames = 0;
    int pvcWins = 0;
    int pvpGames = 0;
    int pvpWins = 0;
    Date lastPlayed = new Date();
    
    public PlayerProfile(String name) {
        this.name = name;
    }
    
    public double getWinRate() {
        if (gamesPlayed == 0) return 0.0;
        return (double) gamesWon / gamesPlayed * 100.0;
    }
    
    public double getPvcWinRate() {
        if (pvcGames == 0) return 0.0;
        return (double) pvcWins / pvcGames * 100.0;
    }
    
    public double getPvpWinRate() {
        if (pvpGames == 0) return 0.0;
        return (double) pvpWins / pvpGames * 100.0;
    }
}