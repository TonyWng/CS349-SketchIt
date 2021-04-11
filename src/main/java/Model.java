import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.HashMap;

public class Model {
    private ArrayList<IObserver> Observers = new ArrayList<>();
    private ArrayList<Shape> Shapes = new ArrayList<>();
    private HashMap<String, Boolean> settings;
    private HashMap<String, String> lineProperty;
    private Shape selectedShape;


    Model(){
        this.settings = new HashMap<String, Boolean>();
        settings.put("select", false);
        settings.put("erase", false);
        settings.put("line", false);
        settings.put("circle", false);
        settings.put("rectangle", false);
        settings.put("fill", false);
        this.lineProperty = new HashMap<String, String>();
        lineProperty.put("lineColour", "Black");
        lineProperty.put("fillColour", "Black");
        lineProperty.put("lineThickness", "1");
        lineProperty.put("lineStyle", "4");
        this.selectedShape = null;
    }

    public void addObserver(IObserver newView) {
        Observers.add(newView);
    }

    public ArrayList<Shape> getShapes() { return this.Shapes; }

    public HashMap<String, String> getLineProperty(){
        return this.lineProperty;
    }

    public HashMap<String, Boolean> getSettings(){
        return settings;
    }

    public void changeSettings(String newSetting){
        this.settings.put(newSetting, true);
        this.settings.forEach((k,v) -> {
            if(k != newSetting){
                this.settings.put(k, false);
            }
        });
    }

    public void changeWidth(String newWidth){
        this.lineProperty.put("lineThickness", newWidth);
    }

    public void changeStyle(String newStyle){
        this.lineProperty.put("lineStyle", newStyle);
    }

    public void addShape(double startX, double startY, double endX, double endY, Paint strokeCol, Paint fillCol, double strokeWidth, double dashStroke, double tranStroke){
        System.out.println("Add Shape");
        if(settings.get("line")){
            //Add line shape
            System.out.println("Add Line");
            Line newLine = new Line(startX, startY, endX, endY);
            newLine.setStroke(strokeCol);
            newLine.setStrokeWidth(strokeWidth);
            newLine.getStrokeDashArray().addAll(dashStroke, tranStroke);
            Shapes.add(newLine);
            update(false);
        } else if(settings.get("circle")){
            //Add circle shape
            System.out.println("Add Circle");
            double radius = Math.sqrt((endX-startX)*(endX-startX) + (endY-startY)*(endY-startY));
            Circle newCircle = new Circle(startX, startY, radius);
            newCircle.setStroke(strokeCol);
            newCircle.setFill(fillCol);
            newCircle.setStrokeWidth(strokeWidth);
            newCircle.getStrokeDashArray().addAll(dashStroke, tranStroke);
            Shapes.add(newCircle);
            update(false);
        } else if(settings.get("rectangle")){
            //Add rectangle shape
            System.out.println("Add Rectangle");
            double width = Math.abs(endX - startX);
            double height = Math.abs(endY - startY);
            Rectangle newRect = new Rectangle(Math.min(startX, endX), Math.min(startY, endY), width, height);
            newRect.setStroke(strokeCol);
            newRect.setFill(fillCol);
            newRect.setStrokeWidth(strokeWidth);
            newRect.getStrokeDashArray().addAll(dashStroke, tranStroke);
            Shapes.add(newRect);
            update(false);
        }
    }

    double distance(double x1, double y1, double x2, double y2){
        double dis = Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
        return dis;
    }

    int findShape(double xCord, double yCord){
        for(int i = 0; i < Shapes.size(); i++){
            if(Shapes.get(i) instanceof Rectangle){
                Rectangle rect = ((Rectangle) Shapes.get(i));
                if((xCord >= rect.getX()) && (xCord <= rect.getX() + rect.getWidth()) && (yCord >= rect.getY()) && (yCord <= rect.getY() + rect.getHeight())){
                    System.out.println("Erase Rect");
                    return i;
                }
            } else if(Shapes.get(i) instanceof Line){
                Line line = ((Line) Shapes.get(i));
                double dis1 = distance(line.getStartX(), line.getStartY(), xCord, yCord);
                double dis2 = distance(xCord, yCord, line.getEndX(), line.getEndY());
                double dis3 = distance(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY());
                if( ((dis1 + dis2) <= (dis3 + 1)) && ((dis1 + dis2) >= (dis3 - 1))){
                    return i;
                }
            } else if(Shapes.get(i) instanceof Circle){
                Circle circle = ((Circle) Shapes.get(i));
                double val1 = (xCord - circle.getCenterX())*(xCord - circle.getCenterX()) + (yCord - circle.getCenterY())*(yCord - circle.getCenterY());
                if(val1 <= circle.getRadius()*circle.getRadius()){
                    return i;
                }
            }
        }
        return -1;
    }

    public void setSelectedNull(){
        this.selectedShape = null;
    }

    public Shape getSelected(){
        return this.selectedShape;
    }

    public void fillShape(double xCord, double yCord){
        int shapeInd = findShape(xCord, yCord);
        if(shapeInd == -1){
            return;
        } else {
            Color fillCol = Color.web(lineProperty.get("fillColour"));
            Shapes.get(shapeInd).setFill(fillCol);
            update(true);
        }
    }

    public void selectShape(double xCord, double yCord){
        System.out.println("Select Shape");
        int shapeInd = findShape(xCord, yCord);
        if(shapeInd == -1){
            return;
        } else {
            if(this.selectedShape == Shapes.get(shapeInd)){
                this.selectedShape = null;
            } else {
                this.selectedShape = Shapes.get(shapeInd);
            }
            System.out.println("Shape " + shapeInd);
            update(true);
        }
    }

    public void eraseShape(double xCord, double yCord){
        System.out.println("ERASE SHAPE");
        int shapeInd = findShape(xCord, yCord);
        if(shapeInd == -1){
            return;
        } else {
            Shapes.remove(shapeInd);
            update(true);
        }
    }

    public void update(Boolean isErase){
        System.out.println("Calls Update");
        //Update views
        for(int i = 0; i < Observers.size(); i++){
            Observers.get(i).update(isErase);
        }
    }

}
