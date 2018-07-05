/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prova02;

import java.util.Scanner;

public class Cliente {

    public static void main(String[] args) { 
        String endereco = "192.168.5.216";
        Scanner keyb = new Scanner(System.in); 
        String resp;
        ClienteTCP c = new ClienteTCP(endereco, 12345);
        String[] dados = c.connectTCP();
        
        ClienteUDP u = new ClienteUDP(endereco, dados[1], dados[2], dados[3], dados[4]);
        u.start();
        do {
            
            System.out.println("DIGITE 'FIM' SE DESEJA PARAR.");
            resp = keyb.nextLine();
            
        } while(!"fim".equals(resp));
        
        u.endthis();
    }
}