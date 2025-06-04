import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Animation Manager - Handles all animations and visual effects
 * Provides smooth animations for text, components, and game events
 */
public class AnimationManager {
    
    /**
     * Animate text with typing effect
     */
    public static void animateText(JLabel label, String targetText, int delayMs) {
        Timer timer = new Timer(delayMs, null);
        final int[] charIndex = {0};
        
        timer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (charIndex[0] <= targetText.length()) {
                    label.setText(targetText.substring(0, charIndex[0]));
                    charIndex[0]++;
                } else {
                    timer.stop();
                }
            }
        });
        
        label.setText("");
        timer.start();
    }
    
    /**
     * Animate component fade in
     */
    public static void fadeIn(JComponent component, int durationMs) {
        Timer timer = new Timer(20, null);
        final float[] alpha = {0.0f};
        final float increment = 1.0f / (durationMs / 20);
        
        component.setOpaque(false);
        
        timer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                alpha[0] += increment;
                if (alpha[0] >= 1.0f) {
                    alpha[0] = 1.0f;
                    timer.stop();
                    component.setOpaque(true);
                }
                component.repaint();
            }
        });
        
        timer.start();
        EnhancedSoundManager.playSound("fade_in");
    }
    
    /**
     * Animate component slide in from direction
     */
    public static void slideIn(JComponent component, String direction, int durationMs) {
        Point originalLocation = component.getLocation();
        Dimension size = component.getSize();
        
        // Set starting position based on direction
        Point startPosition = new Point(originalLocation);
        switch (direction.toLowerCase()) {
            case "left":
                startPosition.x = -size.width;
                break;
            case "right":
                startPosition.x = component.getParent().getWidth();
                break;
            case "top":
                startPosition.y = -size.height;
                break;
            case "bottom":
                startPosition.y = component.getParent().getHeight();
                break;
        }
        
        component.setLocation(startPosition);
        
        Timer timer = new Timer(20, null);
        final int steps = durationMs / 20;
        final int[] currentStep = {0};
        
        timer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentStep[0]++;
                float progress = (float) currentStep[0] / steps;
                
                // Ease-out animation
                progress = 1 - (1 - progress) * (1 - progress);
                
                int newX = (int) (startPosition.x + (originalLocation.x - startPosition.x) * progress);
                int newY = (int) (startPosition.y + (originalLocation.y - startPosition.y) * progress);
                
                component.setLocation(newX, newY);
                
                if (currentStep[0] >= steps) {
                    component.setLocation(originalLocation);
                    timer.stop();
                }
            }
        });
        
        timer.start();
    }
    
    /**
     * Animate component bounce effect
     */
    public static void bounce(JComponent component, int bounceHeight, int durationMs) {
        Point originalLocation = component.getLocation();
        Timer timer = new Timer(20, null);
        final int steps = durationMs / 20;
        final int[] currentStep = {0};
        
        timer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentStep[0]++;
                double progress = (double) currentStep[0] / steps;
                double bounceProgress = Math.sin(progress * Math.PI * 2) * Math.exp(-progress * 3);
                
                int offsetY = (int) (bounceProgress * bounceHeight);
                component.setLocation(originalLocation.x, originalLocation.y + offsetY);
                
                if (currentStep[0] >= steps) {
                    component.setLocation(originalLocation);
                    timer.stop();
                }
            }
        });
        
        timer.start();
    }
    
    /**
     * Animate text color change
     */
    public static void animateTextColor(JLabel label, Color fromColor, Color toColor, int durationMs) {
        Timer timer = new Timer(20, null);
        final int steps = durationMs / 20;
        final int[] currentStep = {0};
        
        timer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentStep[0]++;
                float progress = (float) currentStep[0] / steps;
                
                int red = (int) (fromColor.getRed() + (toColor.getRed() - fromColor.getRed()) * progress);
                int green = (int) (fromColor.getGreen() + (toColor.getGreen() - fromColor.getGreen()) * progress);
                int blue = (int) (fromColor.getBlue() + (toColor.getBlue() - fromColor.getBlue()) * progress);
                
                label.setForeground(new Color(red, green, blue));
                
                if (currentStep[0] >= steps) {
                    label.setForeground(toColor);
                    timer.stop();
                }
            }
        });
        
        timer.start();
    }
    
    /**
     * Create pulsing effect for component
     */
    public static Timer createPulseEffect(JComponent component, Color normalColor, Color pulseColor, int pulseSpeed) {
        Timer timer = new Timer(pulseSpeed, null);
        final boolean[] increasing = {true};
        final float[] intensity = {0.0f};
        
        timer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (increasing[0]) {
                    intensity[0] += 0.05f;
                    if (intensity[0] >= 1.0f) {
                        intensity[0] = 1.0f;
                        increasing[0] = false;
                    }
                } else {
                    intensity[0] -= 0.05f;
                    if (intensity[0] <= 0.0f) {
                        intensity[0] = 0.0f;
                        increasing[0] = true;
                    }
                }
                
                int red = (int) (normalColor.getRed() + (pulseColor.getRed() - normalColor.getRed()) * intensity[0]);
                int green = (int) (normalColor.getGreen() + (pulseColor.getGreen() - normalColor.getGreen()) * intensity[0]);
                int blue = (int) (normalColor.getBlue() + (pulseColor.getBlue() - normalColor.getBlue()) * intensity[0]);
                
                if (component instanceof JLabel) {
                    ((JLabel) component).setForeground(new Color(red, green, blue));
                } else {
                    component.setBackground(new Color(red, green, blue));
                }
            }
        });
        
        return timer;
    }
    
    /**
     * Show animated result message
     */
    public static void showResultAnimation(Container parent, String message, GameResult result, boolean isGameEnd) {
        // Create overlay panel
        JPanel overlay = new JPanel();
        overlay.setLayout(new BorderLayout());
        overlay.setBackground(new Color(0, 0, 0, 150)); // Semi-transparent
        overlay.setBounds(0, 0, parent.getWidth(), parent.getHeight());
        
        // Create message label
        JLabel messageLabel = new JLabel(message, JLabel.CENTER);
        messageLabel.setFont(new Font("Arial", Font.BOLD, isGameEnd ? 48 : 36));
        messageLabel.setForeground(Color.WHITE);
        
        // Set color based on result
        if (result != null) {
            switch (result) {
                case WIN:
                    messageLabel.setForeground(new Color(50, 205, 50)); // Lime Green
                    break;
                case LOSE:
                    messageLabel.setForeground(new Color(255, 69, 0)); // Red Orange
                    break;
                case DRAW:
                    messageLabel.setForeground(new Color(255, 215, 0)); // Gold
                    break;
            }
        }
        
        overlay.add(messageLabel, BorderLayout.CENTER);
        
        // Add to parent
        parent.add(overlay, 0); // Add on top
        parent.revalidate();
        parent.repaint();
        
        // Animate in
        slideIn(overlay, "top", 500);
        
        // Play appropriate sound
        if (isGameEnd) {
            EnhancedSoundManager.playResultSound(result, true);
        } else {
            EnhancedSoundManager.playResultSound(result, false);
        }
        
        // Auto-remove after delay
        Timer removalTimer = new Timer(isGameEnd ? 3000 : 2000, e -> {
            parent.remove(overlay);
            parent.revalidate();
            parent.repaint();
        });
        removalTimer.setRepeats(false);
        removalTimer.start();
    }
    
    /**
     * Create dancing/disco light effect for title text
     */
    public static Timer createDiscoEffect(JLabel titleLabel) {
        Color[] colors = {
            new Color(255, 0, 0),     // Red
            new Color(255, 165, 0),   // Orange
            new Color(255, 255, 0),   // Yellow
            new Color(0, 255, 0),     // Green
            new Color(0, 191, 255),   // Deep Sky Blue
            new Color(75, 0, 130),    // Indigo
            new Color(238, 130, 238)  // Violet
        };
        
        Timer timer = new Timer(200, null);
        final int[] colorIndex = {0};
        
        timer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                titleLabel.setForeground(colors[colorIndex[0]]);
                colorIndex[0] = (colorIndex[0] + 1) % colors.length;
                
                // Add slight movement
                if (colorIndex[0] % 2 == 0) {
                    titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 50f));
                } else {
                    titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 48f));
                }
            }
        });
        
        return timer;
    }
    
    /**
     * Animate countdown with scaling effect
     */
    public static void animateCountdown(JLabel countdownLabel, String text) {
        // Scale animation
        Font originalFont = countdownLabel.getFont();
        float originalSize = originalFont.getSize();
        
        Timer scaleTimer = new Timer(50, null);
        final int[] step = {0};
        final int maxSteps = 10;
        
        scaleTimer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                step[0]++;
                float scale = 1.0f + (float) Math.sin(step[0] * Math.PI / maxSteps) * 0.3f;
                float newSize = originalSize * scale;
                
                countdownLabel.setFont(originalFont.deriveFont(newSize));
                countdownLabel.setText(text);
                
                if (step[0] >= maxSteps) {
                    countdownLabel.setFont(originalFont);
                    scaleTimer.stop();
                }
            }
        });
        
        scaleTimer.start();
        
        // Color animation
        animateTextColor(countdownLabel, Color.WHITE, new Color(255, 0, 0), 200);
    }
    
    /**
     * Create confetti effect for celebrations
     */
    public static void showConfetti(Container parent) {
        List<JLabel> confetti = new ArrayList<>();
        
        for (int i = 0; i < 20; i++) {
            JLabel piece = new JLabel("🎉");
            piece.setFont(new Font("Arial", Font.PLAIN, 20));
            piece.setBounds(
                (int) (Math.random() * parent.getWidth()), 
                -30, 
                30, 
                30
            );
            confetti.add(piece);
            parent.add(piece, 0);
        }
        
        Timer fallTimer = new Timer(50, null);
        final int[] step = {0};
        
        fallTimer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                step[0]++;
                boolean allFallen = true;
                
                for (JLabel piece : confetti) {
                    int newY = piece.getY() + 5;
                    if (newY < parent.getHeight()) {
                        piece.setLocation(piece.getX(), newY);
                        allFallen = false;
                    }
                }
                
                if (allFallen || step[0] > 200) {
                    fallTimer.stop();
                    for (JLabel piece : confetti) {
                        parent.remove(piece);
                    }
                    parent.revalidate();
                    parent.repaint();
                }
            }
        });
        
        parent.revalidate();
        parent.repaint();
        fallTimer.start();
    }
}