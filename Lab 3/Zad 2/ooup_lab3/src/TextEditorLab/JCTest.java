package TextEditorLab;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class JCTest extends JComponent {

    public JCTest() {
        setFocusable(true);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    Window window = SwingUtilities.getWindowAncestor(JCTest.this);
                    if (window != null) {
                        window.dispose();
                    }
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.RED);

        int width = getWidth();
        int height = getHeight();

        g.drawLine(0, height / 2, width, height / 2);
        g.drawLine(width / 2, 0, width / 2, height);
        g.setColor(Color.BLACK);

        g.drawString("Redak #1", 10, 20);
        g.drawString("Redak #2", 10, 40);
    }
}
