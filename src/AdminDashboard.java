import javax.swing.*;
import java.awt.*;

public class AdminDashboard extends JDialog {
    // The constructor no longer needs parameters
    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(300, 150);
        setLocationRelativeTo(null); // Centered on screen
        setModal(true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JButton addButton = new JButton("Add New Product");
        addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // The button is now always enabled and has a simple action
        addButton.addActionListener(e -> {
            // The AddProductForm will use the static Main.shopInstance
            // to add the product to the l
        	new AddProductForm();
        });

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        panel.add(addButton);

        add(panel);
        setVisible(true);
    }
}