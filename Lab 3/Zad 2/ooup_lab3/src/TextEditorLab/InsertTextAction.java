package TextEditorLab;

public class InsertTextAction implements EditAction {
    private final TextEditorModel model;
    private final String text;
    private final Location location;

    public InsertTextAction(TextEditorModel model, String text, Location location) {
        this.model = model;
        this.text = text;
        this.location = location.copy();
    }

    @Override
    public void execute_do() {
        model.insertWithoutUndo(text, location);
    }

    @Override
    public void execute_undo() {
        if(text.equals("\n")) {
            model.moveCursor(location);
            model.mergeLineWithPrevious(location.line + 1);
            return;
        }
        int targetLength = text.length() - (int)text.chars().filter(ch -> ch == '\n').count(), line = location.line;
        while(targetLength > model.getLine(line).length()) {
            targetLength -= model.getLine(line).length();
            line += 1;
            System.out.println(".");
        }
        Location end = new Location(line, targetLength + (location.line == line ? location.column : 0));
        model.deleteRangeWithoutUndo(new LocationRange(location, end));
    }
}
