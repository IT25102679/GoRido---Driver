package com.example.gorido.Model;
import jakarta.persistence.*;

@Entity
@Table(name = "active")
public class Active {
    @Id
    private int id;
    private String name;

    public Active(){}

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setName(String type_name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
