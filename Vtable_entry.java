package xtc.oop;
import java.util.ArrayList;
import xtc.tree.GNode;
import xtc.tree.Node;

public class Vtable_entry{
	public String returntype;
	public String name;
	public ArrayList<String> params;
	public String ownerClass;
	public ArrayList<String> pnames;
	public GNode mnode;
	public boolean isVirtual;
	
	Vtable_entry(String rtype, String mname, ArrayList<String> paramstypes, 
				 String sclass,ArrayList<String> paramnames,GNode node){
		name = mname;
		returntype = rtype;
		params = new ArrayList<String>(paramstypes);
		ownerClass = sclass;
		pnames = new ArrayList<String>(paramnames);
		mnode=node;
	}
	Vtable_entry(boolean virtual, String rtype, String mname, ArrayList<String> paramstypes, 
				 String sclass,ArrayList<String> paramnames,GNode node){
		isVirtual = virtual;
		name = mname;
		returntype = rtype;
		params = new ArrayList<String>(paramstypes);
		ownerClass = sclass;
		pnames = new ArrayList<String>(paramnames);
		mnode=node;
	}
	Vtable_entry(String rtype, String mname, ArrayList<String> paramstypes, 
				 String sclass,ArrayList<String> paramnames){
		name = mname;
		returntype = rtype;
		params = new ArrayList<String>(paramstypes);
		ownerClass = sclass;
		pnames = new ArrayList<String>(paramnames);
		
	}

}
