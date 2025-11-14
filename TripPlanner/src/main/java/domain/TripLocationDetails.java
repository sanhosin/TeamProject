package main.java.domain;

import java.util.ArrayList;
import java.util.Scanner;

public class TripLocationDetails {
    String name;
    String location;
    float rating; // min:0 , max:5
    String description;
    ArrayList<String> photoPathList = new ArrayList<>();

    void read(Scanner scan) {
        name = scan.next();
        location = scan.next();
        rating = scan.nextFloat();
        description = scan.next();
        while (true) {
            String imagePath = scan.next();
            if (imagePath.equals("0"))
                break;
            photoPathList.add(imagePath);
        }
    }
}
