import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * Game History Manager - Handles saving and loading game history to/from CSV
 * Provides statistics and historical data management
 */
public class GameHistory {
    private static final String HISTORY_FILE = "game_history.csv";
    private static final String CSV_HEADER = "Date,Time,GameMode,Rounds,Player1Score,Player2Score,Winner,Duration";
    private static List<GameRecord> gameRecords = new ArrayList<>();
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    
    /**
     * Initialize the game history system
     */
    public static void initialize() {
        loadHistoryFromFile();
    }
    
    /**
     * Add a new game record to history
     */
    public static void addGameRecord(GameMode mode, int rounds, int player1Score, 
                                   int player2Score, String winner, long durationMs) {
        Date now = new Date();
        GameRecord record = new GameRecord(
            now, mode, rounds, player1Score, player2Score, winner, durationMs
        );
        
        gameRecords.add(record);
        saveHistoryToFile();
    }
    
    /**
     * Get all game records
     */
    public static List<GameRecord> getAllRecords() {
        return new ArrayList<>(gameRecords);
    }
    
    /**
     * Get total number of games played
     */
    public static int getTotalGames() {
        return gameRecords.size();
    }
    
    /**
     * Get total wins for player 1 (or user in PvC mode)
     */
    public static int getTotalWins() {
        int wins = 0;
        for (GameRecord record : gameRecords) {
            if (record.getWinner().contains("Player 1") || record.getWinner().contains("You")) {
                wins++;
            }
        }
        return wins;
    }
    
    /**
     * Get win percentage for player 1 (or user in PvC mode)
     */
    public static double getWinPercentage() {
        if (gameRecords.isEmpty()) return 0.0;
        return (double) getTotalWins() / getTotalGames() * 100.0;
    }
    
    /**
     * Get statistics for a specific game mode
     */
    public static GameModeStats getStatsForMode(GameMode mode) {
        List<GameRecord> modeRecords = new ArrayList<>();
        for (GameRecord record : gameRecords) {
            if (record.getGameMode() == mode) {
                modeRecords.add(record);
            }
        }
        
        return new GameModeStats(mode, modeRecords);
    }
    
    /**
     * Get recent games (last N games)
     */
    public static List<GameRecord> getRecentGames(int count) {
        List<GameRecord> recent = new ArrayList<>(gameRecords);
        Collections.reverse(recent); // Most recent first
        
        if (recent.size() <= count) {
            return recent;
        }
        
        return recent.subList(0, count);
    }
    
    /**
     * Clear all game history
     */
    public static void clearHistory() {
        gameRecords.clear();
        saveHistoryToFile();
    }
    
