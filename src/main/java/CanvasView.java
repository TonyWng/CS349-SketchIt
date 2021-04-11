import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;

public class CanvasView implements IObserver {
    private Model model;
    private GraphicsContext gc;
    private int canvasWidth;
    private int canvasHeight;
    private double startX;
    private double startY;
    private double moveX;
    private double moveY;
    private Canvas canvasRef;
    private Image selectIcon = new Image("file:resources/icons/selectIcon.png");

    CanvasView(Model model){
        this.model = model;
    }

    public Canvas constructCanvas(){
        canvasWidth = 2000;
        canvasHeight = 2000;
        Canvas drawCanvas = new Canvas(canvasWidth, canvasHeight);
        this.canvasRef = drawCanvas;
        drawCanvas.setStyle("-fx-border-style: solid;" +
                "-fx-border-width: 2;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: black;"
        );
        this.gc = drawCanvas.getGraphicsContext2D();

        drawCanvas.setOnMouseClicked(e -> {
            double x = e.getX();
            double y = e.getY();
            if(model.getSettings().get("erase")){
                model.eraseShape(x, y);
            } else if(model.getSettings().get("select")){
                model.selectShape(x,y);
            } else if(model.getSettings().get("fill")){
                model.fillShape(x, y);
            }
        });

        drawCanvas.setOnMousePressed(e -> {
            double x = e.getX();
            double y = e.getY();
            this.startX = x;
            this.startY = y;
        });

        drawCanvas.setOnMouseReleased(e -> {
            Paint strokeCol = Color.web(model.getLineProperty().get("lineColour"));
            Paint fillCol = Color.web(model.getLineProperty().get("fillColour"));
            int strokeWidth = 2 * Integer.parseInt(model.getLineProperty().get("lineThickness"));
            int dashStroke = 8 * Integer.parseInt(model.getLineProperty().get("lineStyle"));
            int tranStroke = 10 * Integer.parseInt(model.getLineProperty().get("lineStyle"));

            double endX = e.getX();
            double endY = e.getY();
            this.model.addShape(this.startX, this.startY, endX, endY, strokeCol, fillCol, strokeWidth, dashStroke, tranStroke);
        });

        drawCanvas.setOnMouseDragged(e -> {
            if(model.getSettings().get("line") || model.getSettings().get("rectangle") || model.getSettings().get("circle") ){
                Paint newCol = Color.web(model.getLineProperty().get("lineColour"));
                int strokeWidth = 2 * Integer.parseInt(model.getLineProperty().get("lineThickness"));
                int lineStyle = Integer.parseInt(model.getLineProperty().get("lineStyle"));

                if(lineStyle == 4 ){
                    double[] dashes = {0};
                    gc.setLineDashes(dashes);
                } else {
                    double[] dashes = { 8*lineStyle, 10*lineStyle };
                    gc.setLineDashes(dashes);
                }

                gc.setStroke(newCol);
                gc.setLineWidth(strokeWidth);
                double x = e.getX();
                double y = e.getY();
                if(x != this.moveX || y != this.moveY){
                    gc.clearRect(0,0, drawCanvas.getWidth(), drawCanvas.getHeight());
                }
                this.moveX = x;
                this.moveY = y;
                if(model.getSettings().get("line")){
                    gc.strokeLine(this.startX, this.startY, x, y);
                    update(false);
                } else if(model.getSettings().get("rectangle")){
                    double width = Math.abs(moveX - startX);
                    double height = Math.abs(moveY - startY);
                    gc.strokeRect(Math.min(startX, moveX), Math.min(startY, moveY), width, height);
                    update(false);
                } else if(model.getSettings().get("circle")){
                    double radius = Math.sqrt((moveX-startX)*(moveX-startX) + (moveY-startY)*(moveY-startY));
                    gc.strokeOval(startX - radius, startY - radius, radius * 2, radius * 2);
                    update(false);
                }
            }

        });

        return drawCanvas;
    }

    public void clearCanvas(){
        gc.clearRect(0,0, canvasRef.getWidth(), canvasRef.getHeight());
        model.getShapes().clear();
    }



    public void update(Boolean isErase){
        if(isErase){
            gc.clearRect(0,0,canvasRef.getWidth(), canvasRef.getHeight());
        }
        ArrayList<Shape> shapeList = this.model.getShapes();
        Shape selectedShape = this.model.getSelected();

        for(int i = 0; i < shapeList.size(); i++){
            Shape drawShape = shapeList.get(i);
            gc.setStroke(drawShape.getStroke());
            gc.setFill(drawShape.getFill());
            gc.setLineWidth(drawShape.getStrokeWidth());
            if(drawShape.getStrokeDashArray().get(0) == 32){
                double[] dashes = {0};
                gc.setLineDashes(dashes);
            } else {
                double[] dashes = {drawShape.getStrokeDashArray().get(0), drawShape.getStrokeDashArray().get(1)};
                gc.setLineDashes(dashes);
            }

            if(shapeList.get(i) instanceof Line){
                Line drawLine = ((Line) drawShape);
                gc.strokeLine(drawLine.getStartX(), drawLine.getStartY(), drawLine.getEndX(), drawLine.getEndY());
            } else if(shapeList.get(i) instanceof Circle){
                Circle drawCircle = ((Circle) drawShape);
                gc.strokeOval(drawCircle.getCenterX() - drawCircle.getRadius(), drawCircle.getCenterY() - drawCircle.getRadius(), drawCircle.getRadius() * 2, drawCircle.getRadius() * 2);
                gc.fillOval(drawCircle.getCenterX() - drawCircle.getRadius(), drawCircle.getCenterY() - drawCircle.getRadius(), drawCircle.getRadius() * 2, drawCircle.getRadius() * 2);
            } else if(shapeList.get(i) instanceof Rectangle){
                Rectangle drawRect = ((Rectangle) drawShape);
                gc.strokeRect(drawRect.getX(), drawRect.getY(), drawRect.getWidth(), drawRect.getHeight());
                gc.fillRect(drawRect.getX(), drawRect.getY(), drawRect.getWidth(), drawRect.getHeight());
            }

            if(selectedShape != null){
                if(shapeList.get(i) == selectedShape){
                    if(shapeList.get(i) instanceof Line){
                        Line line = ((Line) drawShape);
                        gc.drawImage(selectIcon, line.getStartX(), line.getStartY(), 30,30);
                    } else if(shapeList.get(i) instanceof Circle){
                        Circle circle = ((Circle) drawShape);
                        gc.drawImage(selectIcon, circle.getCenterX(), circle.getCenterY(), 30,30);
                    } else if(shapeList.get(i) instanceof Rectangle){
                        Rectangle rect = ((Rectangle) drawShape);
                        gc.drawImage(selectIcon, rect.getX(), rect.getY(), 30,30);
                    }
                }
            }
        }
    }
}
