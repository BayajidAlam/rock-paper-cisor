import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * History Panel - Display game history and statistics
 * Shows game records in table format with export functionality
 */
public class HistoryPanel extends JPanel {
    private RockPaperScissorsApp parent;
    private JTable historyTable;
    private DefaultTableModel tableModel;
    private JLabel statsLabel;
    private JButton backButton;
    private JButton exportButton;
    private JButton clearButton;
    private JComboBox<String> filterCombo;
    
    private final String[] columnNames = {
        "Date", "Time", "Mode", "Rounds", "Your Score", "Opponent Score", "Result", "Duration"
    };
    
    public HistoryPanel(RockPaperScissorsApp parent) {
        this.parent = parent;
        initializeUI();
        refreshHistory();
    }
    
    /**
     * Initialize the history panel UI
     */
    private void initializeUI() {
        setLayout(new BorderLayout());
        
        // Top panel with title and stats
        JPanel topPanel = createTopPanel();
        
        // Center panel with table
        JPanel centerPanel = createCenterPanel();
        
        // Bottom panel with controls
        JPanel bottomPanel = createBottomPanel();
        
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Create top panel with title and statistics
     */
    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        
        // Title
        JLabel titleLabel = new JLabel("📊 GAME HISTORY");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        
        // Statistics
        statsLabel = new JLabel("Loading statistics...");
        statsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        statsLabel.setHorizontalAlignment(JLabel.CENTER);
        
        // Filter controls
        JPanel filterPanel = createFilterPanel();
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(statsLabel, BorderLayout.CENTER);
        panel.add(filterPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * Create filter panel
     */
    private JPanel createFilterPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        
        JLabel filterLabel = new JLabel("Filter:");
        filterLabel.setFont(new Font("Arial", Font.BOLD, 12));
        
        String[] filterOptions = {"All Games", "Player vs Computer", "Player vs Player", "Wins Only", "Recent 10"};
        filterCombo = new JComboBox<>(filterOptions);
        filterCombo.addActionListener(e -> applyFilter());
        
        panel.add(filterLabel);
        panel.add(Box.createRigidArea(new Dimension(10, 0)));
        panel.add(filterCombo);
        
        return panel;
    }
    
    /**
     * Create center panel with history table
     */
    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // Create table model
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        
        // Create table
        historyTable = new JTable(tableModel);
        historyTable.setFont(new Font("Arial", Font.PLAIN, 12));
        historyTable.setRowHeight(25);
        historyTable.setGridColor(Color.LIGHT_GRAY);
        historyTable.setSelectionBackground(new Color(184, 207, 229));
        
        // Set column widths
        historyTable.getColumnModel().getColumn(0).setPreferredWidth(80);  // Date
        historyTable.getColumnModel().getColumn(1).setPreferredWidth(60);  // Time
        historyTable.getColumnModel().getColumn(2).setPreferredWidth(120); // Mode
        historyTable.getColumnModel().getColumn(3).setPreferredWidth(60);  // Rounds
        historyTable.getColumnModel().getColumn(4).setPreferredWidth(80);  // Your Score
        historyTable.getColumnModel().getColumn(5).setPreferredWidth(100); // Opponent Score
        historyTable.getColumnModel().getColumn(6).setPreferredWidth(80);  // Result
        historyTable.getColumnModel().getColumn(7).setPreferredWidth(70);  // Duration
        
        // Custom renderer for result column
        historyTable.getColumnModel().getColumn(6).setCellRenderer(new ResultCellRenderer());
        
        // Create scroll pane
        JScrollPane scrollPane = new JScrollPane(historyTable);
        scrollPane.setPreferredSize(new Dimension(700, 400));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Create bottom panel with control buttons
     */
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        
        // Back button
        backButton = new JButton("🏠 Back to Main");
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setPreferredSize(new Dimension(150, 35));
        backButton.addActionListener(e -> parent.showMainPage());
        
        // Export button
        exportButton = new JButton("📤 Export CSV");
        exportButton.setFont(new Font("Arial", Font.PLAIN, 12));
        exportButton.setPreferredSize(new Dimension(120, 35));
        exportButton.addActionListener(e -> exportHistory());
        
        // Clear button
        clearButton = new JButton("🗑️ Clear History");
        clearButton.setFont(new Font("Arial", Font.PLAIN, 12));
        clearButton.setPreferredSize(new Dimension(130, 35));
        clearButton.setForeground(Color.RED);
        clearButton.addActionListener(e -> clearHistory());
        
        // Refresh button
        JButton refreshButton = new JButton("🔄 Refresh");
        refreshButton.setFont(new Font("Arial", Font.PLAIN, 12));
        refreshButton.setPreferredSize(new Dimension(100, 35));
        refreshButton.addActionListener(e -> refreshHistory());
        
        panel.add(backButton);
        panel.add(Box.createRigidArea(new Dimension(10, 0)));
        panel.add(refreshButton);
        panel.add(Box.createRigidArea(new Dimension(10, 0)));
        panel.add(exportButton);
        panel.add(Box.createRigidArea(new Dimension(10, 0)));
        panel.add(clearButton);
        
        return panel;
    }
    
