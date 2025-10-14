import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

public class BinaryDecimal {
  public static void main(String[] args) {

    int[] binarWert = {4096, 2048, 1024, 512, 256, 128, 64, 32, 16, 8, 4, 2, 1};
    ArrayList<Integer> binarAusgabe = new ArrayList<>();

    System.out.print("Dezimalwert? : ");
    Scanner dezimalWertScanner = new Scanner(System.in);
    String dezimalWertString = dezimalWertScanner.nextLine();
    int dezimalWertInt = Integer.parseInt(dezimalWertString);

//    for (int i = 0; i < binarWert.length; i++) {
//        if (dezimalWertInt >= binarWert[i]) {
//            dezimalWertInt -= binarWert[i];
//            binarAusgabe.add(1);
//        } else {
//            binarAusgabe.add(0);
//        }
//    }
//
//    System.out.print("Bin�r: ");
//    for (int b : binarAusgabe) {
//      System.out.print(b);
//    }

    long binaer = 0;
    long digit = 1;
    while(dezimalWertInt!=0) {
        binaer += (dezimalWertInt % 2) * digit;
        dezimalWertInt /= 2;
        digit *= 10;
    }
    System.out.println("Binär: " + binaer);
    
// end of while
    
    

  }
}
