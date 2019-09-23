package orzu.org.chat;

import android.graphics.Bitmap;

public class chatItems {
    String Name,chat,time,notification,id;
    Bitmap img;

    public chatItems() {
    }

    public chatItems(String name, String chat, String time, String notification, Bitmap img) {
        Name = name;
        this.chat = chat;
        this.time = time;
        this.notification = notification;
        this.img = img;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }
}
