package cn.miludeer.freejava.convert;

/**
 * program: freejava
 * <p>
 * description: ${description}
 * <p>
 * author: lujinfei
 * <p>
 * create: 2019-07-01 22:31
 **/
public class StringToSimple {

    public static Object Convert(String source, String type) {
        switch (type.hashCode()) {
            case 1195259493:    // string
                if(type.equals("java.lang.String")) {
                    return source;
                } else {
                    throw new RuntimeException();
                }
            case 3327612:    // long
                if(type.equals("long")) {
                    return Long.parseLong(source);
                } else {
                    throw new RuntimeException();
                }
            case 398795216:
                if(type.equals("java.lang.Long")) {
                    return Long.parseLong(source);
                } else {
                    throw new RuntimeException();
                }
            case 104431:    // int
                if(type.equals("int")) {
                    return Integer.parseInt(source);
                } else {
                    throw new RuntimeException();
                }
            case -2056817302:
                if(type.equals("java.lang.Integer")) {
                    return Integer.parseInt(source);
                } else {
                    throw new RuntimeException();
                }
            case -1325958191:    // double
                if(type.equals("double")) {
                    return Double.parseDouble(source);
                } else {
                    throw new RuntimeException();
                }
            case 761287205:
                if(type.equals("java.lang.Double")) {
                    return Double.parseDouble(source);
                } else {
                    throw new RuntimeException();
                }
            case 97526364:    // float
                if(type.equals("float")) {
                    return Float.parseFloat(source);
                } else {
                    throw new RuntimeException();
                }
            case -527879800:
                if(type.equals("java.lang.Float")) {
                    return Float.parseFloat(source);
                } else {
                    throw new RuntimeException();
                }
            case 64711720:    // bool
                if(type.equals("boolean")) {
                    return Boolean.parseBoolean(source);
                } else {
                    throw new RuntimeException();
                }
            case 344809556:
                if(type.equals("java.lang.Boolean")) {
                    return Boolean.parseBoolean(source);
                } else {
                    throw new RuntimeException();
                }
            case 3039496:
                if(type.equals("byte")) {
                    return (byte)source.charAt(0);
                } else {
                    throw new RuntimeException();
                }
            case 398507100:
                if(type.equals("java.lang.Byte")) {
                    return (byte)source.charAt(0);
                } else {
                    throw new RuntimeException();
                }
            case 3052374:
                if(type.equals("char")) {
                    return source.charAt(0);
                } else {
                    throw new RuntimeException();
                }
            case 155276373:
                if(type.equals("java.lang.Character")) {
                    return source.charAt(0);
                } else {
                    throw new RuntimeException();
                }
            case 109413500:    //short
                if(type.equals("short")) {
                    return Short.parseShort(source);
                } else {
                    throw new RuntimeException();
                }
            case -515992664:
                if(type.equals("java.lang.Short")) {
                    return Short.parseShort(source);
                } else {
                    throw new RuntimeException();
                }
            default:
                throw new RuntimeException();
        }
    }
}
