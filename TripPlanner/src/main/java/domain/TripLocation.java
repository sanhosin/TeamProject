package main.java.domain;

import java.util.ArrayList;
import java.util.Scanner;

import main.java.enums.*;
import main.java.facade.UIData;
import main.java.mgr.Manageable;

public class TripLocation implements Manageable,UIData {
    // 국가명, 지역명, 카테고리, 맛집정보, 여행지후기, 대표 관광지/행사정보, 여행지 날씨, 설명, 사진
    String country;
    String region; //key
    ArrayList<CategoryRef> categoryList = new ArrayList<>();
    ArrayList<Restaurant> restaurantList = new ArrayList<>();
    ArrayList<TripReview> reviewList = new ArrayList<>();
    ArrayList<Festival> festivalList = new ArrayList<>();
    String weather;
    String description;
    ArrayList<String> photoPathList = new ArrayList<>();

    public static final int LOC_COUNTY=0;
    public static final int LOC_REGION=1;
    public static final int LOC_CATEGORIES=2;
    public static final int LOC_RESTAURANTS=3;
    public static final int LOC_REVIEWS=4;
    public static final int LOC_FESTIVALS=5;
    public static final int LOC_WEATHER=6;
    public static final int LOC_DESCRIPTION=7;
    public static final int LOC_PHOTO=8;

    @Override
    public void set(Object[] uidata) {
        
    }

    @Override
    public Object[] getUidata() {
        Object[] uidata={country,region,categoryList,restaurantList,reviewList,festivalList,weather,description,photoPathList};
        return uidata;
    }

    @Override
    public void read(Scanner scan) {
        country = scan.next();
        region = scan.next();
        // 카테고리 입력부
        while (true) {
            String categoryName = scan.next();
            if (categoryName.equals("0"))
                break;

            CategoryRef cRef = new CategoryRef(categoryName);
            if (cRef.getCategory() == null) {
                System.out.println("카테고리 입력 오류: " + categoryName);
            } else {
                categoryList.add(cRef);
            }
        }
        // restaurant 입력부: 몇개 입력받을건지 숫자 선입력받아야함
        int resNum = scan.nextInt();
        for (int i = 0; i < resNum; i++) {
            Restaurant res = new Restaurant();
            res.read(scan);
            restaurantList.add(res);
        }
        // Festival 입력부
        int fstNum = scan.nextInt();
        for (int i = 0; i < fstNum; i++) {
            Festival fst = new Festival();
            fst.read(scan);
            festivalList.add(fst);
        }

        weather = scan.next();
        description = scan.next();
        // 사진 입력부: "경로1" "경로2" ... 0
        while (true) {
            String imagePath = scan.next();
            if (imagePath.equals("0"))
                break;
            photoPathList.add(imagePath);
        }
    }

    @Override
    public void print() {
        System.out.printf("여행지: %-8s %-8s | ", country, region); 
        for (CategoryRef c : categoryList) {
            System.out.printf("%-8s", c.getCategory().getMessage()); 
        }
        System.out.printf("\n");
    }

    @Override
    public String toString(){
        return region;
    }

    @Override
    public boolean matches(String kwd) {
        if (country.equals(kwd))
            return true;
        if (region.equals(kwd))
            return true;
        for (CategoryRef c : categoryList) {
            if (c.equals(kwd))
                return true;
        }
        return false;
    }

    class Restaurant extends TripLocationDetails {
        // 식당명, 위치, 평점, 대표 메뉴, 사진
        void read(Scanner scan) {
            super.read(scan);
        }
    }

    class Festival extends TripLocationDetails {
        // 행사명, 위치, 평점, 비용, 설명, 사진
        int cost;

        void read(Scanner scan) {
            super.read(scan);
            cost = scan.nextInt();
        }
    }

}
