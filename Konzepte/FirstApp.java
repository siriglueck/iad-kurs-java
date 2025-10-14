import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

public class FirstApp {
  public static void main(String[] args) {
    int i = 205;
    float f = 1.19f;
    float mwst = i * f;
    System.out.printf("%.2f%n", mwst);
    
    
    System.out.println("Bitte Name eingeben:");
    Scanner eingabe = new Scanner(System.in);
    String ausgabe = eingabe.nextLine();
    System.out.println("Hallo " + ausgabe);
    
    
    System.out.println("Wie alt sind Sie?");
    Scanner age = new Scanner(System.in);
    String age2 = age.nextLine();
    int age3 = Integer.parseInt(age2);
    
    if (age3 >= 18) {
      System.out.println("Jawohl, Sie sind vollj�hrig.");
    } else {
      System.out.println("Schade, Sie sind nicht vollj�hrig");
    } // end of if-else
    
    int a = 2;
    int b = 6;
    int[] field = {3,4,5, a+b};
    System.out.printf("%d%n", field[3]);
    // %d for Dezimalzahl
    
    // change value in array
    field[2] = 555;
    
    // String auslesen
    for (int y = 0; y < 4; y++) {
      System.out.printf("%d%n", field[y]);
    }
    
    String zahlen = "";
    for (int z : field) {
      zahlen += z + ", ";
    }
    System.out.println(zahlen);
    
    // vordefinierte String darf nicht nacher hinzuf�gen
    
    // leere Arrays erzeugen
    
    ArrayList<Integer> intlist = new ArrayList<>();
    intlist.add(2);
    intlist.add(5);
    for (int z : intlist) {
      System.out.printf("%d%n", z);
    }
    
    ArrayList<String> stringlist = new ArrayList<>();
    stringlist.add("Max");
    stringlist.add("Moritz");
    for (String s : stringlist) {
      System.out.printf("%s%n", s);
    }
  }
}





