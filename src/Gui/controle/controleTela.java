package Gui.controle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import Gramatica.Lexico;
//import Gramatica.Semantico;
import Gramatica.Sintatico;
import InterfaceGrafica.visao.Tela;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class controleTela implements ActionListener {

    private final Tela tela;
    private Lexico LinCLex;
    private Sintatico LinCSin;
//    private Semantico LinCSem;
    private String codigo;

    public controleTela(Tela tela) {
        this.tela = tela;
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        

        if (evt.getActionCommand().equals("salvar")) {

            if (LinCLex != null) {
                LinCLex.setCodigoNoArquivo(codigo, tela.getTextAreaCodigo().getText());
                tela.getTextAreaConsole().setText("Salvo com sucesso!");
            } else {
                JOptionPane.showMessageDialog(null, "Arquivo nao carregado!");
            }
        }

        if (evt.getActionCommand().equals("carregar")) {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Selecionar arquivo!");
            chooser.setFileFilter(new FileNameExtensionFilter("Texto", "txt"));
            chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            chooser.setAcceptAllFileFilterUsed(false);
            chooser.showOpenDialog(null);
            File file = chooser.getSelectedFile();
            codigo = file.toString();

            LinCLex = new Lexico(codigo);
            LinCSin = new Sintatico(codigo);
//            LinCSem = new Semantico(codigo);
            tela.getTextAreaCodigo().setText(LinCLex.getCodigoNoArquivo());
            tela.getTextAreaConsole().setText("Carregado com sucesso!");
            tela.repaint();
        }

        if (evt.getActionCommand().equals("A.Lexica")) {
            if (LinCLex != null) {
                tela.getTextAreaConsole().setText("");
                LinCLex = new Lexico(codigo);
                LinCLex.VerificarContexto();
                tela.getTextAreaConsole().setText(LinCLex.getResultadoNoConsole());
                tela.repaint();
            } else {
                JOptionPane.showMessageDialog(null, "Arquivo nao carregado!");
            }
        }
        if (evt.getActionCommand().equals("A.Sintatica")) {
            if (LinCSin != null) {
                tela.getTextAreaConsole().setText("");
                LinCSin = new Sintatico(codigo);
                LinCSin.VerificarContexto();
                tela.getTextAreaConsole().setText(LinCSin.getResultadoNoConsole());
                tela.repaint();
            } else {
                JOptionPane.showMessageDialog(null, "Arquivo nao carregado!");
            }
        }

//        if (evt.getActionCommand().equals("A.Semantica")) {
//            if (LinCSin != null) {
//                tela.getTextAreaConsole().setText("");
//                LinCSem = new Semantico(codigo);
//                LinCSem.VerificarContexto();
//                tela.getTextAreaConsole().setText(LinCSem.getResultadoNoConsole());
//                tela.repaint();
//            } else {
//                JOptionPane.showMessageDialog(null, "Arquivo nao carregado!");
//            }
//        }
    }

}
