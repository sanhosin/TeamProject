package main.java.domain;
import java.util.ArrayList;
import java.util.Scanner;

import main.java.mgr.Manageable;

public class User implements Manageable{
    //아이디, 비밀번호, 닉네임, 여행일정리스트, 친구목록
    String id;
    String password;
    String nickname;
    ArrayList<TripPlan> tripList=new ArrayList<>();
    ArrayList<User> friedsList=new ArrayList<>();

    
    @Override
    public void read(Scanner scan) {
        
    }
    @Override
    public void print() {
        
    }
    @Override
    public void matches(String kwd) {
        
    }
}
