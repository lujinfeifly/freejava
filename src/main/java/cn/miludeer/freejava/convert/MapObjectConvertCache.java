package cn.miludeer.freejava.convert;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * program: freejava
 * description: ${description}
 * author: lujinfei
 * create: 2019-07-02 00:04
 **/
public class MapObjectConvertCache {

    public static  MapObjectConvertCache instance;

    public static MapObjectConvertCache getInstance() {
        if(instance == null) {
            synchronized (MapObjectConvertCache.class) {
                if(instance == null) {
                    instance = new MapObjectConvertCache();
                }
            }
        }
        return instance;
    }

    public Map<String, Map<String, FunValue>> mapmap = new HashMap<String, Map<String, FunValue>>();

    public <T> Map<String, FunValue> getCache(Class<T>  clazz) {
        String className = clazz.getName();

        if(mapmap.containsKey(className)) {       // 如果包含了就直接返回
            return mapmap.get(className);
        }

        Map<String, FunValue> map = new HashMap<String, FunValue>();

        Field[] fields = clazz.getDeclaredFields();
        Method[] methods = clazz.getDeclaredMethods();

        for (Field f:fields) {
            String name = f.getName();
            String type = f.getType().getName();
            int kind = 0;
            Method method = null;
            if((f.getModifiers()&1) == 1) {
                kind = 1;                        // derect;
            } else{
                for(Method m:methods) {
                    char[] cs = name.toCharArray();
                    cs[0] -= 32;

                    if (m.getName().equals("set" + String.valueOf(cs))) {
                        kind = 2;
                        method = m;
                    }
                }
            }
            if(kind != 0) {
                FunValue fun = new FunValue(name, kind, method, f, type);
                map.put(name, fun);
            }
        }

        mapmap.put(className, map);
        return map;
    }

    public class FunValue {
        String key;
        int kind;
        Method m;
        Field f;
        String type;

        public FunValue(String key, int kind, Method m, Field f, String type) {
            this.kind = kind;
            this.key = key;
            this.m = m;
            this.f = f;
            this.type = type;
        }
    }
}
