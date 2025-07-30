package TextEditorLab;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String text = "";
            TextEditorModel model = new TextEditorModel(text);
            TextEditorFrame frame = new TextEditorFrame(model);
        });
    }
}