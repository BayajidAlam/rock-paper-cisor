import javax.swing.*;
import java.awt.*;

/**
 * Enhanced Theme Manager - Professional themes with gradients and modern styling
 * Provides beautiful, modern themes with smooth transitions
 */
public class EnhancedThemeManager {
    private boolean isDarkTheme = false;
    
    // Professional Light Theme Colors
    private final Color LIGHT_BACKGROUND = new Color(248, 250, 252);      // Modern light gray
    private final Color LIGHT_PANEL_BG = new Color(255, 255, 255);        // Pure white
    private final Color LIGHT_CARD_BG = new Color(249, 250, 251);         // Slight gray for cards
    private final Color LIGHT_TEXT = new Color(30, 41, 59);               // Professional dark blue
    private final Color LIGHT_SECONDARY_TEXT = new Color(71, 85, 105);    // Medium gray
    private final Color LIGHT_ACCENT = new Color(59, 130, 246);           // Professional blue
    private final Color LIGHT_BORDER = new Color(226, 232, 240);          // Light border
    
    // Professional Dark Theme Colors (Improved)
    private final Color DARK_BACKGROUND = new Color(18, 18, 18);          // Very dark background
    private final Color DARK_PANEL_BG = new Color(32, 32, 32);            // Dark panel background
    private final Color DARK_CARD_BG = new Color(48, 48, 48);             // Card background
    private final Color DARK_TEXT = new Color(255, 255, 255);             // Pure white text
    private final Color DARK_SECONDARY_TEXT = new Color(200, 200, 200);   // Light gray text
    private final Color DARK_ACCENT = new Color(96, 165, 250);            // Bright blue accent
    private final Color DARK_BORDER = new Color(64, 64, 64);              // Dark border
    
    // Game Result Colors (Professional)
    private final Color WIN_COLOR = new Color(34, 197, 94);               // Modern green
    private final Color LOSE_COLOR = new Color(239, 68, 68);              // Modern red
    private final Color DRAW_COLOR = new Color(245, 158, 11);             // Modern amber
    
    // Button Colors
    private final Color PRIMARY_BUTTON = new Color(59, 130, 246);         // Blue
    private final Color PRIMARY_BUTTON_HOVER = new Color(37, 99, 235);    // Darker blue
    private final Color SUCCESS_BUTTON = new Color(34, 197, 94);          // Green
    private final Color SUCCESS_BUTTON_HOVER = new Color(22, 163, 74);    // Darker green
    private final Color DANGER_BUTTON = new Color(239, 68, 68);           // Red
    private final Color DANGER_BUTTON_HOVER = new Color(220, 38, 38);     // Darker red
    
    /**
     * Set light theme
     */
    public void setLightTheme() {
        isDarkTheme = false;
        // Removed theme sound
    }
    
    /**
     * Set dark theme
     */
    public void setDarkTheme() {
        isDarkTheme = true;
        // Removed theme sound
    }
    
    /**
     * Check if current theme is dark
     */
    public boolean isDarkTheme() {
        return isDarkTheme;
    }
    
    // Getter methods for colors
    public Color getBackgroundColor() {
        return isDarkTheme ? DARK_BACKGROUND : LIGHT_BACKGROUND;
    }
    
    public Color getPanelBackgroundColor() {
        return isDarkTheme ? DARK_PANEL_BG : LIGHT_PANEL_BG;
    }
    
    public Color getCardBackgroundColor() {
        return isDarkTheme ? DARK_CARD_BG : LIGHT_CARD_BG;
    }
    
    public Color getTextColor() {
        return isDarkTheme ? DARK_TEXT : LIGHT_TEXT;
    }
    
    public Color getSecondaryTextColor() {
        return isDarkTheme ? DARK_SECONDARY_TEXT : LIGHT_SECONDARY_TEXT;
    }
    
    public Color getAccentColor() {
        return isDarkTheme ? DARK_ACCENT : LIGHT_ACCENT;
    }
    
    public Color getBorderColor() {
        return isDarkTheme ? DARK_BORDER : LIGHT_BORDER;
    }
    
    public Color getWinColor() { return WIN_COLOR; }
    public Color getLoseColor() { return LOSE_COLOR; }
    public Color getDrawColor() { return DRAW_COLOR; }
    
    public Color getPrimaryButtonColor() { return PRIMARY_BUTTON; }
    public Color getPrimaryButtonHoverColor() { return PRIMARY_BUTTON_HOVER; }
    public Color getSuccessButtonColor() { return SUCCESS_BUTTON; }
    public Color getSuccessButtonHoverColor() { return SUCCESS_BUTTON_HOVER; }
    public Color getDangerButtonColor() { return DANGER_BUTTON; }
    public Color getDangerButtonHoverColor() { return DANGER_BUTTON_HOVER; }
    
