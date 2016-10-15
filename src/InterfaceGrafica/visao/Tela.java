package InterfaceGrafica.visao;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JComboBox;

import Gui.controle.controleTela;

import java.awt.Font;
import java.awt.Window.Type;
import javax.swing.DefaultComboBoxModel;
import java.awt.Toolkit;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Tela extends JFrame {

	private JPanel telaPane;
	private JTextArea textAreaConsole;
	private JTextArea textAreaCodigo;
	private controleTela contTela;
	private JComboBox comboBoxLinguagem;

	/**
	 * Launch the application.
	 * Create the frame.
	 */
	public Tela() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(Tela.class.getResource("/InterfaceGrafica/visao/logo_C_icon.jpg")));
		setTitle("Compilador C");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 720, 480);
		setLocationRelativeTo(null);
		contTela = new controleTela(this);
		
		telaPane = new JPanel();
		telaPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		telaPane.setLayout(null);
		setContentPane(telaPane);
		
		textAreaCodigo = new JTextArea();
		textAreaCodigo.setFont(new Font("Monospaced", Font.PLAIN, 13));
		textAreaCodigo.setLineWrap(true);
		JScrollPane scrollPaneCodigo = new JScrollPane(textAreaCodigo);
		scrollPaneCodigo.setBounds(10, 20, 694, 195);
		telaPane.add(scrollPaneCodigo);
		
		JPanel ButtonPanel = new JPanel();
		FlowLayout fl_ButtonPanel = (FlowLayout) ButtonPanel.getLayout();
		fl_ButtonPanel.setHgap(25);
		ButtonPanel.setBounds(10, 226, 694, 35);
		telaPane.add(ButtonPanel);
		
		
		
		JButton btnCarregarArquivo = new JButton("Carregar");
		btnCarregarArquivo.setActionCommand("carregar");
		btnCarregarArquivo.addActionListener(contTela);
		ButtonPanel.add(btnCarregarArquivo);
              
		
		
		JButton btnSalvar = new JButton("Salvar");
		btnSalvar.setActionCommand("salvar");
		btnSalvar.addActionListener(contTela);
		ButtonPanel.add(btnSalvar);
		
//		JButton btnALexica = new JButton("L\u00E9xico");
//		btnALexica.setActionCommand("A.Lexica");
//		btnALexica.addActionListener(contTela);
//		ButtonPanel.add(btnALexica);
		
		JButton btnASintatica = new JButton("Compilar");
		btnASintatica.setActionCommand("A.Sintatica");
		btnASintatica.addActionListener(contTela);
		ButtonPanel.add(btnASintatica);
		
//		JButton btnASemantica = new JButton("A. Sem\u00E2ntica");
//		btnASemantica.setActionCommand("A.Semantica");
//		btnASemantica.addActionListener(contTela);
//		ButtonPanel.add(btnASemantica);
		
		
		textAreaConsole = new JTextArea();
		textAreaConsole.setEditable(false);
		textAreaConsole.setLineWrap(true);
		JScrollPane scrollPaneConsole = new JScrollPane(textAreaConsole);
		scrollPaneConsole.setBounds(10, 283, 694, 157);
		telaPane.add(scrollPaneConsole);
		
		JLabel lblConsole = new JLabel("Saida:");
		lblConsole.setBounds(10, 265, 76, 14);
		telaPane.add(lblConsole);
		
		JLabel lblCdigo = new JLabel("Entrada:");
		lblCdigo.setBounds(10, 2, 76, 16);
		telaPane.add(lblCdigo);
		
//		JLabel lblSelecineALinguagem = new JLabel("Selecine a Linguagem: ");
//		lblSelecineALinguagem.setBounds(383, 2, 141, 14);
//		telaPane.add(lblSelecineALinguagem);
//		
//		comboBoxLinguagem = new JComboBox();
//		comboBoxLinguagem.setModel(new DefaultComboBoxModel(new String[] {"Linguagem C", "---outras---"}));
//		comboBoxLinguagem.setBounds(534, 1, 170, 18);
//		telaPane.add(comboBoxLinguagem);
		
		
	}
	public JTextArea getTextAreaConsole() {
		return textAreaConsole;
	}
	public JTextArea getTextAreaCodigo() {
		return textAreaCodigo;
	}
	public JComboBox getComboBoxLinguagem() {
		return comboBoxLinguagem;
	}
}
