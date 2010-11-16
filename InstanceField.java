package xtc.oop;
import java.util.ArrayList;
import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;



public class InstanceField{
	
	public ArrayList<String> modifiers;	
	public String type;
	public String var_name;
	public String value;
	
	InstanceField(ArrayList<String> mods,String name,String typ,String val){
		modifiers = mods;
		type = typ;
		var_name = name;
		value=val;
		//initialize blank values
		if(value.equals("")){
		
			if(type.equals("String")) value ="null";
			if(type.equals("int")) value ="0";
			if(type.equals("float")) value ="0.0";
			if(type.equals("long")) value ="0";
			if(type.equals("short")) value ="0";
			if(type.equals("double")) value ="0.0";
			if(type.equals("byte")) value ="0";
			if(type.equals("char")) value ="''";
			if(type.equals("boolean")) value ="false";
			if( (!type.equals("String"))&&
				(!type.equals("int"))&&
				(!type.equals("float"))&&
				(!type.equals("long"))&&
				(!type.equals("short"))&&
				(!type.equals("double"))&&
				(!type.equals("byte"))&&
				(!type.equals("char"))&&
				(!type.equals("boolean"))
			   )value ="null";
		
		
		}
			
	}

}