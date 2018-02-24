package com.xyc.okutils.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by hasee on 2018/1/10.
 */

public class CheckObjUtils {
    public static boolean check(Object o) throws InvocationTargetException, IllegalAccessException {
        Method[] declaredMethods = o.getClass().getDeclaredMethods();
        for (Method method : declaredMethods) {
            String name = method.getName();
            if (name.startsWith("get")) {
                Object invoke = method.invoke(o);
                if (invoke == null || "".equals(invoke.toString())) {
                    return false;
                }
            }
        }
        return true;
    }
}
