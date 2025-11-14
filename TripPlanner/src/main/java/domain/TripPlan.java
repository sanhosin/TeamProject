package main.java.domain;
import java.util.ArrayList;
import java.util.Scanner;
import main.java.mgr.Manageable;

public class TripPlan implements Manageable{
    // 일정 제목, 날짜, 여행지, 설명, 사진, 예약 숙소, 같이 여행하는 친구(유저) 정보
    String title;
    String date;
    TripLocation location;
    String photoPath;
    String accommodationName;
    ArrayList<User> friendsList = new ArrayList<>();
    ArrayList<PlanReview> reviews=new ArrayList<>();

    
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
