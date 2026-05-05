package coopen.coopen;

import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;

public class BtnBrowse {

    private final TextField appDir;
    private final String title;

    private final FileChooser fileChooser = new FileChooser();
    private final FileChooser.ExtensionFilter filterExe = new FileChooser.ExtensionFilter("Select", "*.exe");

    public BtnBrowse(TextField appDir, String title) {
        this.appDir = appDir;
        this.title = title;
    }

    public void isClicked() {
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().setAll(filterExe);
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            appDir.setText(selectedFile.getAbsolutePath());
        }
    }

}
