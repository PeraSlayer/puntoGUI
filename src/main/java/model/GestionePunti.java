package model;


import java.util.ArrayList;
import java.util.Comparator;

public class GestionePunti implements Comparator<Punto> {

    private ArrayList<Punto> puntos = new ArrayList<>();
    private  Punto baricentro;

    public GestionePunti(ArrayList<Punto> puntos) {
        this.puntos = puntos;
    }

    public Punto calc_baricentro(ArrayList<Punto> lista) {
        double sumX = 0;
        double sumY = 0;
        for (Punto p : lista) {
            sumX += p.getX();
            sumY += p.getY();
        }
        // Restituisce un nuovo punto per evitare di modificare gli originali

        return new Punto(sumX / lista.size(), sumY / lista.size());
    }



    @Override
    public int compare(Punto p1, Punto p2) {

        double angle1 = Math.atan2(p1.getY() - baricentro.getY(), p1.getX() - baricentro.getX());
        double angle2 = Math.atan2(p2.getY() - baricentro.getY(), p2.getX() - baricentro.getX());

        return Double.compare(angle1, angle2);
    }



    public ArrayList<Punto> star_shaping(ArrayList<Punto> punti) {
        this.baricentro = calc_baricentro(punti);
        ArrayList<Punto> star_shaper = punti;
        star_shaper.sort(this);
        return star_shaper;

    }


}
