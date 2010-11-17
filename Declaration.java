package xtc.oop;
import java.util.ArrayList;
import xtc.tree.GNode;
import xtc.tree.Node;

public class Declaration{
	public ArrayList<String> modifiers;
	public String returntype;
	public String name;
	public ArrayList<Fparam> params;
	public String ownerClass;
	public GNode bnode;
	public ArrayList<local_variable> variables;
	public boolean isVirtual;
	public int overloadNum;
	
	Declaration(String rtype, String mname, 
				 String sclass,ArrayList<Fparam> fparams,GNode node,ArrayList<local_variable> lvar){
		name = mname;
		returntype = rtype;
		params = new ArrayList<Fparam>(fparams);
		ownerClass = sclass;
		bnode=node;
		modifiers = new ArrayList<String>(0);

	}
	Declaration(ArrayList<String> mods, boolean virtual, String rtype, String mname, 
				 String sclass,ArrayList<Fparam> fparams,GNode node,ArrayList<local_variable> lvar){
		isVirtual = virtual;
		name = mname;
		returntype = rtype;
		params = new ArrayList<Fparam>(fparams);
		ownerClass = sclass;
		bnode=node;
		variables = new ArrayList<local_variable>(lvar);
		modifiers = new ArrayList<String>(mods);
	}
	Declaration(String rtype, String mname, 
				 String sclass,ArrayList<Fparam> fparams,ArrayList<local_variable> lvar){
		name = mname;
		returntype = rtype;
		params = new ArrayList<Fparam>(fparams);
		ownerClass = sclass;
		modifiers = new ArrayList<String>(0);
		
	}
	
	/**
	 * will cycle through all variables for name
	 * returns the type of that name
	 *
	 */
	public String search_for_type(String name){
		for(int i=0;i<variables.size();i++){
			if(name.equals(variables.get(i).name))
				return variables.get(i).type;
		}
		return "variable does not exist";
	
	}
	
	/**
	 * will cycle throu all variables for name
	 * and then update the type to the new type
	 *
	 */
	public void update_type(String name, String newtype){
		
		for(int i=0;i<variables.size();i++){
			
			if(name.equals(variables.get(i).name))
				variables.get(i).type = newtype;
	
		}

	}

}
class local_variable{

	String name;
	String type;
	
	

	local_variable(String typ, String nm){
	
		name = nm;
		type = typ;
		
	}

}
class Fparam{
	public ArrayList<String> modifiers;
	public String type;
	public String var_name;
	Fparam(ArrayList<String> mods, String typ, String name){
		modifiers = mods;
		type = typ;
		var_name =name;
	}
	
}
