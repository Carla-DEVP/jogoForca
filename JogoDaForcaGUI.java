package jogoForca;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class JogoDaForcaGUI {
    private JFrame frame;
    private JTextField campoLetra;
    private JLabel palavraOculta;
    private JLabel dicaIA;
    private JLabel letrasUsadas;
    private PainelDesenho painelDesenho;
    private String palavra;
    private StringBuilder exibicao;
    private StringBuilder letrasChutadas = new StringBuilder();
    private int erros = 0;

    private final String[] palavras = {
        "JAVA", "OBJETO", "CLASSE", "HERANCA", "POLIMORFISMO", "INTERFACE", "PACOTE", "CONSTRUTOR", "METODO", "ATRIBUTO"
    };

    public JogoDaForcaGUI() {
        frame = new JFrame("Jogo da Forca - Versão Gráfica");
        frame.setSize(600, 450);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));

        iniciarNovaPalavra();

        // Topo com dica e palavra oculta
        JPanel painelTopo = new JPanel();
        painelTopo.setLayout(new BoxLayout(painelTopo, BoxLayout.Y_AXIS));
        dicaIA = new JLabel("Clique em Dica da IA para uma sugestão!");
        dicaIA.setAlignmentX(Component.CENTER_ALIGNMENT);
        palavraOculta = new JLabel(formatarExibicaoComContagem());
        palavraOculta.setFont(new Font("Courier New", Font.BOLD, 18));
        palavraOculta.setHorizontalAlignment(SwingConstants.CENTER);
        palavraOculta.setAlignmentX(Component.CENTER_ALIGNMENT);
        painelTopo.add(dicaIA);
        painelTopo.add(palavraOculta);
        frame.add(painelTopo, BorderLayout.NORTH);

        // Painel de desenho
        painelDesenho = new PainelDesenho();
        painelDesenho.setPreferredSize(new Dimension(300, 250));
        frame.add(painelDesenho, BorderLayout.CENTER);

        // Inferior com campo, botões e letras usadas
        JPanel painelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        campoLetra = new JTextField(2);
        JButton botaoChutar = new JButton("Chutar");
        JButton botaoDica = new JButton("Dica da IA");
        letrasUsadas = new JLabel("Letras chutadas: ");

        painelInferior.add(new JLabel("Letra:"));
        painelInferior.add(campoLetra);
        painelInferior.add(botaoChutar);
        painelInferior.add(botaoDica);
        painelInferior.add(letrasUsadas);
        frame.add(painelInferior, BorderLayout.SOUTH);

        // Ações
        botaoChutar.addActionListener(e -> verificarLetra());
        botaoDica.addActionListener(e -> mostrarDica());

        frame.setVisible(true);
    }

    private void iniciarNovaPalavra() {
        Random rand = new Random();
        palavra = palavras[rand.nextInt(palavras.length)];
        exibicao = new StringBuilder();
        for (int i = 0; i < palavra.length(); i++) exibicao.append("_");
    }

    private void verificarLetra() {
        String letra = campoLetra.getText().toUpperCase();
        campoLetra.setText("");

        if (letra.length() != 1 || !Character.isLetter(letra.charAt(0))) {
            JOptionPane.showMessageDialog(frame, "Digite apenas uma letra.");
            return;
        }

        if (letrasChutadas.toString().contains(letra)) {
            JOptionPane.showMessageDialog(frame, "Você já tentou essa letra!");
            return;
        }

        letrasChutadas.append(letra).append(" ");
        letrasUsadas.setText("Letras chutadas: " + letrasChutadas);

        boolean acertou = false;
        for (int i = 0; i < palavra.length(); i++) {
            if (palavra.charAt(i) == letra.charAt(0)) {
                exibicao.setCharAt(i, letra.charAt(0));
                acertou = true;
            }
        }

        palavraOculta.setText(formatarExibicaoComContagem());

        if (!acertou) {
            erros++;
            painelDesenho.setErros(erros);
            if (erros >= 6) {
                JOptionPane.showMessageDialog(frame, "Você perdeu! A palavra era: " + palavra);
                reiniciarJogo();
            }
        } else if (exibicao.toString().equals(palavra)) {
            JOptionPane.showMessageDialog(frame, "Parabéns! Você acertou a palavra: " + palavra);
            reiniciarJogo();
        }
    }

    private void mostrarDica() {
        String dica = switch (palavra) {
            case "JAVA" -> "É uma linguagem de programação multiplataforma.";
            case "OBJETO" -> "Elemento fundamental da programação orientada a objetos.";
            case "CLASSE" -> "Molde para criar objetos.";
            case "HERANCA" -> "Permite reutilizar código de uma classe base.";
            case "POLIMORFISMO" -> "Permite que o mesmo método se comporte de formas diferentes.";
            case "INTERFACE" -> "Define métodos que uma classe deve implementar.";
            case "PACOTE" -> "Forma de organizar classes em Java.";
            case "CONSTRUTOR" -> "Método especial chamado na criação de um objeto.";
            case "METODO" -> "Define um comportamento de um objeto.";
            case "ATRIBUTO" -> "Propriedade ou característica de um objeto.";
            default -> "Sem dica disponível.";
        };
        dicaIA.setText("Dica da IA: " + dica);
    }

    private void reiniciarJogo() {
        iniciarNovaPalavra();
        palavraOculta.setText(formatarExibicaoComContagem());
        erros = 0;
        painelDesenho.setErros(erros);
        dicaIA.setText("Clique em Dica da IA para uma sugestão!");
        letrasChutadas.setLength(0);
        letrasUsadas.setText("Letras chutadas: ");
    }

    private String formatarExibicaoComContagem() {
        return exibicao.toString().replace("", " ").trim() + " (" + palavra.length() + " letras)";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(JogoDaForcaGUI::new);
    }
    
}

class PainelDesenho extends JPanel {
    private int erros;

    public void setErros(int erros) {
        this.erros = erros;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawLine(50, 200, 150, 200); // base
        g.drawLine(100, 200, 100, 50); // vertical
        g.drawLine(100, 50, 200, 50);  // topo
        g.drawLine(200, 50, 200, 80);  // corda

        if (erros > 0) g.drawOval(180, 80, 40, 40);         // cabeça
        if (erros > 1) g.drawLine(200, 120, 200, 160);      // corpo
        if (erros > 2) g.drawLine(200, 130, 170, 150);      // braço esq
        if (erros > 3) g.drawLine(200, 130, 230, 150);      // braço dir
        if (erros > 4) g.drawLine(200, 160, 170, 190);      // perna esq
        if (erros > 5) g.drawLine(200, 160, 230, 190);      // perna dir
    }
}
