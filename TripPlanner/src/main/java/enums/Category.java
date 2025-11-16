package main.java.enums;
public enum Category {
    RETREAT("자연/휴양"),
    EXPLORATION("도시/문화탐방"),
    ADVENTURE("체험/액티비티"),
    ROMANCE("감성/로맨틱"),
    EXPEDITION("이색/모험"),
    FLAVORS("음식탐방");

    private final String message;

    Category(String message){
        this.message=message;
    }

    public String getMessage(){
        return message;
    }
}
