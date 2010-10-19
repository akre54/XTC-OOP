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
	public ArrayList<Vtable_entry> Vt_ptrs;
	public ArrayList<Vtable_entry> local;//all methods defined IN THIS CLASS
	public InheritanceTree superclass;
	public ArrayList<InheritanceTree> subclasses;

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
		Vt_ptrs.add(new Vtable_entry("Class", "__isa",new ArrayList<String>(0),className));
		
		//add hashcode to Vtable
		ArrayList<String> hashcode = new ArrayList<String>(0);
		hashcode.add("Object");
		Vt_ptrs.add(new Vtable_entry("int32_t","hashcode",
									 hashcode,"Object"));
		//add equals to Vtable
		ArrayList<String> equals = new ArrayList<String>(0);
		equals.add("Object");
		Vt_ptrs.add(new Vtable_entry("bool","equals",
									 equals,"Object"));
		//add getClass to Vtable
		ArrayList<String> getClass = new ArrayList<String>(0);
		getClass.add("Object");
		Vt_ptrs.add(new Vtable_entry("Class","getClass",
									 getClass,"Object"));
		//add toString to Vtable
		ArrayList<String> toString = new ArrayList<String>(0);
		toString.add("Object");
		Vt_ptrs.add(new Vtable_entry("String","toString",
									 toString,"Object"));

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
		
		//adds virtual Class methods ptrs to virtual Vtable
		ArrayList<String> toString = new ArrayList<String>(0);
		toString.add("Class");
		Vt_ptrs.add(new Vtable_entry("String","toString",
									 toString,"Class"));
		ArrayList<String> getName = new ArrayList<String>(0);
		getName.add("Class");
		Vt_ptrs.add(new Vtable_entry("String","getName",
									 getName,"Class"));
		ArrayList<String> getSuperclass = new ArrayList<String>(0);
		getSuperclass.add("Class");
		Vt_ptrs.add(new Vtable_entry("String","getSuperclass",
									 getSuperclass,"Class"));
		ArrayList<String> getComponentType = new ArrayList<String>(0);
		getComponentType.add("Class");
		Vt_ptrs.add(new Vtable_entry("String","getComponentType",
									 getComponentType,"Class"));
		ArrayList<String> isPrimitive = new ArrayList<String>(0);
		isPrimitive.add("Class");
		Vt_ptrs.add(new Vtable_entry("String","isPrimitive",
									 isPrimitive,"Class"));
		ArrayList<String> isArray = new ArrayList<String>(0);
		isArray.add("Class");
		Vt_ptrs.add(new Vtable_entry("String","isArray",
									 isArray,"Class"));
		ArrayList<String> isInstance = new ArrayList<String>(0);
		isInstance.add("Class");
		isInstance.add("Object");
		Vt_ptrs.add(new Vtable_entry("String","isInstance",
									 isInstance,"Class"));
		
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
		
		//local arraylist defined
		local = new ArrayList<Vtable_entry>(0);
				
		//copies the superclass's Vtable into virtual Vtable
		Vt_ptrs = new ArrayList<Vtable_entry>(supr.Vt_ptrs);
		
		//change __isa field to point to this class's feild
		Vt_ptrs.get(0).ownerClass = className;
		
		//defines arraylist of the virtual methods of this class
		ArrayList<Vtable_entry> virtual = addvirtualptrs(n);
		
		//checks to if virtual methods overwrite superclass methods
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
			
			
			public void visitClassDeclaration(GNode n){
				className = n.getString(1);
				visit(n);
			}
			public void visitMethodDeclaration(GNode n){
				//clear variables
				retrn="";
				methodname = n.getString(3);
				params.clear();
				System.out.println(params.isEmpty());
				is_virtual = true;
				
				//go into tree to get info
				visit(n);
				
				
				//test to see if the modifier was public (if it should be virtual)
				if(is_virtual) virtual.add(new Vtable_entry(retrn,methodname,params,className));
				//add it to local no matter what 	
				local.add(new Vtable_entry(retrn,methodname,params,className));
								
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
			public void visit(Node n) {
				for (Object o : n) if (o instanceof Node) dispatch((Node)o);
			}
			
		}.dispatch(n);
	
		return virtual;
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