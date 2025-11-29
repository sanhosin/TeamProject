package main.java.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import main.java.facade.UIData;
import main.java.mgr.Manageable;
import main.java.mgr.Manager;

public class Trip implements Manageable,UIData {
    String title;
    String imagePath;
    String locationDetail;
    String dateDuration;
    Manager<TripPlan> planMgr=new Manager<>();

    public static int TRIP_TITLE=0;
    public static int TRIP_IMG=1;

    public String getLocationDetail(){
        return locationDetail;
    }
    public String getDateDuration(){
        return dateDuration;
    }

    @Override
    public void set(Object[] uidata) {
        title=(String)uidata[TRIP_TITLE];
        imagePath=(String)uidata[TRIP_IMG];
    }

    @Override
    public String[] getUidata() {
        String[] uidata={title,imagePath};
        return uidata;
    }

    @Override
    public void read(Scanner scan) {
        title=replaceSpace(scan.next());
        imagePath=scan.next();
        locationDetail=scan.next();
        dateDuration=scan.next();
        changeNUll();

        int planNum=scan.nextInt();
        for(int i=0;i<planNum;i++){
            TripPlan tp=new TripPlan();
            tp.read(scan);
            planMgr.mList.add(tp);
        }
    }

    private void changeNUll(){
        if(title.equals("null"))title=null;
        if(imagePath.equals("null"))imagePath=null;
        if(locationDetail.equals("null"))locationDetail=null;
        if(dateDuration.equals("null"))dateDuration=null;
    }

    private String replaceSpace(String s){
        return s.replaceAll("_"," ");
    }

    @Override
    public void print() {
        // 구현 내용 없음
    }

    @Override
    public boolean matches(String kwd) {
        if(title.contains(kwd))return true;
        if(locationDetail!=null&&locationDetail.contains(kwd))return true;
        //날짜 검색도 가능하도록 수정
        return false;
    }

    public List<TripPlan> findAllPlans(String kwd){
        return planMgr.findAll(kwd);
    }

    public void addPlan(Object[] uiData) {
        TripPlan tp=new TripPlan();
        tp.set(uiData);
        planMgr.mList.add(tp);
    }

    public List<TripPlan> getPlanList() {
        return planMgr.mList;
    }

    public String getTitle() {
        return title;
    }

    /**********TripCard 세부정보 설정****** */
    public void setDetails() {
        ArrayList<TripPlan> list = (ArrayList<TripPlan>) planMgr.mList;

        if(list.isEmpty()){
            locationDetail="미정";
            dateDuration="미정";
            return;
        }

        // 정렬 로직 (순서 기반 정렬)
        for(int i = 0; i < list.size()-1; i ++){
            for(int j = 0; j < list.size()-1-i; j ++){

                String s1 = list.get(j).getDateDetail();
                String s2 = list.get(j + 1).getDateDetail();

                long date1;
                long date2;

                if(s1.contains("?")|| !isAllDigit(s1)){
                    date1 = Long.MAX_VALUE;
                }
                else{
                    date1 = Long.parseLong(s1);
                }

                if(s2.contains("?")||!isAllDigit(s2)){
                    date2 = Long.MAX_VALUE;
                }
                else{
                    date2 = Long.parseLong(s2);
                }

                if(date1 > date2){
                    TripPlan temp = list.get(j);
                    list.set(j, list.get(j+1));
                    list.set(j+1, temp);
                }

            }
        }

        // 위치 및 기간 계산
        int startOfDate=999999;
        int endOfDate=0;
        String locations="";
        int maxLocationNum=planMgr.mList.size();
        int count=3;

        for(TripPlan tp:planMgr.mList){
            String loc=tp.getLocation();

            if (count > 0) {
                if (maxLocationNum > 1 && !locations.contains(loc)) {
                    locations += loc + ",";
                    maxLocationNum--;
                    count--;
                }
                if (maxLocationNum == 1 && !locations.contains(loc)) {
                    locations += loc;
                    maxLocationNum--;
                    count--;
                }
            }
            String date=tp.getDateDetail();
            date=date.substring(0,6);
            if(date.contains("?"))continue;

            if(!isAllDigit(date))continue;
            int intDate=Integer.parseInt(date);

            if(startOfDate>intDate)startOfDate=intDate;
            if(endOfDate<intDate)endOfDate=intDate;
        }
        locationDetail=locations;
        if(endOfDate==0||startOfDate==999999)dateDuration="미정";
        else dateDuration=parseDate(startOfDate)+"~"+parseDate(endOfDate);
    }

    private boolean isAllDigit(String date){
        for(int i=0;i<date.length();i++){
            char c=date.charAt(i);
            if(!Character.isDigit(c))return false;
        }
        return true;
    }

    private String parseDate(int intDate){
        String target=intDate+"";
        int targetLen=target.length();
        switch (targetLen) {
            case 5:
                target="0"+target;
                break;
            case 4:
                target="00"+target;
            default:
                break;
        }

        StringBuffer buff=new StringBuffer(target);
        buff.insert(4,'/');
        buff.insert(2,'/');
        target=buff.toString();
        return target;
    }

    public String getImagePath() {
        return imagePath;
    }
}