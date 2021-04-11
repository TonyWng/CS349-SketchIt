import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;

public class ToolView implements IObserver {
    private Model model;
    private Image selectIcon = new Image("file:resources/icons/selectIcon.png");
    private Image eraseIcon = new Image("file:resources/icons/eraserIcon.png");
    private Image lineIcon = new Image("file:resources/icons/lineIcon.png");
    private Image circleIcon = new Image("file:resources/icons/circleIcon.png");
    private Image rectangleIcon = new Image("file:resources/icons/rectangleIcon.png");
    private Image fillIcon = new Image("file:resources/icons/fillIcon.png");
    private Font labelFont = Font.loadFont("file:resources/fonts/Chillow-Regular.ttf", 40);

    ToolView(Model model) {
        this.model = model;
    }

    void onSettingsChange(String setting) {
        this.model.changeSettings(setting);
    }

    public VBox constructToolBox(){
        Label headerLabel = new Label("Tool Kit");
        headerLabel.setFont(labelFont);
        //Row 1
        ImageView selectIconView = new ImageView(selectIcon);
        ImageView eraseIconView = new ImageView(eraseIcon);
        selectIconView.setFitHeight(40);
        selectIconView.setPreserveRatio(true);
        eraseIconView.setFitHeight(40);
        eraseIconView.setPreserveRatio(true);
        Button selectButton = new Button();
        Button eraseButton = new Button();
        selectButton.setGraphic(selectIconView);
        eraseButton.setGraphic(eraseIconView);
        HBox row1 = new HBox(selectButton, eraseButton);
        row1.setAlignment(Pos.CENTER);
        row1.setPadding(new Insets(15,0,25,0));

        //Row 2
        ImageView lineIconView = new ImageView(lineIcon);
        ImageView circleIconView = new ImageView(circleIcon);
        lineIconView.setFitHeight(40);
        lineIconView.setPreserveRatio(true);
        circleIconView.setFitHeight(40);
        circleIconView.setPreserveRatio(true);
        Button lineButton = new Button();
        Button circleButton = new Button();
        lineButton.setGraphic(lineIconView);
        circleButton.setGraphic(circleIconView);
        HBox row2 = new HBox(lineButton, circleButton);
        row2.setAlignment(Pos.CENTER);
        row2.setPadding(new Insets(15,0,25,0));

        //Row 3
        ImageView rectangleIconView = new ImageView(rectangleIcon);
        ImageView fillIconView = new ImageView(fillIcon);
        rectangleIconView.setFitHeight(40);
        rectangleIconView.setPreserveRatio(true);
        fillIconView.setFitHeight(40);
        fillIconView.setPreserveRatio(true);
        Button rectangleButton = new Button();
        Button fillButton = new Button();
        rectangleButton.setGraphic(rectangleIconView);
        fillButton.setGraphic(fillIconView);
        HBox row3 = new HBox(rectangleButton, fillButton);
        row3.setAlignment(Pos.CENTER);
        row3.setPadding(new Insets(15,0,25,0));


        VBox toolBox = new VBox(headerLabel, row1, row2, row3);
        toolBox.setPrefWidth(100);

        //Set button sizes
        selectButton.setMinWidth(toolBox.getPrefWidth());
        eraseButton.setMinWidth(toolBox.getPrefWidth());
        lineButton.setMinWidth(toolBox.getPrefWidth());
        circleButton.setMinWidth(toolBox.getPrefWidth());
        rectangleButton.setMinWidth(toolBox.getPrefWidth());
        fillButton.setMinWidth(toolBox.getPrefWidth());

        //Attach mouse click event handlers
        selectButton.setOnMouseClicked(mouseEvent -> {
            onSettingsChange("select");
        });
        eraseButton.setOnMouseClicked(mouseEvent -> {
            onSettingsChange("erase");
        });
        lineButton.setOnMouseClicked(mouseEvent -> {
            onSettingsChange("line");
        });
        circleButton.setOnMouseClicked(mouseEvent -> {
            onSettingsChange("circle");
        });
        rectangleButton.setOnMouseClicked(mouseEvent -> {
            onSettingsChange("rectangle");
        });
        fillButton.setOnMouseClicked(mouseEvent -> {
            onSettingsChange("fill");
        });

        toolBox.setStyle("-fx-border-style: solid none solid none;" +
                "-fx-border-width: 1;" +
                "-fx-border-radius: 1;" +
                "-fx-border-color: black;"
        );
        toolBox.setAlignment(Pos.TOP_CENTER);
        return toolBox;
    }

    public void update(Boolean isErase){

    }
}
