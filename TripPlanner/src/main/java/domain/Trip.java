package main.java.domain;

import java.util.List;
import java.util.Scanner;

import main.java.facade.UIData;
import main.java.mgr.Manageable;
import main.java.mgr.Manager;

public class Trip implements Manageable,UIData {
    String title;
    String imagePath;
    Manager<TripPlan> planMgr=new Manager<>();

    public static int TRIP_TITLE=0;
    public static int TRIP_IMG=1;

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
    
}
