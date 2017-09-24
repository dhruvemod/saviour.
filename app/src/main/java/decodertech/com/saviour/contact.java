package decodertech.com.saviour;

import java.io.Serializable;

/**
 * Created by Dhruve on 9/18/2017.
 */

public class contact implements Serializable {
public String person_name,number,loc;
    public contact(){

    }
    public contact(String n,String num,String l){
        person_name=n;
        number=num;
        loc=l;
    }

    public String getLoc() {
        return loc;
    }

    public String getNumber() {
        return number;
    }

    public String getPerson_name() {
        return person_name;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setPerson_name(String person_name) {
        this.person_name = person_name;
    }
}
