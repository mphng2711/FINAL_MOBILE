package com.example.purepawapp.data.model;

public class Pet {
    private String name = "";
    private String species = "";
    private String breed = "";
    private double weightKg = 0.0;
    private int age = 0;
    private String note = "";

    public Pet() {
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSpecies() { return species; }
    public void setSpecies(String species) { this.species = species; }

    public String getBreed() { return breed; }
    public void setBreed(String breed) { this.breed = breed; }

    public double getWeightKg() { return weightKg; }
    public void setWeightKg(double weightKg) { this.weightKg = weightKg; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
