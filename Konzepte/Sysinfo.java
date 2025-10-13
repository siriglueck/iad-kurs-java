import java.util.Enumeration;

public class Sysinfo{
  public static void main(String[] args) {
    Enumeration<?> properties = System.getProperties().propertyNames();
    System.out.println("Systemeingenschaften:\n");
    while (properties.hasMoreElements()) { 
      String prop = properties.nextElement().toString();
      System.out.print(prop + ":");
      for (int i = prop.length(); i < 40 ; i++ ) {
        System.out.print(" ");
      } // end of for
      System.out.println(System.getProperty(prop));
    } // end of while
  }
}


