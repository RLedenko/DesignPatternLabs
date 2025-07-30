import java.util.*;

public class CompositeShape extends AbstractGraphicalObject {

    private final List<GraphicalObject> children;

    private final GraphicalObjectListener goListener = new GraphicalObjectListener() {
        @Override
        public void graphicalObjectChanged(GraphicalObject go) {
            notifyListeners();
        }

        @Override
        public void graphicalObjectSelectionChanged(GraphicalObject go) {
            boolean anySelected = false;
            for(GraphicalObject child : children)
                if(child.isSelected()) {
                    anySelected = true;
                    break;
                }
            setSelected(anySelected);
        }
    };

    public CompositeShape(List<GraphicalObject> children) {
        super(new Point[0]);

        this.children = children;
        for(GraphicalObject child : children)
            child.addGraphicalObjectListener(goListener);
    }

    public List<GraphicalObject> getChildren() {
        return children;
    }

    @Override
    public Rectangle getBoundingBox() {
        if(children.isEmpty())
            return new Rectangle(0, 0, 0, 0);

        Rectangle result = children.getFirst().getBoundingBox();
        int minX = result.getX();
        int minY = result.getY();
        int maxX = result.getX() + result.getWidth();
        int maxY = result.getY() + result.getHeight();

        for(int i = 1; i < children.size(); i++) {
            Rectangle r = children.get(i).getBoundingBox();
            minX = Math.min(minX, r.getX());
            minY = Math.min(minY, r.getY());
            maxX = Math.max(maxX, r.getX() + r.getWidth());
            maxY = Math.max(maxY, r.getY() + r.getHeight());
        }

        return new Rectangle(minX, minY, maxX - minX, maxY - minY);
    }

    @Override
    public double selectionDistance(Point mousePoint) {
        return children.stream().mapToDouble(c -> c.selectionDistance(mousePoint)).min().orElse(Double.MAX_VALUE);
    }

    @Override
    public void render(Renderer r) {
        for(GraphicalObject child : children)
            child.render(r);
    }

    @Override
    public String getShapeName() {
        return "Kompozit";
    }

    @Override
    public GraphicalObject duplicate() {
        List<GraphicalObject> dupChildren = new ArrayList<>();
        for(GraphicalObject child : children)
            dupChildren.add(child.duplicate());
        return new CompositeShape(dupChildren);
    }

    @Override
    public void translate(Point dp) {
        for(GraphicalObject child : children)
            child.translate(dp);
        notifyListeners();
    }

    @Override
    public String getShapeID() {
        return "@COMP";
    }

    @Override
    public void load(Stack<GraphicalObject> stack, String data) {
        if(!IOUtil.containsOnlyNumbers(data))
            return;
        int childrenCount = Integer.parseInt(data);
        while(childrenCount-- > 0)
            children.add(stack.pop());
        Collections.reverse(children);

        stack.push(this);
    }

    @Override
    public void save(List<String> rows) {
        for(GraphicalObject go : children)
            go.save(rows);
        rows.add(getShapeID() + " " + children.size());
    }
}
