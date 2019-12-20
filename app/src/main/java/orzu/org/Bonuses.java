package orzu.org;

public class Bonuses {
    private String name, logo, percentage,id,descrip;
    private int logos;

    Bonuses(String id,String name, String percentage, String logo,String descrip) {
        this.name = name;
        this.id = id;
        if(logo.equals("null")){
            this.logo = "images/logoNew.png";
        }else{
            this.logo = logo;
        }
        this.percentage = percentage;
        this.descrip = descrip;
    }

    Bonuses(String id,String name, String percentage, int logos) {
        this.name = name;
        this.id = id;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescrip() {
        return descrip;
    }

    public void setDescrip(String descrip) {
        this.descrip = descrip;
    }
}
