package com.team60.ournews.module.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Misutesu on 2016/12/29 0029.
 */

public class OtherUser implements Parcelable {
    private long id;
    private String nickName;
    private int sex;
    private String sign;
    private int birthday;
    private String photo;

    public OtherUser() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public int getBirthday() {
        return birthday;
    }

    public void setBirthday(int birthday) {
        this.birthday = birthday;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    protected OtherUser(Parcel in) {
        id = in.readLong();
        nickName = in.readString();
        sex = in.readInt();
        sign = in.readString();
        birthday = in.readInt();
        photo = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(nickName);
        dest.writeInt(sex);
        dest.writeString(sign);
        dest.writeInt(birthday);
        dest.writeString(photo);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OtherUser> CREATOR = new Creator<OtherUser>() {
        @Override
        public OtherUser createFromParcel(Parcel in) {
            return new OtherUser(in);
        }

        @Override
        public OtherUser[] newArray(int size) {
            return new OtherUser[size];
        }
    };
}
