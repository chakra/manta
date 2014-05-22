package com.espendwise.manta.util;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ReflectionUtils {

    private static final Logger logger = Logger.getLogger(ReflectionUtils.class);

    private static final String GETTER_PREFIX = "get";
    private static final String BOOLEAN_GETTER_PREFIX = "is";

    public static List<ClassField> findAllGetterFieldsNames(Class targetClass) {
        return findAllGetterFieldsNames(targetClass, null);
    }

    public static List<ClassField> findAllGetterFieldsNames(Class targetClass, String parent) {

        List<ClassField> x = new ArrayList<ClassField>();

        if (targetClass != null) {

            Method[] methods = org.springframework.util.ReflectionUtils.getAllDeclaredMethods(targetClass);

            for (Method method : methods) {

                String filedName = getGetterFieldName(method);

                ClassField classField = new ClassField();

                classField.setName(filedName);
                classField.setType(method.getReturnType());

                if (Utility.isSet(filedName)) {

                    if (java.util.Collection.class.isAssignableFrom(method.getReturnType())) {
                        classField.setInnerFields(findAllGetterFieldsNames(getComponentType(method.getGenericReturnType())));
                    } else if (method.getReturnType().isArray()) {
                        classField.setInnerFields(findAllGetterFieldsNames(method.getReturnType().getComponentType()));
                    } else if (!BeanUtils.isSimpleValueType(method.getReturnType())) {
                        classField.setInnerFields(findAllGetterFieldsNames(method.getReturnType()));
                    }

                    x.add(classField);

                }
            }
        }

        return x;
    }

    public static Class getComponentType(Type type) {
        if (ParameterizedType.class.isAssignableFrom(type.getClass())) {
            ParameterizedType genericType = (ParameterizedType) type;
            return (Class) genericType.getActualTypeArguments()[0];
        }
        return null;
    }

    public static String getGetterFieldName(Method method) {

        String methodName = method.getName();

        if (!methodName.startsWith(GETTER_PREFIX) && !methodName.startsWith(BOOLEAN_GETTER_PREFIX)) {
            return Constants.EMPTY;
        }

        String propertyName = methodName.startsWith(GETTER_PREFIX)
                ? methodName.substring(GETTER_PREFIX.length())
                : methodName.startsWith(BOOLEAN_GETTER_PREFIX)
                ? methodName.substring(BOOLEAN_GETTER_PREFIX.length())
                : Constants.EMPTY;

        if (!Utility.isSet(propertyName) || !propertyName.equals(StringUtils.capitalize(propertyName))) {
            return Constants.EMPTY;
        }

        if (method.getReturnType() == null || method.getReturnType() == void.class) {
            return Constants.EMPTY;
        }

        if (method.getParameterTypes().length > 0) {
            return Constants.EMPTY;
        }

        return StringUtils.uncapitalize(propertyName);

    }


}
