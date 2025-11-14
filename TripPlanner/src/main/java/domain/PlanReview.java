package main.java.domain;
import java.util.Scanner;
import main.java.mgr.Manageable;
//

public class PlanReview implements Manageable {
    TripPlan targetPlan;
    float rating;
    String photoPath;
    int travelCost;
    String review;

    @Override
    public void read(Scanner scan) {
        
    }
    @Override
    public void print() {
        
    }
    @Override
    public boolean matches(String kwd) {
        return false;
    }
}
