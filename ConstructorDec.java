package xtc.oop;
import java.util.ArrayList;
import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;

public class ConstructorDec{
	
	public ArrayList<String> modifiers;
	public ArrayList<Fparam> fparams;
	public GNode node;
	
	ConstructorDec(ArrayList<String> mod,ArrayList<Fparam> fp,GNode n){
		modifiers = mod;
		fparams = fp;
		node = n;
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