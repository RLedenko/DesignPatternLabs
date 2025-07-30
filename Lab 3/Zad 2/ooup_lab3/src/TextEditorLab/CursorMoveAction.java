package TextEditorLab;

public class CursorMoveAction implements EditAction {
    private final TextEditorModel model;
    private final Location prevLocation, currColation;

    public CursorMoveAction(TextEditorModel model, Location prevLocation, Location currColation) {
        this.model = model;
        this.prevLocation = prevLocation;
        this.currColation = currColation;
    }

    @Override
    public void execute_do() {
        model.moveCursor(currColation);
    }

    @Override
    public void execute_undo() {
        model.moveCursor(prevLocation);
    }
}
