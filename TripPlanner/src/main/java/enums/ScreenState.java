package main.java.enums;
public enum ScreenState {
    LOGIN_SCREEN("로그인 화면"),
    MAIN_SCREEN("메인 화면"),
    TRIP_LOCATION_SEARCH_SCREEN("여행지 검색 화면"),
    SCHEDULE_MANAGEMENT_SCREEN("일정관리 화면");

    private final String message;

    private ScreenState(String message){
        this.message=message;
    }

    public String getMessage(){
        return message;
    }
}
