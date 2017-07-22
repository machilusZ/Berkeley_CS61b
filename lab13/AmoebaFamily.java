import java.util.*;

public class AmoebaFamily implements Iterable<AmoebaFamily.Amoeba>{

	public Amoeba root = null;

	// A constructor that starts an Amoeba family with an amoeba
	// with the given name.
	public AmoebaFamily(String name) {
		root = new Amoeba(name, null);
	}

	// Add a new amoeba named childName as the youngest child
	// of the amoeba named parentName.
	// Precondition: the amoeba family contains an amoeba named parentName.
	public void addChild(String parentName, String childName) {
		if (root != null) {
            root.addChild(parentName, childName);
		}
	}

	// Makes all Amoeba names only lower case letters.
	public void makeNamesLowercase() {
		// Your goal is to make this as similar as possible to addChild
		if (root != null) {
			root.makeNamesLowercase();
		}
	}

	// Replaces the name of an amoeba named currentName with the name newName.
	// Precondition: the amoeba family contains an amoeba named currentName.
	public void replaceName(String currentName, String newName) {
		// Your goal is to make this as similar as possible to addChild
		if (root != null) {
			root.replaceName(currentName,newName);
		}
	}

	// Print the names of all amoebas in the family.
	// later you will write print() that has more interesting formatting
	public void printFlat() {
		// Your goal is to make this as similar as possible to addChild
		if (root != null) {
			root.printFlat();
		}
	}

	// Print the names of all amoebas in the family.
	// Names should appear in preorder, with children's names
	// printed oldest first.
	// Members of the family constructed with the main program above
	// should be printed in the following sequence:
	// Amos McCoy, mom/dad, me, Mike, Bart, Lisa, Homer, Marge,
	// Bill, Hilary, Fred, Wilma, auntie
    // This is the pretty print exercise.
	public void print() {
		if (root != null) {
			root.print(0);
		}
		System.out.println();
	}

	// returns the length of the longest name in the Amoeba Family
	public int longestNameLength() {
		if (root != null) {
			return root.longestNameLength();
		}
		return 0;
	}

	// instead of returning the length of the longest name, this method should
	// return the name that is longest.
	public String longestName() {
		// your goal is to make this look as similar as possible to
		// longestNameLength
		if (root != null) {
			return root.longestName();
		}
		return "";
	}

	public int size() {
		if (root != null) {
			return root.size();
		}
		return 0;
	}

	// Return an iterator of the amoeba family.
	public Iterator<Amoeba> iterator() {
		return new AmoebaIterator();
	}

	public int height() {
		if (root != null) {
			return root.height(root);
		}
		return 0;
	}

	public static void main(String[] args) {
		AmoebaFamily family = new AmoebaFamily("Amos McCoy");
		family.addChild("Amos McCoy", "mom/dad");
		family.addChild("Amos McCoy", "auntie");
		family.addChild("mom/dad", "me");
		family.addChild("mom/dad", "Fred");
		family.addChild("mom/dad", "Wilma");
		family.addChild("me", "Mike");
		family.addChild("me", "Homer");
		family.addChild("me", "Marge");
		family.addChild("Mike", "Bart");
		family.addChild("Mike", "Lisa");
		family.addChild("Marge", "Bill");
		family.addChild("Marge", "Hilary");
		System.out.println("Here's the family:");
		//family.print();
		//Iterator a = family.iterator();
		//while(a.hasNext()){
		//	System.out.print(a.next());
		//}

	}

	public class AmoebaIterator implements Iterator<Amoeba> {
		// Amoebas in the family are enumerated in preorder,
		// with children enumerated oldest first.
		// Members of the family constructed with the main program above
		// should be enumerated in the following sequence:
		// Amos McCoy, mom/dad, me, Mike, Bart, Lisa, Homer, Marge,
		// Bill, Hilary, Fred, Wilma
		// Complete enumeration of a family of N amoebas should take
		// O(N) operations.

