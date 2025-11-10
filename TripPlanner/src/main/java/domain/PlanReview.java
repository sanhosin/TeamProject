package main.java.domain;
import java.util.Scanner;

import javax.swing.ImageIcon;

import main.java.mgr.Manageable;

public class PlanReview implements Manageable {
    TripPlan targetPlan;
    float rating;
    ImageIcon photo;
    int travelCost;
    String review;

    @Override
    public void read(Scanner scan) {
        
    }
    @Override
    public void print() {
        
    }
    @Override
    public void matches(String kwd) {
        
    }
}
