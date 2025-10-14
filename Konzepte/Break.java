public class Break {
  public static void main(String[] args) {
    float j = 0f;
    
    for (int i = 0 ; i <= 100 ; i++ ) {
      if(i==75) { break;}
      
      j = (i / 5.0f);
      if(j == 10) {continue;}
      
      System.out.print(i + ", ");
    } // end of for     
  }
}
