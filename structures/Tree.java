package structures;

import java.util.*;

/**
 * This class implements an HTML DOM Tree. Each node of the tree is a TagNode, with fields for
 * tag/text, first child and sibling.
 * 
 */
public class Tree {
	
	/**
	 * Root node
	 */
	TagNode root=null;
	
	/**
	 * Scanner used to read input HTML file when building the tree
	 */
	Scanner sc;
	
	/**
	 * Initializes this tree object with scanner for input HTML file
	 * 
	 * @param sc Scanner for input HTML file
	 */
	public Tree(Scanner sc) {
		this.sc = sc;
		root = null;
	}
	
	/**
	 * Builds the DOM tree from input HTML file, through scanner passed
	 * in to the constructor and stored in the sc field of this object. 
	 * 
	 * The root of the tree that is built is referenced by the root field of this object.
	 */
	public void build() {
		/** COMPLETE THIS METHOD **/
		String name = sc.nextLine();
		root = new TagNode("html", null, null);
		TagNode ptr = root;
		Stack folders = new Stack();
		folders.push(ptr);
		
		int i = 0;
		while(!folders.isEmpty()){
			
			//assigns the next line to name
			name = sc.nextLine();
			
			//checks if it is at the end of the html file
			if(name.equals("</html>")){
				break;
			}
			
			ptr = (TagNode) folders.peek();
			
			
			
			
			//builds the tree
			
			//if it has < & >
			if(name.charAt(0)=='<'){
				
				//if it is closing a folder
				if(name.charAt(1)=='/'){
					folders.pop();
				}
				
				//if it is opening a folder
				else{
					name = name.substring(1,name.length()-1);
					
					//adding to the firstChild
					if(ptr.firstChild == null){
						ptr.firstChild = new TagNode(name, null, null);
						folders.push(ptr.firstChild);
						
					}
					//adding to the sibling
					else{
						ptr = ptr.firstChild;
						while(true){
							
							if(ptr.sibling == null){
								ptr.sibling = new TagNode(name,null,null);
								folders.push(ptr.sibling);
								
								break;
							}
							ptr = ptr.sibling;
						}
					}
				}
			}
			//if it doesn't have < & >
			else{
				if(ptr.firstChild == null){
					ptr.firstChild = new TagNode(name,null,null);
				}
				else{
					ptr = ptr.firstChild;
					while(true){
						if(ptr.sibling == null){
							ptr.sibling = new TagNode(name,null,null);
							break;
						}
						ptr = ptr.sibling;
					}
					
				}
			}
		}
	}
	
