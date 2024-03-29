package nucleo;

public class Jogador {

    private static Jogador jogador;
    private String nome;
    private int pontos;
    private GridLogico gridLogico;
    private boolean turno;
    private int contador;
    private int contadorConsecutivo;
    private boolean servidor;

    public void atualizaGridLogico(int x, int y, String icone) {
        gridLogico.colocaBarco(x, y, icone);
    }

    public void limpaGridLogico() {
        gridLogico.limpa();
    }

    public String getPosicaoLogico(int x, int y) {
        return gridLogico.getPosicao(x, y);
    }

    public String getPontosLogico(int x, int y) {
        return gridLogico.abreCampo(x, y);
    }

    public static Jogador getJogador() {
        if (jogador == null) {
            jogador = new Jogador();
        }
        return jogador;
    }

    private Jogador() {
        contador = 0;
        contadorConsecutivo = 0;
        pontos = 0;
        servidor = false;
        gridLogico = new GridLogico();
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setPontos(int pontos) {
        this.pontos = pontos;
    }

    public int getPontos() {
        return pontos;
    }

    public void setTurno(boolean turno) {
        this.turno = turno;
    }

    public boolean getTurno() {
        return turno;
    }

    public void incrementaContador() {
        contador++;
    }

    public int getContador() {
        return contador;
    }

    public void zeraContador() {
        contador = 0;
    }

    public void incrementaContadorConsecutivo() {
        contadorConsecutivo++;
    }

    public int getContadorConsecutivo() {
        return contadorConsecutivo;
    }

    public void zeraContadorConsecutivo() {
        contadorConsecutivo = 0;
    }

    public void setServidor(boolean estado) {
        this.servidor = estado;
    }

    public boolean getServidor() {
        return servidor;
    }
}