/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.rizzo.tictactoe;

/**
 *
 * @author Simone
 */

public enum CellStates {
    INIT(0),
    X(1),
    O(2);
    private final int st;
    CellStates(final int val){
        this.st = val;
    }

    @Override
    public String toString() {
        return ""+st;
    }
    
    public int getValue() { return st; }
}