	/**
	 * Replaces all occurrences of an old tag in the DOM tree with a new tag
	 * 
	 * @param oldTag Old tag
	 * @param newTag Replacement tag
	 */
	public void replaceTag(String oldTag, String newTag) {
		/** COMPLETE THIS METHOD **/
		TagNode ptr = root;
		Stack tags = new Stack();
		Stack checked = new Stack();
		tags.push(ptr);
		checked.push(ptr);
		TagNode prev;
		
		while(true){
			if(tags.isEmpty()){
				break;
			}
			
			ptr = (TagNode) tags.peek();
			
			//when it is popping nodes out of tags and pushing into checked
			if(ptr.firstChild == null && ptr.sibling == null){
				System.out.println(tags.peek());
				checked.push(tags.pop());
			}
			else if(checked.peek() == ptr.sibling){
				System.out.println(checked.peek());
				checked.push(tags.pop());
			}
			else if(checked.peek() == ptr.firstChild && ptr.sibling == null){
				System.out.println(checked.peek());
				checked.push(tags.pop());
			}
			//when it is pushing nodes into tagged
			else{
				if(ptr.firstChild != null && ptr.firstChild != checked.peek()){
					tags.push(ptr.firstChild);
				}
				else if(ptr.sibling != null && ptr.sibling != checked.peek()){
					tags.push(ptr.sibling);
				}
				
			}
			
		}//end of loop
		
		System.out.println();
		
		
		//Goes through stack and changes the names
		while(!checked.isEmpty()){
			System.out.println(checked.peek());
			ptr = (TagNode) checked.pop();
			if(ptr.tag.equals(oldTag)){
				ptr.tag = newTag;
			}
			
		}
		
	} 
	
	
	/**
	 * Boldfaces every column of the given row of the table in the DOM tree. The boldface (b)
	 * tag appears directly under the td tag of every column of this row.
	 * 
	 * @param row Row to bold, first row is numbered 1 (not 0).
	 */
	public void boldRow(int row) {
		/** COMPLETE THIS METHOD **/
		int counter = 0;
		Stack tags = new Stack();
		Stack checked = new Stack();
		Stack rowCheck = new Stack();
		rowCheck.push(root);
		tags.push(root);
		checked.push(root);
		TagNode ptr = root;
		
		
		while(true){
			if(tags.isEmpty()){
				break;
			}
			
			ptr = (TagNode) tags.peek();
			
			if(ptr.tag.equals("tr") && ptr!= rowCheck.peek()){
				counter ++;
				rowCheck.push(ptr);
			}
			
			if(counter == row && ptr.tag.equals("td")){
				
				TagNode temp = new TagNode("b", ptr.firstChild, null);
				if(!ptr.firstChild.tag.equals("b")){
					ptr.firstChild = temp;
				}
			}
				
			//when it is popping nodes out of tags and pushing into checked
			if(ptr.firstChild == null && ptr.sibling == null){
				System.out.println(tags.peek());
				checked.push(tags.pop());
			}
			else if(checked.peek() == ptr.sibling){
				System.out.println(checked.peek());
				checked.push(tags.pop());
			}
			else if(checked.peek() == ptr.firstChild && ptr.sibling == null){
				System.out.println(checked.peek());
				checked.push(tags.pop());
			}
			//when it is pushing nodes into tagged
			else{
				if(ptr.firstChild != null && ptr.firstChild != checked.peek()){
					tags.push(ptr.firstChild);
				}
				else if(ptr.sibling != null && ptr.sibling != checked.peek()){
					tags.push(ptr.sibling);
				}
				
			}
			
		}//end of loop
		
	}
	
	/**
	 * Remove all occurrences of a tag from the DOM tree. If the tag is p, em, or b, all occurrences of the tag
	 * are removed. If the tag is ol or ul, then All occurrences of such a tag are removed from the tree, and, 
	 * in addition, all the li tags immediately under the removed tag are converted to p tags. 
	 * 
	 * @param tag Tag to be removed, can be p, em, b, ol, or ul
	 */
	public void removeTag(String tag) {
		/** COMPLETE THIS METHOD **/
		
		Stack tags = new Stack();
		Stack checked = new Stack();
		tags.push(root);
		checked.push(root);
		TagNode ptr = root;
		TagNode prev = null;
		TagNode ptr2 = null;
		int counter = 0;
		
		while(true){
			if(tags.isEmpty()){
				break;
			}
			
			counter ++;
			if(counter >100){
				System.out.println("infinite");
				break;
			}
			
			
			
			
			ptr = (TagNode) tags.peek();
			System.out.println("pointer: " + ptr.tag);
			System.out.println("stack: " + tags.peek());
			
			
			//if the node's tag = the tag
			if(ptr.tag.equals(tag)){
				
				
				System.out.println("prev: " + prev.tag);
				System.out.println("ptr: " + ptr.tag);
				
				
//********************************************************************************			
				//if tag = "ul" or "ol"
				if(tag.equals("ul") || tag.equals("ol")){
					
					if(tags.peek()==ptr){
						tags.pop();
					}
					//use a while loop to check if any of the nodes children are li tags
					if(ptr.firstChild!= null){
						ptr2 = ptr.firstChild;
						//loops to go through all the children
						while(ptr2 != null){
							if(ptr2.tag.equals("li")){
								ptr2.tag = "p";
							}
							ptr2 = ptr2.sibling;
						}
					}
					
					
					if(ptr == prev.firstChild){
						prev.firstChild = ptr.firstChild;
						ptr2 = prev.firstChild;
					}
					if(ptr == prev.sibling){
						prev.sibling = ptr.firstChild;
						ptr2 = prev.sibling;
					}
					
					ptr.sibling = null;
					ptr.firstChild = null;
					System.out.println(ptr2.tag);
					tags.push(ptr2);
					continue;
					
				}//end of if tag = ul or ol
//********************************************************************************
				
				
				//if tag is not "ul" or "ol"
				else{
					//if ptr has a child node
					if(ptr.firstChild != null){
						
						//if ptr is at the top of the stack
						if(tags.peek() == ptr){
							tags.pop();
						}
												
						if(ptr == prev.sibling){
							prev.sibling = ptr.firstChild;
							ptr2 = prev.sibling;
						}
						else if(ptr == prev.firstChild){
							prev.firstChild = ptr.firstChild;
							ptr2 = prev.firstChild;
							System.out.println("ptr2: " + ptr2.tag);
						}
						ptr.firstChild = null;
								
						
						
						//assigns the node's sibling to the child's sibling
						if(ptr.sibling!= null){
							ptr2.sibling = ptr.sibling;
							ptr.sibling = null;
						}
						tags.push(ptr2);
						continue;
						
					}
					
					//if node has no child, but has sibling
					else if(ptr.sibling != null){
						
						//if ptr is at the top of the stack
						if(tags.peek() == ptr){
							tags.pop();
						}
						
						prev.sibling = ptr.sibling;
						ptr.sibling = null;
						ptr2 = prev.sibling;
						tags.push(ptr2);
						continue;
					}
					
					//if node has no child or sibling
					else{
						if(ptr == prev.sibling){
							prev.sibling = null;
						}
						else if(ptr == prev.firstChild){
							prev.firstChild = null;
						}
							
					}
					
				}//end of else statement
				
			}//end of if ptr.tag = tag
//*****************************************************************************************
			
			//traversing the tree
			//when it is popping nodes out of tags and pushing into checked
			if(ptr.firstChild == null && ptr.sibling == null){
				checked.push(tags.pop());
			}
			else if(checked.peek() == ptr.sibling){
				checked.push(tags.pop());
			}
			else if(checked.peek() == ptr.firstChild && ptr.sibling == null){
				checked.push(tags.pop());
			}
			//when it is pushing nodes into tagged
			else{
				if(ptr.firstChild != null && ptr.firstChild != checked.peek()){
					tags.push(ptr.firstChild);
				}
				else if(ptr.sibling != null && ptr.sibling != checked.peek()){
					tags.push(ptr.sibling);
				}
				
			}
			prev = ptr;
		}//end of loop
		
		
	}
	
