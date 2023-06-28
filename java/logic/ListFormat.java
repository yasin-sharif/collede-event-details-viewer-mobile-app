package logic;
/*
* format of each list item
* */
public class ListFormat {
    private String title,date,time,loc,dept,type;
    public ListFormat(){
        // do nothing
    }
    public ListFormat(String title_in,String date_in,String time_s,String time_e,String location_in,String department,String type_in){
        title=title_in;
        time=time_s+"-"+time_e;
        date=date_in;
        loc=location_in;
        dept=department;
        type=type_in;
    }

    public String getTitle(){
        return title;
    }
    public String getDate(){
        return date;
    }
    public String getTime(){
        return time;
    }
    public String getLoc(){
        return loc;
    }
    public String getDept(){
        return dept;
    }
    public String getType(){
        return type;
    }

}
