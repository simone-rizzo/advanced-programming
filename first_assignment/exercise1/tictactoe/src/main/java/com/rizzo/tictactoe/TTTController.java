/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.rizzo.tictactoe;
import com.rizzo.tictactoe.StateEnum.State;
import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import javax.swing.JLabel;
/**
 *
 * @author Simone
 */
public class TTTController extends JLabel implements VetoableChangeListener,PropertyChangeListener {
    
    private int gameState;
    
    static final int IDLE = -1;
    static final int XTURN = 1;
    static final int OTURN = 2;
    static final int WINNER = 0;
           
    public TTTController() {
        setText("SONO STATO INIZIALIZZATO");
        gameState = IDLE;
        updateText();
    }

    @Override
    public void vetoableChange(PropertyChangeEvent evt) throws PropertyVetoException{
        var cellstate = evt.getNewValue();
        if(gameState==IDLE){
            gameState=cellstate==State.O?XTURN:OTURN;
        }else if(gameState==OTURN && cellstate==State.O){
            gameState=XTURN;
        }else if(gameState==XTURN && cellstate==State.X){
            gameState=OTURN;
        }else{
            throw new PropertyVetoException("Wrong player action", evt); // in the case the action is forbidden.
        }
        updateText();
    }
    
    public void winner(){
        gameState = WINNER;
        updateText();
        setBackground(Color.decode("#90be6d")); // green color.
    }
    public void reset(){
        gameState = IDLE;
        updateText();
        setBackground(Color.decode("#0066FF")); // default blue color.
    }
    public void updateText(){
        switch(gameState){
            case IDLE:
                setText("START GAME");
                break;
            case OTURN:
                setText("NEXT MOVE: O");
                break;
            case XTURN:
                setText("NEXT MOVE: X");
                break;
            case WINNER:
                setText("THE WINNER");
                break;
        }
    }  

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals("reset")){
            reset();
        }else if(evt.getPropertyName().equals("win")){
            winner();
        }
    }
    
}
