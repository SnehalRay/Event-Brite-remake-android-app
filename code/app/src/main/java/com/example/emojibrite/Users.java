package com.example.emojibrite;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Objects;

/**
 * Users class to store the user's information
 */
public class Users implements Parcelable{
    // attributes
    private String homePage;
    private String name;
    private String autoGenImageUri;
    private String uploadedImageUri;
    private String email;
    private String number;
    private String role;
    private String profileUid;

    // set to false by default in constructors
    private boolean enableNotification;
    private boolean enableGeolocationTracking;

    private boolean enableAdmin;
    private String imagePath;

    // notification token
    private String fcmToken;

    /**
     * Users constructor
     */
    public Users() {}

    /**
     * Users constructor with uid
     *
     * @param profileUid unique id of the user
     */
    public Users(String profileUid) {
        this.profileUid = profileUid;
        this.name = null;
        this.email = null;
        this.autoGenImageUri = null;
        this.uploadedImageUri = null;
        this.number = null;
        this.enableNotification = false;
        this.enableGeolocationTracking = false;
        this.homePage = null;
        this.fcmToken = null;

    }

    /**
     * Users constructor with all attributes
     * @param enableAdmin enable admin
     */
    public void setEnableAdmin(boolean enableAdmin) {
        this.enableAdmin = enableAdmin;
    }

    /**
     * Users constructor with all attributes
     * @return enableAdmin enable admin
     */
    public boolean getEnableAdmin() {
        return enableAdmin;
    }

    /**
     * Users constructor with all attributes
     * @param profileUid unique id of the user
     */
    public void setProfileUid(String profileUid) {
        this.profileUid = profileUid;
    }

    /**
     * gets the uploaded image
     * @return uploadedImage : the image uploaded by the user
     */
    public String getUploadedImageUri() {
        return uploadedImageUri;
    }

    /**
     * sets the uploaded image
     * @param uploadedImageUri : the image uploaded by the user
     */
    public void setUploadedImageUri(String uploadedImageUri) {
        this.uploadedImageUri = uploadedImageUri;

    }

    /**
     * gets the home page
     * @return homePage : the home page of the user
     */
    public String getHomePage() {
        return homePage;
    }

