package nucleo;

import componentesUI.Chat;
import janelas.EmJogo;
import janelas.Main;

import javax.swing.JOptionPane;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Conexao {

    private static Conexao conexao;
    private Socket soqueteCliente;
    private ServerSocket soqueteServidor;
    private InputStreamReader streamReader;
    private BufferedReader reader;
    private PrintWriter writer;

    public static Conexao getConexao() {
        if (conexao == null) {
            conexao = new Conexao();
        }

        return conexao;
    }

    public void inicializarFluxos() {
        try {
            Conexao.getConexao().streamReader = new InputStreamReader(soqueteCliente.getInputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        Conexao.getConexao().reader = new BufferedReader(streamReader);

        try {
            Conexao.getConexao().writer = new PrintWriter(soqueteCliente.getOutputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void conectarCliente(String _endereco, int _porta) {
        try {
            soqueteCliente = new Socket(_endereco, _porta);
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        inicializarFluxos();
    }

    public class Ouvinte implements Runnable {

        private String mensagem;

        @Override
        public void run() {
            while (true) {
                try {
                    while ((mensagem = Conexao.getConexao().reader.readLine()) != null) {
                        if (mensagem.charAt(0) == 'c') {
                            String temp = mensagem.substring(2);
                            Chat.getChat().colocaMensagemAreaChat(temp);
                        }
                        if (mensagem.charAt(0) == 'j') {
                            int x = Integer.parseInt(Character.toString(mensagem.charAt(2)));
                            int y = Integer.parseInt(Character.toString(mensagem.charAt(4)));
                            String resultado = Jogador.getJogador().getPontosLogico(x, y);

                            int pontos = 0;
                            if (!resultado.equals("sea_tile")) {
                                pontos = 10;
                            }
                            EmJogo.getMini().setBotao(x, y, resultado);
                            enviarPontuacao(pontos, resultado, x, y);
                        }
                        if (mensagem.charAt(0) == 'p') {
                            String vetor[] = mensagem.split(":");
                            int pontos = Integer.parseInt(vetor[1]);
                            String icone = vetor[2];
                            int x = Integer.parseInt(vetor[3]);
                            int y = Integer.parseInt(vetor[4]);

                            Jogador.getJogador().setPontos(Jogador.getJogador().getPontos() + pontos);
                            EmJogo.getGrid().setBotao(x, y, icone);
                        }
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public class EventoAceitaConexao implements Runnable {

        @Override
        public void run() {
            try {
                Conexao.getConexao().soqueteCliente = soqueteServidor.accept();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            Conexao.getConexao().inicializarFluxos();

            Main.mostraConfigGrid();

            Thread ouvinte = new Thread(new Ouvinte());
            ouvinte.start();
        }
    }

    public void inicializarServidor(int _porta) {
        try {
            soqueteServidor = new ServerSocket(_porta);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        Thread aceita = new Thread(new EventoAceitaConexao());
        aceita.start();
    }

    public void enviarMensagemChat(String _msg) {

        if (soqueteCliente == null) {
            System.out.println("soquetecliente null");
        }

        System.out.println("c:" + _msg);

        writer.print("c:" + _msg);
        writer.flush();
    }

    public void enviarCoordenadas(int x, int y) {

        System.out.println("j:" + x + ":" + y);

        writer.print("j:" + x + ":" + y);
        writer.flush();
    }

    public void enviarPontuacao(int pontos, String icone, int x, int y) {

        System.out.println("p:" + pontos + ":" + icone + ":" + x + ":" + y);

        writer.print("p:" + pontos + ":" + icone + ":" + x + ":" + y);
        writer.flush();
    }

    public String detectarIP() {
        String enderecoIP = null;
        try {
            enderecoIP = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (enderecoIP.equals("127.0.0.1")) {
            JOptionPane.showMessageDialog(null, "Não há conexão de rede disponível.", "Alerta", JOptionPane.WARNING_MESSAGE);
        }

        return enderecoIP;
    }
}
