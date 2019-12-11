package orzu.org;

public class Bonuses {
    private String name, logo, percentage;
    private int logos;

    Bonuses(String name, String percentage, String logo) {
        this.name = name;
        this.logo = logo;
        this.percentage = percentage;
    }

    Bonuses(String name, String percentage, int logos) {
        this.name = name;
        this.logos = logos;
        this.percentage = percentage;
    }

    Bonuses(String name, String percentage) {
        this.name = name;
        this.percentage = percentage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    int getLogos() {
        return logos;
    }

    public void setLogos(int logos) {
        this.logos = logos;
    }

    String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }
}
