package XOlogs;
import java.io.File;

public class SoundItem {
	String ActionPattern = null;
	String SoundPath;
	public SoundItem (String Pattern, String Path){
		ActionPattern = Pattern;
		SoundPath = Path;
	}
	public String GetPath (){ return SoundPath; }
	public String GetActionPattern () { return ActionPattern; }
	
	public void SetPath (String p){SoundPath = p;}
	public void SetPattern (String ptrn){ActionPattern =ptrn;}
	public File GetFile (){
		return new File(GetPath());
	}
		
	
}
