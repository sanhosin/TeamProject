package main.java.domain;

import java.util.Scanner;

import main.java.mgr.Factory;
import main.java.mgr.Manager;

public class TestDataManage {
    Manager<TripLocation> locMgr=new Manager<>();
    Manager<User> userMgr=new Manager<>();

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
}
