package orzu.org;

public class BonusList {
    String idUser, date, value, reason;

    public BonusList(String idUser, String date, String value, String reason) {
        this.idUser = idUser;
        this.date = date;
        this.value = value;
        this.reason = reason;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
