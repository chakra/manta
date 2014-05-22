package com.espendwise.manta.util.arguments;

import com.espendwise.manta.util.Pair;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.AppLocale;
import com.espendwise.manta.util.arguments.resolvers.ResolverFactory;

import java.util.ArrayList;
import java.util.List;


public class Args {

    public static TypedArgument[] joins(TypedArgument[]... argsArray) {

        if (argsArray != null) {

            List<TypedArgument> x = new ArrayList<TypedArgument>();

            for (TypedArgument[] args : argsArray) {
                if (Utility.isSet(args)) {
                    for (TypedArgument arg : args) {
                        x.add(arg);
                    }
                }
            }

            return !x.isEmpty() ? x.toArray(new TypedArgument[x.size()]) : new TypedArgument[0];

        }

        return null;
    }
    
    public static TypedArgument[] typed(Object... objects) {

        TypedArgument[] arguments = null;

        if (objects != null) {
            int i = 0;
            arguments = new TypedArgument[objects.length];
            for (Object obj : objects) {
                arguments[i++] = obj instanceof TypedArgument
                        ? (TypedArgument) obj
                        : ArgumentTypeFactory.getFactory().getArgImp(obj);
            }
        }

        return arguments;

    }

    public static TypedArgument[] i18nTyped(Object... objects) {
        TypedArgument[] arguments = null;

        if (objects != null) {
            int i = 0;
            arguments = new TypedArgument[objects.length];
            for (Object obj : objects) {
                arguments[i++] = obj instanceof TypedArgument
                        ? (TypedArgument) obj
                        : ArgumentTypeFactory.getFactory().getI18nArgImp(obj);
            }
        }

        return arguments;
    }


    public static TypedArgument[] typed(Pair<Object, ArgumentType>... objects) {

        TypedArgument[] arguments = null;

        if (objects != null) {
            int i = 0;
            arguments = new TypedArgument[objects.length];
            for (Pair<Object, ArgumentType> obj : objects) {
                arguments[i++] = obj.getObject1() instanceof TypedArgument
                        ? (TypedArgument) obj.getObject1()
                        : ArgumentTypeFactory.getFactory().getArgImp(obj.getObject1(), obj.getObject2());
            }
        }

        return arguments;

    }

    public static String toDbValue(Object obj, ArgumentType type) {
        if (obj == null) {
            return null;
        }
        ArgumentResolver<?> resolver = ResolverFactory.getFactory().getArgumentResolver(type, obj.getClass());
        return ((ArgumentResolver) resolver).resolve(obj);
    }

    public static String toViewValue(Object obj, ArgumentType type, AppLocale locale) {
        if (obj == null) {
            return null;
        }
        ArgumentResolver<?> resolver = ResolverFactory.getFactory().getArgumentResolver(type, obj.getClass());
        return ((ArgumentResolver) resolver).resolve(locale, obj);
    }


}