		// You will supply the details of this class in a future lab.
		//ArrayList<Amoeba> fringe;
		LinkedList<Amoeba> fringe;
		public AmoebaIterator() {
			fringe = new LinkedList<Amoeba>();
			if(root!=null){
				System.out.print(root);
				fringe.add(root);
			}
			else{
				throw new UnsupportedOperationException();
			}
		}

		public boolean hasNext() {
			return fringe.size()>0;
		//	return !fringe.isEmpty();
			//throw new UnsupportedOperationException();
		}

		public Amoeba next() {
			if (!hasNext()) {
				throw new NoSuchElementException("tree ran out of elements");
			}
			else{
				Amoeba node = fringe.removeFirst();
				if (node.children!=null){
					for(Amoeba a : node.children){
						fringe.add(a);
					}
				}
				return node;

			}
		}
			//throw new UnsupportedOperationException();
		//	else {
		//		Amoeba node = fringe.get(fringe.size()-1);
		//		fringe.remove(fringe.size()-1);
		//		if (node.children!=null){
		//			for(int i = node.children.size()-1; i >= 0; i--){
		//				fringe.add(node.children.get(i));
		//			}
		//		}
		//		return node;
		//	}
		//}

		public void remove() {
			// Not used for now -- removal from a tree can be difficult.
			// Once you've learned about different ways to remove from
			// trees, it might be a good exercise to come back and
			// try to implement this.
		}

	} // end of AmoebaIterator nested class

	public static class Amoeba {

		public String name; // amoeba's name
		public Amoeba parent; // amoeba's parent
		public ArrayList<Amoeba> children; // amoeba's children

		public Amoeba(String name, Amoeba parent) {
			this.name = name;
			this.parent = parent;
			children = new ArrayList<Amoeba>();
		}

		public String toString() {
			return name;
		}

		public Amoeba parent() {
			return parent;
        }

        //Add a child if parent name matches an amoeba's name,
        //or if parentName matches any of the descendents
        public void addChild(String parentName, String childName) {
            if (name.equals(parentName)) {
                Amoeba child = new Amoeba(childName, this);
                children.add(child);
            } else {
                for (Amoeba a : children) {
                    a.addChild(parentName, childName);
                }
            }
        }
        
        public void makeNamesLowercase() {
        	name = name.toLowerCase();
        	for (Amoeba a : children) {
        		a.makeNamesLowercase();
        	}
        }
        
        public void replaceName(String currentName, String newName) {
        	if (name.equals(currentName)) {
        		name = newName;
        	} else {
        		for (Amoeba a : children) {
        			a.replaceName(currentName, newName);
        		}
        	}
        	
        }
        
        public void printFlat() {
        	System.out.println(name);
        	for (Amoeba a : children) {
        		a.printFlat();
        	}
        }
        
        public void print(int indentation) {
    		System.out.println();
    		for (int i = 0; i < indentation; i += 1) {
    			System.out.print("    ");
    		}
    		System.out.print(name);
    		for (Amoeba a : children) {
    			a.print(indentation + 1);
    		}
        }

        //Returns the length of the longest name of this Amoeba's children
        public int longestNameLength() {
            int maxLengthSeen = name.length();
            for (Amoeba a : children) {
                maxLengthSeen = Math.max(maxLengthSeen, a.longestNameLength());
            }
            return maxLengthSeen;
        }
        
        public String longestName() {
    		String maxName = name;
    		for (Amoeba a : children) {
    			String childMaxName = a.longestName();
    			if (childMaxName.length() > maxName.length()) {
    				maxName = childMaxName;
    			}
    		}
    		return maxName;
        }
        
        public int size() {
    		int size = 1;
    		for (Amoeba a : children) {
    			size += a.size();
    		}
    		return size;
        }
        
        public int height(Amoeba x) {
			if (x.children.isEmpty()) {
				return 1;
			} else {
				int bestSoFar = 1;
				for (Amoeba a : x.children) {
					if (this.height(a) >= bestSoFar) {
						bestSoFar = 1 + Math.max(this.height(a), bestSoFar);
					}
				}
				return bestSoFar;
			}
		} 
    }
} 

