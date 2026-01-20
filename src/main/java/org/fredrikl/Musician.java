package org.fredrikl;

public record Musician(String name, String albums) {
    public Musician(String name) {
        this(name, null);
    }
}

