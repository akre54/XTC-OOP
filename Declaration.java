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
	public int specificity;
	
	Declaration(int ol,String rtype, String mname, 
				 String sclass,ArrayList<Fparam> fparams,GNode node,ArrayList<local_variable> lvar){
		name = mname;
		returntype = rtype;
		params = new ArrayList<Fparam>(fparams);
		ownerClass = sclass;
		bnode=node;
		modifiers = new ArrayList<String>(0);
		overloadNum =ol;
		specificity =0;

	}
	Declaration(int ol,ArrayList<String> mods, boolean virtual, String rtype, String mname, 
				 String sclass,ArrayList<Fparam> fparams,GNode node,ArrayList<local_variable> lvar){
		isVirtual = virtual;
		name = mname;
		returntype = rtype;
		params = new ArrayList<Fparam>(fparams);
		ownerClass = sclass;
		bnode=node;
		variables = new ArrayList<local_variable>(lvar);
		modifiers = new ArrayList<String>(mods);
		overloadNum =ol;
		specificity =0;

	}
	//constructor for Class and Object methods
	Declaration(String rtype, String mname, 
				 String sclass,ArrayList<Fparam> fparams){
		name = mname;
		returntype = rtype;
		params = new ArrayList<Fparam>(fparams);
		ownerClass = sclass;
		modifiers = new ArrayList<String>(0);
		overloadNum =0;
		specificity =0;
	}
	
	/**
	 * will cycle through all variables for name
	 * returns the type of that name
	 *
	 */
	public ArrayList<String> search_for_type(String name){
		for(int i=0;i<variables.size();i++){
			if(name.equals(variables.get(i).name)){
				ArrayList<String> type = new ArrayList<String>(variables.get(i).packages);
				type.add(variables.get(i).type);
				return type;
			}
		}
		return null;//error type does not exist
	
	}
	
	/**
	 * will cycle throu all variables for name
	 * and then update the type to the new type
	 * will create a new local_variable if none exists
	 */
	public void update_type(String name,ArrayList<String> newpack, String newtype){
				boolean found = false;
		
				for(int i=0;i<variables.size();i++){
					if(name.equals(variables.get(i).name)){
						found = true;
						variables.get(i).type = newtype;
						variables.get(i).packages = newpack;
					}
				}
				if(!found){
					variables.add(new local_variable(newpack,newtype,name));	
				
				}
	}
	public boolean is_static(){
		for(int i=0;i<modifiers.size();i++){
			if(modifiers.get(i).equals("static"))
				return true;
		}
		return false;
	}

}
class local_variable{
	ArrayList<String> packages;
	String name;
	String type;

	local_variable(ArrayList<String> pack,String typ, String nm){
		packages = pack;
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
