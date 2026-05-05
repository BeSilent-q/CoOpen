package coopen.coopen;

import com.dustinredmond.fxtrayicon.FXTrayIcon;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.stage.Stage;


import java.io.IOException;

public class Main extends Application {


    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("MainFrame.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Controller controller = fxmlLoader.getController();
        controller.readConfig();
        controller.processMonitor();

        stage.setTitle("CoOpen");
        Image icon = new Image("icon.png");
        stage.getIcons().add(icon);
        stage.setResizable(false);
        stage.setScene(scene);

        if (controller.isMinimizeToTrayEnabled()) {
            FXTrayIcon trayIcon = new FXTrayIcon(stage, getClass().getResource("/icon.png"));
            trayIcon.show();
            trayIcon.addMenuItem("Open app window", e -> {
                stage.setIconified(false); // Снимаем свёрнутость
                stage.show();             // Показываем окно
                stage.toFront();          // Делаем активным
                stage.requestFocus();     // Фокусируем
            });
            trayIcon.addMenuItem("Close app", e -> System.exit(0));

            // При нажатии на "✕" — закрываем приложение
            stage.setOnCloseRequest(e -> {
                System.exit(0); // Полное завершение
            });

            // При нажатии на "─" — сворачиваем в трей
            stage.iconifiedProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal) { // Если окно свернуто
                    stage.hide(); // Прячем окно
                    trayIcon.show(); // Показываем иконку в трее
                }
            });
        } else {
            stage.show();
        }

    }

    public static void main(String[] args) {
        launch();
    }
}
