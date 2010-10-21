package xtc.oop;
import java.util.ArrayList;
import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;

/**
 * InheritanceTree builds a tree structure so subclasses 
 *  can access superclass's vtables when building their own vtables.
 * links all the classes together in a heiarchy for easy searchability
 *
 */
public class InheritanceTree{
	
	public final String className;
	public ArrayList<InstanceField> fields;
	public ArrayList<Vtable_entry> Vt_ptrs;
	public ArrayList<Vtable_entry> local;//all methods defined IN THIS CLASS virtual or not!
	public InheritanceTree superclass;
	public ArrayList<InheritanceTree> subclasses;
	public ArrayList<ConstructorDec> constructors;

	/**
	 * The constructor for creating the Object class Inheritance tree.
	 * @param none
	 * 
	 */
	InheritanceTree(){
		//defines class name
		className = "Object";
		
		//no superclass for Object
		superclass = null;
		
		//creates Vtable arraylist
		Vt_ptrs = new ArrayList<Vtable_entry>(0);
		
		//add __isa to Vtable
		Vt_ptrs.add(new Vtable_entry("Class", "__isa",new ArrayList<String>(0),
									 className,new ArrayList<String>(0)));
		
		//add hashcode to Vtable
		ArrayList<String> hashcode = new ArrayList<String>(0);
		Vt_ptrs.add(new Vtable_entry("int32_t","hashcode",
									 hashcode,"Object",new ArrayList<String>(0)));
		//add equals to Vtable
		ArrayList<String> equals = new ArrayList<String>(0);
		Vt_ptrs.add(new Vtable_entry("bool","equals",
									 equals,"Object",new ArrayList<String>(0)));
		//add getClass to Vtable
		ArrayList<String> getClass = new ArrayList<String>(0);
		Vt_ptrs.add(new Vtable_entry("Class","getClass",
									 getClass,"Object",new ArrayList<String>(0)));
		//add toString to Vtable
		ArrayList<String> toString = new ArrayList<String>(0);
		Vt_ptrs.add(new Vtable_entry("String","toString",
									 toString,"Object",new ArrayList<String>(0)));

		//subclass are initalized to a 0 element arraylist
		subclasses = new ArrayList<InheritanceTree>(0);

	}
	
