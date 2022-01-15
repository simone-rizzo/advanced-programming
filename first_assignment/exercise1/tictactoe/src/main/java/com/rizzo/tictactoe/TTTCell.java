/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.rizzo.tictactoe;

import com.rizzo.tictactoe.StateEnum.State;
import java.awt.Color;
import java.awt.PageAttributes;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeSupport;
import java.util.List;
import javax.swing.UIManager;

/**
 *
 * @author Simone
 */
public class TTTCell extends javax.swing.JPanel implements PropertyChangeListener {

    private Color[] colors = {UIManager.getColor("Panel.background"),Color.decode("#FFFF41"),Color.decode("#E000FF"),Color.decode("#00E000")};
    
    public Color getColor(){
        return colors[cellstate.getValue()];
    }   
    
    private State cellstate = State.INIT;
    private final VetoableChangeSupport vtoCngSpp = new VetoableChangeSupport(this);
    private final PropertyChangeSupport prpCngSpp = new PropertyChangeSupport(this);
    
    private int cellId;
    public void setCellId(int id){
        this.cellId = id;
    }
    public State getCellState(){
        return this.cellstate;
    }
    
    /**
     * Creates new form TTTCell
     */
    public TTTCell() {
        initComponents();
    }

    /**
    * Add new vetoable change listener.
    */
    public void addVetoChangeListener(VetoableChangeListener listener) {
        vtoCngSpp.addVetoableChangeListener(listener);
    }
    public void addPropChangeListener(PropertyChangeListener listner){
        prpCngSpp.addPropertyChangeListener(listner);
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals("reset")){
            reset();
        }else if(evt.getPropertyName().equals("win")){
            List<TTTCell> cells = (List<TTTCell>)evt.getNewValue();
            if(cells.contains(this)){
                winner();
            }
        }else if(evt.getPropertyName().equals("draw")){
            
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        oButton = new javax.swing.JButton();
        xButton = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        setMaximumSize(new java.awt.Dimension(100, 100));

        oButton.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        oButton.setText("O");
        oButton.setAlignmentY(0.0F);
        oButton.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        oButton.setContentAreaFilled(false);
        oButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                oButtonActionPerformed(evt);
            }
        });

        xButton.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        xButton.setText("X");
        xButton.setAlignmentY(0.0F);
        xButton.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        xButton.setContentAreaFilled(false);
        xButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                xButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(oButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(xButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(xButton, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(oButton, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void xButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xButtonActionPerformed
        // TODO add your handling code here:
        this.setCellState(State.X);
    }//GEN-LAST:event_xButtonActionPerformed

    private void oButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_oButtonActionPerformed
        // TODO add your handling code here:
        this.setCellState(State.O);
    }//GEN-LAST:event_oButtonActionPerformed

    public void winner(){
        this.cellstate = State.WIN;
        this.setBackground(getColor());
    }
    
    public void setCellState(State newState){
        try {
            vtoCngSpp.fireVetoableChange("cellState", getCellState(), newState); //if it doesnt launch an exception we can continue.
            this.cellstate = newState;
            this.setBackground(getColor());
            switch(newState){
                case O:
                    xButton.setText("");
                    xButton.setEnabled(false);
                    break;
                case X:
                    oButton.setText("");
                    oButton.setEnabled(false);
                    break;
                case WIN:
                    xButton.setEnabled(false);
                    oButton.setEnabled(false);
                    break;
            }
            prpCngSpp.firePropertyChange("cellstate", null, null);
        }
        catch(PropertyVetoException exception){
            //exception.printStackTrace();
            System.out.println("wrong action");
        }
    }
    public void disable(){
        oButton.setEnabled(false);        
        xButton.setEnabled(false);
    }
    
    public void reset(){
        cellstate=State.INIT;
        oButton.setText("O");
        oButton.setEnabled(true);        
        xButton.setEnabled(true);
        xButton.setText("X");
        this.setBackground(getColor());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton oButton;
    private javax.swing.JButton xButton;
    // End of variables declaration//GEN-END:variables
}
