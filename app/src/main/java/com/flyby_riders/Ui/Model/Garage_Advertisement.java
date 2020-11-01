package com.flyby_riders.Ui.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Garage_Advertisement implements Parcelable {

    public String ADIMAGEPATH,USERID,Advertising_ID,Advertising_PostDate,Advertising_Title,Advertising_Details,Advertising_Images,Advertising_Video,Advertising_CoverPic,Advertising_costPrice;
    public ArrayList<Advertising_UserAction> advertising_userActions = new ArrayList<>();
    public ArrayList<GarageOwnerDetails> garageOwnerDetails = new ArrayList<>();

    public Garage_Advertisement() {
    }

    protected Garage_Advertisement(Parcel in) {
        ADIMAGEPATH = in.readString();
        USERID = in.readString();
        Advertising_ID = in.readString();
        Advertising_PostDate = in.readString();
        Advertising_Title = in.readString();
        Advertising_Details = in.readString();
        Advertising_Images = in.readString();
        Advertising_Video = in.readString();
        Advertising_CoverPic = in.readString();
        Advertising_costPrice = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ADIMAGEPATH);
        dest.writeString(USERID);
        dest.writeString(Advertising_ID);
        dest.writeString(Advertising_PostDate);
        dest.writeString(Advertising_Title);
        dest.writeString(Advertising_Details);
        dest.writeString(Advertising_Images);
        dest.writeString(Advertising_Video);
        dest.writeString(Advertising_CoverPic);
        dest.writeString(Advertising_costPrice);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Garage_Advertisement> CREATOR = new Creator<Garage_Advertisement>() {
        @Override
        public Garage_Advertisement createFromParcel(Parcel in) {
            return new Garage_Advertisement(in);
        }

        @Override
        public Garage_Advertisement[] newArray(int size) {
            return new Garage_Advertisement[size];
        }
    };

    public String getADIMAGEPATH() {
        return ADIMAGEPATH;
    }

    public void setADIMAGEPATH(String ADIMAGEPATH) {
        this.ADIMAGEPATH = ADIMAGEPATH;
    }

    public String getUSERID() {
        return USERID;
    }

    public void setUSERID(String USERID) {
        this.USERID = USERID;
    }

    public String getAdvertising_ID() {
        return Advertising_ID;
    }

    public void setAdvertising_ID(String advertising_ID) {
        Advertising_ID = advertising_ID;
    }

    public String getAdvertising_PostDate() {
        return Advertising_PostDate;
    }

    public void setAdvertising_PostDate(String advertising_PostDate) {
        Advertising_PostDate = advertising_PostDate;
    }

    public String getAdvertising_Title() {
        return Advertising_Title;
    }

    public void setAdvertising_Title(String advertising_Title) {
        Advertising_Title = advertising_Title;
    }

    public String getAdvertising_Details() {
        return Advertising_Details;
    }

    public void setAdvertising_Details(String advertising_Details) {
        Advertising_Details = advertising_Details;
    }

    public String getAdvertising_Images() {
        return Advertising_Images;
    }

    public void setAdvertising_Images(String advertising_Images) {
        Advertising_Images = advertising_Images;
    }

    public String getAdvertising_Video() {
        return Advertising_Video;
    }

    public void setAdvertising_Video(String advertising_Video) {
        Advertising_Video = advertising_Video;
    }

    public String getAdvertising_CoverPic() {
        return Advertising_CoverPic;
    }

    public void setAdvertising_CoverPic(String advertising_CoverPic) {
        Advertising_CoverPic = advertising_CoverPic;
    }

    public String getAdvertising_costPrice() {
        return Advertising_costPrice;
    }

    public void setAdvertising_costPrice(String advertising_costPrice) {
        Advertising_costPrice = advertising_costPrice;
    }

    public ArrayList<Advertising_UserAction> getAdvertising_userActions() {
        return advertising_userActions;
    }

    public void setAdvertising_userActions(ArrayList<Advertising_UserAction> advertising_userActions) {
        this.advertising_userActions = advertising_userActions;
    }

    public ArrayList<GarageOwnerDetails> getGarageOwnerDetails() {
        return garageOwnerDetails;
    }

    public void setGarageOwnerDetails(ArrayList<GarageOwnerDetails> garageOwnerDetails) {
        this.garageOwnerDetails = garageOwnerDetails;
    }

    public static class Advertising_UserAction  {
        public String ByeNow,ContactStore,NoUserAction;

        public String getByeNow() {
            return ByeNow;
        }

        public void setByeNow(String byeNow) {
            ByeNow = byeNow;
        }

        public String getContactStore() {
            return ContactStore;
        }

        public void setContactStore(String contactStore) {
            ContactStore = contactStore;
        }

        public String getNoUserAction() {
            return NoUserAction;
        }

        public void setNoUserAction(String noUserAction) {
            NoUserAction = noUserAction;
        }
    }

    public static class GarageOwnerDetails implements Parcelable {
        public String ID,UserName,GarageName,Address,ProfilePicture,city;






        public GarageOwnerDetails() {
        }

        public GarageOwnerDetails(Parcel in) {
            ID = in.readString();
            UserName = in.readString();
            GarageName = in.readString();
            Address = in.readString();
            ProfilePicture = in.readString();
            city = in.readString();
        }

        public static final Creator<GarageOwnerDetails> CREATOR = new Creator<GarageOwnerDetails>() {
            @Override
            public GarageOwnerDetails createFromParcel(Parcel in) {
                return new GarageOwnerDetails(in);
            }

            @Override
            public GarageOwnerDetails[] newArray(int size) {
                return new GarageOwnerDetails[size];
            }
        };

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public String getUserName() {
            return UserName;
        }

        public void setUserName(String userName) {
            UserName = userName;
        }

        public String getGarageName() {
            return GarageName;
        }

        public void setGarageName(String garageName) {
            GarageName = garageName;
        }

        public String getAddress() {
            return Address;
        }

        public void setAddress(String address) {
            Address = address;
        }

        public String getProfilePicture() {
            return ProfilePicture;
        }

        public void setProfilePicture(String profilePicture) {
            ProfilePicture = profilePicture;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(ID);
            dest.writeString(UserName);
            dest.writeString(GarageName);
            dest.writeString(Address);
            dest.writeString(ProfilePicture);
            dest.writeString(city);
        }
    }


}
