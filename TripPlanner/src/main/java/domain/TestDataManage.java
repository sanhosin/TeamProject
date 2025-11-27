package main.java.domain;

import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import main.java.mgr.Factory;
import main.java.mgr.Manager;
import main.java.GUImain.MainPane;

public class TestDataManage {

    public String rootPath=getRootPath();
    User loginedUser=null;
    Trip currentTrip=null;
    public static Manager<User> userMgr=new Manager<>();

    public TestDataManage() {
        readAllUser("user.txt");
    }
    private String getRootPath(){
        String[] sArray=(new File("").getAbsolutePath()).split("\\\\");
        String root=sArray[sArray.length-1];
        String result="";
        if(root.equals("TeamProject")){
            result+="TripPlanner/src/main/resources/images/";
        }
        if(root.equals("TripPlanner")){
            result+="src/main/resources/images/";
        }
        return result;
    }
    
    //*********************데이터 파일 저장****************/
    public void readData(){
        loginedUser.readTrip(loginedUser.getID()+"_trip.txt");
    }
    public void saveUserData() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("user.txt"))) {
            for(User u:userMgr.mList){
                bw.write(u.getID()+" "+u.getPwd()+" "+u.getNick()+"\n");
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void saveTripData(String userID){

        try(BufferedWriter bw=new BufferedWriter(new FileWriter(userID+"_trip.txt"))){
            for(Trip t:loginedUser.getTripList()){
                bw.write(replaceSpace(t.getTitle())+" "+t.getImagePath()+" "+
                replaceSpace(t.getLocationDetail())+" "+replaceSpace(t.getDateDuration())+"\n");

                List<TripPlan> pList=t.getPlanList();
                bw.write(pList.size()+"\n");

                for(TripPlan tp:pList){
                    bw.write(replaceSpace(tp.getTitle())+" "+replaceSpace(tp.getDate())+
                    " "+replaceSpace(tp.getTime())+" "+replaceSpace(tp.getLocation())+" "+
                    replaceSpace(tp.getReview())+"\n");
                }
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    //read에서 scan.next()읽기때문에 스페이스바를 _로 대체시킴
    private String replaceSpace(String haveSpace){
        if(haveSpace==null)return "null";
        if(haveSpace.equals(""))return "null";
        String result="";
        String[] sArray=haveSpace.split(" ");
        if(sArray.length==1)return haveSpace;
        for(String s:sArray){
            result+=s+"_";
        }
        return result.substring(0,result.length()-1);
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

    public void deleteTrip(Trip t){
        loginedUser.deleteTrip(t);
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
