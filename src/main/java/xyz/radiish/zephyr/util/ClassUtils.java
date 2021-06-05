package xyz.radiish.zephyr.util;


import java.lang.reflect.*;
import java.util.*;

public class ClassUtils {
  public static Class<?> highestCommonFactor(Set<Class<?>> clazzes) {
    return TreeUtils.highestCommonFactor(clazzes, clazz -> {
      Set<Class<?>> supers = new HashSet<>();
      if (clazz.getSuperclass() != null) {
        supers.add(clazz.getSuperclass());
      }
      return supers;
    }).flatMap(oldClazz -> oldClazz.equals(Object.class) ? TreeUtils.highestCommonFactor(clazzes, clazz -> new HashSet<>(Arrays.asList(clazz.getInterfaces()))) : Optional.of(oldClazz)).orElse(Object.class);
  }

  public static Class<?> ofType(Type type) {
    if(type instanceof Class<?>) {
      return (Class<?>) type;
    } else if(type instanceof ParameterizedType) {
      return (Class<?>) ((ParameterizedType) type).getRawType();
    } else if(type instanceof GenericArrayType) {
      return ofType(((GenericArrayType) type).getGenericComponentType());
    } else if(type instanceof TypeVariable<?>) {
      return ofType(((TypeVariable<?>) type).getBounds()[0]);
    } else if(type instanceof WildcardType) {
      return ofType(((WildcardType) type).getUpperBounds()[0]);
    }
    return Object.class;
  }

  public static class ParameterizedTypeBuilder {
    private List<Type> parameters;
    private final Class<?> raw;

    public ParameterizedTypeBuilder(Class<?> raw) {
      this.raw = raw;
      parameters = new ArrayList<>();
    }

    public ParameterizedTypeBuilder setParameters(List<Type> parameters) {
      this.parameters = parameters;
      return this;
    }

    public ParameterizedTypeBuilder addParameter(Type parameter) {
      parameters.add(parameter);
      return this;
    }


    public ParameterizedType build() {
      return new ParameterizedType() {
        @Override
        public Type[] getActualTypeArguments() {
          return parameters.toArray(new Type[0]);
        }

        @Override
        public Type getRawType() {
          return raw;
        }

        @Override
        public Type getOwnerType() {
          return null;
        }
      };
    }


  }
}
