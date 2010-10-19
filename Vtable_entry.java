package xtc.oop;
import java.util.ArrayList;

public class Vtable_entry{
	public String returntype;
	public String name;
	public ArrayList<String> params;
	public String ownerClass;
	
	Vtable_entry(String rtype, String mname, ArrayList<String> paramstypes, String sclass){
		name = mname;
		returntype = rtype;
		params = new ArrayList<String>(paramstypes);
		ownerClass = sclass;
	}
}
