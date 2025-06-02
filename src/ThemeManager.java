import javax.swing.*;
import java.awt.*;

/**
 * Theme Manager - Handles light and dark themes
 * Manages color schemes and applies them to UI components
 */
public class ThemeManager {
    private boolean isDarkTheme = false;
    
    // Light theme colors
    private final Color LIGHT_BACKGROUND = new Color(240, 248, 255); // Alice Blue
    private final Color LIGHT_PANEL_BG = new Color(255, 255, 255);   // White
    private final Color LIGHT_TEXT = new Color(25, 25, 112);         // Midnight Blue
    private final Color LIGHT_SECONDARY_TEXT = new Color(70, 70, 70); // Dark Gray
    private final Color LIGHT_BORDER = new Color(200, 200, 200);     // Light Gray
    
    // Dark theme colors
    private final Color DARK_BACKGROUND = new Color(45, 45, 45);     // Dark Gray
    private final Color DARK_PANEL_BG = new Color(60, 60, 60);       // Medium Dark Gray
    private final Color DARK_TEXT = new Color(220, 220, 220);        // Light Gray
    private final Color DARK_SECONDARY_TEXT = new Color(180, 180, 180); // Medium Light Gray
    private final Color DARK_BORDER = new Color(80, 80, 80);         // Medium Gray
    
    // Common colors (same for both themes)
    private final Color WIN_COLOR = new Color(34, 139, 34);          // Forest Green
    private final Color LOSE_COLOR = new Color(220, 20, 60);         // Crimson
    private final Color DRAW_COLOR = new Color(255, 140, 0);         // Dark Orange
    private final Color BUTTON_COLOR = new Color(34, 139, 34);       // Forest Green
    private final Color BUTTON_HOVER = new Color(50, 205, 50);       // Lime Green
    
    /**
     * Set light theme
     */
    public void setLightTheme() {
        isDarkTheme = false;
    }
    
    /**
     * Set dark theme
     */
    public void setDarkTheme() {
        isDarkTheme = true;
    }
    
    /**
     * Check if current theme is dark
     */
    public boolean isDarkTheme() {
        return isDarkTheme;
    }
    
    /**
     * Get background color for current theme
     */
    public Color getBackgroundColor() {
        return isDarkTheme ? DARK_BACKGROUND : LIGHT_BACKGROUND;
    }
    
    /**
     * Get panel background color for current theme
     */
    public Color getPanelBackgroundColor() {
        return isDarkTheme ? DARK_PANEL_BG : LIGHT_PANEL_BG;
    }
    
    /**
     * Get text color for current theme
     */
    public Color getTextColor() {
        return isDarkTheme ? DARK_TEXT : LIGHT_TEXT;
    }
    
    /**
     * Get secondary text color for current theme
     */
    public Color getSecondaryTextColor() {
        return isDarkTheme ? DARK_SECONDARY_TEXT : LIGHT_SECONDARY_TEXT;
    }
    
    /**
     * Get border color for current theme
     */
    public Color getBorderColor() {
        return isDarkTheme ? DARK_BORDER : LIGHT_BORDER;
    }
    
    /**
     * Get win color (same for both themes)
     */
    public Color getWinColor() {
        return WIN_COLOR;
    }
    
    /**
     * Get lose color (same for both themes)
     */
    public Color getLoseColor() {
        return LOSE_COLOR;
    }
    
    /**
     * Get draw color (same for both themes)
     */
    public Color getDrawColor() {
        return DRAW_COLOR;
    }
    
    /**
     * Get button color (same for both themes)
     */
    public Color getButtonColor() {
        return BUTTON_COLOR;
    }
    
    /**
     * Get button hover color (same for both themes)
     */
    public Color getButtonHoverColor() {
        return BUTTON_HOVER;
    }
    
    /**
     * Apply theme to a component and all its children
     */
    public void applyTheme(Component component) {
        if (component instanceof JFrame) {
            applyThemeToFrame((JFrame) component);
        } else if (component instanceof JPanel) {
            applyThemeToPanel((JPanel) component);
        } else if (component instanceof JLabel) {
            applyThemeToLabel((JLabel) component);
        } else if (component instanceof JButton) {
            applyThemeToButton((JButton) component);
        } else if (component instanceof JComboBox) {
            applyThemeToComboBox((JComboBox<?>) component);
        } else if (component instanceof JScrollPane) {
            applyThemeToScrollPane((JScrollPane) component);
        } else if (component instanceof JTable) {
            applyThemeToTable((JTable) component);
        } else if (component instanceof JMenuBar) {
            applyThemeToMenuBar((JMenuBar) component);
        }
        
        // Apply theme to all child components
        if (component instanceof Container) {
            Container container = (Container) component;
            for (Component child : container.getComponents()) {
                applyTheme(child);
            }
        }
    }
    
    /**
     * Apply theme to JFrame
     */
    private void applyThemeToFrame(JFrame frame) {
        frame.getContentPane().setBackground(getBackgroundColor());
        
        // Apply to menu bar if exists
        if (frame.getJMenuBar() != null) {
            applyThemeToMenuBar(frame.getJMenuBar());
        }
    }
    
