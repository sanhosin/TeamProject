package main.java.domain;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Scanner;

import main.java.facade.UIData;
import main.java.mgr.Manageable;

public class TripPlan implements Manageable,UIData{
    // 일정 제목, 날짜, 여행지, 설명, 사진, 예약 숙소, 같이 여행하는 친구(유저) 정보
    int key;
    String title;
    String date;
    String time;
    String location;
    ArrayList<User> friendsList = new ArrayList<>();
    String review; //일정 리뷰는 잠시 String으로 변경해뒀습니다.(기존: ArrayList<PlanReview> reviews)
    Color color;

    public static final int PLAN_TITLE=0;
    public static final int PLAN_DATE=1;
    public static final int PLAN_TIME=2;
    public static final int PLAN_LOC=3;
    public static final int PLAN_REVIEW=4;
    public static final int PLAN_COLOR=5;
    
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
    @Override
    public void set(Object[] uidata) {
        title=(String)uidata[PLAN_TITLE];
        date=(String)uidata[PLAN_DATE];
        time=(String)uidata[PLAN_TIME];
        location=(String)uidata[PLAN_LOC];
        review=(String)uidata[PLAN_REVIEW];
        color=(Color)uidata[PLAN_COLOR];
    }
    @Override
    public Object[] getUidata() {
        Object[] uidata={title,date,time,location,review,color};
        return uidata;
    }


    
}
