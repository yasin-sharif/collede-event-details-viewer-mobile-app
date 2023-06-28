package logic;
/*
* node type of event in the firebase
* */
public class Event {
    public String type,date,timeS,timeE,loc,link,coord,coordNum,addedBy;
    public Event(){

    }
    public Event(String type,String date,String timeS,String timeE,String coord,String coordNum,String link,String loc,String addedBy){
        this.type=type;
        this.date=date;
        this.timeE=timeE;
        this.timeS=timeS;
        this.coord=coord;
        this.coordNum=coordNum;
        this.link=link;
        this.loc=loc;
        this.addedBy=addedBy;
    }
}
