if (runtime.test("test")) {
runtime.console().p("testing...").pln().flush();

/*________________________EDITED BY PAIGE PONZEKA 10/13/10____________________________*/


/*
 Visits the CompilationUnit
 -UNKNOWN
 -ImportDeclaration
 -ClassDeclaration
 */
new Visitor() {
//public void visitCompilationUnit(GNode n) {
//		visit(n);
//runtime.console().p("Number of methods: ").p(count).pln().flush();
//	}//end of visitCompilationUnit Method


/*
 
 CHILD VISIT MISSING HERE!!!
 
 
 */


//visit the ImportDeclartion
public void visitImportDeclaration(GNode n) {

//While debugging... Print the name of the current tree node
if(DEBUG){runtime.console().p(n.getName()).pln().flush();}

//creates a new visitor to visit the ImportDeclaration subtree
/*
 Visits ImportDeclaration:
 UNKNOWN
 -QualifiedIdentifier
 UNKNOWN
 */
new Visitor(){

public void visitQualifiedIdentifier(GNode n) {

//While debugging... Print the name of the current tree node
if(DEBUG){runtime.console().p(n.getName()).pln().flush();}

//runs a for loop for ALL the children of the QualifiedIdentifier Subtree
for(int i=0;i<(n.size());i++)
{
//n.getString(i) gets all the string names of the subtree	
runtime.console().p(n.getString(i)).pln().flush(); 
}
visit(n);
}//end of visitQualifiedIdentifier Method

public void visit(Node n) {
for (Object o : n) if (o instanceof Node) dispatch((Node)o);
} //end of visit method

}.dispatch(n);//end of ImportDeclaration Subtree

visit(n);
}//end of visitImportDeclaration Method



//visit Class Declaration
public void visitClassDeclaration(GNode n){
/*
 Visit Each ClassDeclaration Subtree
 -Modifiers
 ClassName
 -UNKNOWN
 -UNKNOWN
 -UNKNOWN
 -Class Body
 */

//While debugging... Print the name of the current tree node
if(DEBUG){runtime.console().p(n.getName()).pln().flush();}

//create a new visitor for the Modifier Subtree
new Visitor(){

//visit ClassDeclaration->modifier
public void visitModifier(GNode n) {

/*************ISSUE****************
 While this does get the modifer for the class it 
 also visits the modifer for any 
 subtree values  in the ClassBody ( A Child of ClassDeclaration)
 *********************************/

//While debugging... Print the name of the current tree node
if(DEBUG){runtime.console().p(n.getName()).pln().flush();}

for(int i=0;i<(n.size());i++)
{
//print the name of each modifier
runtime.console().p(n.getString(i)).pln().flush();
}
visit(n);
}//end of visitModifier Method

public void visit(Node n) {
for (Object o : n) if (o instanceof Node) dispatch((Node)o);
}//end of visit methods

}.dispatch(n); //end of Modifer subtree visitor

//print the ClassName
runtime.console().p(n.getString(1)).pln().flush();


/*
 
 CHILD VISIT MISSING HERE!!!
 
 
 */

/*
 
 CHILD VISIT MISSING HERE!!!
 
 
 */

/*
 
 CHILD VISIT MISSING HERE!!!
 
 
 */




//visit ClassDeclaration->ClassBody
/*
 Visit ClassBody subtree
 -constructorDeclaration
 -MethodDeclaration
 
 */
new Visitor(){
public void visitClassBody(GNode n)
{

//While debugging... Print the name of the current tree node
if(DEBUG){runtime.console().p(n.getName()).pln().flush();}

/*
 Visit ClassBody Subtree
 -MethodDeclaration
 */
new Visitor(){
//visit ClassBody-> ConstructionDeclaration
public void visitConstructorDeclaration(GNode n) {

//While debugging... Print the name of the current tree node
if(DEBUG){runtime.console().p(n.getName()).pln().flush();}

/*
 Visit ConstructorDeclaration Subtree
 -Modifiers
 UNKNOWN
 ConstructorName
 -FormalParameters
 UNKNOWN
 Block()
 */

//print out the name of the contructor
runtime.console().p(n.getString(2)).pln().flush();

new Visitor(){

//Visit ConstructionDeclaration-> Modifier
public void visitModifier(GNode n) {
//runs a for loop for ALL the children of the Qualified Identifier Subtree
for(int i=0;i<(n.size());i++)
{
//here we have to insert file readers and create the subfolders for the import declaration
runtime.console().p(n.getString(i)).pln().flush(); //xtc.tree.node
}
visit(n);
}//end of visitModifier method

/*
 
 CHILD VISIT MISSING HERE!!!
 
 
 */



public void visitFormalParameter(GNode n)
{
//While debugging... Print the name of the current tree node
if(DEBUG){runtime.console().p(n.getName()).pln().flush();} 


/*
 Visit FormalParameter Subtree
 Modifiers()
 -Type
 UNKNOWN
 parameterName
 UNKNOWN
 */
new Visitor(){
/*
 
 CHILD VISIT MISSING HERE!!!
 What to do with Motifiers()?
 
 
 */
public void visitType(GNode n){

//While debugging... Print the name of the current tree node
if(DEBUG){runtime.console().p(n.getName()).pln().flush();} 

//NEEDS TO PUT IN SUPPORT FOR ADDITIONAL TYPES IN HERE
/*
 Visit Type Subtree
 -PrimitiveType
 UNKNOWN
 */
new Visitor(){

public void visitPrimitiveType(GNode n){

//While debugging... Print the name of the current tree node
if(DEBUG){runtime.console().p(n.getName()).pln().flush();} 

//gets all the type children 
for(int i=0;i<(n.size());i++)
{
//print the type 
runtime.console().p(n.getString(i)).pln().flush(); 
}

visit(n);
}//end of visitPrimitiveType method


/*
 
 CHILD VISIT MISSING HERE!!!
 
 
 */


public void visit(Node n) {
for (Object o : n) if (o instanceof Node) dispatch((Node)o);
}
}.dispatch(n); //end of type subtree

visit(n);
}//end of visitType method




public void visit(Node n) {
for (Object o : n) if (o instanceof Node) dispatch((Node)o);
}//end of visit method

}.dispatch(n); //end of FormalParameter subtree Visitor


/*
 
 CHILD VISIT MISSING HERE!!!
 
 
 */


//print out ParameterName
runtime.console().p(n.getString(3)).pln().flush(); 


/*
 
 CHILD VISIT MISSING HERE!!!
 
 
 */


visit(n);
}//end of visitFormalParameter Method




/*
 
 CHILD VISIT MISSING HERE!!!
 
 
 */




/*
 
 CHILD VISIT MISSING HERE!!!
 What to do for Block()?
 
 */


public void visit(Node n) {
for (Object o : n) if (o instanceof Node) dispatch((Node)o);
}//end of visit method

}.dispatch(n);//end of ConstructorDeclaration subtree 
visit(n);
}//end of visitConstructorDeclaration methiod

//visit classBody-> MethodDeclaration
public void visitMethodDeclaration(GNode n) {

//While debugging... Print the name of the current tree node
if(DEBUG){runtime.console().p(n.getName()).pln().flush();} 

/*
 Visit MethodDeclaration Subtree
 -Modifiers
 UNKNOWN
 VoidType()-Other Types
 methodNAme
 -FormalParameters
 UNKNOWN
 UNKNOWN
 Block()
 */

new Visitor(){
//Visit MethodDeclaration-> Modifier
public void visitModifier(GNode n) {

//While debugging... Print the name of the current tree node
if(DEBUG){runtime.console().p(n.getName()).pln().flush();} 

//runs a for loop for ALL the children of the ModifierSubtree
for(int i=0;i<(n.size());i++)
{
//here we have to insert file readers and create the subfolders for the import declaration
runtime.console().p(n.getString(i)).pln().flush(); //xtc.tree.node
}
visit(n);
}//end of visitModifier Method
public void visit(Node n) {
for (Object o : n) if (o instanceof Node) dispatch((Node)o);
}
}.dispatch(n); //end of Modifier Subtree visitor



/*
 
 CHILD VISIT MISSING HERE!!!
 
 
 */






/*********ISSUE***************
 
 CHILD VISIT MISSING HERE!!!
 //print out the name of the type
 //can't print out VoidType
 runtime.console().p(n.getString(2)).pln().flush(); //<--Throws a ClassCastException: Not a Token
 
 
 ****************************/






//print out the methodName
runtime.console().p(n.getString(3)).pln().flush();

/*
 Visit FormalParameter Subtree
 Modifiers()
 -Type
 UNKNOWN
 parameterName
 UNKNOWN
 */
new Visitor(){
/*
 
 CHILD VISIT MISSING HERE!!!
 What to do with Motifiers()?
 
 
 */
public void visitType(GNode n){

//While debugging... Print the name of the current tree node
if(DEBUG){runtime.console().p(n.getName()).pln().flush();} 

//NEED TO PUT IN SUPPORT FOR ADDITIONAL TYPES IN HERE
/*
 Visit Type Subtree
 -QualifiedIdentifier
 -Dimensions
 
 */
new Visitor(){

public void visitQualifiedIdentifier(GNode n){

//While debugging... Print the name of the current tree node
if(DEBUG){runtime.console().p(n.getName()).pln().flush();} 

//gets all the children 
for(int i=0;i<(n.size());i++)
{
//print the children
runtime.console().p(n.getString(i)).pln().flush(); 
}

visit(n);
}//end of visitQualifiedIdentifier method

public void visitDimensions(GNode n){

//While debugging... Print the name of the current tree node
if(DEBUG){runtime.console().p(n.getName()).pln().flush();} 

//gets all the children 
for(int i=0;i<(n.size());i++)
{
//print the children
runtime.console().p(n.getString(i)).pln().flush(); 
}
visit(n);
}//end of visitDimensions method											
public void visit(Node n) {
for (Object o : n) if (o instanceof Node) dispatch((Node)o);
}
}.dispatch(n); //end of type subtree

visit(n);
}//end of visitType method





public void visit(Node n) {
for (Object o : n) if (o instanceof Node) dispatch((Node)o);
}
}.dispatch(n); //end of methodDeclaration->FormalParameter subtree Visitor


/*
 
 CHILD VISIT MISSING HERE!!!
 
 
 */


/************************ISSUE*****************
 //print out ParameterName
 runtime.console().p(n.getString(2)).pln().flush(); <--Returns java.lang.ClassCastException: not a token
 ***************************************/


/*
 
 CHILD VISIT MISSING HERE!!!
 
 
 */


/*********************************************************/
/*
 
 CHILD VISIT MISSING HERE!!!
 
 
 */
/*
 
 CHILD VISIT MISSING HERE!!!
 
 
 */

/*
 
 CHILD VISIT MISSING HERE!!!
 What to Do with Block()?
 
 */
visit(n);
}//end of visitMethodDeclaration method	

public void visit(Node n) {
for (Object o : n) if (o instanceof Node) dispatch((Node)o);
} // end of visit method

}.dispatch(n);//end of methodDeclaration subtree visitor


visit(n);
}//end of visitClassBody method


public void visit(Node n) {
for (Object o : n) if (o instanceof Node) dispatch((Node)o);
}

}.dispatch(n); //end of ClassBody Subtree visitor

visit(n);
}//end of visitClassDeclaration Method





//end of edited by paige 10/11/10


public void visit(Node n) {
for (Object o : n) if (o instanceof Node) dispatch((Node)o);
}

}.dispatch(node); //end of main dispatch