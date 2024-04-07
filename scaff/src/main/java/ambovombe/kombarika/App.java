package ambovombe.kombarika;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String args[]) {

        double calcul =0;
        double budget=100000;
        double rano = 1300;
        double rano2j=rano*2;
        calcul+=10000; // lundi
        calcul+=10000; // mardi
        calcul+=1500; //
        double sakafo = 5000*4;

        double restes=budget-calcul-rano2j-sakafo;
        System.out.println("reste budget "+restes);
        System.out.println("total :"+(calcul+rano2j+sakafo));
        System.out.println("rano 2 jours : "+ rano2j);
    }
}
