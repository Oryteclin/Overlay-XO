package XOlogs;

public class Player implements Comparable <Player>{
 Integer teamNumber =0;
 String Name = null;
 Integer PS =0;
 Integer number =0;
 String uid;



 public Player(String name, Integer team){
	 Name = name;
	 teamNumber = team;

 }
 public Player(String name, Integer team, Integer N, String uid){
	 Name = name;
	 teamNumber = team;
	 number =N;
	 PS = 0;
	 this.uid = uid;
 }



 public String getName(){return Name;}
 public Integer getTeamNumber(){return teamNumber;}
 public Integer getPS() {return PS;}
 public Integer getNumber(){return number;}

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

 public void setName(String name){Name = name;}
 public void setTeamNumber(Integer Number){teamNumber = Number;}
 public void setPS(Integer ps){PS = ps;}
 public void setNumber(Integer Num){number=Num;}

    @Override
    public int compareTo(Player o) {
        return this.Name.compareTo(o.getName());
    }
}
