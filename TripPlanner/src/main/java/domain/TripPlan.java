package main.java.domain;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Scanner;

import main.java.facade.UIData;
import main.java.mgr.Manageable;

public class TripPlan implements Manageable, UIData {
    // 일정 제목, 날짜, 여행지, 설명, 사진, 예약 숙소, 같이 여행하는 친구(유저) 정보
    String title;
    String date;
    String time;
    String location;
    ArrayList<User> friendsList = new ArrayList<>();
    String review; // 일정 리뷰는 잠시 String으로 변경해뒀습니다.(기존: ArrayList<PlanReview> reviews)
    String colorRGB;

    public static final int PLAN_TITLE = 0;
    public static final int PLAN_DATE = 1;
    public static final int PLAN_TIME = 2;
    public static final int PLAN_LOC = 3;
    public static final int PLAN_REVIEW = 4;
    public static final int PLAN_COLOR = 5;
    
    // 정보 저장
    @Override
    public void read(Scanner scan) {
        this.title = replaceSpace(scan.next());
        this.date = scan.next();
        this.time = scan.next();
        this.location = replaceSpace(scan.next());
        this.review=replaceSpace(scan.next());
        this.colorRGB=scan.next();
        changeNUll();
    }

    private void changeNUll(){
        if(title.equals("null"))title=null;
        if(date.equals("null"))date=null;
        if(time.equals("null"))time=null;
        if(location.equals("null"))location=null;
        if(review.equals("null"))review=null;
    }

    private String replaceSpace(String s){
        return s.replaceAll("_"," ");
    }

    // 디버깅용 화면 출력
    @Override
    public void print() {
        System.out.printf("[%s] 날짜:%s 시간:%s 장소:%s\n", title, date, time, location);
    }

    @Override
    public boolean matches(String kwd) {
        if(title.contains(kwd))return true;
        if(kwd.equals(date))return true;
        if(kwd.equals(time))return true;
        if(location.contains(kwd))return true;
        return false;
    }

    @Override
    public void set(Object[] uidata) {
        title = (String) uidata[PLAN_TITLE];
        date = (String) uidata[PLAN_DATE];
        time = (String) uidata[PLAN_TIME];
        location = (String) uidata[PLAN_LOC];
        review = (String) uidata[PLAN_REVIEW];
        colorRGB = (String) uidata[PLAN_COLOR];
    }

    @Override
    public String[] getUidata() {
        String[] uidata = { title, date, time, location, review, colorRGB };
        return uidata;
    }

    // ************Trip리스트에 띄울 위치정보, 기간정보 얻기위한 메소드********8 */
    public String getDateDetail() {
        String[] tArray = time.split(":");
        String[] dArray = date.split("/");
        String result = "";

        if(dArray.length==3){
            for(int i=0;i<3;i++){
                result+=makeTwoChar(dArray[i]);
            }
        }
        else{
            result+="??????";
        }

        if(tArray.length==2){
            for(int i=0;i<2;i++){
                result+=makeTwoChar(tArray[i]);
            }
        }
        else{
            result+="????";
        }
        
        return result;
    }
    private String makeTwoChar(String word){
        int len=word.length();
        switch(len){
            case 1:word="0"+word; break;
            case 2:break;
            default:word="??";break;
        }
        return word;
    }
    public String getLocation() {
        return location;
    }
    public String getTitle() {
        return title;
    }
    public String getDate() {
        return date;
    }
    public String getTime() {
        return time;
    }
    public String getReview() {
        return review;
    }
    public String getColorRGB(){
        return colorRGB;
    }

}
