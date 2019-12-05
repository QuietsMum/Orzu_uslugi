package orzu.org;

public class Bonuses {
    String name,description,date,logo,percentage;
    int logos;

    public Bonuses(String name, String description, String date,String percentage, String logo) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.logo = logo;
        this.percentage = percentage;
    }

    public Bonuses(String name, String description, String date,String percentage, int logos) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.logos = logos;
        this.percentage = percentage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public int getLogos() {
        return logos;
    }

    public void setLogos(int logos) {
        this.logos = logos;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }
}
