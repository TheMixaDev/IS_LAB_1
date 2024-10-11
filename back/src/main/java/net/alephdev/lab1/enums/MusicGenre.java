package net.alephdev.lab1.enums;

public enum MusicGenre {
    ROCK("Рок"),
    PSYCHEDELIC_CLOUD_RAP("Психоделик-клауд-рэп"),
    JAZZ("Джаз"),
    PUNK_ROCK("Панк-рок"),
    BRIT_POP("Брит-поп");

    private final String name;

    MusicGenre(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}