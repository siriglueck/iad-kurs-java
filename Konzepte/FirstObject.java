public class FirstObject {
  public static class Person {
    // Attributes
      String name;
      String vorname;
      int alter;
      String ort;
      String strasse;
      int plz;
    
    // Konstruktor
    public Person(String name, String vorname, int alter, String ort, String strasse,int plz) {
      this.name = name;
      this.vorname = vorname;
      this.alter = alter;
      this.ort = ort;
      this.strasse = strasse;
      this.plz = plz; 
    }
    
    // auch Kontruktor
     public Person() {
      // Optionally initialize default values, if desired
    }
    
    // Methode
    public void printPerson() {
      System.out.println(vorname + " " + name + " " + alter + " Jahre alt und wohnt in " + ort + " in der " + strasse);
    }
  }  
    public static void main(String[] args) {
      Person person1 = new Person("M�ller", "Max", 30, "Berlin", "Hauptstrasse 1", 10234);
      Person person2 = new Person();
      person2.name = "Meier";
      person2.vorname = "Moritz";
      person2.alter = 35;
      person2.ort = "Hamburg";
      person2.strasse = "Elbchaussee 5";
      person2.plz = 20123;
      
      person1.printPerson();
      person2.printPerson();     
    }
}


