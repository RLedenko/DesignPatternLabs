import javax.swing.*;
import java.awt.*;

public class Canvas extends JComponent {
    private DocumentModel document;
    private State curentState;

    public Canvas(DocumentModel document, State curentState) {
        this.document = document;
        this.curentState = curentState;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        Renderer r = new G2DRendererImpl(g2d);
        for(GraphicalObject o : document.list()) {
            o.render(r);
            curentState.afterDraw(r, o);
        }
        curentState.afterDraw(r);
    }

    public void updateState(State newState) {
        curentState = newState;
    }
};
