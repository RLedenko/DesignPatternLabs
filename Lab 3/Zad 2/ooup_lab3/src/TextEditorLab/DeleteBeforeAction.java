package TextEditorLab;

public class DeleteBeforeAction implements EditAction {
    private final TextEditorModel model;
    private final String deletedText;
    private final Location deletedAt;

    public DeleteBeforeAction(TextEditorModel model, String deletedText, Location deletedAt) {
        this.model = model;
        this.deletedText = deletedText;
        this.deletedAt = deletedAt.copy();
    }

    @Override
    public void execute_do() {
        model.deleteRangeWithoutUndo(new LocationRange(new Location(deletedAt.line, deletedAt.column - deletedText.length()), deletedAt));
    }

    @Override
    public void execute_undo() {
        model.insertWithoutUndo(deletedText.charAt(0), new Location(deletedAt.line, deletedAt.column - 1));
    }
}
