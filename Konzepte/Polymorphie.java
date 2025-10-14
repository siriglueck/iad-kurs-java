abstract class Figur {
  abstract double getFlaeche();
  }

class Rechteck extends Figur{
  private double a, b;
  public Rechteck(double a, double b){
    this.a = a;
    this.b = b;
    }
  public double getFlaeche(){
    return a*b;
    }
  }

  class Kreis extends Figur{
    private double r;
    public Kreis (double r){
      this.r = r;
      }
    public double getFlaeche(){
      return Math.PI * Math.pow(r, 2);
      }
  }


public class Polymorphie {
  public static void main(String[] args) {
    Rechteck re = new Rechteck(5,6);
    System.out.println("Fläche des Rechtecks: " + re.getFlaeche());
    
    Kreis kr = new Kreis(5.0);
    System.out.println("Fläche des Kreises: " + kr.getFlaeche());
  }
}
