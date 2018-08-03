import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.StringTokenizer;

class TreeNode{
    int value;
    TreeNode left;
    TreeNode right;

    TreeNode(int value){
        this.value = value;
        left = null;
        right = null;
    }
}

class SplayTree {
    TreeNode root;
    Node<Integer> tree;

    public SplayTree() {

    }

    SplayTree(int value){
        root = new TreeNode(value);
    }

    TreeNode rightRotate(TreeNode node) {
        TreeNode temp = node.left;
        node.left = temp.right;
        temp.right = node;

        return temp;
    }

    TreeNode leftRotate(TreeNode node) {
        TreeNode temp = node.right;
        node.right = temp.left;
        temp.left = node;

        return temp;
    }

    TreeNode splay(TreeNode node, int x){
        if(node==null || node.value==x) return node;

        if(node.value>x){
            if(node.left==null) return node;

            if(node.left.value>x){    //Zig-Zig (LL)
                node.left.left = splay(node.left.left, x);
                node = rightRotate(node);
            }
            else if (node.left.value<x){     //Zig-Zag (LR)
                node.left.right = splay(node.left.right, x);
                if(node.left.right != null) node.left = leftRotate(node.left);
            }

            return  (node.left==null)? node:rightRotate(node);
        }
        else {
            if(node.right==null) return node;

            if(node.right.value>x){    //Zag-Zig (RL)
                node.right.left = splay(node.right.left, x);
                if(node.right.left != null) node.right = rightRotate(node.right);
            }
            else if(node.right.value<x){   //Zag-Zag
                node.right.right = splay(node.right.right, x);
                node = leftRotate(node);
            }

            return (node.right == null)? node:leftRotate(node);
        }
    }

    TreeNode insert(TreeNode node, int x){
        if(node==null) return new TreeNode(x);

        node = splay(node, x);

        if(node.value==x) return node;

        TreeNode temp = new TreeNode(x);

        if(node.value>x){
            temp.right = node;
            temp.left = node.left;
            node.left = null;
        }
        else {
            temp.left = node;
            temp.right = node.right;
            node.right = null;
        }

        return temp;
    }

    TreeNode delete(TreeNode node, int x){
        if(node==null) return null;

        node = splay(node, x);

        if(node.value != x) return node;

        node.left  = splay(node.left, x);

        if(node.left == null) node = node.right;
        else {
            node.left.right = node.right;
            node = node.left;
        }

        return node;
    }

    Node<Integer> test(TreeNode node){
        Node<Integer> rc=null;
        if(node != null){
            rc = new Node<Integer>(node.value);
            rc.left = test(node.left);
            rc.right = test(node.right);
        }
        return rc;
    }
}

class Main{
    public static void main(String[] args) throws IOException {
        SplayTree st = new SplayTree();
        Scanner s = new Scanner(System.in);
        String str;

        double start = System.nanoTime();

        int N = 0;

        /*while(true){
            System.out.println("1.Print(T)  2.Splay(x)  3.Search(x)  4.Insert(x)  5.Delete(x)  6.Quit");
            System.out.print("Task : ");
            int input = s.nextInt();
*/
        //while (++N<=10004) {
            //Random random = new Random();
            //int input = random.nextInt(3) + 3;

        int n=0;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("1305072_input2.txt"));
            br.readLine();
            n = Integer.parseInt(br.readLine());
        } catch (Exception e) {}

        for(int i=1;i<=n;i++){
            str = br.readLine();
            StringTokenizer strt = new StringTokenizer(str, " ");
            int input = Integer.parseInt(strt.nextToken());

            if (input == 1) {
                st.tree = st.test(st.root);
                BTreePrinter.printNode(st.tree);
            } else if (input == 2) {
                //System.out.print("Splay : ");
                //int x = s.nextInt();
                int x = Integer.parseInt(strt.nextToken());
                st.root = st.splay(st.root, x);
            } else if (input == 3) {
                //System.out.print("Search : ");
                //int x = s.nextInt();
                //int x = random.nextInt(104);
                int x = Integer.parseInt(strt.nextToken());
                st.root = st.splay(st.root, x);
            } else if (input == 4) {
                //System.out.print("Insert : ");
                //int x = s.nextInt();
                //int x = random.nextInt(104);
                int x = Integer.parseInt(strt.nextToken());
                st.root = st.insert(st.root, x);
            } else if (input == 5) {
                //System.out.print("Delete : ");
                //int x = s.nextInt();
                //int x = random.nextInt(104);
                int x = Integer.parseInt(strt.nextToken());
                st.root = st.delete(st.root, x);
            } else if (input == 6) break;
        }

        double end = System.nanoTime();

        System.out.println("Total Operations time : "+(end-start)/1000000 + " ms");
    }
}
