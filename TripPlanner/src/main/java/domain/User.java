package main.java.domain;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

import main.java.facade.UIData;
import main.java.mgr.Factory;
import main.java.mgr.Manageable;
import main.java.mgr.Manager;

public class User implements Manageable,UIData{
    //아이디, 비밀번호, 닉네임, 여행일정리스트, 친구목록
    String id;
    String password;
    String nickname;
    Manager<Trip> tripMgr=new Manager<>();

    ArrayList<User> friedsList = new ArrayList<>(); // 친구 목록 (오타 유지)
    ArrayList<User> receivedRequests = new ArrayList<>();

    @Override
    public void read(Scanner scan) {
        id=scan.next();
        password=scan.next();
        nickname=scan.next();
    }
    public void readTrip(String filename){
        tripMgr.readAll(filename, new Factory<Trip>() {
            public Trip create(){
                return new Trip();
            }
        });
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
        tripMgr.mList.add(t);
    }

    public List<Trip> getTripList() {
        return tripMgr.mList;
    }

    public void deleteTrip(Trip t) {
        tripMgr.mList.remove(t);  // 정상적으로 삭제
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

    public String getID() {
        return id;
    }

    public String getNick() {
        return nickname;
    }
    // 닉네임 수정하기
    public void setNick(String nickname) {
        this.nickname = nickname;
    }

    public String getPwd() {
        return password;
    }
    // 비번 수정하기
    public void setPwd(String password) {
        this.password = password;
    }

    public ArrayList<User> getFriendsList() {
        return friedsList;
    }

    // 2. 친구 추가하기 (오류 나던 부분 해결)
    public void addFriend(User u) {
        if (!friedsList.contains(u)) {
            friedsList.add(u);
        }
    }

    // 3. 친구 삭제하기
    public void removeFriend(User u) {
        friedsList.remove(u);
    }

    // --- [친구 요청(Request) 관련 기능] ---

    // 4. 받은 요청 목록 가져오기
    public ArrayList<User> getReceivedRequests() {
        return receivedRequests;
    }

    // 5. 요청 받기
    public void receiveFriendRequest(User sender) {
        if (!friedsList.contains(sender) && !receivedRequests.contains(sender)) {
            receivedRequests.add(sender);
        }
    }

    // 6. 요청 수락 (내 친구로 추가 + 상대방 친구로 추가 + 대기열 삭제)
    public void acceptFriendRequest(User sender) {
        if (receivedRequests.contains(sender)) {
            receivedRequests.remove(sender);
            this.addFriend(sender);
            sender.addFriend(this);
        }
    }

    // 7. 요청 거절
    public void rejectFriendRequest(User sender) {
        receivedRequests.remove(sender);
    }

    // 8. 이미 요청했는지 확인
    public boolean hasReceivedRequestFrom(User sender) {
        return receivedRequests.contains(sender);
    }
}
