package coopen.coopen;

import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class ReadConfig {

    private final TextField mainAppDir, secondaryAppDir;
    private final Label stateLabel;
    private final String outputConfigDir;
    private final CheckBox minimizeCheckBox;

    public ReadConfig(TextField mainAppDir, TextField secondaryAppDir, Label stateLabel, String outputConfigDir, CheckBox minimizeCheckBox) {
        this.mainAppDir = mainAppDir;
        this.secondaryAppDir = secondaryAppDir;
        this.stateLabel = stateLabel;
        this.outputConfigDir = outputConfigDir;
        this.minimizeCheckBox = minimizeCheckBox;
    }

    public void isRead() {
        File file = new File(outputConfigDir);
        try {
            if (Files.exists(file.toPath())) {
                String config = Files.readString(file.toPath(), StandardCharsets.UTF_8);
                String[] settings = config.split("\\|");
                if (settings.length >= 3) {
                    mainAppDir.setText(settings[0]);
                    secondaryAppDir.setText(settings[1]);
                    minimizeCheckBox.setSelected(settings[2].equals("M"));
                    stateLabel.setText("Config successfully loaded!");
                    stateLabel.setTextFill(Color.GREEN);
                }
            } else {
                throw new IOException();
            }
        } catch (IOException e) {
            stateLabel.setText("Failed to load config.");
            stateLabel.setTextFill(Color.RED);
            e.printStackTrace();
        }
    }

}
