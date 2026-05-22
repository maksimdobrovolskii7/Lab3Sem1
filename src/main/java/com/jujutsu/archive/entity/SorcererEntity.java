package com.jujutsu.archive.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public class SorcererEntity {
    private String name;
    private String rank;
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getRank() { return rank; }
    public void setRank(String rank) { this.rank = rank; }
}