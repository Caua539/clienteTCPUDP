/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prova02;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author caua5
 */
public class ClienteTCP {
    
    private Socket conexao;
    private final String endereco;
    private final int portatcp;
    
    public ClienteTCP(String endereco, int porta){
        this.endereco = endereco;
        this.portatcp = porta;
        connect();
    }
    
    private void connect(){
        try {
            conexao = new Socket(endereco, portatcp);
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            System.out.println("Conectado ao servidor " + endereco + ", na porta: " + portatcp);
        }
    }
 
    public String[] connectTCP() {
        OutputStream os;
        InputStream is;
        String mensagem = "";

        try {       
            
            os = conexao.getOutputStream();
            mensagem = "GET LOCALS";
            os.write(mensagem.getBytes());
            
            is = conexao.getInputStream();
            byte[] b = new byte[256];
            is.read(b);
            String resposta = new String(b);
            System.out.println("SERVIDOR >>>> "+resposta);
            
            String[] cidades = resposta.split("\\|");
            System.out.println(Arrays.toString(cidades));
            mensagem = "REGISTER "+cidades[1];
            os.write(mensagem.getBytes());
            
            is.read(b);
            resposta = new String(b);
            System.out.println("SERVIDOR >>>> "+resposta);
            conexao.close();
            String[] tcpinfo = resposta.split("\\|");
            
            return tcpinfo;
            
            
        } catch (IOException e) {
            System.err.println("erro: " + e.toString());
            return null;
        }
 
    }
}