    /**
     * sets the home page
     * @param homePage : the home page of the user
     */
    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }
    /**
     * gets the auto generated image
     * @param autoGenImageUri : the image generated by the app
     */
    public void setAutoGenImageUri(String autoGenImageUri) {
        this.autoGenImageUri = autoGenImageUri;

    }

    /**
     * gets the auto generated image
     * @return autoGenImage : the image generated by the app
     */
    public String getAutoGenImageUri() {
        return autoGenImageUri;
    }

    /**
     * gets the user's unique id
     * @return uid : the unique id of the user
     */
    public String getProfileUid() {
        return profileUid;
    }

    /**
     * Gets the name of the user
     * @return name of the user
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the user
     * @param name of the user
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the email of the user
     * @return email of the user
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email of the user
     * @param email of the user
     */
    public void setEmail(String email) {
        this.email = email;
    }


    /**
     * Gets the ImagePath of the user
     * @return ImagePath of the user
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * Sets the ImagePath of the user
     * @param autoGenImage of the user
     */
    public void setImagePath(String autoGenImage) {
        this.imagePath = autoGenImage;
    }

    /**
     * Gets the number of the user
     * @return the number of the user
     */
    public String getNumber() {
        return number;
    }

    /**
     * Sets the number of the user
     * @param number of the user
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * Gets the enableNotification of the user
     * @return the enableNotification of the user
     */
    public boolean getEnableNotification() {
        return enableNotification;
    }

    /**
     * Sets the enableNotification of the user
     * @param enableNotification of the user
     */
    public void setEnableNotification(boolean enableNotification) {
        this.enableNotification = enableNotification;
    }

    /**
     * Gets the enableGeolocationTracking of the user
     * @return the enableGeolocationTracking of the user
     */
    public boolean getEnableGeolocation(){

        return enableGeolocationTracking;
    }

    /**
     * Sets the enableGeolocationTracking of the user
     * @param enableGeolocationTracking of the user
     */
    public void setEnableGeolocation(boolean enableGeolocationTracking) {
        this.enableGeolocationTracking = enableGeolocationTracking;
    }

    /**
     * Gets the role of the user
     * @param role of the user
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Sets the role of the user
     * @return role of the user
     */
    public String getRole() {
        return role;
    }


    /**
     * Gets the FCM token
     * @return a string of the FCM token
     */
    public String getFcmToken() {
        return fcmToken;
    }

    /**
     * Sets the FCM token
     * @param fcmToken a string of the FCM token
     */
    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    /**
     * Parcelable constructor for the Users class
     * @param in the parcel object
     */
    protected Users(Parcel in) {
        //deserialization of the object
        name = in.readString();
        autoGenImageUri = in.readString();
        uploadedImageUri = in.readString();
        email = in.readString();
        number = in.readString();
        role = in.readString();
        profileUid = in.readString();
        homePage = in.readString();
        enableNotification = in.readByte() != 0;
        enableGeolocationTracking = in.readByte() != 0;
        enableAdmin = in.readByte() != 0;
        fcmToken = in.readString();

    }
    /**
     * Parcelable creator for the Users class
     */
    @Override
    public int describeContents() {
        return 0;
    }
    /**
     * Parcelable creator for the Users class
     */
    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {

        dest.writeString(name);
        dest.writeString(autoGenImageUri);
        dest.writeString(uploadedImageUri);
        dest.writeString(email);
        dest.writeString(number);
        dest.writeString(role);
        dest.writeString(profileUid);
        dest.writeString(homePage);
        dest.writeByte((byte) (enableNotification ? 1 : 0));
        dest.writeByte((byte) (enableGeolocationTracking ? 1 : 0));
        dest.writeByte((byte) (enableAdmin ? 1 : 0));
        dest.writeString(fcmToken);
    }

    /**
     * Parcelable creator for the Users class
     */
    public static final Creator<Users> CREATOR = new Creator<Users>() {

        @Override
        public Users createFromParcel(Parcel in) {
            return new Users(in);
        }

        @Override
        public Users[] newArray(int size) {
            return new Users[size];
        }
    };

    /**
     * Indicates whether some other object is "equal to" this one.
     * The `equals()` method implements an equivalence relation on non-null object references:
     *
     * - It is reflexive: for any non-null reference value x, x.equals(x) should return true.
     * - It is symmetric: for any non-null reference values x and y, x.equals(y) should return true if and only if y.equals(x) returns true.
     * - It is transitive: for any non-null reference values x, y, and z, if x.equals(y) returns true and y.equals(z) returns true, then x.equals(z) should return true.
     * - It is consistent: for any non-null reference values x and y, multiple invocations of x.equals(y) consistently return true or consistently return false, provided no information used in equals comparisons on the objects is modified.
     * - For any non-null reference value x, x.equals(null) should return false.
     *
     * @param o the reference object with which to compare.
     * @return true if this object is the same as the obj argument; false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // checks if the object references the same instance (reflexive).
        if (o == null || getClass() != o.getClass()) return false; // checks if the passed object is not null and is of the same class.
        Users users = (Users) o; // Type casts the object to a Users object.
        return Objects.equals(getProfileUid(), users.getProfileUid()); // Compares the profile UIDs for equality.
    }

    /**
     * Returns a hash code value for the object. This method is supported for the benefit of hash tables such as those provided by `HashMap`.
     *
     * The general contract of `hashCode()` is:
     * - Whenever it is invoked on the same object more than once during an execution of a Java application, the `hashCode()` method must consistently return the same integer, provided no information used in `equals()` comparisons on the object is modified.
     * - If two objects are equal according to the `equals()` method, then calling the `hashCode()` method on each of the two objects must produce the same integer result.
     * - It is not required that if two objects are unequal according to the `equals()` method, then calling the `hashCode()` method on each of the two objects must produce distinct integer results. However, the programmer should be aware that producing distinct integer results for unequal objects may improve the performance of hash tables.
     *
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getProfileUid());  // Generates the hash code based on the profile UID.
    }

}