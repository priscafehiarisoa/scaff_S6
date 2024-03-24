package ambovombe.kombarika;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String args[]) {

        String s = "abc";
        int count=0;
        for(int i =0 ; i<s.length();i++){
            count+=(int)s.charAt(i) * i;
            System.out.println(i);
        }
        System.out.println(count);
    }
}
