package componentesUI;

import janelas.Main;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.text.DefaultCaret;
import nucleo.Conexao;
import nucleo.Jogador;

public class Chat extends JPanel {
    
    private static Chat chat;
    private JTextArea areaChat;
    private JScrollPane scrollChat;
    private JButton botNome;
    private JTextField areaEntrada;
    private JButton botEnviar;
    
    public static Chat getChat() {
        if (chat == null) {
            chat = new Chat();
        }
        return chat;
    }
    
    private Chat() {
        setLayout(null);
        setPreferredSize(new Dimension(700, 300));

        // areaChat
        areaChat = new JTextArea();
        areaChat.setEditable(false);
        areaChat.setLineWrap(true);
        areaChat.setAutoscrolls(false);
        areaChat.setBorder(null);
        areaChat.setCursor(new Cursor(Cursor.TEXT_CURSOR));
        
        DefaultCaret caret = (DefaultCaret)areaChat.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        
        scrollChat = new JScrollPane(areaChat);
        scrollChat.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollChat.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollChat.setAutoscrolls(true);

        // tamanho e posicionamento da areaChat
        scrollChat.setPreferredSize(new Dimension(700, 100));
        Dimension tamAreaChat = scrollChat.getPreferredSize();
        scrollChat.setBounds(0, 0, tamAreaChat.width, tamAreaChat.height);
        add(scrollChat);

        // botNome
        botNome = new JButton(Jogador.getJogador().getNome());
        botNome.setMargin(new Insets(0, 0, 0, 0));

        // tamanho e posicionamento botNome
        botNome.setPreferredSize(new Dimension(100, 22));
        botNome.setToolTipText("Editar nome");
        Dimension tamBotNome = botNome.getPreferredSize();
        botNome.setBounds(0, 8 + tamAreaChat.height, tamBotNome.width, tamBotNome.height);
        botNome.addActionListener(new EventoBotaoNome());
        add(botNome);

        // areaEntrada
        areaEntrada = new JTextField();
        areaEntrada.addKeyListener(new EventoAreaEntrada());

        // tamanho e posicionamento da areaEntrada
        areaEntrada.setPreferredSize(new Dimension(513, 23));
        Dimension tamAreaEntrada = areaEntrada.getPreferredSize();
        areaEntrada.setBounds(tamBotNome.width + 8, 8 + tamAreaChat.height, tamAreaEntrada.width, tamAreaEntrada.height);
        add(areaEntrada);

        // botEnviar
        botEnviar = new JButton("Enviar");
        botEnviar.addActionListener(new EventoBotEnviar());

        // tamanho e posicionamento do botEnviar
        botEnviar.setPreferredSize(new Dimension(70, 22));
        Dimension tamBotEnviar = botEnviar.getPreferredSize();
        botEnviar.setBounds(16 + tamBotNome.width + tamAreaEntrada.width, 8 + tamAreaChat.height, tamBotEnviar.width, tamBotEnviar.height);
        add(botEnviar);
    }

    // adiciona a frase à janela de chat
    private void enviarMensagem(String _msg) {
        colocaMensagemAreaChat(_msg);
        Conexao.getConexao().enviaMensagemChat(_msg);
        
        areaEntrada.setText("");
        areaEntrada.requestFocus();
    }
    
    public void colocaMensagemAreaChat(String _msg) {
        areaChat.append(_msg + "\n");
    }
    
    public void limpaAreaChat() {
        areaChat.setText("");
    }
    
    public void setBotNome(String _nome) {
        botNome.setText(_nome);
    }
    
    private class EventoBotEnviar implements ActionListener {
        
        @Override
        public void actionPerformed(ActionEvent ev) {
            if (!areaEntrada.getText().equals("")) {
                enviarMensagem(botNome.getText() + ": " + areaEntrada.getText());
            }
        }
    }
    
    private class EventoAreaEntrada extends KeyAdapter {
        
        @Override
        public void keyReleased(KeyEvent ev) {
            if (ev.getKeyCode() == KeyEvent.VK_ENTER) {
                if (!areaEntrada.getText().equals("")) {
                    enviarMensagem(botNome.getText() + ": " + areaEntrada.getText());
                }
            }
        }
    }
    
    private class EventoBotaoNome implements ActionListener {
        
        @Override
        public void actionPerformed(ActionEvent ev) {
            String nome = JOptionPane.showInputDialog(Main.getJanela(), "Digite seu novo nome:", "Alteração de nome", JOptionPane.QUESTION_MESSAGE);
            if (!nome.equals("") && nome != null) {
                setBotNome(nome);
            }
        }
    }
}
