import TextEditorLab.ClipboardStack;
import TextEditorLab.Plugin;
import TextEditorLab.TextEditorModel;
import TextEditorLab.UndoManager;

import javax.swing.*;

public class Stats implements Plugin {
    @Override
    public String getName() {
        return "Statistika";
    }

    @Override
    public String getDescription() {
        return "Računa broj riječi slova i redaka.";
    }

    @Override
    public void execute(TextEditorModel model, UndoManager undoManager, ClipboardStack clipboardStack) {
        int lines = 0, words = 0, letters = 0;

        lines = model.getLineCount();
        for(String line : model.getLines()) {
            String[] words_arr = line.split(" ");
            words += words_arr.length;
            for(String word : words_arr) {
                letters += word.length();
            }
        }

        JOptionPane.showMessageDialog(null, String.format("%d redaka\n%d riječi\n%d slova\n", lines, words, letters), "Statistika", JOptionPane.INFORMATION_MESSAGE);
    }
}
