import javax.swing.border.AbstractBorder;
import java.awt.*;

/**
 * A custom border class to create rounded corners for Swing components.
 */
public class RoundedBorder extends AbstractBorder {

    private int radius;
    private Color color;
    private Stroke stroke;
    private Insets insets;

    public RoundedBorder(int radius, Color color, int thickness) {
        this.radius = radius;
        this.color = color;
        this.stroke = new BasicStroke(thickness);
        int padding = radius / 2 + thickness;
        this.insets = new Insets(padding, padding, padding, padding);
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(this.color);
        g2d.setStroke(this.stroke);
        int thickness = (int)((BasicStroke) stroke).getLineWidth();
        g2d.drawRoundRect(x + thickness / 2, y + thickness / 2, width - thickness, height - thickness, radius, radius);
        g2d.dispose();
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return insets;
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        insets.left = this.insets.left;
        insets.top = this.insets.top;
        insets.right = this.insets.right;
        insets.bottom = this.insets.bottom;
        return insets;
    }
}