    /**
     * Refresh the history display
     */
    public void refreshHistory() {
        // Clear existing data
        tableModel.setRowCount(0);
        
        // Get all game records
        List<GameRecord> records = GameHistory.getAllRecords();
        
        // Add records to table
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        
        for (GameRecord record : records) {
            Object[] row = {
                dateFormat.format(record.getDate()),
                timeFormat.format(record.getDate()),
                record.getGameMode().toString(),
                record.getRounds(),
                record.getPlayer1Score(),
                record.getPlayer2Score(),
                determineResult(record),
                record.getFormattedDuration()
            };
            tableModel.addRow(row);
        }
        
        // Update statistics
        updateStatistics();
    }
    
    /**
     * Determine the result text for display
     */
    private String determineResult(GameRecord record) {
        String winner = record.getWinner();
        if (winner.contains("You") || winner.contains("Player 1")) {
            return "WIN";
        } else if (winner.contains("Tie")) {
            return "TIE";
        } else {
            return "LOSS";
        }
    }
    
    /**
     * Update statistics display
     */
    private void updateStatistics() {
        int totalGames = GameHistory.getTotalGames();
        int totalWins = GameHistory.getTotalWins();
        double winRate = GameHistory.getWinPercentage();
        
        String statsText = String.format(
            "Total Games: %d | Wins: %d | Win Rate: %.1f%% | Games Displayed: %d",
            totalGames, totalWins, winRate, tableModel.getRowCount()
        );
        
        statsLabel.setText(statsText);
    }
    
    /**
     * Apply selected filter to the table
     */
    private void applyFilter() {
        String selectedFilter = (String) filterCombo.getSelectedItem();
        List<GameRecord> records = GameHistory.getAllRecords();
        
        // Clear table
        tableModel.setRowCount(0);
        
        // Apply filter
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        
        for (GameRecord record : records) {
            boolean include = false;
            
            switch (selectedFilter) {
                case "All Games":
                    include = true;
                    break;
                case "Player vs Computer":
                    include = (record.getGameMode() == GameMode.PVC);
                    break;
                case "Player vs Player":
                    include = (record.getGameMode() == GameMode.PVP);
                    break;
                case "Wins Only":
                    include = (record.getWinner().contains("You") || record.getWinner().contains("Player 1"));
                    break;
                case "Recent 10":
                    // Will be handled after the loop
                    include = true;
                    break;
            }
            
            if (include) {
                Object[] row = {
                    dateFormat.format(record.getDate()),
                    timeFormat.format(record.getDate()),
                    record.getGameMode().toString(),
                    record.getRounds(),
                    record.getPlayer1Score(),
                    record.getPlayer2Score(),
                    determineResult(record),
                    record.getFormattedDuration()
                };
                tableModel.addRow(row);
            }
        }
        
        // Handle "Recent 10" filter
        if (selectedFilter.equals("Recent 10") && tableModel.getRowCount() > 10) {
            // Keep only the last 10 rows
            while (tableModel.getRowCount() > 10) {
                tableModel.removeRow(0);
            }
        }
        
        updateStatistics();
    }
    
    /**
     * Export history to CSV file
     */
    private void exportHistory() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export Game History");
        fileChooser.setSelectedFile(new java.io.File("game_history_export.csv"));
        
        int userSelection = fileChooser.showSaveDialog(this);
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            java.io.File fileToSave = fileChooser.getSelectedFile();
            
            if (GameHistory.exportToCSV(fileToSave.getAbsolutePath())) {
                JOptionPane.showMessageDialog(this, 
                    "History exported successfully to:\n" + fileToSave.getAbsolutePath(),
                    "Export Successful", 
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Failed to export history. Please try again.",
                    "Export Failed",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Clear all game history
     */
    private void clearHistory() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to clear all game history?\nThis action cannot be undone.",
            "Clear History",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            GameHistory.clearHistory();
            refreshHistory();
            JOptionPane.showMessageDialog(this,
                "Game history cleared successfully!",
                "History Cleared",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
}

/**
 * Custom cell renderer for the result column
 */
class ResultCellRenderer extends JLabel implements TableCellRenderer {
    
    public ResultCellRenderer() {
        setOpaque(true);
        setHorizontalAlignment(JLabel.CENTER);
        setFont(new Font("Arial", Font.BOLD, 12));
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        
        String result = value.toString();
        setText(result);
        
        if (isSelected) {
            setBackground(table.getSelectionBackground());
            setForeground(table.getSelectionForeground());
        } else {
            setBackground(table.getBackground());
            
            // Set color based on result
            switch (result) {
                case "WIN":
                    setForeground(new Color(34, 139, 34)); // Green
                    break;
                case "LOSS":
                    setForeground(new Color(220, 20, 60)); // Red
                    break;
                case "TIE":
                    setForeground(new Color(255, 140, 0)); // Orange
                    break;
                default:
                    setForeground(table.getForeground());
                    break;
            }
        }
        
        return this;
    }
}