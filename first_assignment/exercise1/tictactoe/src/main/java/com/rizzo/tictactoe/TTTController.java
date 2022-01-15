/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.rizzo.tictactoe;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import javax.swing.JLabel;
/**
 *
 * @author Simone
 */
public class TTTController extends JLabel implements VetoableChangeListener {
    
    public TTTController() {
        setText("SONO STATO INIZIALIZZATO");
    }

    @Override
    public void vetoableChange(PropertyChangeEvent evt) throws PropertyVetoException {
        TTTCell cell = (TTTCell)evt.getNewValue();
        // throw new PropertyVetoException("Wrong player: Next move is X", pce); // in the case the action is forbidden.
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
}
