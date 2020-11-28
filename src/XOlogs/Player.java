package XOlogs;

public class Player implements Comparable <Player>{
 Integer teamNumber =0;
 String name = null;
 Integer ps =0;
 Integer number =0;
 String uid;
 String designHash;


 public Player(String name, Integer team){
	 this.name = name;
	 teamNumber = team;

 }
 public Player(String name, Integer team, Integer N, String uid, String designHash){
	 this.name = name;
	 teamNumber = team;
	 number =N;
	 ps = 0;
	 this.uid = uid;
	 this.designHash = designHash;
 }



 public String getName(){return name;}
 public Integer getTeamNumber(){return teamNumber;}
 public Integer getPs() {return ps;}
 public Integer getNumber(){return number;}

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

 public void setName(String name){
     this.name = name;}
 public void setTeamNumber(Integer Number){teamNumber = Number;}
 public void setPs(Integer ps){
     this.ps = ps;}
    public String getDesignHash() {
        return designHash;
    }

    public void setDesignHash(String designHash) {
        this.designHash = designHash;
    }
     public void setNumber(Integer Num){number=Num;}

    @Override
    public int compareTo(Player o) {
        return this.name.compareTo(o.getName());
    }
}
