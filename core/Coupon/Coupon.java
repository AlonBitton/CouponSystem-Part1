package Coupon;

import java.time.LocalDate;

import Category.Category;

public class Coupon {

    private int id;
    private int Companies_ID;
    private Category category;
    private String title;
    private String descripton;
    private String image;
    private LocalDate Start_Date;
    private LocalDate End_Date;
    private int amoumt;
    private double price;
    private int plusDays;

    public Coupon() {

    }

    public Coupon(int id, int companies_ID, Category category, String title, String descripton, String image,
            int amoumt, double price, int plusDays) {
        this.id = id;
        this.plusDays = plusDays;
        Companies_ID = companies_ID;
        this.category = category;
        this.title = title;
        this.descripton = descripton;
        this.image = image;
        this.amoumt = amoumt;
        this.price = price;
    }

    public Coupon(int id, int companies_ID, Category category, String title, String descripton, String image,
            LocalDate start_Date, LocalDate end_Date, int amoumt, double price) {
        this.id = id;
        Companies_ID = companies_ID;
        this.category = category;
        this.title = title;
        this.descripton = descripton;
        this.image = image;
        Start_Date = start_Date;
        End_Date = end_Date;
        this.amoumt = amoumt;
        this.price = price;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[Coupon:  ");
        stringBuilder.append("id: ").append(this.id).append("\t");
        stringBuilder.append("CompanyID: ").append(Companies_ID).append("\t");
        stringBuilder.append("CategoryID: ").append(category).append("\n");
        stringBuilder.append("Title: ").append(title).append("\t");
        stringBuilder.append("Description: ").append(descripton).append("\t");
        stringBuilder.append("Image: ").append(image).append("\n");
        stringBuilder.append("Start Date: ").append(Start_Date).append("\t");
        stringBuilder.append("End Date: ").append(End_Date).append("\t");
        stringBuilder.append("Amount: ").append(amoumt).append("\t");
        stringBuilder.append("Price: ").append(price).append(".]\n" + "\n");
        return stringBuilder.toString();

    }

    public LocalDate getStart_Date() {
        return Start_Date;
    }

    public void setStart_Date(LocalDate start_Date) {
        Start_Date = start_Date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCompanies_ID() {
        return Companies_ID;
    }

    public void setCompanies_ID(int companies_ID) {
        Companies_ID = companies_ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescripton() {
        return descripton;
    }

    public void setDescripton(String descripton) {
        this.descripton = descripton;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public LocalDate getEnd_Date() {
        return End_Date;
    }

    public void setEnd_Date(LocalDate end_Date) {
        End_Date = end_Date;
    }

    public int getAmoumt() {
        return amoumt;
    }

    public void setAmoumt(int amoumt) {
        this.amoumt = amoumt;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getPlusDays() {
        return plusDays;
    }

}
