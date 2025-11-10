package main.java.domain;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.ImageIcon;

import main.java.enums.Category;
import main.java.mgr.Manageable;

public class TripLocation implements Manageable {
    // 국가명, 지역명, 카테고리, 맛집정보, 여행지후기, 대표 관광지/행사정보, 여행지 날씨, 설명, 사진
    String country;
    String region;
    Category category;
    ArrayList<Restaurant> restaurantList = new ArrayList<>();
    ArrayList<TripReview> reviewList = new ArrayList<>();
    ArrayList<Attraction> attractionList = new ArrayList<>();
    ArrayList<Festival> FestivalList = new ArrayList<>();
    String weather;
    String description;
    ArrayList<ImageIcon> photos = new ArrayList<>();

    
    @Override
    public void read(Scanner scan) {
        
    }
    @Override
    public void print() {
        
    }
    @Override
    public void matches(String kwd) {
        
    }

    class Restaurant {
        // 식당명, 위치, 평점, 대표 메뉴, 사진
        String name;
        String location;
        float rating; // min:0 , max:5
        String signatureMenu;
        ArrayList<ImageIcon> photos = new ArrayList<>();
    }

    class Attraction {
        // 관광지, 위치, 평점, 비용, 설명, 사진
        String name;
        String location;
        float rating; // min:0 , max:5
        int cost;
        String desctiption;
        ArrayList<ImageIcon> photos = new ArrayList<>();
    }

    class Festival {
        // 행사명, 위치, 평점, 비용, 설명, 사진
        String name;
        String location;
        float rating; // min:0 , max:5
        int cost;
        String desctiption;
        ArrayList<ImageIcon> photos = new ArrayList<>();
    }

}
