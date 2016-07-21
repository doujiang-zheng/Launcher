package tech.doujiang.launcher.model;

/**
 * Created by 豆浆 on 2016-07-12.
 */
public class MessageBean {

    private int id;
    private String name;
    private String date;
    private String text;
    private int type;
    private int layoutID;
    private boolean is_read;

    public MessageBean() {
    }

    public MessageBean(String name, String date, String text, int layoutID) {
        super();
        this.name = name;
        this.date = date;
        this.text = text;
        this.layoutID = layoutID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getLayoutID() {
        return layoutID;
    }

    public void setLayoutID(int layoutID) {
        this.layoutID = layoutID;
    }

    public boolean isIs_read() {
        return is_read;
    }

    public void setIs_read(boolean is_read) {
        this.is_read = is_read;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
