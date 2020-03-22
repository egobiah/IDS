package BD;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message implements Serializable {
    Person owner;
    String data;
    String date;
   // private static final DateFormat dateFormat = new SimpleDateFormat("YY/MM/dd HH:mm:ss");
    public Message(String data, Person person){
        this.owner = person;
        this.data = data;
        date = (new Date()).toString();
    }

    public String getDate() {
        //return dateFormat.format(date);
        return date.toString();
    }

    public Person getOwner() {
        return owner;
    }

    public String getData() {
        return data;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setDate(String date) {
        this.date = date;
    }




    @Override
    public String toString() {
        return ("[" + getDate() + "] " +  owner.toString() + " : " + getData());
    }


}
