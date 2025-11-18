package main.java.domain;

import java.util.ArrayList;
import java.util.Scanner;

import main.java.mgr.Factory;
import main.java.mgr.Manager;

public class TestDataManage {
    public static User loginedUser=new User(); //임시로 설정해뒀습니다. 나중에는 실제 로그인한 유저로 변경할게요
    public static Manager<TripLocation> locMgr=new Manager<>();
    public static Manager<User> userMgr=new Manager<>();

    public TestDataManage(){
        readAllLocation("TripPlanner/src/tripLocation.txt");
        readAllUser("TripPlanner/src/user.txt");
    }
    //*********************입력데이터 콘솔창 입출력*************** */ */
    public void readAllLocation(String fileName){
        locMgr.readAll(fileName,new Factory<TripLocation>(){
            public TripLocation create(){
                return new TripLocation();
            }
        });
    }

    public void readAllUser(String fileName){
        userMgr.readAll(fileName,new Factory<User>(){
            public User create(){
                return new User();
            }
        });
    }

    public void printAllLocation(){
        locMgr.printAll();
    }

    public void printAllUser(){
        userMgr.printAll();
    }

    public void searchLocation(Scanner keyScanner){
        System.out.println("여행지 검색");
        locMgr.search(keyScanner);
    }

    public void searchUser(Scanner keyScanner){
        System.out.println("유저 검색");
        userMgr.search(keyScanner);
    }

    //*************UI데이터 입력 및 연동******************* */

    public void addPlan(Object[] uidata){
        TripPlan newPlan=new TripPlan();
        newPlan.set(uidata);
        loginedUser.addPlan(newPlan);
    }

    public ArrayList<TripPlan> getPlanList(){
        return loginedUser.getPlanList();
    }

    public TripLocation findLocation(String kwd){
        return locMgr.find(kwd);
    }

    public User findUser(String kwd){
        return userMgr.find(kwd);
    }

    public User isExistUser(String id,String pwd){
        for(User user:userMgr.mList){
            if(user.isCorrect(id, pwd))return user;
        }
        return null;
    }
}
