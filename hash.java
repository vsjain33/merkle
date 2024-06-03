import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;


interface ObjectHasher<T> {
    String hash(T obj);
}
class DefaultObjectHasher<T> implements ObjectHasher<T> {
    @Override
    public String hash(T obj) {
        StringBuilder stringBuilder = new StringBuilder();
        Class<?> objClass = obj.getClass();
        Field[] fields = objClass.getDeclaredFields();

        try {
            for (Field field : fields) {
                field.setAccessible(true);
                Object value = field.get(obj);
                if (value != null) {
                    stringBuilder.append(value.toString());
                }
                field.setAccessible(false);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Error accessing fields for hashing", e);
        }

        return hashString(stringBuilder.toString());
    }

    private String hashString(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }
}