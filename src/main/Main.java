package main;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        new Start().start(stage);
    }

    public static void main(String[] args) {
        //System.out.println(Runtime.getRuntime().availableProcessors());
        launch();
    }

}
