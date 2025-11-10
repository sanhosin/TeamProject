package main.java.domain;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.ImageIcon;

import main.java.mgr.Manageable;

public class TripPlan implements Manageable{
    // 일정 제목, 날짜, 여행지, 설명, 사진, 예약 숙소, 같이 여행하는 친구(유저) 정보
    String title;
    String date;
    ArrayList<TripLocation> locationList = new ArrayList<>();
    ImageIcon photo;
    Accommodation accommodation;
    ArrayList<User> friendsList = new ArrayList<>();

    
    @Override
    public void read(Scanner scan) {
        
    }
    @Override
    public void print() {
        
    }
    @Override
    public void matches(String kwd) {
        
    }

    class Accommodation {
        // 숙소명, 위치, 평점, 사진, 보유객실정보(카테고리 ex: 트윈룸, 스위트룸, 오션뷰 등)
        String name;
        String location;
        float rating; // min:0 , max:5
        ArrayList<ImageIcon> photos = new ArrayList<>();
        ArrayList<Room> roomList = new ArrayList<>();

        class Room {
            String name;
            ArrayList<ImageIcon> photos = new ArrayList<>();
            String description;
        }

    }

}
