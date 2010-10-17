package oop;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;

import xtc.util.Tool;
/**
 * Detects and correctly translates the java System.out.print and prinln
 * 
 * What needs to happen here:
 * Identify a call to System.out.print
 * Translate the contents of that call into c++
 * -look for toString
 *
 * Only difference between print and println
 * is that println should end with << eldl;
 */ 
public class SystemPrint {

	public void SystemPrint () {

	}

}