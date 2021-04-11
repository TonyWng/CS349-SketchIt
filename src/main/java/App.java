import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.io.File;

public class App extends Application {
    public static void main(String[] args){
        System.out.println("Main");
        launch();
    }

    @Override
    public void init() throws Exception{
        super.init();
        System.out.println("INIT");
    }

    @Override
    public void start(Stage stage){
        Model model = new Model();
        CanvasView canvasView = new CanvasView(model);
        PropertyView propertyView = new PropertyView(model);
        model.addObserver(canvasView);
        model.addObserver(propertyView);
        ToolView toolView = new ToolView(model);


        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        MenuItem fileNew = new MenuItem("New");
        MenuItem fileOpen = new MenuItem("Load");
        MenuItem fileSave = new MenuItem("Save");
        MenuItem fileQuit = new MenuItem("Quit");
        fileMenu.getItems().addAll(fileNew, fileOpen, fileSave, fileQuit);

        Menu helpMenu = new Menu("Help");
        menuBar.getMenus().addAll(fileMenu, helpMenu);
        fileSave.setOnAction(actionEvent -> {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showSaveDialog(stage);
        });
        fileNew.setOnAction(actionEvent -> {
            canvasView.clearCanvas();
        });
        fileQuit.setOnAction(actionEvent -> {
            System.exit(0);
        });



        VBox toolBox = toolView.constructToolBox();
        VBox propertyBox = propertyView.constructPropertyBox();
        VBox settingsContainer = new VBox(toolBox, propertyBox);
        settingsContainer.setStyle("-fx-border-style: solid;" +
                "-fx-border-width: 4;" +
                "-fx-border-radius: 1;" +
                "-fx-border-color: black;"
        );
        Canvas drawCanvas = canvasView.constructCanvas();
        HBox container = new HBox(settingsContainer, drawCanvas);
        VBox application = new VBox(menuBar, container);
        Scene screen = new Scene(application);
        stage.setScene(screen);
        stage.setHeight(800);
        stage.setWidth(1280);
        stage.setResizable(false);
        stage.setTitle("Tony's Sketch It");
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        System.out.println("STOP");
    }
}
