import cn.miludeer.freejava.basic.MapObjectConvert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * program: freejava
 * <p>
 * description: ${description}
 * <p>
 * author: lujinfei
 * <p>
 * create: 2019-04-26 23:57
 **/
public class BasicTest {
    @Test
    public void random() {
        long unboundedLong = new Random().nextLong();
        System.out.println(unboundedLong);
    }


    @Test
    public void ff() {

        int e = "int".hashCode();
        int e2 = "boolean".hashCode();
        int e3 = "long".hashCode();
        int e4 = "double".hashCode();
        int e5 = "float".hashCode();
        int e6 = "byte".hashCode();
        int e7 = "char".hashCode();
        int e8 = "String".hashCode();
        int e9 = "short".hashCode();

        Map<String, String> a = new HashMap<String, String>();
        a.put("a", "1");
        a.put("b", "aaaaaa");
        a.put("c", "10");
        a.put("d", "100000");
        a.put("e","2.3");
        a.put("f","35454.677");
        a.put("g","4");
        a.put("h","3");
        a.put("i","true");


        Abc c = MapObjectConvert.MapToObject(a, Abc.class);

        System.out.println(c);
    }

}
