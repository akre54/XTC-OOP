package xtc.oop;
import java.util.ArrayList;
import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;



public class InstanceField extends Variable{
	
	public ArrayList<String> modifiers;	
	public String value;
	
	InstanceField(String pkg,ArrayList<String> mods,String typ,String name,String val){
		super(pkg,typ,name);
		this.modifiers = mods;
		this.value=val;
		if (this.value.equals("")){//default values for primitive types
			if(this.type.equals("int32_t"))this.value ="0";
			if(this.type.equals(""))this.value ="";
			if(this.type.equals(""))this.value ="";

		}
	}
	public boolean isStatic(){
		for(String m: this.modifiers){
			if(m.equals("static") )return true;
		}
		return false;
	}
	public boolean isConst(){
		for(String m: this.modifiers){
			if(m.equals("const") )return true;
		}
		return false;
	}
	
}