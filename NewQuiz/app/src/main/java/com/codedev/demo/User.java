package com.codedev.demo;

public class User {
    String Nom;
    String password;

    public User(String nom,String Password) {
        Nom = nom;
        password=Password;
    }



    public String getNom() {
        return Nom;
    }

    public void setNom(String nom) {
        Nom = nom;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
