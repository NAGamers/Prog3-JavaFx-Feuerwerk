/**
 * Created by user on 31.03.16.
 */
import com.sun.xml.internal.ws.api.streaming.XMLStreamWriterFactory;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.stage.Stage;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;Feuerwerk
public class MoveIt extends Application {
 
    private List<MovingEllipse> ovals = new ArrayList<MovingEllipse>();
    private Group group = new Group();  //root node for the play window
    private final Random random = new Random();
    private final Button bt = new Button("Restart");
    final Pane pane= new Pane(group);
     Scene scene;
    int c = 0 ;
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        final BorderPane borderPane;
        primaryStage.setTitle("Ellipse & AnimationTimer Example");        
         //create a pane for a group with all moving objects
         pane.setPrefSize(700,900);
         pane.setStyle("-fx-background-image: url('" + "http://images.guff.com/media/24621/list/637866/bg-weather-partly-cloudy-night.jpg" + "'); " +
                "-fx-background-position: center center; " +
                "-fx-background-size: cover;");
        //create a restart button
        bt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
            	ovals.clear();              //clear List with references
                group.getChildren().clear();//clear all moving objects  
           }
        });
        //create the main window lauout
        borderPane = new BorderPane();
        borderPane.setTop(bt);
        borderPane.setCenter(pane);
        scene = new Scene(borderPane, 750, 900, Color.ANTIQUEWHITE);
        primaryStage.setScene(scene);
        //set pane autoresisable
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                System.out.println("Width: " + newSceneWidth);
                pane.setPrefWidth(scene.getWidth());
            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                System.out.println("Height: " + newSceneHeight);
                pane.setPrefHeight(scene.getHeight());
            }
        });

        pane.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) { //create one more moving circle
                for(int i=0 ; i<100;i++){
                     Color color = Color.rgb(random.nextInt(256),random.nextInt(256),random.nextInt(256));
                     generate(color, e.getSceneX(), e.getSceneY() - bt.getHeight(), 2.0, true);
                }
           }
        });
        primaryStage.show();




        new AnimationTimer() { //animate all circles
            @Override
            public void handle(long now) {
                for(MovingEllipse e:ovals){
                    checkBorders(e);
                    e.getEllipse().setCenterX(e.getEllipse().getCenterX()+e.getStepX());
                    e.getEllipse().setCenterY(e.getEllipse().getCenterY()+e.getStepY());
                    if(e.getTimer() < 0){
                        e.setStepY(e.getStepY() + 0.005);
                    }else{
                      e.setTimer(e.getTimer()-1);
                    }

                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                }
            }
        }.start();
    }
    
    private void generate(Color c, Double x, Double y, Double radius, boolean clickable){
                        Ellipse localCircle = new Ellipse(x, y, radius, radius);
                        localCircle.setFill(c);
                        if(clickable){  //add event handler
                            localCircle.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent e) { //create one more moving circle



                                }
                            });
                        }
                        ovals.add(new MovingEllipse(localCircle, (random.nextDouble()* 2 - 1)/3, (random.nextDouble()* 2 - 1)/3));
                        group.getChildren().add(localCircle); //add obect to the group
        
    }


    public void checkBorders(MovingEllipse c) {
        final Bounds bounds = pane.getBoundsInLocal();
        boolean atRightBorder = c.getEllipse().getCenterX() >= (scene.getWidth() - c.getEllipse().getRadiusX());
        boolean  atLeftBorder = c.getEllipse().getCenterX() <= (0 + c.getEllipse().getRadiusX());
        boolean  atBottomBorder = c.getEllipse().getCenterY() >= (scene.getHeight() - bt.getHeight() - c.getEllipse().getRadiusY());
        boolean  atTopBorder = c.getEllipse().getCenterY() <= (- bt.getHeight() + c.getEllipse().getRadiusY());

        if (atRightBorder || atLeftBorder) {
            c.setStepX(c.getStepX()*-1);
        }
        if ( atTopBorder) {
            c.setStepY(c.getStepY()*-1);
        }
        if (atBottomBorder ) {
            c.setStepX(0);
            c.setStepY(0);
            group.getChildren().remove(c.getEllipse());
        }

    }



    private class MovingEllipse{
        private double stepX; //
        private double stepY;
        private Ellipse c; //reference on a circle
        int timer = 100;




        MovingEllipse(Ellipse c, double dx, double dy){
            this.c = c;
            stepX = dx;
            stepY = dy;
        }
        public double getStepX() {
            return stepX;
        }
        public void setStepX(double stepX) {
            this.stepX = stepX;
        }
        public double getStepY() {
            return stepY;
        }
        public void setStepY(double stepY) {
            this.stepY = stepY;
        }

        public int getTimer() {
            return timer;
        }
        public void setTimer(int timer) {
            this.timer = timer;
        }

        public Ellipse getEllipse() {
            return c;
        }
        public void setEllipse(Ellipse c) {
            this.c = c;
        }
        double getDistance(Ellipse e){
            double x = c.getCenterX() - e. getCenterX();
            double y = c.getCenterY() - e. getCenterY();
            return Math.sqrt(x * x + y * y);
        }
        double getDistance(double coordX, double coordY){
            double x = c.getCenterX() - coordX;
            double y = c.getCenterY() - coordY;
            return Math.sqrt(x * x + y * y);
        }
    }
}