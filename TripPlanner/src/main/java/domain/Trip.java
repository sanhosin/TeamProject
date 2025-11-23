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
    public Object[] getUidata() {
        Object[] uidata={title,imagePath};
        return uidata;
    }

    @Override
    public void read(Scanner scan) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'read'");
    }

    @Override
    public void print() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'print'");
    }

    @Override
    public boolean matches(String kwd) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'matches'");
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
        if(planMgr.mList.isEmpty()){
            locationDetail="미정";
            dateDuration="미정";
            return;
        }
        // 메인 화면 일정 순으로 정렬 (여행 추가에 일정을 입력 하는 창을 만들었다는 가정하에 만들어서 오류가 발생할 수 있음)
        ArrayList<TripPlan> list = (ArrayList<TripPlan>) planMgr.mList;

        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = 0; j < list.size() - 1 - i; j++) {
                
                String s1 = list.get(j).getDateDetail();
                String s2 = list.get(j + 1).getDateDetail();

                long date1, date2;

                if (s1.contains("?")) date1 = Long.MAX_VALUE;
                else date1 = Long.parseLong(s1);

                if (s2.contains("?")) date2 = Long.MAX_VALUE;
                else date2 = Long.parseLong(s2);

                if (date1 > date2) {
                    TripPlan temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                }
            }
        }



        int startOfDate=999999;
        int endOfDate=0;
        String locations="";
        int maxLocationNum=planMgr.mList.size();

        for(TripPlan tp:planMgr.mList){

            if(maxLocationNum>1){
                locations+=tp.getLocation()+",";
                maxLocationNum--;
            }
            else if(maxLocationNum==1){
                locations+=tp.getLocation();
                maxLocationNum--;
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



}
