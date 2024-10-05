package org.tyby.javaapacheexp;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;

public class JavaApacheExp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(JavaApacheExp.class.getResource("/fxml/Main.fxml"));
        stage.setTitle("ApacheOfbizScan");
        Scene scene = new Scene(fxmlLoader.load());

        stage.setScene(scene);
        // 退出程序的时候，子线程也一起退出
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });
        //设置窗口不可拉伸
        stage.setResizable(false);

        stage.getIcons().add(new Image(JavaApacheExp.class.getResource("/img/logo.jpg").toString()));

        stage.show();
    }

/*    private Button lastClickedButton = null;

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox(10);

        // 创建按钮
        for (int i = 1; i <= 5; i++) {
            Button button = new Button("Button " + i);

            button.setStyle("-fx-background-color: lightgray;");

            button.setOnAction(event -> {
                // 恢复上一个被点击按钮的颜色
                if (lastClickedButton != null) {
                    lastClickedButton.setStyle("-fx-background-color: lightgray;");
                }

                // 设置当前被点击按钮的颜色
                button.setStyle("-fx-background-color: lightblue;");
                lastClickedButton = button;
            });

            root.getChildren().add(button);
        }

        Scene scene = new Scene(root, 300, 200);
        primaryStage.setTitle("Button Color Toggle");
        primaryStage.setScene(scene);
        primaryStage.show();
    }*/


    public static void main(String[] args) {
        launch();
    }
}