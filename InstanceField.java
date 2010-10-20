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
	}

}