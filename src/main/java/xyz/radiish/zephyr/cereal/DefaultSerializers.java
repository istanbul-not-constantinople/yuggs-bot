package xyz.radiish.zephyr.cereal;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import xyz.radiish.zephyr.util.ClassUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class DefaultSerializers {
  public static List<JsonSerializer<?>> generateDefaultSerializers() {
    return Arrays.asList(
      new JsonSerializer<Number>(Number.class) {
        @Override
        public JsonElement serialize(Number instance) {
          return new JsonPrimitive(instance);
        }

        @Override
        public Number deserialize(Optional<Number> instance, JsonElement element, Type type) {
          return element.getAsNumber();
        }
      },

      new JsonSerializer<String>(String.class) {
        @Override
        public JsonElement serialize(String instance) {
          return new JsonPrimitive(instance);
        }

        @Override
        public String deserialize(Optional<String> instance, JsonElement element, Type type) {
          return element.getAsString();
        }
      },

      new JsonSerializer<Optional<?>>(Optional.class) {
        @Override
        public JsonElement serialize(Optional<?> instance) {
          return JsonSerializing.serialize(instance.orElse(null));
        }

        @Override
        public Optional<?> deserialize(Optional<Optional<?>> instance, JsonElement element, Type type) {
          if(instance.isPresent()) {
            return instance;
          }
          if(type instanceof ParameterizedType) {
            return Optional.ofNullable(JsonSerializing.deserialize(((ParameterizedType) type).getActualTypeArguments()[0], element));
          }
          return instance;
        }
      },

      new JsonSerializer<Collection<?>>(Collection.class) {
        @Override
        public JsonElement serialize(Collection<?> instance) {
          return new JsonArrayBuilder().addAll(((Collection<?>) instance).stream().map(JsonSerializing::serialize).collect(Collectors.toList())).build();
        }

        @Override
        public Collection<?> deserialize(Optional<Collection<?>> instance, JsonElement element, Type type) {
          if(element.getAsJsonArray().size() == 0) {
            return new ArrayList<>();
          } else {
            List<Object> elements = new ArrayList<>();
            if(type instanceof ParameterizedType) {
              Type elementType = ((ParameterizedType) type).getActualTypeArguments()[0];
              elements.addAll(StreamSupport.stream(element.getAsJsonArray().spliterator(), false).map(elem -> JsonSerializing.deserialize(elementType, elem)).collect(Collectors.toList()));
            }
            return elements;
          }
        }
      },

      new JsonSerializer<Map<?, ?>>(Map.class) {
        @Override
        public JsonElement serialize(Map<?, ?> instance) {
          return new JsonArrayBuilder().addAll(instance.entrySet().stream().map(entry -> new JsonObjectBuilder().put("key", JsonSerializing.serialize(entry.getKey())).put("value", JsonSerializing.serialize(entry.getValue())).build()).collect(Collectors.toList())).build();
        }

        @Override
        public Map<?, ?> deserialize(Optional<Map<?, ?>> instance, JsonElement element, Type type) {
          if(element.getAsJsonArray().size() == 0) {
            return new HashMap<>();
          } else {
            Map<Object, Object> map = new HashMap<>();
            if(type instanceof ParameterizedType) {
              Type keyType = ((ParameterizedType) type).getActualTypeArguments()[0];
              Type valueType = ((ParameterizedType) type).getActualTypeArguments()[1];
              StreamSupport.stream(element.getAsJsonArray().spliterator(), false).forEach(elem -> {
                Object key =   JsonSerializing.deserialize(keyType,   elem.getAsJsonObject().get("key"));
                Object value = JsonSerializing.deserialize(valueType, elem.getAsJsonObject().get("value"));
                map.put(key, value);
              });
            }
            return map;
          }
        }
      }
    );
  }
}
