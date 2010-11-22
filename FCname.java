package xtc.oop;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;

class FCname {
}
	// private StringBuffer newName;
// 	InheritanceTree treeClass;
// 	Declaration treeMethod;
// 	GNode n;
// 	String name;
// 	FCname(final InheritanceTree treeClass1, final Declaration treeMethod1, final GNode n1, String name1) {
// 		treeClass = treeClass1;
// 		treeMethod = treeMethod1;
// 		n = n1;
// 		name = name1;
// 	}
// 	public String makeName () {
// 		if (handlePrint(name)) return newName.toString();
		
// 		return name;
// 	}
// 	private boolean handlePrint(String name) {
// 		boolean isPrint = false, isPrintln = false;
// 		if (name.contains("System.out.print"))
// 			isPrint = true;
// 		if (name.contains("System.out.println"))
// 			isPrintln = true;
// 		if(isPrint) newName.append("std::cout<<");
// 		//must visit the arguments
		
// 		if(isPrintln) newName.append("std::endl");
// 		return (toPrint);
// 	}
// }