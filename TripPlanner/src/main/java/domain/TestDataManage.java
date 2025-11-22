package main.java.domain;

import java.util.Scanner;
import java.util.List;

import main.java.mgr.Factory;
import main.java.mgr.Manager;

public class TestDataManage {
    public static User loginedUser=null;
    public static Trip currentTrip=null;
    public static Manager<User> userMgr=new Manager<>();

    public TestDataManage() {
        readAllUser("user.txt");
    }
    //*********************입력데이터 콘솔창 입출력*************** */ */
    

    public void readAllUser(String fileName){
        userMgr.readAll(fileName,new Factory<User>(){
            public User create(){
                return new User();
            }
        });
    }

    public void printAllUser(){
        userMgr.printAll();
    }

    public void searchUser(Scanner keyScanner){
        System.out.println("유저 검색");
        userMgr.search(keyScanner);
    }

    //*************UI데이터 입력 및 연동******************* */


    public User findUser(String kwd){
        return userMgr.find(kwd);
    }

    public User isExistUser(String id,String pwd){
        for(User user:userMgr.mList){
            if(user.isCorrect(id, pwd))return user;
        }
        return null;
    }
    
    public void setLoginedUser(User loginedUser2) {
        loginedUser=loginedUser2;
    }

    public User getLoginedUser(){
        return loginedUser;
    }

    public void addTrip(Object[] uidata) {
        loginedUser.addTrip(uidata);
    }

    public List<Trip> getTripList(){
        return loginedUser.getTripList();
    }


    public void setCurrentTrip(Trip t) {
        currentTrip=t;
    }


    public void addPlan(Object[] uiData) {
        currentTrip.addPlan(uiData);
        currentTrip.setDetails();
    }

    public void modifyPlan(){
        currentTrip.setDetails();
    }

    public List<TripPlan> getPlanList(){
        return currentTrip.getPlanList();
    }


    public String getCurrentTripTitle() {
        return currentTrip.getTitle();
    }


    public boolean isExistUserID(String id){
        if(userMgr.find(id)!=null)return true;
        return false;
    }

    public void addUser(Object[] uidata) {
        User user=new User();
        user.set(uidata);
        userMgr.mList.add(user);
    }


    public void deletePlan(TripPlan tp) {
        currentTrip.planMgr.mList.remove(tp);
        currentTrip.setDetails();
    }
    
}
