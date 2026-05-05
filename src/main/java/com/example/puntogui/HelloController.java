package com.example.puntogui;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import model.*;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import org.jetbrains.annotations.NotNull;
import javafx.util.Duration;
import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;



import java.util.ArrayList;
import java.util.List;

public class HelloController {

    @FXML private Pane root = new Pane();
    @FXML private TextFlow LOG_PUNTI;
    private final ArrayList<Punto> punti_disegnati = new ArrayList<>();
    private final GestionePunti g = new GestionePunti(punti_disegnati);
    private final ArrayList<Label> labels = new ArrayList<>();
    private final ArrayList<Circle> punti_disegnati_circle = new ArrayList<>();
    private final ArrayList<Line> linee = new ArrayList<>();
    private final ArrayList<Circle> punti_disegnati_media = new ArrayList<>();
    private final ArrayList<Punto> punti_media = new ArrayList<>();
    private SequentialTransition sequenzaCorrente;
    private final Color violetto = Color.web("#a972fc");

    @FXML
    public void getCoords(@NotNull MouseEvent mouseEvent) {
        Punto p = new Punto(mouseEvent.getX(), mouseEvent.getY());
        Circle c = new Circle (mouseEvent.getX(), mouseEvent.getY(),4);
        c.setFill(violetto);
        punti_disegnati.add(p);
        punti_disegnati_circle.add(c);
        root.getChildren().add(c);
        c.toFront();


        int larghezza = 600;
        int altezza = 400;
        String coordinata = String.format("Punto %d: (%.0f, %.0f)\n", punti_disegnati.size(),
                mouseEvent.getX()- (larghezza/2), (mouseEvent.getY()- (altezza/2))*-1);
        Text t = new Text(coordinata);
        t.setFill(Color.BLACK);
        LOG_PUNTI.getChildren().add(t);
    }


    @FXML
    public void getCoords(double x, double y) {
        Punto p = new Punto(x, y);
        Circle c = new Circle(x, y, 4);
        c.setFill(Color.PINK);
        punti_media.add(p);
        punti_disegnati_media.add(c);
        root.getChildren().add(c);
        c.toFront();

    }


    @FXML
    public void getCoords(Punto p) {
        Circle c = new Circle(p.getX(), p.getY(), 4);
        c.setFill(Color.ORANGE);
        punti_media.add(p);
        punti_disegnati_media.add(c);
        root.getChildren().add(c);
        c.toFront();

    }

    // Variabile globale per mantenere e sovrascrivere un'unica istanza
    private Timeline timelineUnica;

    public void eseguiAnimazioneDisegno() throws InterruptedException {
        // 1. Ferma e pulisci la vecchia timeline se esiste
        if (timelineUnica != null) {
            timelineUnica.stop();
            timelineUnica.getKeyFrames().clear();
        }

        timelineUnica = new Timeline();

        // Definiamo i "punti di arrivo" nel tempo per simulare la sequenza
        double fineLinee = 300;       // Le linee appaiono da 0 a 300ms
        double finePunti = 500;       // I punti appaiono da 300ms a 500ms
        double fineLabels = 700;      // Le etichette appaiono da 500ms a 700ms

        // 2. Anima le Linee
        for (Line l : linee) {
            l.setOpacity(0);
            // Va da 0 a 1.0 nei primi 300ms
            timelineUnica.getKeyFrames().add(
                    new KeyFrame(Duration.millis(fineLinee), new KeyValue(l.opacityProperty(), 1.0))
            );
        }

        // 3. Anima i Punti Medi
        for (Circle c : punti_disegnati_media) {
            c.setOpacity(0);
            // Mantiene l'opacità a 0 fino a 'fineLinee' (300ms), poi sfuma a 1.0 fino a 'finePunti' (500ms)
            timelineUnica.getKeyFrames().addAll(
                    new KeyFrame(Duration.millis(fineLinee), new KeyValue(c.opacityProperty(), 0.0)),
                    new KeyFrame(Duration.millis(finePunti), new KeyValue(c.opacityProperty(), 1.0))
            );
        }

        // 4. Anima le Etichette (Labels)
        for (Label label : labels) {
            label.setOpacity(0);
            // Mantiene l'opacità a 0 fino a 'finePunti' (500ms), poi sfuma a 1.0 fino a 'fineLabels' (700ms)
            timelineUnica.getKeyFrames().addAll(
                    new KeyFrame(Duration.millis(finePunti), new KeyValue(label.opacityProperty(), 0.0)),
                    new KeyFrame(Duration.millis(fineLabels), new KeyValue(label.opacityProperty(), 1.0))
            );
        }

        // 5. Avvia il motore
        timelineUnica.play();
    }



    @FXML
    public void erase() {
        // Creiamo una lista unica di nodi da animare per la scomparsa
        List<Node> nodiDaRimuovere = new ArrayList<>();
        nodiDaRimuovere.addAll(linee);
        nodiDaRimuovere.addAll(punti_disegnati_circle);
        nodiDaRimuovere.addAll(labels);
        nodiDaRimuovere.addAll(punti_disegnati_media);

        for (Node nodo : nodiDaRimuovere) {
            FadeTransition ft = new FadeTransition(Duration.millis(400), nodo);
            ft.setFromValue(1.0);
            ft.setToValue(0.0);

            // Al termine dell'animazione, rimuoviamo fisicamente dal root
            ft.setOnFinished(e -> root.getChildren().remove(nodo));
            ft.play();
        }

        // Pulizia delle liste logiche (immediata)
        linee.clear();
        punti_disegnati_circle.clear();
        punti_disegnati.clear();
        labels.clear();
        punti_media.clear();
        LOG_PUNTI.getChildren().clear();
    }



    @FXML
    public void draw() throws InterruptedException {
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

            l.setEndX(s.get((i+1)%s.size()).getX()); // % per collegare l' ultimo al primo
            l.setEndY(s.get((i+1)%s.size()).getY());
            l.setStroke(Color.BLACK);
            linee.add(l);
            root.getChildren().add(l);
        }


        for (int i = 0; i < linee.size(); i++) {
            Label label = getLabel(i);
            label.setTextFill(Color.BLACK);


            labels.add(label);
            root.getChildren().add(label);
            label.toFront();

        }

        for (Line l : linee){
            l.toFront();
        }
        eseguiAnimazioneDisegno();
    }


    @NotNull
    private Label getLabel(int i) {

        Punto punto1 = new Punto(linee.get(i).getStartX(),linee.get(i).getStartY());
        Punto punto2 = new Punto(linee.get(i).getEndX(),linee.get(i).getEndY());

        double lunghezza = punto1.GetDistance(punto2);

        double media_x = (punto1.getX() + punto2.getX())/2;
        double media_y = (punto1.getY() + punto2.getY())/2;
        Punto puntoMedia = new Punto(media_x,media_y);
        getCoords(puntoMedia);

        Label label = new Label(String.format("%.2f",lunghezza));


        label.setLayoutX(media_x);
        label.setLayoutY(media_y);

        return label;
    }
}
