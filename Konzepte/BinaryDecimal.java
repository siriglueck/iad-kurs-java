import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

public class BinaryDecimal {
  public static void main(String[] args) {

    int[] binarWert = {4096, 2048, 1024, 512, 256, 128, 64, 32, 16, 8, 4, 2, 1};
    ArrayList<Integer> binarAusgabe = new ArrayList<>();

    System.out.print("Dezimalwert? (von 1 bis 8191) : ");
    Scanner dezimalWertScanner = new Scanner(System.in);
    String dezimalWertString = dezimalWertScanner.nextLine();
    int dezimalWertInt = Integer.parseInt(dezimalWertString);

    for (int i = 0; i < binarWert.length; i++) {
        if (dezimalWertInt >= binarWert[i]) {
            dezimalWertInt -= binarWert[i];
            binarAusgabe.add(1);
        } else {
            binarAusgabe.add(0);
        }
    }

    System.out.print("Binär: ");
    for (int b : binarAusgabe) {
      System.out.print(b);
    }
    
// end of while
    
    

  }
}
