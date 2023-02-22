package ru.testlog;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Ioc {

    private Ioc() {
    }

    public static Calculator createClass(Object obj) {
        Class<?>[] interfaces = obj.getClass().getInterfaces();
        InvocationHandler handler = new InvocationHandlerImpl(obj, interfaces);
            return (Calculator) Proxy.newProxyInstance(Ioc.class.getClassLoader(), interfaces, handler);
    }

    static class InvocationHandlerImpl implements InvocationHandler {
        private final Object myClass;
        private Map<Method, ArrayList<String>> methodAnnotations = new HashMap<>();
        public InvocationHandlerImpl(Object myClass, Class<?>[] interfaces) {
            this.myClass = myClass;

            Class<?> clazz;
            try {
                clazz = Class.forName(this.myClass.getClass().getName());
                Method[] methods = clazz.getMethods();
                for (Method method : methods) {
                    Annotation[] annotations = method.getDeclaredAnnotations();
                    ArrayList<String> strList = new ArrayList<>();
                    for (Class<?> iClass : interfaces) {
                        for (Method iMethod : iClass.getMethods()){
                            if (equalMethod(iMethod, method)) {
                                for (Annotation annotation : annotations) {
                                    strList.add(annotation.toString());
                                }
                                if (!strList.isEmpty()) methodAnnotations.put(iMethod, strList);
                            }
                        }
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

            if (methodAnnotations.get(method) != null && methodAnnotations.get(method).contains("@ru.testlog.annotations.Log()")) {
                String strParams = "";
                for (Object arg : args){
                    strParams = strParams + " " + arg.toString();
                }
                System.out.printf("executed method: %s params:%s ", method.getName(), strParams);
                System.out.println();
            }
            return method.invoke(myClass, args);
        }

        @Override
        public String toString() {
            return "InvocationHandlerImpl {" +
                    "myClass=" + myClass +
                    " }";
        }

        private boolean equalParamTypes(Class<?>[] params1, Class<?>[] params2) {
            if (params1.length == params2.length) {
                for (int i = 0; i < params1.length; i++) {
                    if (params1[i] != params2[i])
                        return false;
                }
                return true;
            }
            return false;
        }

        private boolean equalMethod(Method met1, Method met2) {
            return met1.getName().equals(met2.getName())
                    && met1.getReturnType() == met2.getReturnType()
                    &&  equalParamTypes(met1.getParameterTypes(),met2.getParameterTypes());
        }

    }
}
