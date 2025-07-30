import java.util.HashMap;
import java.util.Map;

public class GraphicalObjectFactory {
    private final static Map<String, GraphicalObject> baseObjects = new HashMap<>();
    private static GraphicalObjectFactory instance;

    private GraphicalObjectFactory() {}

    public static GraphicalObjectFactory getInstance() {
        if(instance == null)
            instance = new GraphicalObjectFactory();

        return instance;
    }

    public void register(String tag, GraphicalObject baseObject) {
        baseObjects.put(tag, baseObject);
    }

    public GraphicalObject generate(String tag) {
        if(!baseObjects.containsKey(tag)) return null;
        return baseObjects.get(tag).duplicate();
    }
}
