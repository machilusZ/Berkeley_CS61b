import jdk.internal.org.objectweb.asm.Handle;
import jdk.internal.org.objectweb.asm.tree.analysis.Interpreter;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import org.apache.commons.cli.HelpFormatter;
import sun.awt.image.ImageWatched;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;

public class BinaryTree {

    private TreeNode root;

    public BinaryTree() {
        root = null;
    }

    public BinaryTree(TreeNode t) {
        root = t;
    }

    public TreeNode getRoot() {
        return root;
    }

    // Print the values in the tree in preorder: root value first,
    // then values in the left subtree (in preorder), then values
    // in the right subtree (in preorder).
    public void printPreorder() {
        if (root == null) {
            System.out.println("(empty tree)");
        } else {
            root.printPreorder();
            System.out.println();
        }
    }
    public static BinaryTree fibTree(int n) {
        BinaryTree result = new BinaryTree();
        //TreeNode temp = new TreeNode(0);
        result.root= Helper(n, result.root);
        //System.out.println(result.root.getItem());
        return result;
    }
    public static TreeNode Helper(int n, TreeNode node){
        if(node==null){
            node = new TreeNode(0);
        }
        if(n<2){
            node.item=n;
            return node;
        }
        node.left = Helper(n-1, node.left);
        node.right = Helper(n-2, node.right);
        node.item = (int)node.getLeft().getItem()+(int)node.getRight().getItem();
        return node;
    }
    // Print the values in the tree in inorder: values in the left
    // subtree first (in inorder), then the root value, then values
    // in the right subtree (in inorder).
    public void printInorder() {
        if (root == null) {
            System.out.println("(empty tree)");
        } else {
            root.printInorder();
            System.out.println();
        }
    }

    public void print() {
        if (root != null) {
            root.print(0);
        }
    }

    public boolean check() {
        alreadySeen = new ArrayList();
        try {
            isOK(root);
            return true;
        } catch (IllegalStateException e) {
            return false;
        }
    }

    private void isOK(TreeNode t) {
        //System.out.print(item + " ");
        LinkedList<TreeNode> list = new LinkedList<TreeNode>();
        t.traverse(list);
        for(int i=0; i<list.size();i++){
            //Object comparator = list.get(i).getItem();
            for(int j=i+1; j<list.size();j++){
                if(list.get(j).getItem().equals(list.get(i).getItem())){
                    throw new IllegalStateException("");
                }
            }
        }
        //return true;
    }

    // Contains nodes already seen in the traversal.
    private ArrayList alreadySeen;
    //(IllegalStateException is provided in Java.)

    public void fillSampleTree1() {
        TreeNode temp = new TreeNode("a");
        root = new TreeNode("a", temp, temp);
    }

    public void fillSampleTree2() {
        root = new TreeNode("a", new TreeNode("b", new TreeNode("d",
                new TreeNode("e"), new TreeNode("f")), null), new TreeNode("c"));
    }

    public static BinaryTree exprTree(String s) {
        BinaryTree result = new BinaryTree();
        result.root = result.exprTreeHelper(s);
        return result;
    }

