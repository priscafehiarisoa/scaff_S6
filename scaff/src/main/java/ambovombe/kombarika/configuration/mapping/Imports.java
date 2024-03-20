package ambovombe.kombarika.configuration.mapping;

    /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;


@Getter @Setter
public class Imports {
    private String[] controller;
    private String[] entity;
    private String[] repository;

    //CONSTRUCTOR
    public Imports(){}

    //METHODS


    @Override
    public String toString() {
        return "Imports{" +
                "controller=" + Arrays.toString(controller) +
                ", entity=" + Arrays.toString(entity) +
                ", repository=" + Arrays.toString(repository) +
                '}';
    }
}
