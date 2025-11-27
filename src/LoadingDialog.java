import javax.swing.*;
import javax.swing.border.EmptyBorder;
// Required import for the UI update
import javax.swing.plaf.basic.BasicProgressBarUI;
import java.awt.*;

/**
 * A modern loading dialog for showing progress during operations
 */
public class LoadingDialog extends JDialog {
    private JProgressBar progressBar;
    private JLabel statusLabel;
    
    public LoadingDialog(Frame parent, String title, String message) {
        super(parent, title, true);
        setupDialog(message);
    }
    
    public LoadingDialog(Dialog parent, String title, String message) {
        super(parent, title, true);
        setupDialog(message);
    }
    
    private void setupDialog(String message) {
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        
        JPanel contentPanel = new JPanel(new BorderLayout(10, 15));
        contentPanel.setBorder(new EmptyBorder(25, 30, 25, 30));
        contentPanel.setBackground(Color.WHITE);
        
        // Icon
        JLabel iconLabel = new JLabel("⏳", SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        
        // Status message
        statusLabel = new JLabel(message, SwingConstants.CENTER);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        statusLabel.setForeground(ModernUIUtils.TEXT_COLOR);
        
        // Progress bar
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setPreferredSize(new Dimension(250, 10)); // Slightly thicker
        // CORRECTED: Use SECONDARY_COLOR instead of LIGHT_GRAY
        progressBar.setBackground(ModernUIUtils.SECONDARY_COLOR);
        progressBar.setForeground(ModernUIUtils.PRIMARY_COLOR);
        progressBar.setBorderPainted(false); // Flatter look

        // UI Enhancement for a modern, flat progress bar
        progressBar.setUI(new BasicProgressBarUI() {
            @Override
            protected Color getSelectionBackground() {
                return Color.BLACK; // Not used in indeterminate mode
            }
            @Override
            protected Color getSelectionForeground() {
                return Color.WHITE; // Not used in indeterminate mode
            }
        });
        
        // Layout
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(Color.WHITE);
        topPanel.add(iconLabel, BorderLayout.WEST);
        topPanel.add(statusLabel, BorderLayout.CENTER);
        
        contentPanel.add(topPanel, BorderLayout.NORTH);
        contentPanel.add(progressBar, BorderLayout.CENTER);
        
        add(contentPanel);
        pack();
        setLocationRelativeTo(getParent());
    }
    
    public void updateMessage(String message) {
        statusLabel.setText(message);
    }
    
    public void setProgress(int progress) {
        progressBar.setIndeterminate(false);
        progressBar.setValue(progress);
    }
    
    public void setIndeterminate(boolean indeterminate) {
        progressBar.setIndeterminate(indeterminate);
    }
    
    /**
     * Show loading dialog with a task that runs in background
     */
    public static void showWithTask(Component parent, String title, String message, Runnable task) {
        Frame parentFrame = (Frame) SwingUtilities.getAncestorOfClass(Frame.class, parent);
        LoadingDialog dialog = new LoadingDialog(parentFrame, title, message);
        
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                task.run();
                return null;
            }
            
            @Override
            protected void done() {
                dialog.dispose();
            }
        };
        
        worker.execute();
        dialog.setVisible(true);
    }
}