/**
 * Builds a tree structure so subclasses can access superclass's vtables when
 * building their own vtables. links all the classes together in a heiarchy for
 * easy searchability
 *
 * (C) 2010 P.Hammer, A.Krebs, L. Pelka, P.Ponzeka
 */
package xtc.oop;

import java.util.ArrayList;
import java.util.LinkedList;
import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;

public class InheritanceTree{
	public InheritanceTree root;
	public final String className;
	public ArrayList<InstanceField> fields;
	public ArrayList<Declaration> constructors;
	public ArrayList<Declaration> local; //all methods defined IN THIS CLASS virtual or not!
	public ArrayList<Declaration> Vt_ptrs; //all methods able to be inherited by children
	public InheritanceTree superclass;
	public ArrayList<InheritanceTree> subclasses;
	public ArrayList<String> packages;


	/**
	 * The constructor for creating the Object class Inheritance tree.
	 * @param none
	 * 
	 */
	InheritanceTree(){

		className = "Object";
		superclass = null; //no superclass for Object

		fields = new ArrayList<InstanceField>(); //no instancefields in Object
		Vt_ptrs = new ArrayList<Declaration>(); // for Vtable
		
		//add __isa to Vtable
		Vt_ptrs.add(new Declaration("Class", "__isa",
                                    className,new ArrayList<Fparam>(0)));
		
		//add __delete to Vtable
		ArrayList<Fparam> p = new ArrayList<Fparam>();
		p.add(new Fparam(new ArrayList<String>(),"__Object*","__this"));
		Vt_ptrs.add(new Declaration("void", "__delete",
                                    "Object", p));
		
		//add hashcode to Vtable
		p.clear();
		p.add(new Fparam(new ArrayList<String>(),"Object","__this"));
		Vt_ptrs.add(new Declaration("int32_t", "hashCode",
                                    "Object", p));
		//add equals to Vtable
		p.clear();
		p.add(new Fparam(new ArrayList<String>(),"Object","__this"));
		p.add(new Fparam(new ArrayList<String>(),"Object","other"));
		Vt_ptrs.add(new Declaration("bool", "equals",
                                    "Object", p));
		//add getClass to Vtable
		p.clear();
		p.add(new Fparam(new ArrayList<String>(),"Object","__this"));
		Vt_ptrs.add(new Declaration("Class","getClass",
                                    "Object",p));
		//add toString to Vtable
		p.clear();
		p.add(new Fparam(new ArrayList<String>(),"Object","__this"));
		Vt_ptrs.add(new Declaration("String","toString",
                                    "Object",p));

		//subclass are initalized to a 0 element arraylist
		subclasses = new ArrayList<InheritanceTree>(0);
		
		this.root = this;

	}
	
