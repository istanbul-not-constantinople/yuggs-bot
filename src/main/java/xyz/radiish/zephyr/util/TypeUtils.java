package xyz.radiish.zephyr.util;


import java.lang.reflect.*;
import java.util.*;

public class TypeUtils {
  public static Class<?> highestCommonFactor(Set<Class<?>> clazzes) {
    return TreeUtils.highestCommonFactor(clazzes, clazz -> {
      Set<Class<?>> supers = new HashSet<>();
      if (clazz.getSuperclass() != null) {
        supers.add(clazz.getSuperclass());
      }
      supers.addAll(Arrays.asList(clazz.getInterfaces().clone()));
      return supers;
    }).orElse(Object.class);
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
    private Type owner;
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

    public ParameterizedTypeBuilder setOwner(Type owner) {
      this.owner = owner;
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
          return owner;
        }
      };
    }
  }

  public static class WildcardTypeBuilder {
    private List<Type> upper;
    private List<Type> lower;

    public WildcardTypeBuilder() {
      upper = new ArrayList<>();
      lower = new ArrayList<>();
    }

    public WildcardTypeBuilder addUpper(Type bound) {
      upper.add(bound);
      return this;
    }

    public WildcardTypeBuilder addLower(Type lower) {
      upper.add(lower);
      return this;
    }

    public WildcardType build() {
      return new WildcardType() {
        @Override
        public Type[] getUpperBounds() {
          return upper.toArray(new Type[0]);
        }

        @Override
        public Type[] getLowerBounds() {
          return lower.toArray(new Type[0]);
        }
      };
    }
  }

  public static class GenericArrayTypeBuilder {
    private final Type component;

    public GenericArrayTypeBuilder(Type component) {
      this.component = component;
    }

    public GenericArrayType build() {
      return new GenericArrayType() {
        @Override
        public Type getGenericComponentType() {
          return component;
        }
      };
    }
  }
}
