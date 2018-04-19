package sprders.object.creator;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ObjectCreator {

    @SuppressWarnings("unchecked")
    public static <T> T createDate(Class<T> t) {

        String[] names = {"Emma", "Mary", "Allen", "Olivia", "Natasha",
                "Kevin", "Rose", "Kelly", "Jeanne", "James",
                "Edith", "Sophia", "Charles", "Ashley", "William",
                "Hale", "Steve", "David", "Richard", "Daniel"};
        Object o;
        try {
            Class<?> aClass = Class.forName(t.getName());
            o = aClass.getConstructor().newInstance();
            Field[] declaredFields = aClass.getDeclaredFields();
            List<String> map = new ArrayList<>(declaredFields.length);
            for (Field declaredField : declaredFields) {
                if (declaredField.getType().getSimpleName().contains(t.getSimpleName())) {
                    continue;
                }
                map.add(declaredField.getName());
            }
            for (String s : map) {
                Field declaredField = aClass.getDeclaredField(s);
                declaredField.setAccessible(true);
                if (declaredField.getType().isArray()) {
                    Random random = new Random(System.currentTimeMillis());
                    switch (declaredField.getType().getComponentType().getName()) {
                        case "String":
                            Object string = Array.newInstance(String.class, 5);
                            for (int i = 0; i < Array.getLength(string); i++) {
                                int nextInt = random.nextInt(names.length);
                                Array.set(string, i, names[nextInt]);
                            }
                            declaredField.set(o, string);
                            break;
                        case "int":
                            Object in = Array.newInstance(Integer.TYPE, 5);
                            for (int i = 0; i < Array.getLength(in); i++) {
                                int nextInt = random.nextInt();
                                Array.set(in, i, nextInt);
                            }
                            declaredField.set(o, in);
                            break;
                        case "long":
                            Object lo = Array.newInstance(Long.TYPE, 5);
                            for (int i = 0; i < Array.getLength(lo); i++) {
                                int nextInt = random.nextInt();
                                Array.set(lo, i, nextInt);
                            }
                            declaredField.set(o, lo);
                            break;
                        default:
                            Object instance = Array.newInstance(declaredField.getType().getComponentType(), 5);
                            for (int i = 0; i < Array.getLength(instance); i++) {
                                Object date = createDate(declaredField.getType().getComponentType());
                                Array.set(instance, i, date);
                            }
                            declaredField.set(o, instance);
                            break;
                    }
                } else {
                    Random random = new Random();
                    switch (declaredField.getType().getSimpleName()) {
                        case "String":
                            int i = random.nextInt(names.length);
                            declaredField.set(o, names[i]);
                            break;
                        case "int":
                            declaredField.setInt(o, random.nextInt(200));
                            break;
                        case "long":
                            declaredField.setLong(o, random.nextLong());
                            break;
                        case "boolean":
                            declaredField.setBoolean(o, false);
                            break;
                        default:
                            declaredField.set(o, createDate(declaredField.getType()));
                            break;

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return (T) o;
    }
}
