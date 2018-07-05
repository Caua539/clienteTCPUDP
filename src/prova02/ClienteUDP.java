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
 * @author caua5
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
            byte[] sendData = new byte[1024];
            byte[] recieveData = new byte[1024];
            
            mensagem = "START "+token;
            sendData = mensagem.getBytes();
            DatagramPacket sendpct = new DatagramPacket(sendData, sendData.length, IP, 54321);
            clientSocket.send(sendpct);
            
            DatagramPacket recievepct = new DatagramPacket(recieveData, recieveData.length);
            clientSocket.receive(recievepct);
            resposta = new String(recievepct.getData());
            System.out.println("SERVIDOR >>>> "+resposta);
            resposta = resposta.trim();
            
            if (resposta.equals("OK")) {
                System.out.println("Oi?");
                byte[] sdata = new byte[1024];
                DatagramPacket spct = new DatagramPacket(sdata, sdata.length, IP, portaudp);
                
                do {
                    
                    clientSocket.receive(spct);
                    resposta = new String(spct.getData());
                    System.out.println("SERVIDOR >>>>> "+resposta);
                    resposta.replace("TEMP ", "");
                    dados = resposta.split(separador);
                    int aux = 0;
                    
                    for (Map.Entry<String, String> entry : dict.entrySet()){
                        
                        dict.put(entry.getKey(), dados[aux]);
                        aux++;
                    }
                    
                    System.out.println("========================================================");
                    System.out.println(String.format("Localidade: %s", dict.get("L")));
                    System.out.println(String.format("Data: %s", dict.get("D")));
                    System.out.println(String.format("Temperaturas máxima e mínima previstas: [Mín: %s] e [Max: %s]", dict.get("M"), dict.get("X")));
                    System.out.println(String.format("Temperatura Atual: %s graus às %s horas", dict.get("T"), dict.get("H")));
                    System.out.println("========================================================");
                    
                } while(controle);
                
                mensagem = "STOP "+token;
                sendData = mensagem.getBytes();
                sendpct = new DatagramPacket(sendData, sendData.length, IP, portaudp);
                clientSocket.send(sendpct);
                
                clientSocket.receive(recievepct);
                resposta = new String(recievepct.getData());
                System.out.println("SERVIDOR >>>> "+resposta);
            }
            
            
        } catch (IOException e) {
            System.err.println("erro: " + e.toString());
        }
    }
    
}
