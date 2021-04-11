import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;

public class PropertyView implements IObserver {
    private Model model;
    private Font labelFont = Font.loadFont("file:resources/fonts/Chillow-Regular.ttf", 40);
    private Font sLabelFont = Font.loadFont("file:resources/fonts/Chillow-Regular.ttf", 25);
    private Image line1 = new Image("file:resources/icons/lineThickness1.png");
    private Image line2 = new Image("file:resources/icons/lineThickness2.png");
    private Image line3 = new Image("file:resources/icons/lineThickness3.png");
    private Image line4 = new Image("file:resources/icons/lineThickness4.png");
    private Image lineStyle1 = new Image("file:resources/icons/lineStyle1.png");
    private Image lineStyle2 = new Image("file:resources/icons/lineStyle2.png");
    private Image lineStyle3 = new Image("file:resources/icons/lineStyle3.png");
    private Image lineStyle4 = new Image("file:resources/icons/lineStyle4.png");
    private ColorPicker colPicker1;
    private ColorPicker colPicker2;

    PropertyView(Model model){
        this.model = model;
    }

    void setColor(boolean isLine, Color c){
        String cString = c.toString();
        if(isLine){
            model.getLineProperty().put("lineColour", cString);
        } else {
            model.getLineProperty().put("fillColour", cString);
        }
    }

    public VBox constructPropertyBox(){
        Label headerLabel = new Label("Properties");
        headerLabel.setFont(labelFont);

        //Line Color Row
        ColorPicker lineColorPicker = new ColorPicker(Color.BLACK);
        lineColorPicker.setOnAction(e -> {
            setColor(true, lineColorPicker.getValue());
                }
        );
        this.colPicker1 = lineColorPicker;
        Label lineLabel = new Label("Line");
        lineLabel.setPadding(new Insets(0, 5, 0, 0));
        lineLabel.setFont(sLabelFont);
        HBox row1 = new HBox(lineLabel, lineColorPicker);
        row1.setAlignment(Pos.CENTER);
        row1.setPadding(new Insets(15,0,25,0));


        //Fill Color Row
        ColorPicker fillColorPicker = new ColorPicker(Color.BLACK);
        fillColorPicker.setOnAction(e -> {
            setColor(false, fillColorPicker.getValue());
        });
        fillColorPicker.setValue(Color.web("White"));
        this.colPicker2 = fillColorPicker;
        Label fillLabel = new Label("Fill");
        fillLabel.setPadding(new Insets(0, 5, 0, 0));
        fillLabel.setFont(sLabelFont);
        HBox row2 = new HBox(fillLabel, fillColorPicker);
        row2.setAlignment(Pos.CENTER);
        row2.setPadding(new Insets(15,0,25,0));

        Label thicknessLabel = new Label("Thickness:");
        thicknessLabel.setFont(sLabelFont);

        //Line Thickness
        ImageView line1View = new ImageView(line1);
        ImageView line2View = new ImageView(line2);
        ImageView line3View = new ImageView(line3);
        ImageView line4View = new ImageView(line4);
        line1View.setFitHeight(25);
        line1View.setPreserveRatio(true);
        line2View.setFitHeight(25);
        line2View.setPreserveRatio(true);
        line3View.setFitHeight(25);
        line3View.setPreserveRatio(true);
        line4View.setFitHeight(25);
        line4View.setPreserveRatio(true);
        Button line1But = new Button();
        Button line2But = new Button();
        Button line3But = new Button();
        Button line4But = new Button();
        line1But.setGraphic(line1View);
        line2But.setGraphic(line2View);
        line3But.setGraphic(line3View);
        line4But.setGraphic(line4View);

        line1But.setOnMouseClicked(e -> {
            this.model.changeWidth("1");
        });
        line2But.setOnMouseClicked(e -> {
            this.model.changeWidth("2");
        });
        line3But.setOnMouseClicked(e -> {
            this.model.changeWidth("3");
        });
        line4But.setOnMouseClicked(e -> {
            this.model.changeWidth("4");
        });

        HBox row3 = new HBox(line1But, line2But, line3But, line4But);
        row3.setAlignment(Pos.CENTER);
        row3.setPadding(new Insets(15,0,25,0));

        Label styleLabel = new Label("Line Style:");
        styleLabel.setFont(sLabelFont);

        //Line Style
        ImageView lineStyle1View = new ImageView(lineStyle1);
        ImageView lineStyle2View = new ImageView(lineStyle2);
        ImageView lineStyle3View = new ImageView(lineStyle3);
        ImageView lineStyle4View = new ImageView(lineStyle4);
        lineStyle1View.setFitHeight(25);
        lineStyle1View.setPreserveRatio(true);
        lineStyle2View.setFitHeight(25);
        lineStyle2View.setPreserveRatio(true);
        lineStyle3View.setFitHeight(25);
        lineStyle3View.setPreserveRatio(true);
        lineStyle4View.setFitHeight(25);
        lineStyle4View.setPreserveRatio(true);
        Button lineStyle1But = new Button();
        Button lineStyle2But = new Button();
        Button lineStyle3But = new Button();
        Button lineStyle4But = new Button();
        lineStyle1But.setGraphic(lineStyle1View);
        lineStyle2But.setGraphic(lineStyle2View);
        lineStyle3But.setGraphic(lineStyle3View);
        lineStyle4But.setGraphic(lineStyle4View);

        lineStyle1But.setOnMouseClicked(e -> {
            this.model.changeStyle("4");
        });
        lineStyle2But.setOnMouseClicked(e -> {
            this.model.changeStyle("3");
        });
        lineStyle3But.setOnMouseClicked(e -> {
            this.model.changeStyle("2");
        });
        lineStyle4But.setOnMouseClicked(e -> {
            this.model.changeStyle("1");
        });

        HBox row4 = new HBox(lineStyle1But, lineStyle2But, lineStyle3But, lineStyle4But);
        row4.setAlignment(Pos.CENTER);
        row4.setPadding(new Insets(15,0,25,0));

        VBox propertyBox = new VBox(headerLabel, row1, row2, thicknessLabel, row3, styleLabel, row4);
        propertyBox.setAlignment(Pos.TOP_CENTER);
        propertyBox.setPrefWidth(100);
        propertyBox.setStyle("-fx-border-style: solid none none none;" +
                "-fx-border-width: 2;" +
                "-fx-border-radius: 1;" +
                "-fx-border-color: black;"
        );
        return propertyBox;
    }

    public void update(Boolean isErase){
        System.out.println("Property Box Update");
        if(this.model.getSettings().get("select") && (this.model.getSelected() != null)){
            Shape selShape = this.model.getSelected();
            Color selStrokeCol = Color.web(selShape.getStroke().toString());
            colPicker1.setValue(selStrokeCol);
            Color selFillCol = Color.web(selShape.getFill().toString());
            colPicker2.setValue(selFillCol);
        } else {
            colPicker1.setValue(Color.web("Black"));
        }
    }
}
