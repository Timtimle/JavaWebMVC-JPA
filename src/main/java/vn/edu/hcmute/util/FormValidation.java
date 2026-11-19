package vn.edu.hcmute.util;

public final class FormValidation {
    private FormValidation() {}

    public static boolean email(String value) {
        return value != null && value.length() <= 255 &&
                value.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
    }
    public static boolean username(String value) {
        return value != null && value.matches("[A-Za-z0-9_.-]{3,100}");
    }
    public static boolean password(String value) {
        return value != null && value.length() >= 8 && value.length() <= 128;
    }
    public static boolean fullName(String value) {
        return value != null && value.length() <= 255;
    }
    public static boolean phone(String value) {
        return value != null && (value.isBlank() || value.matches("[+0-9 ()-]{8,20}"));
    }
    public static boolean category(String value) {
        return value != null && !value.isBlank() && value.length() <= 255;
    }
    public static boolean videoTitle(String value) {
        return value != null && !value.isBlank() && value.length() <= 255;
    }
}
