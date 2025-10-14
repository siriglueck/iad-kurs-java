import java.io.*;
import java.util.Arrays;
import java.util.Comparator;

public class SortArray {
  public static void main(String[] args) {
    int[] zahl = {5,25,800,236,2,4158,23,20589,4,69};
    Arrays.sort(zahl);
    
    for (int z: zahl ) {
      System.out.print(z + ", ");
    } // end of for
    
    System.out.println(); 
    
    String[] words = {"Banane","Apfel","Mango", "Kiwi", "Kirche", "Pflaume", "Birne"};
    Arrays.sort(words, Comparator.reverseOrder());
    System.out.println(Arrays.toString(words));
    
    String ausgabe = Arrays.toString(words);
    ausgabe = ausgabe.substring(1, ausgabe.length()-1);
    System.out.println(ausgabe);        
  }
}
