package xyz.radiish.zephyr.util;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TreeUtils {
  public static <T> List<T> findAllParents(T child, Function<T, Set<T>> parentizer) {
    System.out.printf("parents of (%s): ", child);
    List<T> results = new ArrayList<>();
    results.add(child);
    for(T parent : parentizer.apply(child)) {
      results.addAll(findAllParents(parent, parentizer));
    }
    System.out.printf("%s%n", results);
    return results;
  }
  public static <T> Optional<T> highestCommonFactor(Set<T> children, Function<T, Set<T>> parentizer) {
    List<List<T>> lists = children.stream().map(child -> findAllParents(child, parentizer)).collect(Collectors.toList());

    List<T> possibles = new ArrayList<>(lists.get(0));
    for(List<T> list : lists.subList(1, lists.size())) {
      possibles = possibles.stream().filter(list::contains).collect(Collectors.toList());
    }
    return possibles.stream().findFirst();
  }
}
