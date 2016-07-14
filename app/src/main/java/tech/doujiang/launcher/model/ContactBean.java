package tech.doujiang.launcher.model;

/**
 * Created by 豆浆 on 2016-07-12.
 */
public class ContactBean {
    private int contactId;
    private String displayName;
    private String phoneNum;
    private String sortKey;
    private Long photoId;
    private String lookUpKey;
    private int selectId = 0;
    private String formattedNumber;
    private String pinYin;

    public int getContactId() {
        return contactId;
    }
    public void setContactId(int contactId) {
        this.contactId = contactId;
    }
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    public String getPhoneNum() {
        return phoneNum;
    }
    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
    public String getSortKey() {
        return sortKey;
    }

    public Long getPhotoId() {
        return photoId;
    }
    public void setPhotoId(Long photoId) {
        this.photoId = photoId;
    }
    public String getLookUpKey() {
        return lookUpKey;
    }
    public void setLookUpKey(String lookUpKey) {
        this.lookUpKey = lookUpKey;
    }
    public int getSelectId() {
        return selectId;
    }
    public void setSelectId(int selectId) {
        this.selectId = selectId;
    }
    public String getFormattedNumber() {
        return formattedNumber;
    }
    public void setFormattedNumber(String formattedNumber) {
        this.formattedNumber = formattedNumber;
    }
    public String getPinYin() {
        return pinYin;
    }
    public void setPinYin(String pinYin) {
        this.pinYin = pinYin;
    }
    public void setSortkey(String sortKey) {
        this.sortKey = sortKey;

    }

}
