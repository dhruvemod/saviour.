package decodertech.com.saviour;

import java.io.Serializable;

/**
 * Created by Dhruve on 9/25/2017.
 */

public class InfoToSend implements Serializable {
    public String caseID;
    public String location;
    public String phoneNumber;
    public InfoToSend(String caseID, String location,String phoneNumber){
        this.caseID=caseID;
        this.location=location;
        this.phoneNumber=phoneNumber;
    }
public  InfoToSend(){

}

    public String getCaseID() {
        return caseID;
    }

    public String getLocation() {
        return location;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setCaseID(String caseID) {
        this.caseID = caseID;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
