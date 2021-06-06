package xyz.radiish.zephyr.cereal;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import xyz.radiish.zephyr.util.TypeUtils;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class DefaultSerializers {
  public static final JsonSerializer<Number> NUMBER_SERIALIZER = new JsonSerializer<>(Number.class) {
    @Override
    public JsonElement serialize(Number instance) {
      return new JsonPrimitive(instance);
    }

    @Override
    public Number deserialize(Optional<Number> instance, JsonElement element, Type type) {
      return element.getAsNumber();
    }
  };

  public static final JsonSerializer<Boolean> BOOLEAN_SERIALIZER = new JsonSerializer<>(Boolean.class) {
    @Override
    public JsonElement serialize(Boolean instance) {
      return new JsonPrimitive(instance);
    }

    @Override
    public Boolean deserialize(Optional<Boolean> instance, JsonElement element, Type type) {
      return element.getAsBoolean();
    }
  };

  public static final JsonSerializer<String> STRING_SERIALIZER = new JsonSerializer<>(String.class) {
    @Override
    public JsonElement serialize(String instance) {
      return new JsonPrimitive(instance);
    }

    @Override
    public String deserialize(Optional<String> instance, JsonElement element, Type type) {
      return element.getAsString();
    }
  };

  public static final JsonSerializer<Optional<?>> OPTIONAL_SERIALIZER = new JsonSerializer<>(Optional.class) {
    @Override
    public JsonElement serialize(Optional<?> instance) {
      return JsonSerializing.serialize(instance.orElse(null));
    }

    @Override
    public Optional<?> deserialize(Optional<Optional<?>> instance, JsonElement element, Type type) {
      if (instance.isPresent()) {
        return instance;
      }
      if (type instanceof ParameterizedType) {
        return Optional.ofNullable(JsonSerializing.deserialize(((ParameterizedType) type).getActualTypeArguments()[0], element));
      }
      return instance;
    }
  };

  public static final JsonSerializer<Collection<?>> COLLECTION_SERIALIZER = new JsonSerializer<>(Collection.class) {
    @Override
    public JsonElement serialize(Collection<?> instance) {
      return new JsonArrayBuilder().addAll(((Collection<?>) instance).stream().map(JsonSerializing::serialize).collect(Collectors.toList())).build();
    }

    @Override
    public Collection<?> deserialize(Optional<Collection<?>> instance, JsonElement element, Type type) {
      if (element.getAsJsonArray().size() == 0) {
        return new ArrayList<>();
      } else {
        List<Object> elements = new ArrayList<>();
        if (type instanceof ParameterizedType) {
          Type elementType = ((ParameterizedType) type).getActualTypeArguments()[0];
          elements.addAll(StreamSupport.stream(element.getAsJsonArray().spliterator(), false).map(elem -> JsonSerializing.deserialize(elementType, elem)).collect(Collectors.toList()));
        }
        return elements;
      }
    }
  };

  public static final JsonSerializer<Map<?, ?>> MAP_SERIALIZER = new JsonSerializer<>(Map.class) {
    @Override
    public JsonElement serialize(Map<?, ?> instance) {
      return new JsonArrayBuilder().addAll(instance.entrySet().stream().map(entry -> new JsonObjectBuilder().put("key", JsonSerializing.serialize(entry.getKey())).put("value", JsonSerializing.serialize(entry.getValue())).build()).collect(Collectors.toList())).build();
    }

    @Override
    public Map<?, ?> deserialize(Optional<Map<?, ?>> instance, JsonElement element, Type type) {
      if (element.getAsJsonArray().size() == 0) {
        return new HashMap<>();
      } else {
        Map<Object, Object> map = new HashMap<>();
        if (type instanceof ParameterizedType) {
          Type keyType = ((ParameterizedType) type).getActualTypeArguments()[0];
          Type valueType = ((ParameterizedType) type).getActualTypeArguments()[1];
          StreamSupport.stream(element.getAsJsonArray().spliterator(), false).forEach(elem -> {
            Object key = JsonSerializing.deserialize(keyType, elem.getAsJsonObject().get("key"));
            Object value = JsonSerializing.deserialize(valueType, elem.getAsJsonObject().get("value"));
            map.put(key, value);
          });
        }
        return map;
      }
    }
  };

  public static final JsonSerializer<Type> TYPE_SERIALIZER = new JsonSerializer<>(Type.class)   {
    @Override
    public JsonElement serialize(Type instance) {
      if(instance instanceof Class<?>) {
        return new JsonObjectBuilder().put("class", ((Class<?>) instance).getName()).build();
      } else if(instance instanceof WildcardType) {
        WildcardType type = (WildcardType) instance;
        return new JsonObjectBuilder()
          .put("upper", new JsonArrayBuilder()
            .addAll(Arrays.stream(type.getUpperBounds()).map(TYPE_SERIALIZER::serialize).collect(Collectors.toList()))
            .build())
          .put("lower", new JsonArrayBuilder()
            .addAll(Arrays.stream(type.getLowerBounds()).map(TYPE_SERIALIZER::serialize).collect(Collectors.toList()))
            .build())
          .build();
      } else if(instance instanceof ParameterizedType) {
        ParameterizedType type = (ParameterizedType) instance;

        JsonObjectBuilder builder = new JsonObjectBuilder()
          .put("raw", TYPE_SERIALIZER.serialize(type.getRawType()))
          .put("arguments", new JsonArrayBuilder()
            .addAll(Arrays.stream(type.getActualTypeArguments()).map(TYPE_SERIALIZER::serialize).collect(Collectors.toList()))
            .build());

        if(type.getOwnerType() != null) {
          builder.put("owner", TYPE_SERIALIZER.serialize(type.getOwnerType()));
        }

        return builder.build();
      } else if(instance instanceof GenericArrayType) {
        return new JsonObjectBuilder().put("component", TYPE_SERIALIZER.serialize(((GenericArrayType) instance).getGenericComponentType())).build();
      } else {
        return new JsonObjectBuilder().put("type", instance.getTypeName()).build();
      }
    }

    @Override
    public Type deserialize(Optional<Type> instance, JsonElement element, Type type) {
      JsonObject object = element.getAsJsonObject();
      if(object.has("class")) {
        try {
          return Class.forName(object.get("class").getAsString());
        } catch (ClassNotFoundException ignored) {
          // ignored innit bruv
        }
      } else if(object.has("upper")) {
        TypeUtils.WildcardTypeBuilder builder = new TypeUtils.WildcardTypeBuilder();
        StreamSupport.stream(object.get("upper").getAsJsonArray().spliterator(), false).forEach(sub -> builder.addUpper(TYPE_SERIALIZER.deserialize(Optional.empty(), sub, Type.class)));
        StreamSupport.stream(object.get("lower").getAsJsonArray().spliterator(), false).forEach(sub -> builder.addLower(TYPE_SERIALIZER.deserialize(Optional.empty(), sub, Type.class)));
        return builder.build();
      } else if(object.has("raw")) {
        TypeUtils.ParameterizedTypeBuilder builder = new TypeUtils.ParameterizedTypeBuilder((Class<?>) TYPE_SERIALIZER.deserialize(Optional.empty(), object.get("raw"), Class.class));
        StreamSupport.stream(object.get("arguments").getAsJsonArray().spliterator(), false).forEach(sub -> builder.addParameter(TYPE_SERIALIZER.deserialize(Optional.empty(), sub, Type.class)));
        if(object.get("owner") != JsonNull.INSTANCE) {
          builder.setOwner(TYPE_SERIALIZER.deserialize(Optional.empty(), object.get("owner"), Class.class));
        }
        return builder.build();
      }

      return Object.class;
    }
  };

  public static List<JsonSerializer<?>> generateDefaultSerializers() {
    return Arrays.asList(
      NUMBER_SERIALIZER,
      BOOLEAN_SERIALIZER,
      STRING_SERIALIZER,
      OPTIONAL_SERIALIZER,
      COLLECTION_SERIALIZER,
      MAP_SERIALIZER,
      TYPE_SERIALIZER
    );
  }
}
