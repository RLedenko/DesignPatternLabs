import java.util.*;

public class DocumentModel {

    public final static double SELECTION_PROXIMITY = 6.;
    public final static double HOTPOINT_PROXIMITY = 20.;
    public final static double DELETION_PROXIMITY = 2.;

    private List<GraphicalObject> objects = new ArrayList<>();
    private List<GraphicalObject> roObjects = Collections.unmodifiableList(objects);
    private List<DocumentModelListener> listeners = new ArrayList<>();
    private List<GraphicalObject> selectedObjects = new ArrayList<>();
    private List<GraphicalObject> roSelectedObjects = Collections.unmodifiableList(selectedObjects);

    private final GraphicalObjectListener goListener = new GraphicalObjectListener() {
        @Override
        public void graphicalObjectChanged(GraphicalObject go) {
            notifyListeners();
        }

        @Override
        public void graphicalObjectSelectionChanged(GraphicalObject go) {
            if (go.isSelected() && !selectedObjects.contains(go))
                selectedObjects.add(go);
            else if (!go.isSelected() && selectedObjects.contains(go))
                selectedObjects.remove(go);
            notifyListeners();
        }
    };

    public DocumentModel() {}

    public void clear() {
        for (GraphicalObject go : objects) {
            go.removeGraphicalObjectListener(goListener);
        }
        objects.clear();
        selectedObjects.clear();
        notifyListeners();
    }

    public void addGraphicalObject(GraphicalObject obj) {
        if (!objects.contains(obj)) {
            objects.add(obj);
            obj.addGraphicalObjectListener(goListener);
            if (obj.isSelected()) {
                selectedObjects.add(obj);
            }
            notifyListeners();
        }
    }

    public void removeGraphicalObject(GraphicalObject obj) {
        if (objects.remove(obj)) {
            obj.removeGraphicalObjectListener(goListener);
            selectedObjects.remove(obj);
            notifyListeners();
        }
    }

    public List<GraphicalObject> list() {
        return roObjects;
    }

    public List<GraphicalObject> getSelectedObjects() {
        return roSelectedObjects;
    }

    public void addDocumentModelListener(DocumentModelListener l) {
        listeners.add(l);
    }

    public void removeDocumentModelListener(DocumentModelListener l) {
        listeners.remove(l);
    }

    public void notifyListeners() {
        for (DocumentModelListener l : listeners)
            l.documentChange();
    }

    public void increaseZ(GraphicalObject go) {
        int index = objects.indexOf(go);
        if (index >= 0 && index < objects.size() - 1) {
            Collections.swap(objects, index, index + 1);
            notifyListeners();
        }
    }

    public void decreaseZ(GraphicalObject go) {
        int index = objects.indexOf(go);
        if (index > 0) {
            Collections.swap(objects, index, index - 1);
            notifyListeners();
        }
    }

    public GraphicalObject findSelectedGraphicalObject(Point mousePoint) {
        GraphicalObject closest = null;
        double minDist = Double.MAX_VALUE;

        for (GraphicalObject go : objects) {
            double dist = go.selectionDistance(mousePoint);
            if (dist < SELECTION_PROXIMITY && dist < minDist) {
                minDist = dist;
                closest = go;
            }
        }

        return closest;
    }

    public GraphicalObject findintersectingGraphicalObject(Point mousePoint) {
        for(GraphicalObject go : objects) {
            double dist = go.selectionDistance(mousePoint);
            if(dist < DELETION_PROXIMITY)
                return go;
        }

        return null;
    }

    public int findSelectedHotPoint(GraphicalObject object, Point mousePoint) {
        int selectedIndex = -1;
        double minDist = Double.MAX_VALUE;

        for(int i = 0; i < object.getNumberOfHotPoints(); i++) {
            double dist = object.getHotPointDistance(i, mousePoint);
            if(dist < HOTPOINT_PROXIMITY && dist < minDist) {
                minDist = dist;
                selectedIndex = i;
            }
        }

        return selectedIndex;
    }
}
