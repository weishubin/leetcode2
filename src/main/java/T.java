import java.util.ArrayList;
import java.util.List;

/**
 * Created by weishubin on 2018/11/12.
 */
public class T {
    public static void main(String[] args) {
        List<Integer> a = new ArrayList<Integer>();
        a.add(1);
        a.add(2);
        a.add(3);
        for (int i = 0; i < a.size(); i++) {
            System.out.println(i);
            if (a.get(i) < 2) {
                a.remove(i);
            }
        }

    }

}
