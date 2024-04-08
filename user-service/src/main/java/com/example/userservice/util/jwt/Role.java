package com.example.userservice.util.jwt;

public enum Role {
    USER("Role: User"), ADMIN("Role: Admin");

    final String toString;

    Role(String toString) {
        this.toString = toString;
    }

    public String getString() {
        return toString;
    }

    public static Role fromString(String stringValue) {
        for (Role value : Role.values()) {
            if (value.getString().equals(stringValue)) {
                return value;
            }
        }
        return null;
    }
}