    public void optimize(){
        if(root==null){
            return;
        }
        else{
            root = Helper1(root);
        }
    }
    public static TreeNode Helper1(TreeNode node){
        if(node.left==null){
            return node;
        }
        node.left = Helper1(node.left);
        node.right = Helper1(node.right);
        //Regex r =
        if(node.left.item.toString().matches("[0-9]+")&&node.right.item.toString().matches("[0-9]+")){
            if(node.item.toString().equals("+")){
                node.item=String.valueOf(Integer.parseInt((String)node.left.item) + Integer.parseInt((String) node.right.item));
            }else{

                node.item=String.valueOf(Integer.parseInt((String)node.left.item) * Integer.parseInt((String) node.right.item));
            }
            node.left=null;
            node.right=null;
            //node.right=null;

        }
        return node;
    }
    // Return the tree corresponding to the given arithmetic expression.
// The expression is legal, fully parenthesized, contains no blanks,
// and involves only the operations + and *.
    private TreeNode exprTreeHelper(String expr) {
        if (expr.charAt(0) != '(') {
            return new TreeNode(expr); // you fill this in
        } else {
            // expr is a parenthesized expression.
            // Strip off the beginning and ending parentheses,
            // find the main operator (an occurrence of + or * not nested
            // in parentheses, and construct the two subtrees.
            int nesting = 0;
            int opPos = 0;
            boolean exist = false;
            //expr.charAt(0)="";
            //LinkedList<String> list = new LinkedList<String>();
            int index =0;
            for (int k = 1; k < expr.length() - 1; k++) {
                // you supply the missing code
                char temp = expr.charAt(k);
                if(temp=="(".charAt(0)){
                    index++;
                    exist=true;
                }
                if(expr.charAt(k)==")".charAt(0)){
                    index--;
                }
                if((expr.charAt(k)=="+".charAt(0)||expr.charAt(k)=="*".charAt(0))&&index==0){
                    opPos = k;
                }
            }
            String opnd1 = expr.substring(1, opPos);
            String opnd2 = expr.substring(opPos + 1, expr.length() - 1);
            String op = expr.substring(opPos, opPos + 1);
            System.out.println("expression = " + expr);
            System.out.println("operand 1  = " + opnd1);
            System.out.println("operator   = " + op);
            System.out.println("operand 2  = " + opnd2);
            System.out.println();
            TreeNode temp = new TreeNode(op);
            temp.left = this.exprTreeHelper(opnd1);
            temp.right = this.exprTreeHelper(opnd2);
            return temp;
        }
    }
    public static void main(String[] args) {
        BinaryTree t = new BinaryTree();
        //t.fillSampleTree2();
        //t= BinaryTree.fibTree(5);
        //t.print();
        BinaryTree a = BinaryTree.exprTree("((a+(5*(9+1)))+(6*5))");
        a.optimize();
        a.print();
        //a.print();
    }

    private static void print(BinaryTree t, String description) {
        System.out.println(description + " in preorder");
        t.printPreorder();
        System.out.println(description + " in inorder");
        t.printInorder();
        System.out.println();
    }

    public static class TreeNode {

        public Object item;
        public TreeNode left;
        public TreeNode right;

        public TreeNode(Object obj) {
            item = obj;
            left = right = null;
        }

        public TreeNode(Object obj, TreeNode left, TreeNode right) {
            item = obj;
            this.left = left;
            this.right = right;
        }

        private void printPreorder() {
            System.out.print(item + " ");
            if (left != null) {
                left.printPreorder();
            }
            if (right != null) {
                right.printPreorder();
            }
        }

        private void printInorder() {
            if (left != null) {
                left.printInorder();
            }
            System.out.print(item + " ");
            if (right != null) {
                right.printInorder();
            }
        }

        private static final String indent1 = "    ";

        private void print(int indent) {
            int index = indent+4;
            if (right != null) {
                right.print(index);
            }
            println(this.item,indent);
            if (left != null) {
                left.print(index);
            }
        }

        private static void println(Object obj, int indent) {
            for (int k = 0; k < indent; k++) {
                System.out.print(indent1);
            }
            System.out.println(obj);
        }

        public TreeNode getLeft() {
            return left;
        }

        public TreeNode getRight() {
            return right;
        }

        public Object getItem() {
            return item;
        }

        public void traverse(LinkedList<TreeNode> list){
            list.add(this);
            if (this.left!= null) {
                left.traverse(list);
            }
            if (this.right!= null) {
                right.traverse(list);
            }
        }
        public TreeNode fibinacci(int n){
            TreeNode temp;
            if(n<2){
                temp = new TreeNode(n);
                return temp;
            }
            else{
                temp = new TreeNode(0);
                temp.left = fibinacci(n-1);
                temp.right = fibinacci(n-2);
                temp.item = (int)temp.left.item+(int)temp.right.item;
                //temp = new TreeNode((int)left.item+(int)right.item);//Integer.sum((int)left.item, (int)right.item);
                return temp;
            }
        }
    }
}