    /**
     * Export history to CSV file
     */
    public static boolean exportToCSV(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println(CSV_HEADER);
            
            for (GameRecord record : gameRecords) {
                writer.println(record.toCSVString());
            }
            
            return true;
        } catch (IOException e) {
            System.err.println("Error exporting history to CSV: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Save history to file
     */
    private static void saveHistoryToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(HISTORY_FILE))) {
            writer.println(CSV_HEADER);
            
            for (GameRecord record : gameRecords) {
                writer.println(record.toCSVString());
            }
        } catch (IOException e) {
            System.err.println("Error saving game history: " + e.getMessage());
        }
    }
    
    /**
     * Load history from file
     */
    private static void loadHistoryFromFile() {
        File file = new File(HISTORY_FILE);
        if (!file.exists()) {
            return; // No history file yet
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // Skip header
            
            while ((line = reader.readLine()) != null) {
                GameRecord record = GameRecord.fromCSVString(line);
                if (record != null) {
                    gameRecords.add(record);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading game history: " + e.getMessage());
        }
    }
    
    /**
     * Get formatted statistics summary
     */
    public static String getStatsSummary() {
        if (gameRecords.isEmpty()) {
            return "No games played yet.";
        }
        
        StringBuilder summary = new StringBuilder();
        summary.append("=== GAME STATISTICS ===\n");
        summary.append(String.format("Total Games: %d\n", getTotalGames()));
        summary.append(String.format("Total Wins: %d\n", getTotalWins()));
        summary.append(String.format("Win Rate: %.1f%%\n", getWinPercentage()));
        summary.append("\n");
        
        // Mode-specific stats
        GameModeStats pvcStats = getStatsForMode(GameMode.PVC);
        GameModeStats pvpStats = getStatsForMode(GameMode.PVP);
        
        if (pvcStats.getTotalGames() > 0) {
            summary.append("Player vs Computer:\n");
            summary.append(String.format("  Games: %d, Wins: %d (%.1f%%)\n", 
                pvcStats.getTotalGames(), pvcStats.getWins(), pvcStats.getWinPercentage()));
        }
        
        if (pvpStats.getTotalGames() > 0) {
            summary.append("Player vs Player:\n");
            summary.append(String.format("  Games: %d, Wins: %d (%.1f%%)\n", 
                pvpStats.getTotalGames(), pvpStats.getWins(), pvpStats.getWinPercentage()));
        }
        
        return summary.toString();
    }
}

/**
 * Individual game record
 */
class GameRecord {
    private Date date;
    private GameMode gameMode;
    private int rounds;
    private int player1Score;
    private int player2Score;
    private String winner;
    private long durationMs;
    
    public GameRecord(Date date, GameMode gameMode, int rounds, int player1Score, 
                     int player2Score, String winner, long durationMs) {
        this.date = date;
        this.gameMode = gameMode;
        this.rounds = rounds;
        this.player1Score = player1Score;
        this.player2Score = player2Score;
        this.winner = winner;
        this.durationMs = durationMs;
    }
    
    // Getters
    public Date getDate() { return date; }
    public GameMode getGameMode() { return gameMode; }
    public int getRounds() { return rounds; }
    public int getPlayer1Score() { return player1Score; }
    public int getPlayer2Score() { return player2Score; }
    public String getWinner() { return winner; }
    public long getDurationMs() { return durationMs; }
    
    /**
     * Convert to CSV string format
     */
    public String toCSVString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        
        return String.format("%s,%s,%s,%d,%d,%d,\"%s\",%d",
            dateFormat.format(date),
            timeFormat.format(date),
            gameMode.toString(),
            rounds,
            player1Score,
            player2Score,
            winner,
            durationMs
        );
    }
    
    /**
     * Create GameRecord from CSV string
     */
    public static GameRecord fromCSVString(String csvLine) {
        try {
            String[] parts = csvLine.split(",");
            if (parts.length < 8) return null;
            
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = dateTimeFormat.parse(parts[0] + " " + parts[1]);
            
            GameMode mode = parts[2].contains("Computer") ? GameMode.PVC : GameMode.PVP;
            int rounds = Integer.parseInt(parts[3]);
            int p1Score = Integer.parseInt(parts[4]);
            int p2Score = Integer.parseInt(parts[5]);
            String winner = parts[6].replace("\"", "");
            long duration = Long.parseLong(parts[7]);
            
            return new GameRecord(date, mode, rounds, p1Score, p2Score, winner, duration);
        } catch (Exception e) {
            System.err.println("Error parsing CSV line: " + csvLine);
            return null;
        }
    }
    
    /**
     * Get formatted duration string
     */
    public String getFormattedDuration() {
        long seconds = durationMs / 1000;
        long minutes = seconds / 60;
        seconds = seconds % 60;
        
        if (minutes > 0) {
            return String.format("%d:%02d", minutes, seconds);
        } else {
            return String.format("%ds", seconds);
        }
    }
}

/**
 * Statistics for a specific game mode
 */
class GameModeStats {
    private GameMode mode;
    private List<GameRecord> records;
    
    public GameModeStats(GameMode mode, List<GameRecord> records) {
        this.mode = mode;
        this.records = records;
    }
    
    public int getTotalGames() {
        return records.size();
    }
    
    public int getWins() {
        int wins = 0;
        for (GameRecord record : records) {
            if (record.getWinner().contains("Player 1") || record.getWinner().contains("You")) {
                wins++;
            }
        }
        return wins;
    }
    
    public double getWinPercentage() {
        if (records.isEmpty()) return 0.0;
        return (double) getWins() / getTotalGames() * 100.0;
    }
    
    public long getAverageDuration() {
        if (records.isEmpty()) return 0;
        
        long total = 0;
        for (GameRecord record : records) {
            total += record.getDurationMs();
        }
        
        return total / records.size();
    }
    
    public int getTotalRoundsPlayed() {
        int total = 0;
        for (GameRecord record : records) {
            total += record.getRounds();
        }
        return total;
    }
}