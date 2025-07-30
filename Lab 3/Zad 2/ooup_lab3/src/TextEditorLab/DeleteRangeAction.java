package TextEditorLab;

public class DeleteRangeAction implements EditAction {
    private final TextEditorModel model;
    private final String deletedText;
    private final LocationRange range;
    private final Location originalCursorLocation;

    public DeleteRangeAction(TextEditorModel model, String deletedText, LocationRange range) {
        this.model = model;
        this.deletedText = deletedText;
        this.range = new LocationRange(range.getStart().copy(), range.getEnd().copy());
        this.originalCursorLocation = model.cursorLocation.copy();
    }

    @Override
    public void execute_do() {
        model.deleteRangeWithoutUndo(range);
    }

    @Override
    public void execute_undo() {
        model.setSelectionRange(null);
        model.insertWithoutUndo(deletedText, range.getMin().copy());
        model.cursorLocation = originalCursorLocation.copy();
    }
}
