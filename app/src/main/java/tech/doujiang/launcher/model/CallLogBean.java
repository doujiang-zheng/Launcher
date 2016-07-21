package tech.doujiang.launcher.model;

/**
 * Created by 豆浆 on 2016-07-12.
 */
public class CallLogBean {
    private int id;
    private String name;
    private String number;
    private String date;
    private int type; // 1. 接入(INCOMING_TYPE) 2. 打出(OUTGOING_TYPE) 3. 未接(MISSED_TYPE)
    private int duration; // 通话时长
    private boolean is_read;

    public boolean is_read() {
        return is_read;
    }

    public void setIs_read(boolean is_read) {
        this.is_read = is_read;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

   public int getDuration() {
       return duration;
   }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public CallLogBean() {
    }

}
