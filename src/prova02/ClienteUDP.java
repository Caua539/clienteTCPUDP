/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prova02;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 *
 * @author caua5
 */
public class ClienteUDP extends Thread{
    
    String endereco;
    String token;
    int portaudp;
    String separador;
    boolean controle = true;
    
    public ClienteUDP(String endereco, String token, String separador, int portaudp) {
        this.endereco = endereco;
        this.token = token;
        this.portaudp = portaudp;
        this.separador = separador;
    }
    
    public void endthis() {
        controle = false;
    }
    
    
    
    
    public void run(){
        String mensagem;
        String resposta;

        try {       
            
            DatagramSocket clientSocket = new DatagramSocket();
            InetAddress IP = InetAddress.getByName(endereco);
            byte[] sendData = new byte[1024];
            byte[] recieveData = new byte[1024];
            
            mensagem = "START "+token;
            sendData = mensagem.getBytes();
            DatagramPacket sendpct = new DatagramPacket(sendData, sendData.length, IP, portaudp);
            clientSocket.send(sendpct);
            
            DatagramPacket recievepct = new DatagramPacket(recieveData, recieveData.length);
            clientSocket.receive(recievepct);
            resposta = new String(recievepct.getData());
            
            if (resposta == "OK") {
                
                do {
                    
                }
            }
            
            
        } catch (IOException e) {
            System.err.println("erro: " + e.toString());
        }
    }
    
}
