package cx.com.mvp.bean;

/**
 * Created by xinchang on 2017/7/14.
 */

public class People {
    public int icon;
    public String like;
    public String style;

    public People(int icon, String like, String style) {
        this.icon = icon;
        this.like = like;
        this.style = style;
    }

    public int getIcon() {
        return icon;
    }

    public String getLike() {
        return like;
    }

    public String getStyle() {
        return style;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    @Override
    public String toString() {
        return "Girl{" +
                "icon=" + icon +
                ", like='" + like + '\'' +
                ", style='" + style + '\'' +
                '}';
    }
}
