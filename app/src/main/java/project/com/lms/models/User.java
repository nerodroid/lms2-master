package project.com.lms.models;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

@IgnoreExtraProperties
public class User implements Parcelable {
    public String name,email,course, note_id,user_id,student_id,degree;
    private @ServerTimestamp Date timestamp;
    public int year;

    public User(String email, String course, int year, String user_id) {
        this.name = name;
        this.email = email;
        this.course = course;
        this.year =year;
        this.user_id = user_id;
        this.degree = degree;
    }

    public User() {

    }

    protected User(Parcel in) {
        name = in.readString();
        email = in.readString();
        course = in.readString();


    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public String getUser_id() {
        return user_id;
    }
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public int getYear() {
        return year;
    }
    public String getDegree() {
        return degree;
    }
    public Date getTimestamp() {
        return timestamp;
    }
    public String getNote_id() {
        return note_id;
    }

    public void setName(String name){ this.name = name; }
    public void setEmail(String email){ this.email = email; }
    public void setYear(int year){ this.year = year; }
    public void setDegree(String degree){ this.degree = degree; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(email);
        parcel.writeString(note_id);
        parcel.writeString(user_id);
    }
}
