package com.example.puntogui;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import model.*;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;

public class HelloController {
    @FXML private Pane root;
    private ArrayList<Punto> punti_disegnati = new ArrayList<>();
    @FXML private ArrayList<Circle> punti_disegnati_circle = new ArrayList<>();
    @FXML private ArrayList<Line> punti_disegnati_line = new ArrayList<>();


    @FXML
    public void getCoords(MouseEvent mouseEvent) {
        System.out.println("Click ricevuto! X: " + mouseEvent.getX() + " Y: " + mouseEvent.getY()); // <-- AGGIUNGI QUESTO
        Punto p = new Punto(mouseEvent.getX(), mouseEvent.getY());
        Circle c = new Circle (mouseEvent.getX(), mouseEvent.getY(),4);
        c.setFill(javafx.scene.paint.Color.RED);
        punti_disegnati.add(p);
        punti_disegnati_circle.add(c);
        root.getChildren().add(c);




    }
}
