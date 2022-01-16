/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.rizzo.tictactoe;
import com.rizzo.tictactoe.CellStates;
import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
/**
 * The TTTController has the purpose of managing the game flow
 * intended as the two players alternate and the actions that are allowed
 * @author Simone
 */
public class TTTController extends JLabel implements VetoableChangeListener,PropertyChangeListener {
    
    private int gameState;
    private String[] labelTexts = {"START GAME","NEXT MOVE: X","NEXT MOVE: O","THE WINNER IS:","DRAW"};
    static final int IDLE = 0;
    static final int XTURN = 1;
    static final int OTURN = 2;
    static final int WINNER = 3;
    static final int DRAW = 4;
    
    /**
     * Constructor set the initial state to IDLE.
     */
    public TTTController() {
        setState(IDLE);
    }

    /**
     * Decice if each action applied to a cell is a legit actior or not.
     * If it is it doesnt return nothing e change the internal state, otherwise we throw an PropertyVetoException
     * that invalidate the action.
     * @param evt Message event send from the cells.
     * @throws PropertyVetoException 
     */
    @Override
    public void vetoableChange(PropertyChangeEvent evt) throws PropertyVetoException{
        CellStates newstate = (CellStates)evt.getNewValue();
        CellStates oldstate = (CellStates)evt.getOldValue();
        if(gameState==IDLE){
            gameState=newstate==CellStates.O?XTURN:OTURN;
        }else if(gameState==OTURN && newstate==CellStates.O){
            gameState=XTURN;
        }else if(gameState==XTURN && newstate==CellStates.X){
            gameState=OTURN;
        }else{
            throw new PropertyVetoException("Wrong player action", evt); // in the case the action is forbidden.
        }
        updateText();
    }
    /**
     * Set the game state and update the label text.
     * @param state 
     */
    public void setState(int state){
        gameState = state;
        updateText();
    }
    /**
     * Set the winner state and change the background color to green.
     */
    public void winner(CellStates state){
        setState(WINNER);
        setText(getText()+" "+((state==CellStates.X)?"X":"O"));
        setBackground(Color.decode("#90be6d")); // green color.
    }
    /**
     * Set the game state to Draw and set the default blue color
     */
    public void draw(){
        setState(DRAW);
        setBackground(Color.decode("#0066FF")); // default blue color.
        //JOptionPane.showMessageDialog(null, "If you want to play again hit Restart button.","Game ended with draw",JOptionPane.INFORMATION_MESSAGE);
    }    
    /**
     * Reset the game state by put the value back into IDLE and setting back the backgound color.
     */
    public void reset(){
        setState(IDLE);
        setBackground(Color.decode("#0066FF")); // default blue color.
    }
    /**
     * It update the label text according to the gameState value.
     */
    public void updateText(){
        setText(labelTexts[gameState]);
    }  

    /**
     * Control the messages arriving from the Board and we change the game state according to the messages.
     * @param evt An event sended from the Board
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals("reset")){
            reset();
        }else if(evt.getPropertyName().equals("winner_is")){
            CellStates state = (CellStates) evt.getNewValue();
            winner(state);
        }else if(evt.getPropertyName().equals("draw")){
            draw();
        }
    }
    
}
