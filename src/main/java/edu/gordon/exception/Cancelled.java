/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gordon.exception;

/**
 *
 * @author Zeldorine
 */
public class Cancelled extends Exception {
            /** Constructor
         */
        public Cancelled()
        {
            super("Cancelled by customer");
        }
}
