package main.java.domain;
import java.util.Scanner;

import main.java.mgr.Manageable;

public class TripReview implements Manageable{
    //여행지(필수), 작성자, 별점(필수), 사진(선택), 여행경비(선택), 후기(텍스트, 선택)
    TripLocation tripLocation;
    User reviewer;
    float rating; // min:0 , max:5
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