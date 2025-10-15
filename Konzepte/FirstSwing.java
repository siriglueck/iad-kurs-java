import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class FirstSwing{
  public static void main(String[] args) {

    // Erzeuge das Fenter (all swing object begin with J)
    JFrame frame = new JFrame("Mein erstes Swing-Fenster");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(300,200);  // width and height
    
    // Erzeuge ein Label   
    JLabel label = new JLabel("Hallo Welt");
    label.setHorizontalAlignment(SwingConstants.CENTER);

    // Erzeuge einen Button
    JButton button = new JButton("Klick mich");
    JButton button2 = new JButton("2");
    JButton button3 = new JButton("3");

    // Erzeuge ein AuswahlMenue
      String[] optionen = {"Option 1","Option 2","Option 3"};
      JComboBox<String> select = new JComboBox<>(optionen);
      select.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
              String selected = (String) select.getSelectedItem();
              switch (selected){
                  case "Option 1": functionA();
                  break;
                  case "Option 2": functionB();
                  break;
                  case "Option 3": functionC();
                  break;
              }
          }
      });

    // Eventlistener zum Button hinzufügen
    button.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e) {
        label.setText("Mein Name ist Puter - Kom Puter");
        button.setVisible(false);
      }
    });

    // Elemente in das Fenster einfügen
    frame.getContentPane().add(label, "North");
    frame.getContentPane().add(select, "Center");
    frame.getContentPane().add(button, "South");
    frame.getContentPane().add(button2, "East");
    frame.getContentPane().add(button3, "West");

    // Fenster sichtbar machen
    frame.setVisible(true);
  }

  private static void functionA() {
      System.out.println("Function A wird ausgeführt");
  }

  private static void functionB() {
      System.out.println("Function B wird ausgeführt");
  }

  private static void functionC() {
      System.out.println("Function C wird ausgeführt");
  }

}
