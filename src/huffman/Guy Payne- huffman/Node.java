
package huffman;// for my IDE

/*
Node object
used to build the huffman encoding tree
*/
public class Node implements Comparable <Node> {// implements comparable to override the compareTo method for sorting
    String letter;
    int frequency;
    Node left;
    Node right;
    public Node(String l, int freq)// defualt leaf node constructor
    {
        letter = l;
        frequency = freq;
    }
    public Node(int freq, Node l, Node r)// defualt Node constructor that points down to leafs
    {
        letter = null;
        frequency = freq;
        left = l;
        right = r;
    }
    @Override
    public int compareTo(Node n)// override compareTo method
    {
        //sorts in decendoing order
        int freq = n.frequency;
        if(this.frequency > freq)
            return 1;
        else if (this.frequency == freq)
            return 0;
        else
            return -1;
    }
}
