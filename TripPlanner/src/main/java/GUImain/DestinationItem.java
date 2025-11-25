package main.java.GUImain;

// 이 파일은 여행지 정보를 저장하는 구조체 역할을 합니다.
public class DestinationItem {
    public String name;
    public String country;
    public String description;
    public double rating;
    public String imagePath;
    public String startDate;
    public String endDate;
    public String[] tags;

    public DestinationItem(String name, String country, String description, double rating, String imagePath, String startDate, String endDate, String[] tags) {
        this.name = name;
        this.country = country;
        this.description = description;
        this.rating = rating;
        this.imagePath = imagePath;
        this.startDate = startDate;
        this.endDate = endDate;
        this.tags = tags;
    }
}