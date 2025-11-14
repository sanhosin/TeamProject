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
        id=scan.next();
        password=scan.next();
        nickname=scan.next();
    }

    @Override
    public void print() {
        System.out.printf("%s ( %s )\n",nickname,id);
    }
    
    @Override
    public boolean matches(String kwd) {
        if(kwd.equals(id))return true;
        if(kwd.equals(nickname))return true;
        return false;
    }
}