	/**
	 * The constructor for creating the Class class.
	 * @param InheritanceTree (the Object inheritance tree)
	 */
	InheritanceTree(InheritanceTree supr){

		this.root = supr.root;

		superclass = supr;
		className = "Class";
		
		//copies the superclass's Vtable into virtual Vtable
		Vt_ptrs = new ArrayList<Declaration>(supr.Vt_ptrs);
		
		//change __isa field to point to this class's feild
		Vt_ptrs.get(0).ownerClass = className;
		
		//change __delete field to point to this class's feild
		Vt_ptrs.get(1).ownerClass = className;
		Vt_ptrs.get(1).params.get(0).type ="__"+className+"*";
		
		//change toString to point to class name
		Vt_ptrs.get(5).ownerClass = className;
		Vt_ptrs.get(5).params.get(0).type = className;

		
		//adds virtual Class methods ptrs to virtual Vtable
		ArrayList<Fparam> p = new ArrayList<Fparam>(0);
		p.add(new Fparam(new ArrayList<String>(),"Class","__this"));
		Vt_ptrs.add(new Declaration("String","getName",
									 "Class",p));
		
		p.clear();
		p.add(new Fparam(new ArrayList<String>(),"Class","__this"));
		Vt_ptrs.add(new Declaration("String","getSuperclass",
                                    "Class",p));
		
		p.clear();
		p.add(new Fparam(new ArrayList<String>(),"Class","__this"));
		Vt_ptrs.add(new Declaration("String","getComponentType",
                                    "Class",p));
		
		p.clear();
		p.add(new Fparam(new ArrayList<String>(),"Class","__this"));
		Vt_ptrs.add(new Declaration("String","isPrimitive",
                                    "Class",p));
		
                p.clear();
		p.add(new Fparam(new ArrayList<String>(),"Class","__this"));
		Vt_ptrs.add(new Declaration("String","isArray",
                                    "Class",p));
		
		p.clear();
		p.add(new Fparam(new ArrayList<String>(),"Class","__this"));
		p.add(new Fparam(new ArrayList<String>(),"Object","o"));
		Vt_ptrs.add(new Declaration("String","isInstance",
                                    "Class",p));
		
		//subclass are initalized to a 0 element arraylist
		subclasses = new ArrayList<InheritanceTree>(0);
		
		//add this tree to the superclasses' subclasses
		supr.subclasses.add(this);
			
	}
	/**
	 * The constructor for all InheritanceTree's that do not represent Object or Class.
	 * will create vtable for class and then attach the tree to the superclass tree.
	 * @param GNode (classdeclaration), InheritanceTree (superclass's).
	 */
	InheritanceTree(GNode n, InheritanceTree supr){
		this.root = supr.root;
		superclass = supr;
		className = n.getString(1);
		
		//field arraylist defined with all field declarations
		fields = addfielddeclarations(n);
		
		constructors = new ArrayList<Declaration>(0);
		local = new ArrayList<Declaration>(0);

		//copies the superclass's Vtable into virtual arraylist
		Vt_ptrs = new ArrayList<Declaration>(supr.Vt_ptrs);


		//change __isa methods to point to this class
		Vt_ptrs.get(0).ownerClass = className;


		//change __delete field to point to this class's feild
		Vt_ptrs.get(1).ownerClass = className;
		Vt_ptrs.get(1).params.get(0).type ="__"+className+"*";


		//add __class to local methods
		local.add(new Declaration("Class","__class",className,								 
								  new ArrayList<Fparam>(0)));


		//add __delete to local methods
		Fparam d= new Fparam(new ArrayList<String>(0),className,"__this");
		Vt_ptrs.get(1).params.set(0,d);
		local.add(Vt_ptrs.get(1));
		
		//constructors defined
		//local methods defined
		//virtual methods defined
		//alters Vt_ptr if overridding exists
		//gives unique # if overloading exists
		ArrayList<Declaration> virtual = addmethoddeclarations(n);
		
		
		//add virtual methods to vtable
		Vt_ptrs.addAll(virtual);

			
		//subclass are initalized to a 0 element arraylist
		subclasses = new ArrayList<InheritanceTree>(0);
		
		//add this tree to the superclasses' subclasses
		supr.subclasses.add(this);

	}
	/**
	 *looks in Vt_ptrs and local for methods with same name but diff params
	 * if multiple are found keeps method with max overloadNum stored
	 * does overridden test and changes Vt_ptr if true
	 * returns max [overloadNum,boolean add_to_virtual]
	 */
	private int[] overloaded_ridden_check(String method, ArrayList<Fparam> params, boolean is_virtual,GNode body) {
	
		
		int max = -1;
		for (Declaration i : Vt_ptrs) {
        
			//same name test
			if (method.equals(i.name)) {
				//diff params test
				if (params.size() == i.params.size()){
					if ((!typetest(params, i.params))&&(is_virtual)){
						i.ownerClass = this.className;
						i.bnode = body;
						//signify to NOT ADD TO VIRTUAL
						//bc its overridden
						return new int[]{i.overloadNum-1,0};
					}
				}
				else max = Math.max(i.overloadNum, max);
			}	
		}
		for (Declaration j : local) {
			//same name test
			if (method.equals(j.name))  
					//impossible for a local method to be overriden by a local method
					//so must be overloaded
					max = Math.max(j.overloadNum, max); //store max overloadNum
		}
		return new int[]{max,1};

	}
	/**
	 *helper method used in overload__ridden_check 
	 *  returns true if both params arraylist have same types through and through
	 */
	private boolean typetest(ArrayList<Fparam> vptrs, ArrayList<Fparam> local){
		for (int j = 1; j<vptrs.size(); j++) {//ignore __this param checking
                    //if types do not match up return FALSE
                    if (!(vptrs.get(j).type.equals(local.get(j).type)))
                        return false;//***this means that this is an overloaded method
		}
		return true;
	}
	/**
	 * uses visitors to create the Declarations for virtual methods.
	 * also documents local methods that are not virtual for definition later
	 *@param GNode (always a classDeclaration)
	 */
	private ArrayList<Declaration> addmethoddeclarations(GNode n){
		// cast to a node
		Node node = n;
		//declare a virutal arraylist<Declaration>
		final ArrayList<Declaration> virtual = new ArrayList<Declaration>(0);

		new Visitor(){
			
			//declare initalize variables 
			ArrayList<String> modifiers = new ArrayList<String>(0);
			String retrn = "";
			String className= "";
			String methodname="";
			ArrayList<Fparam> params = new ArrayList<Fparam>(0);
			ArrayList<String> mods = new ArrayList<String>(0);
			String fptype ="";
			String fpname ="";
			int overloaded = 0;
			int[] overloaded_ridden;
			GNode block;

			boolean is_fparam=false;
			boolean is_virtual=false;
			boolean is_constructor =false;
			
			public void visitClassDeclaration(GNode n){
				className = n.getString(1);
				visit(n);
			}
			public void visitConstructorDeclaration(GNode n){
				is_constructor=true;
				
				//clear variables
				modifiers.clear();
				retrn="";
				methodname = n.getString(2);
				params.clear();
				block=null;
				is_virtual = true;
				overloaded=0;
				
				//get info from tree
				visit(n);
				overloaded = constructors.size();
				//add declaration to constructor
				constructors.add(new Declaration(overloaded, modifiers, is_virtual, retrn, methodname, className, params,
								block, new ArrayList<LocalVariable>(0)));
				
				is_constructor = false;
			}
			public void visitMethodDeclaration(GNode n){
				//clear variables
				modifiers.clear();
				retrn = "";
				methodname = n.getString(3);
				params.clear();
				block = null;
				is_virtual = true;
				
				//go into tree to get info
				visit(n);
				
				//search in Vt_ptrs and local for OVERLOADnum and if it is OVERRIDEN
				overloaded_ridden = overloaded_ridden_check(methodname,params,is_virtual,block);
				
				//test to see if the modifier was public or protected(if it should be virtual)
				if((is_virtual)&&(overloaded_ridden[1]==1)) 
					virtual.add(new Declaration(overloaded_ridden[0]+1,modifiers,is_virtual,retrn,methodname,className,params,
								block,new ArrayList<LocalVariable>(0)));
				
				//add it to local with true as isvirtual field
				local.add(new Declaration(overloaded_ridden[0]+1,modifiers,is_virtual,retrn,methodname,className,params,
								block,new ArrayList<LocalVariable>(0)));
				
									
			}
			public void visitBlock(GNode n){
				block = n;
			}
			public void visitModifiers(GNode n){
				visit(n);
			}
			public void visitModifier(GNode n){
				if (is_fparam) mods.add(n.getString(0));
				 //notes that private/staic methods are not virtual and should not go into Vtable
				else if ((n.getString(0).equals("private"))||(n.getString(0).equals("static")))
					 is_virtual = false;
				else modifiers.add(n.getString(0));
			}
			public void visitVoidType(GNode n){
				retrn = "void";
			}
			public void visitQualifiedIdentifier(GNode n){
				String type=n.getString(0);
				if(is_fparam) fptype=type;
				else retrn = type;
			}
			
			public void visitPrimitiveType(GNode n){
				String type=n.getString(0);
				if(n.getString(0).equals("int")) type="int32_t";
				if(n.getString(0).equals("boolean")) type="bool";
				if(is_fparam) fptype = type;
				else retrn = type;
			}
			public void visitDimensions(GNode n){
				if(is_fparam){ 
					if(fptype.equals("Class"))fptype ="ArrayOfClass";
					if(fptype.equals("int32_t"))fptype ="ArrayOfInt";
					if(fptype.equals("Object"))fptype ="ArrayOfObject";
					//more need to be added!!!!!!!!
				
				}
				else{ 
					if(retrn.equals("Class"))retrn ="ArrayOfClass";
					if(retrn.equals("int32_t"))retrn ="ArrayOfInt";
					if(retrn.equals("Object"))retrn ="ArrayOfObject";
					//more need to be added!!!!!!!!
				}
			}
			public void visitFormalParameters(GNode n){
				//add __this parameter for virtual methods
				if((is_virtual)&&(!is_constructor)) 
					params.add(new Fparam(new ArrayList<String>(0),className,"__this"));
				visit(n);
			}
			public void visitFormalParameter(GNode n){//variable name
				is_fparam =true;
				mods.clear();
				fpname =n.getString(3);
				fptype="";
				visit(n);
				params.add(new Fparam(mods,fptype,fpname));
				is_fparam=false;
					
			}
			public void visit(Node n) {
				for (Object o : n) if (o instanceof Node) dispatch((Node)o);
			}
			
		}.dispatch(n);
	
		return virtual;
	}
	/**
	 * returns ArrayList<InstanceField> that holds all the fields fieldDeclaration info.
	 * @param GNode n
	 * however instance fields are defined or declared outside the constructor 
	 *		is how they will be stored here.
	 *
	 */
	private ArrayList<InstanceField> addfielddeclarations(GNode n){
		Node node = n;
	
		final ArrayList<InstanceField> f = new ArrayList<InstanceField>(0);
		
		//---adds all non-private/static fields from superclass to this class
		for(int i=0;i<superclass.fields.size();i++){
			for(int j=0;j<superclass.fields.get(i).modifiers.size();j++){
				String mod = superclass.fields.get(i).modifiers.get(j);
				
				if((mod.equals("private"))||(mod.equals("static"))){}//do nothing
				else f.add(superclass.fields.get(i));//inherit the field 
			}
		}
		
		
		new Visitor(){
			
			ArrayList<String> mods= new ArrayList<String>(0);
			String type="";
			ArrayList<String> names= new ArrayList<String>(0);
			String val;
			boolean is_field = false;
			boolean is_selectExp = false;
			boolean is_arg = false;
			
			
			public void visitClassBody(GNode n){
				visit(n);
			}
			public void visitConstructorDeclaration(GNode n){
				//do not look in constructor
			}
			public void visitMethodDeclaration(GNode n){
				//do not look in methods
			}
			public void visitFieldDeclaration(GNode n){
				is_field = true;
				
				//clear variables
				mods.clear();
				type ="";
				names.clear();
				val="";
				
				visit(n);
				//add instancefield for each name in names
				for(int i=0; i<names.size();i++){
					f.add(new InstanceField(mods,type,names.get(i),val));
				}
				
				is_field = false;
			}
			//if not looking in FieldDeclaration's subtree ignore nodes
			public void visitModifier(GNode n){
				if(is_field)mods.add(n.getString(0));
				
			}
			public void visitPrimitiveType(GNode n){
				if(is_field){
					String type = n.getString(0);
					if(type.equals("int"))type="int32_t";
					if(type.equals("boolean"))type="bool";
				}
				visit(n);
			}
	
			public void visitDeclarator(GNode n){//variable name
				if(is_field) names.add(n.getString(0));
					
			}
			public void visitQualifiedIdentifier(GNode n){//type
				if(is_field) type=n.getString(0);
				
			}
			public void visitAdditiveExpression(GNode n){
				visit(n.getNode(0));
				val = val+" "+n.getString(1);
				visit(n.getNode(2));
			}
			public void visitIntegerLiteral(GNode n){
				val = val+n.getString(0);
			}
			public void visitStringLiteral(GNode n){
				val = val+n.getString(0);
			}
			public void visitCallExpression(GNode n){
				if(is_field){
					visit(n.getNode(0));
					visit(n.getNode(1));
					val = val+n.getString(2);
					visit(n.getNode(3));
				}	
			}
			public void visitArguments(GNode n){
				if(is_field){
					is_arg =true;
					val=val+"(";
					visit(n);
					//val.  take off last ,
					val=val+")";
					is_arg=false;
				}
			}
			public void visitSelectionExpression(GNode n){
				if(is_field){
					is_selectExp=true;
					//get primary identifer
					visit(n);
					//add selectionExp member
					val = val+n.getString(1);
					is_selectExp=false;
				}
			}
			public void visitPrimaryIdentifier(GNode n){
				if(is_field){
					if(is_selectExp)
						val = val+n.getString(0)+".";
					if(is_arg)
						val = val+n.getString(0)+",";
				}
			}
			public void visit(Node n) {
				for (Object o : n) if (o instanceof Node) dispatch((Node)o);
			}
		
		}.dispatch(node);
		return f;
	
	}
		/**
	 * search will return a InheritanceTree of matching className
	 *	or a null InheritanceTree if no tree exists yet as child of the root.
	 *@param String name
	 * this is meant to be used only on the root Object Tree so that the entire tree is searched
	 *
	 */
	public InheritanceTree search(ArrayList<String> pkgs, String name){
		InheritanceTree found = null;
		if ((this.className.equals(name)) && (packages.containsAll(pkgs)))
			return this;
		
		for (InheritanceTree i : subclasses) {
			found = i.search(pkgs,name);
			if (found!=null) return found;
		}
		return found;
	
	}
	/**
	 * method used to retrieve the corret overloaded constructor and append its overloaded number
	 */
	public String search_for_constructor(ArrayList<String> paramtyps){
		//list of possible constructors
		LinkedList<Declaration> possible= new LinkedList<Declaration>();
		
		//---- ACCESSABLE + # PARAMS ----
		//add all constructors to list that have same #params
		for (Declaration i : constructors) {
                    if (i.params.size() == paramtyps.size())
                        possible.add(i);
		}
		// return if only one
		if (possible.size() == 1)
                    return make_name(false,possible.get(0));
		
		//---SPECIFICITY CHECK----
		//need to zero out specificity for next check
		for(Declaration d : possible){
                    d.specificity = 0;
		}
		int ptsize = paramtyps.size();
		for(int m = 0;m<ptsize;m++){
			String type=paramtyps.get(m);
			for(int c=0;c<possible.size();c++){
				String pos_type = possible.get(c).params.get(m).type;
				if(!pos_type.equals(type));
				else {
					int casting = gouptree(pos_type,type);
					if(casting == -1){ possible.remove(c);}
					else possible.get(c).specificity =possible.get(c).specificity+ casting;
				}
			}
		}
		Declaration min = possible.get(0);
		for (Declaration n : possible) {
                    if (min.specificity > n.specificity)
                        min = n;
		}
		return make_name(false,min);
	
	}
	/**
	 * method used to retrieve the corret overloaded method and append its overloaded number
	 */
	public String[] search_for_method(boolean on_instance,ArrayList<String> paramtyps, 
																String method_name){
		LinkedList<Declaration> possible= new LinkedList<Declaration>();
		
		//-----ACCESSABLE/SAME NAME/SAME #PARAMS CHECK
		//looks in local(non virtual, non-private[if on_instance])and VT_ptrs 
		//for same named methods and same number of parameters
		for (Declaration j : Vt_ptrs) {
			if ((j.name.equals(method_name)) && ((j.params.size()-1) == paramtyps.size()))
				possible.add(j);						
		}
		for (Declaration l : local ) {
			if ((l.name.equals(method_name)) && (!l.isVirtual)&&(!((on_instance)&&(l.isprivate())))&&
				 (l.params.size() == paramtyps.size())) 
				possible.add(l);
		}
		//if only one left 
		//RETURN [returntype,accessor+NAME+_number]
		if (possible.size()==1) {
			Declaration choosen = possible.get(0);
			return new String[]{choosen.returntype, make_name(on_instance, choosen)};
		}
		//----SPECIFICITY CHECK
		//need to zero out specificity for next check
		for(Declaration d : possible){
			d.specificity =0;
		}
		int size= paramtyps.size();
		for (int m=0; m<size; m++) {
				String type = paramtyps.get(m);
				for (Declaration c : possible) {
					String pos_type = c.params.get(m).type;
					if (!pos_type.equals(type));
					else {
						int casting = gouptree(pos_type,type);
						if (casting == -1) { possible.remove(c); }//not castable
						else c.specificity = c.specificity + casting;
					}
				}
		}
		//find method with smallest number MUST BE ONLY ONE (MIN)
		Declaration min = possible.get(0);
		for(Declaration n : possible ){
                    if(min.specificity>n.specificity)
                        min=n;
		}
		//RETURN [returntype,accessor+NAME+_number]
		return new String[]{min.returntype, make_name(on_instance,min)};
	}
	/**
	 * helper method finds how specific the casting is 
	 * by way of distance from castee to casted_to
	 *
	 */
	private int gouptree(String casted_to, String castee){
		int distance = 0;
        
		if (castee.equals("char")) {
			if (casted_to.equals("short"))
				return -1;
			else
				castee = "short"; // has same precidence as a short, but can't be casted to short
		} 
		else if (casted_to.equals("char")) 
			return -1;//char cannot be implicitly casted to 
		
		final java.util.List<String> primatives = java.util.Arrays.asList(new String[]
				{"double", "float", "long", "int", "short", "byte" });
            
		if (primatives.contains(casted_to) && primatives.contains(castee))
			return primatives.indexOf(casted_to) - primatives.indexOf(castee);
		
		//search on root of tree for castee class
		InheritanceTree type = this.root.search(this.packages,castee);
		while( !type.className.equals(casted_to) ){
                    distance++;
                    type = type.superclass;
		}
		return distance;
	}	
	/**
	 * helper method returns proper method call 
	 * 
	 */
	private String make_name(boolean on_instance,Declaration d){
		String result="";
		if ((on_instance) && (d.isVirtual))
                    result= "->vtpr->";
		else if ((on_instance) && (!d.isVirtual))
                    result=".";
		else
                    result= "";
		result+= d.name;
		if(d.overloadNum==0)return result;
		else return result+"_"+d.overloadNum;
	}

}