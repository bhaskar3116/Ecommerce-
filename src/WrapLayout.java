import java.awt.*;
import java.util.ArrayList;

/**
 * A FlowLayout subclass that provides a more flexible layout.
 * It acts like FlowLayout but wraps components to the next line if space runs out,
 * similar to how text wraps in a paragraph.
 *
 * This class is useful for panels with a variable number of components or components
 * of varying sizes that need to be laid out in a flow but also respect the panel's width.
 */
public class WrapLayout extends FlowLayout {
    
    /**
     * Constructs a new WrapLayout with a default alignment of LEFT,
     * and a default horizontal and vertical gap of 5 pixels.
     */
    public WrapLayout() {
        super();
    }

    /**
     * Constructs a new WrapLayout with the specified alignment
     * and a default horizontal and vertical gap of 5 pixels.
     * The value of the alignment argument must be one of WrapLayout.LEFT,
     * WrapLayout.RIGHT, WrapLayout.CENTER, WrapLayout.LEADING or
     * WrapLayout.TRAILING.
     * @param align the alignment value
     */
    public WrapLayout(int align) {
        super(align);
    }

    /**
     * Creates a new flow layout manager with the indicated alignment
     * and the indicated horizontal and vertical gaps.
     * <p>
     * The value of the alignment argument must be one of
     * <ul>
     * <li><code>WrapLayout.LEFT</code>
     * <li><code>WrapLayout.RIGHT</code>
     * <li><code>WrapLayout.CENTER</code>
     * <li><code>WrapLayout.LEADING</code>
     * <li><code>WrapLayout.TRAILING</code>
     * </ul>
     * @param align the alignment value
     * @param hgap the horizontal gap between components
     * @param vgap the vertical gap between components
     */
    public WrapLayout(int align, int hgap, int vgap) {
        super(align, hgap, vgap);
    }

    /**
     * Returns the preferred dimensions for this layout given the
     * <i>visible</i> components in the specified target container.
     * @param target the component which needs to be laid out
     * @return the preferred dimensions to lay out the subcomponents of the specified container
     */
    @Override
    public Dimension preferredLayoutSize(Container target) {
        return layoutSize(target, true);
    }

    /**
     * Returns the minimum dimensions for this layout given the
     * <i>visible</i> components in the specified target container.
     * @param target the component which needs to be laid out
     * @return the minimum dimensions to lay out the subcomponents of the specified container
     */
    @Override
    public Dimension minimumLayoutSize(Container target) {
        Dimension minimum = layoutSize(target, false);
        minimum.width -= (getHgap() + 1);
        return minimum;
    }

    /**
     * Calculates the dimensions needed for the layout.
     * @param target the container to be laid out
     * @param preferred true for preferred size, false for minimum size
     * @return the calculated size
     */
    private Dimension layoutSize(Container target, boolean preferred) {
        synchronized (target.getTreeLock()) {
            // Each row must fit within the target's width.
            int targetWidth = target.getSize().width;

            if (targetWidth == 0) {
                targetWidth = Integer.MAX_VALUE;
            }

            int hgap = getHgap();
            int vgap = getVgap();
            Insets insets = target.getInsets();
            int horizontalInsets = insets.left + insets.right;
            int verticalInsets = insets.top + insets.bottom;
            int currentRowWidth = 0;
            int currentRowHeight = 0;
            int totalWidth = 0;
            int totalHeight = verticalInsets;

            for (int i = 0; i < target.getComponentCount(); i++) {
                Component m = target.getComponent(i);
                if (m.isVisible()) {
                    Dimension d = preferred ? m.getPreferredSize() : m.getMinimumSize();

                    if (currentRowWidth + d.width + hgap > targetWidth - horizontalInsets) {
                        // Start a new row
                        totalWidth = Math.max(totalWidth, currentRowWidth);
                        totalHeight += currentRowHeight + vgap;
                        currentRowWidth = 0;
                        currentRowHeight = 0;
                    }

                    if (currentRowWidth > 0) {
                        currentRowWidth += hgap;
                    }
                    currentRowWidth += d.width;
                    currentRowHeight = Math.max(currentRowHeight, d.height);
                }
            }

            totalWidth = Math.max(totalWidth, currentRowWidth);
            totalHeight += currentRowHeight;

            return new Dimension(totalWidth + horizontalInsets, totalHeight);
        }
    }
}