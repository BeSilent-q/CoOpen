package coopen.coopen;

import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class BtnSave {

    private final TextField mainAppDir, secondaryAppDir;
    private final Label stateLabel, restartLabel;
    private final CheckBox minimizeCheckBox;

    public BtnSave(TextField mainAppDir, TextField secondaryAppDir, Label stateLabel, Label restartLabel, CheckBox minimizeCheckBox) {
        this.mainAppDir = mainAppDir;
        this.secondaryAppDir = secondaryAppDir;
        this.stateLabel = stateLabel;
        this.restartLabel = restartLabel;
        this.minimizeCheckBox = minimizeCheckBox;
    }

    public void isClicked() {
        try {
            // Получаем путь к папке с исполняемым файлом
            String exePath = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent() + "\\resources\\config";
            File configFile = new File(exePath, "coopen_config.txt");

            // Проверка на заполнение полей перед сохранением
            if (mainAppDir.getText().isEmpty() || secondaryAppDir.getText().isEmpty()) {
                throw new Exception("please make sure all fields are filled in.");
            } else if (mainAppDir.getText().equals(secondaryAppDir.getText())) {
                throw new Exception("please make sure the fields don't match.");
            } else {

                // Формируем и сохраняем конфиг
                String configContent;
                if (minimizeCheckBox.isSelected()) {
                    configContent = mainAppDir.getText() + "|" + secondaryAppDir.getText() + "|M";
                } else {
                    configContent = mainAppDir.getText() + "|" + secondaryAppDir.getText() + "|N";
                }
                Files.writeString(configFile.toPath(), configContent, StandardCharsets.UTF_8);

                stateLabel.setText("Config saved successfully to:\n" + configFile.getAbsolutePath());
                stateLabel.setTextFill(Color.GREEN);
                restartLabel.setText("Restart app to apply changes!");
                restartLabel.setTextFill(Color.BLUE);
            }
        } catch (Exception e) {
            stateLabel.setText("Error saving file:\n " + e.getMessage());
            stateLabel.setTextFill(Color.RED);
            e.printStackTrace();
        }
    }

}