    /**
     * Apply theme to JPanel
     */
    private void applyThemeToPanel(JPanel panel) {
        // Don't change background if panel is specifically set to be transparent
        if (panel.isOpaque()) {
            panel.setBackground(getPanelBackgroundColor());
        }
        
        // Update border if it's a titled border
        if (panel.getBorder() instanceof javax.swing.border.TitledBorder) {
            javax.swing.border.TitledBorder titledBorder = 
                (javax.swing.border.TitledBorder) panel.getBorder();
            titledBorder.setTitleColor(getTextColor());
        }
    }
    
    /**
     * Apply theme to JLabel
     */
    private void applyThemeToLabel(JLabel label) {
        // Only change color if it's not a special colored label (like result colors)
        Color currentColor = label.getForeground();
        if (currentColor.equals(Color.BLACK) || 
            currentColor.equals(LIGHT_TEXT) || 
            currentColor.equals(DARK_TEXT) ||
            currentColor.equals(LIGHT_SECONDARY_TEXT) ||
            currentColor.equals(DARK_SECONDARY_TEXT)) {
            label.setForeground(getTextColor());
        }
    }
    
    /**
     * Apply theme to JButton
     */
    private void applyThemeToButton(JButton button) {
        // Don't change colored buttons (like start button), only default buttons
        Color currentBg = button.getBackground();
        if (currentBg.equals(UIManager.getColor("Button.background")) ||
            currentBg.equals(Color.LIGHT_GRAY) ||
            currentBg.equals(getPanelBackgroundColor())) {
            
            button.setBackground(getPanelBackgroundColor());
            button.setForeground(getTextColor());
            button.setBorder(BorderFactory.createLineBorder(getBorderColor()));
        }
    }
    
    /**
     * Apply theme to JComboBox
     */
    private void applyThemeToComboBox(JComboBox<?> comboBox) {
        comboBox.setBackground(getPanelBackgroundColor());
        comboBox.setForeground(getTextColor());
        
        // Update the renderer for dropdown items
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                
                if (!isSelected) {
                    c.setBackground(getPanelBackgroundColor());
                    c.setForeground(getTextColor());
                }
                return c;
            }
        });
    }
    
    /**
     * Apply theme to JScrollPane
     */
    private void applyThemeToScrollPane(JScrollPane scrollPane) {
        scrollPane.setBackground(getPanelBackgroundColor());
        scrollPane.getViewport().setBackground(getPanelBackgroundColor());
        
        // Update scroll bars
        scrollPane.getVerticalScrollBar().setBackground(getPanelBackgroundColor());
        scrollPane.getHorizontalScrollBar().setBackground(getPanelBackgroundColor());
    }
    
    /**
     * Apply theme to JTable
     */
    private void applyThemeToTable(JTable table) {
        table.setBackground(getPanelBackgroundColor());
        table.setForeground(getTextColor());
        table.setGridColor(getBorderColor());
        table.setSelectionBackground(isDarkTheme ? new Color(80, 120, 160) : new Color(184, 207, 229));
        table.setSelectionForeground(getTextColor());
        
        // Update table header
        if (table.getTableHeader() != null) {
            table.getTableHeader().setBackground(getBackgroundColor());
            table.getTableHeader().setForeground(getTextColor());
        }
    }
    
    /**
     * Apply theme to JMenuBar
     */
    private void applyThemeToMenuBar(JMenuBar menuBar) {
        menuBar.setBackground(getBackgroundColor());
        menuBar.setForeground(getTextColor());
        
        // Update all menus
        for (int i = 0; i < menuBar.getMenuCount(); i++) {
            JMenu menu = menuBar.getMenu(i);
            if (menu != null) {
                applyThemeToMenu(menu);
            }
        }
    }
    
    /**
     * Apply theme to JMenu
     */
    private void applyThemeToMenu(JMenu menu) {
        menu.setBackground(getBackgroundColor());
        menu.setForeground(getTextColor());
        
        // Update menu items
        for (int i = 0; i < menu.getItemCount(); i++) {
            JMenuItem item = menu.getItem(i);
            if (item != null) {
                item.setBackground(getPanelBackgroundColor());
                item.setForeground(getTextColor());
            }
        }
    }
    
    /**
     * Create themed border
     */
    public javax.swing.border.Border createThemedBorder() {
        return BorderFactory.createLineBorder(getBorderColor());
    }
    
    /**
     * Create themed titled border
     */
    public javax.swing.border.Border createThemedTitledBorder(String title) {
        javax.swing.border.TitledBorder border = BorderFactory.createTitledBorder(
            createThemedBorder(), title
        );
        border.setTitleColor(getTextColor());
        return border;
    }
    
    /**
     * Get result color based on game result
     */
    public Color getResultColor(GameResult result) {
        if (result == null) return getSecondaryTextColor();
        
        switch (result) {
            case WIN: return getWinColor();
            case LOSE: return getLoseColor();
            case DRAW: return getDrawColor();
            default: return getSecondaryTextColor();
        }
    }
    
    /**
     * Create themed panel with proper background
     */
    public JPanel createThemedPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(getPanelBackgroundColor());
        return panel;
    }
    
    /**
     * Create themed label with proper text color
     */
    public JLabel createThemedLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(getTextColor());
        return label;
    }
    
    /**
     * Create themed button with default styling
     */
    public JButton createThemedButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(getPanelBackgroundColor());
        button.setForeground(getTextColor());
        button.setBorder(createThemedBorder());
        button.setFocusPainted(false);
        return button;
    }
}