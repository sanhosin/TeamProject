package main.java.GUImain;

public class DestinationItem {
    public String name;
    public String country;
    public String description;
    public double rating;
    public String imagePath;
    public String startDate;
    public String endDate;
    public String[] tags; // 마지막 인자는 태그 배열입니다.

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