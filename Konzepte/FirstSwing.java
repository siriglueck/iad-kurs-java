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
    
    button.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e) {
        label.setText("Mein Name ist Puter - Kom Puter");  
      }
    });
 
    frame.getContentPane().add(label, "North");
    frame.getContentPane().add(button, "South");
    frame.setVisible(true);  
    
  }
  
  
}
