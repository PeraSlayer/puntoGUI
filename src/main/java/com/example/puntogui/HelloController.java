package com.example.puntogui;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import model.*;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HelloController {
    @FXML private Pane root;
    private ArrayList<Punto> punti_disegnati = new ArrayList<>();
    private GestionePunti g = new GestionePunti(punti_disegnati);
    private ArrayList<Label> labels = new ArrayList<>();
    @FXML private ArrayList<Circle> punti_disegnati_circle = new ArrayList<>();
    @FXML private ArrayList<Line> linee = new ArrayList<>();
    @FXML private ArrayList<Circle> punti_disegnati_media = new ArrayList<>();
    private ArrayList<Punto> punti_media = new ArrayList<>();


    @FXML
    public void getCoords(@NotNull MouseEvent mouseEvent) {
        System.out.println("Click ricevuto! X: " + mouseEvent.getX() + " Y: " + mouseEvent.getY()); // <-- AGGIUNGI QUESTO

        Punto p = new Punto(mouseEvent.getX(), mouseEvent.getY());
        Circle c = new Circle (mouseEvent.getX(), mouseEvent.getY(),4);
        c.setFill(Color.LIGHTSKYBLUE);
        punti_disegnati.add(p);
        punti_disegnati_circle.add(c);
        root.getChildren().add(c);
        c.toFront();
    }



    @FXML
    public void getCoords(double x, double y) {
        System.out.println("Click ricevuto! X: " + x + " Y: " + y); // <-- AGGIUNGI QUESTO
        Punto p = new Punto(x, y);
        Circle c = new Circle (x, y,4);
        c.setFill(Color.PINK);
        punti_media.add(p);
        punti_disegnati_media.add(c);
        root.getChildren().add(c);
        c.toFront();
    }



    @FXML
    public void erase(){
        root.getChildren().removeAll(linee);
        root.getChildren().removeAll(punti_disegnati_circle);
        root.getChildren().removeAll(labels);
        linee.clear();
        punti_disegnati_circle.clear();
        punti_disegnati.clear();
        labels.clear();
        punti_media.clear();
       root.getChildren().removeAll(punti_disegnati_media);

    }
    @FXML
    public void draw() {
        root.getChildren().removeAll(linee);
        root.getChildren().removeAll(labels);
        punti_media.clear();
        root.getChildren().removeAll(punti_disegnati_media);
        linee.clear();
        labels.clear();
        ArrayList<Punto> s = g.star_shaping(punti_disegnati);

        for (int i = 0; i < s.size(); i++) {
            Line l = new Line();


            l.setStartX(s.get(i).getX());
            l.setStartY(s.get(i).getY());

            l.setEndX(s.get((i+1)%s.size()).getX());
            l.setEndY(s.get((i+1)%s.size()).getY());

            linee.add(l);
            root.getChildren().add(l);
        }


        for (int i = 0; i < linee.size(); i++) {
            Label label = getLabel(i);
            labels.add(label);
            root.getChildren().add(label);
            label.toFront();

        }

        for (Line l : linee){
            l.toFront();
        }
    }

    @NotNull
    private Label getLabel(int i) {
        double x_start = linee.get(i).getStartX();
        double x_end = linee.get(i).getEndX();
        double y_start = linee.get(i).getStartY();
        double y_end = linee.get(i).getEndY();
        double lunghezza = Math.sqrt(Math.pow(x_start- x_end, 2) + Math.pow(y_start - y_end, 2));

        double media_x = (x_start + x_end)/2;
        double media_y = (y_start + y_end)/2;
        getCoords(media_x,media_y);


        Label label = new Label(String.format("%.2f",lunghezza));

        label.setLayoutX(media_x);
        label.setLayoutY(media_y);

        return label;
    }
}
