package main.java.mgr;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Manager<T extends Manageable> {
    public ArrayList<T> mList=new ArrayList<>();

    // main.java.mgr.Manager.java 파일 내, readAll 메소드 수정
    public void readAll(String filename, Factory<T> fac){
        Scanner filein = null;
        filein = openFile("src/"+filename);
        if(filein == null)
        {
            filein = openFile("TripPlanner/src/"+filename);
        }
        if(filein == null)
        {
            filein = openFile("TeamProject/TripPlanner/src/"+filename);
        }
        // 파일 입출력 시 예외 처리 및 널 체크 로직 추가
        if (filein == null) {
            System.err.println("경고: 데이터 파일 '" + filename + "'을(를) 찾을 수 없습니다. 데이터 로드를 건너뜁니다.");
            return; // NullPointerException 발생 없이 안전하게 종료
        }

        T m=null;
        while(filein.hasNext()){
            m=fac.create();
            m.read(filein);
            mList.add(m);
        }
        filein.close();
    }

    public void printAll(){
        for(T m:mList){
            m.print();
        }
    }

    public T find(String kwd){
        for(T m:mList){
            if(m.matches(kwd))return m;
        }
        return null;
    }

    public List<T> findAll(String kwd){
        List<T> result=new ArrayList<>();
        for(T m:mList){
            if(m.matches(kwd))result.add(m);
        }
        return result;
    }
    public void search(Scanner keyScanner){
        String kwd=null;
        while(true){
            System.out.print(">>");
            kwd=keyScanner.next();
            if(kwd.equals("end"))break;
            for(T m:mList){
                if(m.matches(kwd))m.print();
            }
        }
    }

    public Scanner openFile(String filePath){
        Scanner filein=null;
        try{
            filein=new Scanner(new File(filePath));
        }
        catch(FileNotFoundException e){
            System.out.println("파일 경로 잘못됨: "+filePath);
        }
        return filein;
    }
}
