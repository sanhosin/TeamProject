package main.java.enums;

public class CategoryRef {
    private Category category;
    public CategoryRef(String categoryName){
        this.category=parseCategory(categoryName);
    }

    public Category getCategory(){
        return category;
    }

    public boolean equals(String categoryName){
        if(category.getMessage().equals(categoryName))return true;
        return false;
    }

    //카테고리 이름에서 카테고리값으로 변환, 이름이 잘못되었을 경우 null반환
    public Category parseCategory(String categoryName){
        for(Category c:Category.values()){
            if(c.getMessage().equals(categoryName)){
                return c;
            }
        }
        return null;
    }
}
