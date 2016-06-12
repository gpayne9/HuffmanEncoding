
package huffman; //for my IDE
/**
 Guy Payne
 6/12/2016
 Dr. Hajja
 huffman encoding Assignment
 */
import java.util.*;
import java.io.*;
public class huffman {
    /*
    fillHasMap
    this method fills the hash map with the unique characters from the file and increments the frequencies if found multiple times
    this returns how many characters there are in the entire file, this is used for later bit comparison and calculation
    */
    public static int fillHashMap(HashMap<String,Integer> frequencies,String readFile)
    {
        int letterCount = 0;// count of letters in the file
        File file = new File(readFile);// creates read file
        
        Scanner fileReader = null;
        try{
            fileReader =  new Scanner(file);// puts file in a scaner
        }
        catch (FileNotFoundException e)
        {
            System.out.println("file not found");
            System.exit(0);
        }
        while(fileReader.hasNext())//while there are still strings in the file
        {
            String line = fileReader.nextLine();// each line is put into this string
            for(int i = 0; i < line.length(); i++)// loop through the line
            {
                letterCount = letterCount + 1;// incriment the letters
                if(frequencies.get(Character.toString(line.charAt(i))) == null) // if the letter is unique and not in the hashmap
                {
                    frequencies.put(Character.toString(line.charAt(i)), 1);// put in the hashmap
                }
                else // if the letter is not unique then increace freq by 1
                    frequencies.put(Character.toString(line.charAt(i)), frequencies.get(Character.toString(line.charAt(i))) + 1); 
                
            }
        }
        return letterCount;// return the total amount of letters in the file
    }
    /*
    generateCode
    generates a unique binary huffman code for each letter in the Tree starting from the root node
    the method is recursivly called until it reaches a leaf(both left and right = null) then it finally stores the code into the hashmap
    
    */
    public static void generateCode(Node n, HashMap<String,String> codes, String str)
    {
        if(n.left == null && n.right == null) // if the current node is a leaf
        {
            codes.put( n.letter, str);// put the letter and the code in the hash map
            return;
        }
        generateCode(n.left, codes, str + "0"); // continue down the left side of the tree and add a 0 to the code
        generateCode(n.right, codes, str + "1");// contine down the right side of the tree and add a 1 to the code
        
            
    }
    /*
    buildTree
    buids a huffman encoding tree from the linked list filled with letters form the hashmap
    stops when there is only one node left in the linked list.
    each time the 2 min nodes are removed it resorts and finds the new min nodes
    
    */
    public static void buildTree(HashMap<String,Integer> frequencies, LinkedList<Node> myTree)
    {
        for(HashMap.Entry<String, Integer> entry : frequencies.entrySet())// for each unique letter in the hash map
        {
            Node node = new Node (entry.getKey(),entry.getValue()); // create a new node for th letter
            myTree.add(node);// add the letter to the linked list
        }
        Collections.sort(myTree);// sorts the linked list by frequencies
        while(myTree.size() != 1)// while the tree is more than one node
        {
            Node min1 = myTree.removeFirst();// remove the first min
            Node min2 = myTree.removeFirst();// remove the second min
            Node newNode = new Node(min1.frequency + min2.frequency, min1, min2); // create a new node that is the sum of the requencies and points to both the min nodes
            myTree.add(newNode);// add this node to the tree
            Collections.sort(myTree);// resort the tree
        }
    }
    /*
    encode
    encodes a text file into an encoded huffman encoding text file
    gets the codes from the codes hashmap
    returns the amount of bits written form the encoding for later calculations
    */
    public static int encode(String readFile, String writeFile, HashMap<String,String> codes)
    {
        int bits = 0; // bits used encoding
        Scanner fileReader = null;
        File file = new File(readFile);
        try{
            FileWriter fileWriter = new FileWriter(writeFile); // create file writter
            BufferedWriter writer =  new BufferedWriter(fileWriter);// creawte buffer writer
            fileReader = new Scanner(file);
             while(fileReader.hasNextLine())// while there are still lines in the text file
             {
                String line = fileReader.nextLine(); // get current line
                for(int i = 0; i < line.length() ; i++)// loop through line
                {
                    String code = codes.get(Character.toString(line.charAt(i)));// gets the code for the letter form the codes hashmap
                    writer.write("" + code + "");// writes to the file
                    bits = bits + code.length(); // sums bits used
                }
             }
            writer.close();// close the file
        }
        catch(IOException e)// if error is caught in creating the file
        {
            System.out.println("error writting to file");
        }
        return bits; // retuns bits used
    }
    /*
    printKey
    prints the huffman encoding key
    */
    public static void printKey(String readFile,HashMap<String,String> codes)
    {
        System.out.println("letter code");// table info
        for(HashMap.Entry<String, String> entry : codes.entrySet())// for each letter in the hashmap
        {
            System.out.println(entry.getKey() + "      " + entry.getValue()); //prints the letter and the code 
        }
    }
    /*
    bitsSaved
    outputs and calculates the total number of bits saved using huffman encdoing.
    This assumes each letter takes up 8 bits if not encoded
    */
    public static void bitsSaved(int letters, int bits)
    {
        letters = letters * 8; // calcs bits taken up form total latters
        System.out.println(letters - bits + " bits were saved through huffman encoding assuming each letter originally takes 8 bits");
    }
    public static void main(String[] args) {
        HashMap<String, Integer> frequencies = new HashMap<String, Integer>();// creates frequencies hash map that will store letters and their frequencies
        int letterCount = fillHashMap(frequencies,"huffman.txt");// filles hashmap from a text file and returns the total amount of letters used
        LinkedList<Node> myTree = new LinkedList<Node>();// creates linkedList to hold the tree
        buildTree(frequencies, myTree);// builds the huffman encoding tree
        HashMap<String, String> codes = new HashMap <String,String>();// creates codes hashmap that store a letter and the huffman code that represents the letter
        generateCode(myTree.getFirst(),codes, "");// generates the code for each letter
        int huffmanBits = encode("huffman.txt","encoded.txt",codes);// encodes a text file and creates a new text file where the encoded text is. also returns bits used encoding
        printKey("huffman.txt", codes);// prints key from encoding
        bitsSaved(letterCount, huffmanBits);// calcs bits saved form encoding
    }
    
}
