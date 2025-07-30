import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class SelectShapeState implements State {

    private DocumentModel model;
    private GraphicalObject selectedObject = null;
    private int selectedHotPoint = -1;
    private Point lastMousePoint = null;

    public SelectShapeState(DocumentModel model) {
        this.model = model;
    }

    @Override
    public void mouseDown(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
        selectedObject = model.findSelectedGraphicalObject(mousePoint);

        if(!ctrlDown) {
            List<GraphicalObject> selected = model.getSelectedObjects();
            for (int i = selected.size() - 1; i >= 0; i--)
                selected.get(i).setSelected(false);
        }

        if(selectedObject != null) {
            selectedObject.setSelected(true);

            selectedHotPoint = model.findSelectedHotPoint(selectedObject, mousePoint);
            if(selectedHotPoint != -1) {
                selectedObject.setHotPointSelected(selectedHotPoint, true);
                lastMousePoint = mousePoint;
            }
        }
    }

    @Override
    public void mouseDragged(Point mousePoint) {
        if (selectedObject != null && selectedHotPoint != -1) {
            selectedObject.setHotPoint(selectedHotPoint, mousePoint);
            lastMousePoint = mousePoint;
        }
    }

    @Override
    public void mouseUp(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
        if (selectedObject != null && selectedHotPoint != -1) {
            selectedObject.setHotPointSelected(selectedHotPoint, false);
            selectedHotPoint = -1;
            lastMousePoint = null;
        }
    }

    @Override
    public void keyPressed(int keyCode) {
        int dx = 0, dy = 0;

        switch (keyCode) {
            case KeyEvent.VK_LEFT -> dx = -1;
            case KeyEvent.VK_RIGHT -> dx = 1;
            case KeyEvent.VK_UP -> dy = -1;
            case KeyEvent.VK_DOWN -> dy = 1;
            case KeyEvent.VK_PLUS, KeyEvent.VK_EQUALS -> {
                for (GraphicalObject go : model.getSelectedObjects())
                    model.increaseZ(go);
                return;
            }
            case KeyEvent.VK_MINUS -> {
                for (GraphicalObject go : model.getSelectedObjects())
                    model.decreaseZ(go);
                return;
            }
            case KeyEvent.VK_G -> {
                List<GraphicalObject> selected = new ArrayList<>(model.getSelectedObjects());
                for (int i = selected.size() - 1; i >= 0; i--)
                    model.removeGraphicalObject(selected.get(i));

                CompositeShape composite = new CompositeShape(selected);
                model.addGraphicalObject(composite);
                composite.setSelected(true);
            }
            case KeyEvent.VK_U -> {
                if(model.getSelectedObjects().size() == 1 && model.getSelectedObjects().getFirst() instanceof CompositeShape composite) {
                    for(GraphicalObject o : composite.getChildren())
                        model.addGraphicalObject(o);
                    model.removeGraphicalObject(composite);
                }
            }
        }

        if (dx != 0 || dy != 0) {
            for (GraphicalObject go : model.getSelectedObjects())
                go.translate(new Point(dx, dy));
        }
    }

    @Override
    public void afterDraw(Renderer r, GraphicalObject go) {
        if (!go.isSelected()) return;

        // nacrtaj bounding box
        Rectangle box = go.getBoundingBox();
        Point topLeft = new Point(box.getX(), box.getY());
        Point topRight = new Point(box.getX() + box.getWidth(), box.getY());
        Point bottomLeft = new Point(box.getX(), box.getY() + box.getHeight());
        Point bottomRight = new Point(box.getX() + box.getWidth(), box.getY() + box.getHeight());

        r.drawLine(topLeft, topRight);
        r.drawLine(topRight, bottomRight);
        r.drawLine(bottomRight, bottomLeft);
        r.drawLine(bottomLeft, topLeft);
    }

    @Override
    public void afterDraw(Renderer r) {
        List<GraphicalObject> selected = model.getSelectedObjects();
        if (selected.size() != 1) return;

        GraphicalObject go = selected.get(0);
        for (int i = 0; i < go.getNumberOfHotPoints(); i++) {
            Point p = go.getHotPoint(i);
            int size = 4;
            Point topLeft = new Point(p.getX() - size, p.getY() - size);
            Point bottomRight = new Point(p.getX() + size, p.getY() + size);
            r.drawLine(topLeft, new Point(bottomRight.getX(), topLeft.getY()));
            r.drawLine(new Point(bottomRight.getX(), topLeft.getY()), bottomRight);
            r.drawLine(bottomRight, new Point(topLeft.getX(), bottomRight.getY()));
            r.drawLine(new Point(topLeft.getX(), bottomRight.getY()), topLeft);
        }
    }

    @Override
    public void onLeaving() {
        List<GraphicalObject> selected = model.getSelectedObjects();
        for (int i = selected.size() - 1; i >= 0; i--)
            selected.get(i).setSelected(false);
    }
}
