/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.rizzo.tictactoe;

/**
 *
 * @author Simone
 */
public class StateEnum {
    public static enum State {
        INIT(0),
        X(1),
        O(2),
        WIN(3);
        private final int st;
        State(final int val){
            this.st = val;
        }
        public int getValue() { return st; 
        
        }
    }
}
