package coopen.coopen;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;


public class Controller {

    @FXML
    private TextField mainAppDir, secondaryAppDir;
    @FXML
    private Label stateLabel, restartLabel;
    @FXML
    private CheckBox minimizeCheckBox;


    public void handleMainAppBrowseClicked(ActionEvent event) {
        BtnBrowse btnMainAppBrowse = new BtnBrowse(mainAppDir, "Select main app");
        btnMainAppBrowse.isClicked();
    }

    public void handleSecondaryAppBrowseClicked(ActionEvent event) {
        BtnBrowse btnSecondaryAppBrowse = new BtnBrowse(secondaryAppDir, "Select secondary app");
        btnSecondaryAppBrowse.isClicked();
    }

    public void handleSaveButtonClicked(ActionEvent event) {
        BtnSave btnSave = new BtnSave(mainAppDir, secondaryAppDir, stateLabel, restartLabel, minimizeCheckBox);
        btnSave.isClicked();
    }

    public void readConfig() {
        String outputConfigDir = "resources/config/coopen_config.txt";
        ReadConfig config = new ReadConfig(mainAppDir, secondaryAppDir, stateLabel, outputConfigDir, minimizeCheckBox);
        config.isRead();
    }

    public boolean isMinimizeToTrayEnabled() {
        return minimizeCheckBox.isSelected();
    }

    public void processMonitor() {
        String mainExe = Paths.get(mainAppDir.getText()).toString();
        String secondaryExe = Paths.get(secondaryAppDir.getText()).toString();

        Thread monitorThread = new Thread(() -> {
            Process secondaryProcess = null;

            while (!Thread.currentThread().isInterrupted()) {
                try {
                    // Проверяем, работает ли основной процесс
                    boolean isMainRunning = ProcessHandle.allProcesses()
                            .anyMatch(ph -> mainExe.equals(ph.info().command().orElse("")));

                    // Управление вторичным процессом
                    if (isMainRunning && secondaryProcess == null) {
                        secondaryProcess = new ProcessBuilder(secondaryExe).start();
                    } else if (!isMainRunning && secondaryProcess != null) {
                        // Стандартное завершение
                        secondaryProcess.destroy();
                        if (!secondaryProcess.waitFor(2, TimeUnit.SECONDS)) {
                            // Принудительное завершение через taskkill
                            Runtime.getRuntime().exec("taskkill /F /T /PID " + secondaryProcess.pid());
                        }
                        secondaryProcess = null;
                    }

                    Thread.sleep(3000);
                } catch (Exception e) {
                    System.err.println("Ошибка: " + e.getMessage());
                    if (secondaryProcess != null) {
                        try {
                            Runtime.getRuntime().exec("taskkill /F /T /PID " + secondaryProcess.pid());
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                    break;
                }
            }
        });

        monitorThread.setDaemon(true);
        monitorThread.start();
    }
}