	/**
	 * The constructor for creating the Class class.
	 * @param InheritanceTree (the Object inheritance tree)
	 */
	InheritanceTree(InheritanceTree supr){
		//defines superclass
		superclass = supr;
		//defines class name
		className = "Class";
		
		//copies the superclass's Vtable into virtual Vtable
		Vt_ptrs = new ArrayList<Vtable_entry>(supr.Vt_ptrs);
		
		//change __isa field to point to this class's feild
		Vt_ptrs.get(0).ownerClass = className;
		
		//change toString to point to class name
		Vt_ptrs.get(4).ownerClass = className;

		//adds virtual Class methods ptrs to virtual Vtable
		ArrayList<String> getName = new ArrayList<String>(0);
		Vt_ptrs.add(new Vtable_entry("String","getName",
									 getName,"Class",new ArrayList<String>(0)));
		ArrayList<String> getSuperclass = new ArrayList<String>(0);
		Vt_ptrs.add(new Vtable_entry("String","getSuperclass",
									 getSuperclass,"Class",new ArrayList<String>(0)));
		ArrayList<String> getComponentType = new ArrayList<String>(0);
		Vt_ptrs.add(new Vtable_entry("String","getComponentType",
									 getComponentType,"Class",new ArrayList<String>(0)));
		ArrayList<String> isPrimitive = new ArrayList<String>(0);
		Vt_ptrs.add(new Vtable_entry("String","isPrimitive",
									 isPrimitive,"Class",new ArrayList<String>(0)));
		ArrayList<String> isArray = new ArrayList<String>(0);
		Vt_ptrs.add(new Vtable_entry("String","isArray",
									 isArray,"Class",new ArrayList<String>(0)));
		ArrayList<String> isInstance = new ArrayList<String>(0);
		isInstance.add("Object");
		Vt_ptrs.add(new Vtable_entry("String","isInstance",
									 isInstance,"Class",new ArrayList<String>(0)));
		
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
		//superclass defined
		superclass = supr;
		
		//className defined from node
		className = n.getString(1);
		
		//field arraylist defined with all field declarations
		fields = addfielddeclarations(n);
		
		//constructors arraylist defined
		constructors = addConstructors(n);
		
		//local arraylist defined
		local = new ArrayList<Vtable_entry>(0);

		//copies the superclass's Vtable into virtual Vtable
		Vt_ptrs = new ArrayList<Vtable_entry>(supr.Vt_ptrs);
		
		//change __isa methods to point to this class
		Vt_ptrs.get(0).ownerClass = className;
		
		//defines arraylist of the virtual methods of this class
		ArrayList<Vtable_entry> virtual = addvirtualptrs(n);
		
		//checks if virtual methods overwrite superclass methods
		check_for_overwrites(virtual);
		
		//add virtual methods to vtable
		Vt_ptrs.addAll(virtual);
			
		//subclass are initalized to a 0 element arraylist
		subclasses = new ArrayList<InheritanceTree>(0);
		
		//add this tree to the superclasses' subclasses
		supr.subclasses.add(this);

	}
	/**
	 *cycles through all Vtable_entrys to see if virtual methods 
	 * overwrite the superclass method.
	 *@param ArrayList<Vtable_entry> ... always the virtual Vtable_entrys
	 */
	private void check_for_overwrites(ArrayList<Vtable_entry> virtual){
		for(int l = 0; l<virtual.size();l++){
			int index = contains(virtual.get(l));
			if(index!=-1)
				//remove the pointer that points to 
				//a method that is redefined in this class
				Vt_ptrs.remove(index);
			
		}
	}
	/**
	 * helper method used in check_for_overwrites() for testing for equal Vtable_entrys.
	 *@param Vtable_entry
	 */
	private int contains(Vtable_entry virtual){
		//starts at i =1 to ignore __isa feild that was already replaced
		for(int i = 1; i < Vt_ptrs.size();i++){
			if((Vt_ptrs.get(i).name.equals(virtual.name))
			   &&(Vt_ptrs.get(i).returntype.equals(virtual.returntype))
			   &&(Vt_ptrs.get(i).params.equals(virtual.params)))
				return i;
		}
		return -1;
	}
	/**
	 * uses visitors to create the Vtable_entrys for virtual methods.
	 * also documents local methods that are not virtual for definition later
	 *@param GNode (always a classDeclaration)
	 */
	public ArrayList<Vtable_entry> addvirtualptrs(GNode n){
		// cast to a node
		Node node = n;
		//declare a virutal arraylist<Vtable_entry>
		final ArrayList<Vtable_entry> virtual = new ArrayList<Vtable_entry>(0);

		new Visitor(){
			
			//declare initalize variables 
			String className= "";
			String retrn="";
			String methodname="";
			ArrayList<String> params = new ArrayList<String>(0);
			boolean is_fparam=false;
			boolean is_virtual=false;
			ArrayList<String> pnames = new ArrayList<String>(0);
			
			
			public void visitClassDeclaration(GNode n){
				className = n.getString(1);
				visit(n);
			}
			public void visitMethodDeclaration(GNode n){
				//clear variables
				retrn="";
				methodname = n.getString(3);
				params.clear();
				pnames.clear();
				is_virtual = true;
				
				//go into tree to get info
				visit(n);
				System.out.println("methods!!! - "+methodname);
				
				//test to see if the modifier was public (if it should be virtual)
				if(is_virtual) virtual.add(new Vtable_entry(retrn,methodname,params,className,pnames,n));
				//add it to local no matter what 	
				local.add(new Vtable_entry(retrn,methodname,params,className,pnames,n));
								
			}
			public void visitModifier(GNode n){
				 //notes that private/protected/staic methods are not virtual and should not go into Vtable
				 if((n.getString(0).equals("private"))||(n.getString(0).equals("protected"))||(n.getString(0).equals("static")))
					 is_virtual=false;
			}
			public void visitVoidType(GNode n){
				retrn = "void";
			}
			public void visitQualifiedIdentifier(GNode n){
				String type=n.getString(0);
				if(n.getString(0).equals("int")) type="int_32_t";
				if(n.getString(0).equals("boolean")) type="bool";
				if(is_fparam) params.add(type);
				else retrn = type;
			}
			public void visitFormalParameter(GNode n){//variable name
				pnames.add(n.getString(3));
				visit(n);
					
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
	public ArrayList<InstanceField> addfielddeclarations(GNode n){
		Node node = n;
		final ArrayList<InstanceField> f = new ArrayList<InstanceField>(0);
		new Visitor(){
			
			ArrayList<String> mods= new ArrayList<String>(0);
			String type;
			String name;
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
				mods.clear();
				type ="";
				name="";
				val="";
				
				visit(n);
				is_field = false;
				f.add(new InstanceField(mods,type,name,val));
			}
			//if not looking in FieldDeclaration's subtree ignore nodes
			
			public void visitModifier(GNode n){
				if(is_field){		
					mods.add(n.getString(0));
				}
				
			}
			public void visitPrimitiveType(GNode n){
				if(is_field){
					String type = n.getString(0);
					if(type.equals("int"))type="int_32_t";
					if(type.equals("boolean"))type="bool";
					
				}
			}
			public void visitDeclarator(GNode n){//variable name
				if(is_field){
					name = n.getString(0);
					

				}
			}
			public void visitQualifiedIdentifier(GNode n){//type
				if(is_field){
					type=n.getString(0);
									
				}
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
	 * returns an arraylist<ConstructorDec> that holds all constructors' info from class.
	 * @param Gnode n
	 * 
	 */
	private ArrayList<ConstructorDec> addConstructors(GNode n){
		Node node = n;
		final ArrayList<ConstructorDec> c = new ArrayList<ConstructorDec>(0);
		new Visitor(){
			ArrayList<String> fmods=new ArrayList<String>(0);
			String ftype;
			String fname;
			ArrayList<String> mods= new ArrayList<String>(0);
			ArrayList<Fparam> fp = new ArrayList<Fparam>(0);
			boolean is_fparam;
			boolean is_constr;
			
			public void visitConstructorDeclaration(GNode n){
				is_constr = true;
				//clear arraylists
				mods.clear();
				fp.clear();

				//get info from tree
				visit(n);
				
				//add constructordec to c
				c.add(new ConstructorDec(mods,fp,n));
				
				is_constr =false;
			}
			public void visitFormalParameter(GNode n){
				is_fparam =true;
				//clear
				fmods.clear();
				ftype ="";
				fname=n.getString(3);
				
				//get info from tree
				visit(n);
				
				//add new Fparam to fp
				fp.add(new Fparam(fmods,ftype,fname));
				is_fparam =false;
			}
			public void visitModifier(GNode n){
				if(is_fparam)fmods.add(n.getString(0));
				if(is_constr)mods.add(n.getString(0));
			}
			public void visitPrimitiveType(GNode n){
				ftype=n.getString(0);
				if(ftype.equals("int")) ftype ="int32_t";
				if(ftype.equals("boolean")) ftype ="bool";

			}
			public void visitQualifiedIdentifier(GNode n){
				ftype=n.getString(0);
			}
			public void visit(Node n) {
				for (Object o : n) if (o instanceof Node) dispatch((Node)o);
			}
	
		
		}.dispatch(node);
		return c;
		
	}
	/**
	 * search will return a InheritanceTree of matching className
	 *	or a null InheritanceTree if no tree exists yet as child of the root.
	 *@param String name
	 * this is meant to be used only on the root Object Tree so that the entire tree is searched
	 *
	 */
	public InheritanceTree search(String name){
		InheritanceTree found = null;
		if(this.className.equals(name))
			return this;
		
		for(int i=0; i< this.subclasses.size();i++){
			found = subclasses.get(i).search(name);
			if(found!=null) return found;
		}
		return found;
	
	}
}