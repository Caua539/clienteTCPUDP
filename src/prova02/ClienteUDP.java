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
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Cauã Martins Pessoa
 */
public class ClienteUDP extends Thread{
    
    String endereco;
    String token;
    int portaudp;
    String separador;
    boolean controle = true;
    String[] formato;
    Map<String, String> dict;
    
    public ClienteUDP(String endereco, String token, String formato, String separador, String portaudp) {
        this.endereco = endereco;
        this.token = token;
        this.portaudp = Integer.valueOf(portaudp);
        this.separador = separador;
        this.dict = criarDict(formato.split(""));
    }
    
    public void endthis() {
        controle = false;
    }
    
    private Map criarDict(String[] formato){
        Map<String, String> dict = new HashMap<String, String>();
        for (String s : formato){
            dict.put(s, "");
        }
        
        return dict;
    }
    
    @Override
    public void run(){
        String mensagem;
        String resposta;
        String[] dados;

        try {       
            
            DatagramSocket clientSocket = new DatagramSocket();
            InetAddress IP = InetAddress.getByName(endereco);
            
            mensagem = "START "+token;
            System.out.println("CLIENTE >>>> "+mensagem);
            byte[] sendData = mensagem.getBytes();
            DatagramPacket sendpct = new DatagramPacket(sendData, sendData.length, IP, 54321);
            clientSocket.send(sendpct);
            
            byte[] receiveData = new byte[1024];
            DatagramPacket receivepct = new DatagramPacket(receiveData, receiveData.length);
            clientSocket.receive(receivepct);
            
            resposta = new String(receivepct.getData());
            resposta = resposta.trim();
            System.out.println("SERVIDOR <<<< "+resposta);
            
            DatagramSocket receivesckt = new DatagramSocket(this.portaudp);
            
            
            
            while(controle) {
                receivesckt.receive(receivepct);
                resposta = new String(receivepct.getData());
                
                System.out.println("SERVIDOR <<<< "+resposta);
                resposta = resposta.replace("TEMP ", "");
                dados = resposta.split(separador);
                int aux = 0;
                    
                for (Map.Entry<String, String> entry : dict.entrySet()){
                        
                    dict.put(entry.getKey(), dados[aux]);
                    aux++;
                }
                    
                System.out.println("========================================================");
                System.out.println(String.format("Localidade: %s", dict.get("H")));
                System.out.println(String.format("Data: %s", dict.get("D")));
                System.out.println(String.format("Temperaturas máxima e mínima previstas: [Mín: %s] e [Max: %s]", dict.get("L"), dict.get("M")));
                System.out.println(String.format("Temperatura Atual: %s graus às %s horas", dict.get("X"), dict.get("T")));
                System.out.println("========================================================");                
            }
            
            byte[] stop = ("STOP "+token).getBytes();
            sendpct = new DatagramPacket(stop, stop.length, IP, 54321);
            clientSocket.send(sendpct);

            clientSocket.receive(receivepct);
            resposta = new String(receivepct.getData());
            System.out.println("SERVIDOR <<<< "+resposta);
            
            
        } catch (IOException e) {
            System.err.println("erro: " + e.toString());
        }
    }
    
}
