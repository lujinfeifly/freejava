package cn.miludeer.freejava.basic;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * program: freejava
 * description: ${description}
 * author: lujinfei
 * create: 2019-06-27 23:20
 **/
public class MapObjectConvert {
    public static <T> T MapToObject(Map<String, String> map, Class<T> clazz) {
        if(map == null) {
            return null;
        }

        Field[] fields = clazz.getDeclaredFields();
        Method[] methods = clazz.getDeclaredMethods();

        T value = null;
        try {
            value = clazz.newInstance();
        } catch (InstantiationException e) {
            return null;
        } catch (IllegalAccessException e) {
            return null;
        }

        for (Field f:fields) {
            String name = f.getName();
            if(map.containsKey(name)) {
                String type = f.getType().getName();

                if((f.getModifiers()&1) == 1){
                    try {
                        f.set(value, convert(map.get(name), type));
                    } catch (IllegalAccessException e) {

                    }
                } else{
                    for(Method m:methods) {
                        char[] cs = name.toCharArray();
                        cs[0]-=32;

                        if(m.getName().equals("set" + String.valueOf(cs))) {
                            try {
                                m.invoke(value, map.get(name));
                            } catch (IllegalAccessException e) {

                            } catch (InvocationTargetException e) {

                            }
                        }
                    }
                }
            }
        }

        return value;
    }

    private static Object convert(String source, String type) {
        switch (type.hashCode()) {
            case -1808118735:    // string
                if(type.equals("String")) {
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
            case 104431:    // int
                if(type.equals("int")) {
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
            case 97526364:    // float
                if(type.equals("float")) {
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
            case 3039496:
                if(type.equals("byte")) {
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
            case 109413500:    //short
                if(type.equals("short")) {
                    return Short.parseShort(source);
                } else {
                    throw new RuntimeException();
                }
            default:
                throw new RuntimeException();
        }
    }
}