	/**
	 * Adds a tag around all occurrences of a word in the DOM tree.
	 * 
	 * @param word Word around which tag is to be added
	 * @param tag Tag to be added
	 */
	public void addTag(String word, String tag) {
		/** COMPLETE THIS METHOD **/
		
		TagNode ptr = root;
		Stack tags = new Stack();
		Stack checked = new Stack();
		tags.push(ptr);
		checked.push(ptr);
		TagNode prev = null;
		int counter = 0;
		
		while(true){
			
			counter ++;
			if(counter > 100)
				break;
			
			if(tags.isEmpty()){
				break;
			}
			
			ptr = (TagNode) tags.peek();
			
			System.out.println("ptr: " + ptr.tag);
//************************************************************************
			
			//searching for the word in the tag
			String temp = ptr.tag + " ";
			int left = 0;
			int right = 0;;
			
			for(int i = 0; i < temp.length()-word.length(); i++){
				if(word.equals(temp.substring(i, i+word.length()))){
					temp = temp.substring(i, i+word.length());
					left = i;
					right = i + word.length() ;
					System.out.println("---FOUND---");
					break;
				}
			}
			
			//if word is found
			//there are four possible scenarios.
				//1. the tag = word
				//2. the word is at the beginning of the tag
				//3. the word is at the end of the tag
				//4. the word is in the middle of the tag
			if(temp.equals(word)){
				System.out.println("---Performance Triggered--");
				if(tags.peek()==ptr){
					tags.pop();
				}
				
				TagNode mochi = null;
				TagNode mochi2 = null;
				TagNode mochi3 = null;
				String rightSide;
				String leftSide;
				
				//Scenario 1
				if(ptr.tag.equals(word)){
					
					if(prev.firstChild == ptr){
						mochi = new TagNode(tag, ptr, ptr.sibling);
						prev.firstChild = mochi;
						ptr.sibling = null;
					}
					else if(prev.sibling == ptr){
						mochi = new TagNode(tag, ptr, ptr.sibling);
						prev.sibling = mochi;
						ptr.sibling = null;
					}
					//change the tag of the node so that it doesn't keep doing stuff with it
					ptr.tag = "temp";
					
					tags.push(mochi);
					continue;
				}
				
				
				//Scenario 2: if the word is at the beginning of the tag
				else if(left == 0){
					rightSide = ptr.tag.substring(right);
					mochi2 = new TagNode(rightSide, null, ptr.sibling);
					mochi = new TagNode(tag, ptr, mochi2);
					ptr.sibling = null;
					ptr.tag = "temp";
					
					if(prev.firstChild == ptr){
						prev.firstChild = mochi;
					}
					else if(prev.sibling == ptr){
						prev.sibling = mochi;
					}
					
					tags.push(mochi);
					continue;
				}
				
				//Scenario 3: if the word is at the end of the tag
				else if(right == ptr.tag.length()){
					leftSide = ptr.tag.substring(0, left);
					mochi2 = new TagNode(tag, ptr, ptr.sibling);
					mochi = new TagNode(leftSide, null, mochi2);
					ptr.sibling = null;
					ptr.tag = "temp";
					
					if(prev.firstChild == ptr){
						prev.firstChild = mochi;
					}
					else if(prev.sibling == ptr){
						prev.sibling = mochi;
					}
					tags.pop();
					continue;
				}
				
				//Scenario 4
				else{
					leftSide = ptr.tag.substring(0, left);
					rightSide = ptr.tag.substring(right);
					mochi3 = new TagNode(rightSide, null, ptr.sibling);
					mochi2 = new TagNode(tag, ptr, mochi3);
					mochi = new TagNode(leftSide, null, mochi2);
					ptr.sibling = null;
					ptr.tag = "temp";
					
					if(prev.firstChild == ptr){
						prev.firstChild = mochi;
					}
					else if(prev.sibling == ptr){
						prev.sibling = mochi;
					}
					tags.pop();
					continue;
				}
				
				
			}
			
//**************************************************************************
			
			//traversing the tree
			if(ptr.firstChild == null && ptr.sibling == null){
				checked.push(tags.pop());
			}
			else if(checked.peek() == ptr.sibling){
				checked.push(tags.pop());
			}
			else if(checked.peek() == ptr.firstChild && ptr.sibling == null){
				checked.push(tags.pop());
			}
			//when it is pushing nodes into tagged
			else{
				if(ptr.firstChild != null && ptr.firstChild != checked.peek()){
					tags.push(ptr.firstChild);
				}
				else if(ptr.sibling != null && ptr.sibling != checked.peek()){
					tags.push(ptr.sibling);
				}
				
			}
			prev = ptr;
			
		}//end of loop
		System.out.println("____________________________");
		//change all the nodes back to their original names
		while(!checked.isEmpty()){
			System.out.println(checked.peek());
			ptr = (TagNode) checked.pop();
			if(ptr.tag.equals("temp")){
				ptr.tag = word;
			}
		}
		
	}
	
