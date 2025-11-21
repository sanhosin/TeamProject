package main.java.domain;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

import main.java.facade.UIData;
import main.java.mgr.Manageable;

public class User implements Manageable,UIData{
    //아이디, 비밀번호, 닉네임, 여행일정리스트, 친구목록
    String id;
    String password;
    String nickname;
    ArrayList<Trip> tripList=new ArrayList<>();
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
        if(id.equals(kwd))return true;
        if(nickname.equals(kwd))return true;
        return false;
    }

    public boolean isCorrect(String id,String pwd){
        return (this.id.equals(id)&&this.password.equals(pwd));
    }

    public void addTrip(Object[] uidata){
        Trip t=new Trip();
        t.set(uidata);
        tripList.add(t);
    }

    public List<Trip> getTripList() {
        return tripList;
    }

    @Override
    public void set(Object[] uidata) {
        id=(String)uidata[0];
        password=(String)uidata[1];
        nickname=(String)uidata[2];
    }

    @Override
    public Object[] getUidata() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUidata'");
    }
}
