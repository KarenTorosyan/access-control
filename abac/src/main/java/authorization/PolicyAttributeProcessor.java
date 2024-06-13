package authorization;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class PolicyAttributeProcessor {

    public static Map<String, Object> loadAttributes(Object o) {
        Map<String, Object> attributes = new HashMap<>();
        for (Field field : o.getClass().getDeclaredFields()) {

            if (field.isAnnotationPresent(PolicyAttribute.class)) {
                field.setAccessible(true);
                PolicyAttribute annotation = field.getAnnotation(PolicyAttribute.class);
                Object fieldValue;
                try {
                    fieldValue = field.get(o);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                if (annotation.recursiveCollect()) {
                    attributes.put(annotation.value(), loadAttributes(fieldValue));
                } else {
                    attributes.put(annotation.value(), fieldValue);
                }
            }
        }
        return attributes;
    }
}
