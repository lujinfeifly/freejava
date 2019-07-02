package cn.miludeer.freejava.convert;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * program: freejava
 * description: 基础转换函数
 * 1. 借鉴了java7之后版本的支持string的switch想法，做的判断方案实现的Convert 函数
 *    因为修改少，这里的hashcode就可以写固定。不为易读考虑了。
 *
 * author: lujinfei
 * create: 2019-07-01 22:31
 **/
public class StringToSimple {

    public static <T> Object Convert(String source, Class<T> typeClass) {
        String type = typeClass.getName();
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
                try {
                    Constructor c1 = typeClass.getDeclaredConstructor(new Class[]{String.class});
                    c1.setAccessible(true);
                    T t = (T)c1.newInstance(source);
                    return t;
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException();
                } catch (IllegalAccessException e) {
                    throw new RuntimeException();
                } catch (InstantiationException e) {
                    throw new RuntimeException();
                } catch (InvocationTargetException e) {
                    throw new RuntimeException();
                }
        }
    }
}
