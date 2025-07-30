package TextEditorLab;

public interface UndoObserver {
    void updateStacks(boolean undoEmpty, boolean redoEmpty);
}