	/**
	 * Gets the HTML represented by this DOM tree. The returned string includes
	 * new lines, so that when it is printed, it will be identical to the
	 * input file from which the DOM tree was built.
	 * 
	 * @return HTML string, including new lines. 
	 */
	public String getHTML() {
		StringBuilder sb = new StringBuilder();
		getHTML(root, sb);
		return sb.toString();
	}
	
	private void getHTML(TagNode root, StringBuilder sb) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			if (ptr.firstChild == null) {
				sb.append(ptr.tag);
				sb.append("\n");
			} else {
				sb.append("<");
				sb.append(ptr.tag);
				sb.append(">\n");
				getHTML(ptr.firstChild, sb);
				sb.append("</");
				sb.append(ptr.tag);
				sb.append(">\n");	
			}
		}
	}
	
	/**
	 * Prints the DOM tree. 
	 *
	 */
	public void print() {
		print(root, 1);
	}
	
	private void print(TagNode root, int level) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			for (int i=0; i < level-1; i++) {
				System.out.print("      ");
			}
			if (root != this.root) {
				System.out.print("|---- ");
			} else {
				System.out.print("      ");
			}
			System.out.println(ptr.tag);
			if (ptr.firstChild != null) {
				print(ptr.firstChild, level+1);
			}
		}
	}
}
