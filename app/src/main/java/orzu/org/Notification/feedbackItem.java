package orzu.org.Notification;

public class feedbackItem implements Literature {
    private int image;
    private String name, count, feed_img, feed_desc;
    public feedbackItem(int image, String name, String count, String feed_img, String feed_desc) {
        this.image = image;
        this.name = name;
        this.count = count;
        this.feed_img = feed_img;
        this.feed_desc = feed_desc;
    }
    public int getImage() {
        return image;
    }
    public void setImage(int image) {
        this.image = image;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCount() {
        return count;
    }
    public void setCount(String count) {
        this.count = count;
    }
    public String getFeed_img() {
        return feed_img;
    }
    public void setFeed_img(String feed_img) {
        this.feed_img = feed_img;
    }
    public String getFeed_desc() {
        return feed_desc;
    }
    public void setFeed_desc(String feed_desc) {
        this.feed_desc = feed_desc;
    }
    @Override
    public int getType() {
        return Literature.TYPE_MAGAZINE;
    }
}
