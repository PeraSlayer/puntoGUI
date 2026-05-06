package model;

import java.io.Serializable;

public class Punto implements Comparable<Punto>, Serializable {

    double x,y;

    public Punto(double x, double y){
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }
    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }


    public double GetDistance(Punto p){
        return Math.sqrt(Math.pow(x - p.getX(), 2) + Math.pow(y - p.getY(), 2));
    }


    @Override
    public int compareTo(Punto p) {
        if (this.x != p.x) {
            return Double.compare(this.x, p.x);
        }
        return Double.compare(this.y, p.y);
    }
}
