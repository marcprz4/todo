package com.app;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Main extends Application {
    public static ConfigurableApplicationContext springContext;
    private Parent rootNode;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FxWeaver fxWeaver = springContext.getBean(FxWeaver.class);
        rootNode = fxWeaver.loadView(MainWindowController.class);
        Scene scene = new Scene(rootNode);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void init() {
        String[] args = getParameters().getRaw().toArray(new String[0]);

        springContext = new SpringApplicationBuilder()
                .sources(Main.class)
                .run(args);
    }

//        springContext = SpringApplication.run(Main.class);
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/MainWindow.fxml"));
//        fxmlLoader.setControllerFactory(springContext::getBean);
//        rootNode = fxmlLoader.load();

    public void stop() throws Exception {
        springContext.close();
        Platform.exit();
    }
}