    /**
     * Apply enhanced theme to component
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
    
    private void applyThemeToFrame(JFrame frame) {
        frame.getContentPane().setBackground(getBackgroundColor());
        if (frame.getJMenuBar() != null) {
            applyThemeToMenuBar(frame.getJMenuBar());
        }
    }
    
    private void applyThemeToPanel(JPanel panel) {
        if (panel.isOpaque()) {
            panel.setBackground(getPanelBackgroundColor());
        }
        
        // Force background color for better visibility
        panel.setBackground(getPanelBackgroundColor());
        panel.setOpaque(true);
        
        if (panel.getBorder() instanceof javax.swing.border.TitledBorder) {
            javax.swing.border.TitledBorder titledBorder = 
                (javax.swing.border.TitledBorder) panel.getBorder();
            titledBorder.setTitleColor(getTextColor());
        }
    }
    
    private void applyThemeToLabel(JLabel label) {
        // Always apply theme colors to labels for better visibility
        Color currentColor = label.getForeground();
        label.setForeground(getTextColor());
        
        // Ensure the label is visible
        if (label.getParent() != null) {
            label.setOpaque(false); // Let parent background show through
        }
    }
    
    private void applyThemeToButton(JButton button) {
        // Style based on button type
        String buttonText = button.getText().toLowerCase();
        
        if (buttonText.contains("start") || buttonText.contains("play")) {
            styleButton(button, getSuccessButtonColor(), getSuccessButtonHoverColor());
        } else if (buttonText.contains("clear") || buttonText.contains("delete")) {
            styleButton(button, getDangerButtonColor(), getDangerButtonHoverColor());
        } else if (buttonText.contains("back") || buttonText.contains("cancel")) {
            styleButton(button, getPanelBackgroundColor(), getCardBackgroundColor());
            button.setForeground(getTextColor());
        } else {
            styleButton(button, getPrimaryButtonColor(), getPrimaryButtonHoverColor());
        }
    }
    
    private void styleButton(JButton button, Color bgColor, Color hoverColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add modern rounded appearance
        button.setOpaque(true);
        
        // Remove old mouse listeners and add new hover effect (without sound)
        for (java.awt.event.MouseListener ml : button.getMouseListeners()) {
            if (ml.getClass().getName().contains("MouseAdapter")) {
                button.removeMouseListener(ml);
            }
        }
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (button.isEnabled()) {
                    button.setBackground(hoverColor);
                    // Removed hover sound
                }
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
            
            public void mousePressed(java.awt.event.MouseEvent evt) {
                // Removed click sound
            }
        });
    }
    
    private void applyThemeToComboBox(JComboBox<?> comboBox) {
        comboBox.setBackground(getCardBackgroundColor());
        comboBox.setForeground(getTextColor());
        comboBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(getBorderColor()),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                
                if (isSelected) {
                    c.setBackground(getAccentColor());
                    c.setForeground(Color.WHITE);
                } else {
                    c.setBackground(getCardBackgroundColor());
                    c.setForeground(getTextColor());
                }
                return c;
            }
        });
    }
    
    private void applyThemeToScrollPane(JScrollPane scrollPane) {
        scrollPane.setBackground(getPanelBackgroundColor());
        scrollPane.getViewport().setBackground(getPanelBackgroundColor());
        scrollPane.setBorder(BorderFactory.createLineBorder(getBorderColor()));
        
        scrollPane.getVerticalScrollBar().setBackground(getCardBackgroundColor());
        scrollPane.getHorizontalScrollBar().setBackground(getCardBackgroundColor());
    }
    
    private void applyThemeToTable(JTable table) {
        table.setBackground(getPanelBackgroundColor());
        table.setForeground(getTextColor());
        table.setGridColor(getBorderColor());
        table.setSelectionBackground(getAccentColor());
        table.setSelectionForeground(Color.WHITE);
        table.setRowHeight(28);
        
        if (table.getTableHeader() != null) {
            table.getTableHeader().setBackground(getCardBackgroundColor());
            table.getTableHeader().setForeground(getTextColor());
            table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, getBorderColor()));
        }
    }
    
    private void applyThemeToMenuBar(JMenuBar menuBar) {
        menuBar.setBackground(getPanelBackgroundColor());
        menuBar.setForeground(getTextColor());
        menuBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, getBorderColor()));
        
        for (int i = 0; i < menuBar.getMenuCount(); i++) {
            JMenu menu = menuBar.getMenu(i);
            if (menu != null) {
                applyThemeToMenu(menu);
            }
        }
    }
    
    private void applyThemeToMenu(JMenu menu) {
        menu.setBackground(getPanelBackgroundColor());
        menu.setForeground(getTextColor());
        menu.setOpaque(true);
        
        for (int i = 0; i < menu.getItemCount(); i++) {
            JMenuItem item = menu.getItem(i);
            if (item != null) {
                item.setBackground(getPanelBackgroundColor());
                item.setForeground(getTextColor());
                // Note: JMenuItem doesn't have setSelectionBackground method
                // The selection colors are handled by the Look and Feel
            }
        }
    }
    
    private boolean isDefaultTextColor(Color color) {
        return color.equals(Color.BLACK) || 
               color.equals(LIGHT_TEXT) || 
               color.equals(DARK_TEXT) ||
               color.equals(LIGHT_SECONDARY_TEXT) ||
               color.equals(DARK_SECONDARY_TEXT);
    }
    
    /**
     * Create gradient background panel
     */
    public JPanel createGradientPanel(Color color1, Color color2) {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, color1,
                    0, getHeight(), color2
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
    }
    
    /**
     * Create card-style panel with subtle shadow effect
     */
    public JPanel createCardPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw shadow
                g2d.setColor(new Color(0, 0, 0, 20));
                g2d.fillRoundRect(2, 2, getWidth() - 2, getHeight() - 2, 8, 8);
                
                // Draw card background
                g2d.setColor(getCardBackgroundColor());
                g2d.fillRoundRect(0, 0, getWidth() - 2, getHeight() - 2, 8, 8);
                
                // Draw border
                g2d.setColor(getBorderColor());
                g2d.drawRoundRect(0, 0, getWidth() - 3, getHeight() - 3, 8, 8);
            }
        };
        
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        return panel;
    }
    
    /**
     * Get result color with theme awareness
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
}