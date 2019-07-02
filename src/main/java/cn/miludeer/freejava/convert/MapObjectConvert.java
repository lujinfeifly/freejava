package cn.miludeer.freejava.convert;

import java.lang.reflect.InvocationTargetException;
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

        Map<String, MapObjectConvertCache.FunValue> culval = MapObjectConvertCache.getInstance().getCache(clazz);

        T object = null;
        try {
            object = clazz.newInstance();
        } catch (InstantiationException e) {
            return null;
        } catch (IllegalAccessException e) {
            return null;
        }


        if(culval != null) {
            for(Map.Entry<String, String> entry :map.entrySet()) {
                MapObjectConvertCache.FunValue funValue = culval.get(entry.getKey());
                if(funValue != null) {
                    switch (funValue.kind) {
                        case 1:
                            try {
                                funValue.f.set(object, StringToSimple.Convert(entry.getValue(), funValue.type));
                            } catch (IllegalAccessException e) {
                            } catch (RuntimeException e) {
                            }
                            break;
                        case 2:
                            try {
                                funValue.m.invoke(object, StringToSimple.Convert(entry.getValue(), funValue.type));
                            } catch (IllegalAccessException e) {
                            } catch (InvocationTargetException e) {
                            } catch (RuntimeException e) {
                            }
                            break;
                    }
                }
            }
        }

        return object;
    }

}
