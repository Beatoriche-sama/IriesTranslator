package UI.overlay;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AreaSelectionOverlay extends Overlay {
    private final JPanel selectArea;
    private Point pPressed, pointSelected;
    private Dimension dimensionSelected;
    private final MouseReleaseEvent mouseReleaseEvent;

    private int mouseClicks;

    public AreaSelectionOverlay(MouseReleaseEvent mouseReleaseEvent) {
        this.mouseReleaseEvent = mouseReleaseEvent;
        //reusable select area panel
        this.selectArea = new JPanel();
        selectArea.setBackground(TRANSPARENT);
        selectArea.setBorder(BorderFactory.createLineBorder(Color.red));

        add(selectArea, "hidemode 3");
        setLayout(new MigLayout());

        SelectionHandler selectionHandler = new SelectionHandler();
        addMouseListener(selectionHandler);
        addMouseMotionListener(selectionHandler);
    }

    private class SelectionHandler extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            mouseClicks += 1;
            if (mouseClicks > 1) {
                System.out.println("more clicks");
                setTransparent();
                return;
            }

            selectArea.setVisible(true);
            pPressed = e.getPoint();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            super.mouseDragged(e);
            Point pDragged = e.getPoint();
            int leftX = Math.min(pPressed.x, pDragged.x);
            int rightX = Math.max(pPressed.x, pDragged.x);
            int leftY = Math.min(pPressed.y, pDragged.y);
            int rightY = Math.max(pPressed.y, pDragged.y);
            selectArea.setBounds(leftX, leftY, rightX - leftX, rightY - leftY);
            pointSelected = selectArea.getLocation();
            dimensionSelected = selectArea.getSize();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            mouseClicks = 0;

            setBackground(TRANSPARENT);
            selectArea.setVisible(false);
            repaint();
            mouseReleaseEvent.onRelease(pointSelected, dimensionSelected);
        }
    }
}
