package Gramatica;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Lexico {
	private final ArrayList<String> Comandos = new ArrayList<>();
	private final ArrayList<String> PalavrasReservadas = new ArrayList<>();
	private final ArrayList<String> CaracteresEspeciais = new ArrayList<>();
	private final ArrayList<Character> Exprecoes = new ArrayList<>();
	private final ArrayList<Variavel> Variaveis = new ArrayList<>();
	private final ArrayList<String> StringsDeImpressao = new ArrayList<>();
	private final ArrayList<Character> Separadores = new ArrayList<>();
	private final ArrayList<Character> Alfabeto = new ArrayList<>();
	private String resultadoNoConsole = "";
	private final ArrayList<Character> Numerico = new ArrayList<>();
	private FileReader arq;
	private BufferedReader lerArq;
	private final String arquivo;
	private String codigoNoArquivo = "";
	private String anaLexica = "";
	
	//##########################################################
	
	public Lexico(String arq){
		this.arquivo = arq;
		adicionarNosArrays();
	}
	
	//##########################################################
	
	private void adicionarNosArrays(){
		
		Comandos.add("printf");Comandos.add("scanf");
		
		//adicionando palavras reservadas
		PalavrasReservadas.add("int ");PalavrasReservadas.add("char ");PalavrasReservadas.add("double ");
		PalavrasReservadas.add("float ");PalavrasReservadas.add("if");PalavrasReservadas.add("for");
		PalavrasReservadas.add("while");PalavrasReservadas.add("switch");PalavrasReservadas.add("case");
		PalavrasReservadas.add("void");
		
		//adicionando strings de imprecao do printf ou 
		StringsDeImpressao.add("%d");StringsDeImpressao.add("%f");StringsDeImpressao.add("%s");
		
		//adicionando caracteres especiais
		CaracteresEspeciais.add("(");CaracteresEspeciais.add(")");CaracteresEspeciais.add("&");
		CaracteresEspeciais.add("|");CaracteresEspeciais.add("{");CaracteresEspeciais.add("}");
		CaracteresEspeciais.add(";");CaracteresEspeciais.add("==");CaracteresEspeciais.add("+=");
		CaracteresEspeciais.add("!=");CaracteresEspeciais.add(">=");CaracteresEspeciais.add("<=");
		CaracteresEspeciais.add("<");CaracteresEspeciais.add(">");CaracteresEspeciais.add("++");
		CaracteresEspeciais.add("--");
		
	
		Exprecoes.add('+');Exprecoes.add('-');Exprecoes.add('/');Exprecoes.add('*');
		
		Separadores.add('\n');Separadores.add('\r');Separadores.add('\t');Separadores.add(' ');
		//adicionando alfabeto ASCII na linguagem
		for (int i = 33; i <= 126; i++) {
			Alfabeto.add((char)i);
		}
		//adicionando numericos
		for (int i = '0'; i <= '9'; i++) {
			Numerico.add((char)i);
		}
		
	}
	
	//##########################################################
	//manda oque esta no textAreaCodigo para o arquivo
	public void setCodigoNoArquivo(String diretorio, String codigo){
		try {
			File arq = new File(diretorio);
			@SuppressWarnings("resource")
			FileWriter fw = new FileWriter(arq);
			FileOutputStream fos = new FileOutputStream(arq);
			if(arq.exists() == false){
				arq.createNewFile();
			}
			fw.write(codigo);
			if(codigo.contains("\r") == false)
				  codigo = codigo.replaceAll("\n", "\r\n");
			fos.write(codigo.getBytes());
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			resultadoNoConsole = "Erro de arquivo: "+e.getMessage()+".\n";
		} catch (IOException e) {
			e.printStackTrace();
			resultadoNoConsole = "Erro de arquivo: "+e.getMessage()+".\n";
		}
	}
	
	//##########################################################
	//pega oque esta no arquivo e retorna uma string contendo todo o código
	public String getCodigoNoArquivo(){
		try{
		ler();
		String lin = lerArq.readLine();
		while(lin != null){
			codigoNoArquivo += lin+"\n";
			lin = lerArq.readLine();
		}
			
		return codigoNoArquivo;
		}catch (Exception e) {
			resultadoNoConsole = "Erro de arquivo: "+e.getMessage()+".\n";
		}
		return "";
	}

	private void ler(){
		try{
			arq = new FileReader(this.arquivo);
			lerArq = new BufferedReader(arq);
		}catch (Exception e) {
			  resultadoNoConsole = "Erro de arquivo: "+e.getMessage()+".\n";
		}
	}
	
	//##########################################################
	//verifica o contexto do codigo NO ARQUIVO CodigoC.txt, fazendo a análise l�xica
	public void VerificarContexto() {
		int numeroDaLinha = 1;
		
		try {
		  numeroDaLinha = 1;
		  resultadoNoConsole = "================Saida:===============\n\n";
		  anaLexica = "\n\n============Analise Lexica:=============\n\n";
		  ler();

		  String linha = lerArq.readLine(); // l� a primeira linha
		  
		  while (linha != null) {
			  char caractereInvalido = ' '; // essa variavel recebera o primeiro caractere invalido da linha
			  int pos = 0;
			  int casoTenhaErro = 1;//essa variavel continuara 1 caso tenha algum caractere invalido no codigo
			  
			  //verificador de caracteres da linha
		    if(linha.length() != 0){
			  for (int i = 0; i < linha.length(); i++) { 
				  pos = i;
				  casoTenhaErro = 1;
				  //aqui verificara nos for's se ha alguma palavra correspondente a gramatica senao da um erro
				  for (int j = 0; j < Alfabeto.size(); j++) {
					  //aqui o 'casoTenhaErro' eh 1 se caso encontre 
					  //uma variavel correspodente ele mudara para 0
					  if(Alfabeto.get(j).equals(linha.charAt(i))){
						  casoTenhaErro = 0;
						  break;
					  }
				  }
				  if(casoTenhaErro == 1){
					  for (int j = 0; j < CaracteresEspeciais.size(); j++) {
						  //aqui tbm so que verifica caracteresEspeciais
						  if(CaracteresEspeciais.get(j).equals(linha.charAt(i))){
							  casoTenhaErro = 0;
							  break;
						  }
					  }
				  }
				  if(casoTenhaErro == 1){
					  for (int j = 0; j < Exprecoes.size(); j++) {
						  //e assim por diante
						  casoTenhaErro = 1;
						  if(Exprecoes.get(j).equals(linha.charAt(i))){
							  casoTenhaErro = 0;
							  break;
						  }  
					  }
				  }
				  if(casoTenhaErro == 1){
					  for (int j = 0; j < Separadores.size(); j++) {
						  casoTenhaErro = 1;
						  if(Separadores.get(j).equals(linha.charAt(i))){
							  casoTenhaErro = 0;
							  break;
						  }  
					  }
				  }
				  if(casoTenhaErro == 1){
					  for (int j = 0; j < Numerico.size(); j++) {
						  casoTenhaErro = 1;
						  if(Numerico.get(j).equals(linha.charAt(i))){
							  casoTenhaErro = 0;
							  break;
						  }  
					  }
				  }//System.out.println(casoTenhaErro);
				  
				  caractereInvalido = linha.charAt(i);
				  if(casoTenhaErro == 1)
					  break;
			   }
			 }else{
				  casoTenhaErro = 3;
				  anaLexica += "Linha "+numeroDaLinha+"- Espaco vazio";
			  }
			  
			  if(casoTenhaErro == 1){
				  anaLexica += "Linha "+numeroDaLinha+"- caractere invalido ("+caractereInvalido+", posicao = "+pos+")";
			  }else{
				if(casoTenhaErro != 3){
				  anaLexica += "Linha "+numeroDaLinha+"- ";
				  
				  for (int i = 0; i < Comandos.size(); i++) {
					  if(linha.contains(Comandos.get(i))){
						  temComando(linha, i);
						  //resultadoNoConsole += Comandos.get(i)+"\n";
					  }
				  }
				  for (int i = 0; i < PalavrasReservadas.size(); i++) {
					  if(linha.contains(PalavrasReservadas.get(i))){
						  temPaReservada(linha, i);
					  }
				  }
				  for (int i = 0; i < CaracteresEspeciais.size(); i++) {
					  if(linha.contains(CaracteresEspeciais.get(i))){
						  temCaracEspecial(linha, i);
					  }
				  }
				  for (int i = 0; i < Numerico.size(); i++) {
					  if(linha.contains(""+Numerico.get(i))){
						  temNumerico(linha, i);
						  break;
					  }
				  }
				  verifSeTemVariavel(linha);
			  
				}
			  
			  }
			  anaLexica = anaLexica.replace("Linha "+numeroDaLinha+"-   ", "Linha "+numeroDaLinha+"-");
			  anaLexica += "\n";
			  numeroDaLinha++;
			  linha = lerArq.readLine();
		  }
		  arq.close();
		  resultadoNoConsole += anaLexica;
		} catch (IOException e) {
			resultadoNoConsole = "Erro de arquivo: "+e.getMessage()+".\n";
		}
		
	}

	//##########################################################

	private void verifSeTemVariavel(String linha) {
		// TODO Auto-generated method stub
		
	}

	private void temNumerico(String linha, int idNumerico) {
		if(linha.indexOf(Numerico.get(idNumerico)) > linha.indexOf("\"")&& 
				linha.indexOf(Numerico.get(idNumerico)) < 
					linha.substring(linha.indexOf("\"")+1).indexOf("\"")+linha.indexOf("\""))
		{
		  return;
		}
		for (int i = 0; i < linha.length(); i++) {
			for (int j = 0; j < Numerico.size(); j++) {
				if(Numerico.get(j).equals(linha.charAt(i))){
					if(linha.charAt(i-1) == ' '){
						String numerico = "";
						int aux = 1, aux2 = i;
						while(aux > 0){
							if(linha.charAt(aux2) >= '0' && linha.charAt(aux2) <= '9')
								numerico += linha.charAt(aux2++);
							else
								aux = -1;
						}
						if(numerico.equals("") == false)
							anaLexica += "   Numerico: "+numerico;
					}
					
				}
			}
		}
			
	}

	private void temCaracEspecial(String linha, int idCaracEsp) {
		if(linha.indexOf(CaracteresEspeciais.get(idCaracEsp)) > linha.indexOf("\"")&& 
				linha.indexOf(CaracteresEspeciais.get(idCaracEsp)) < 
					linha.substring(linha.indexOf("\"")+1).indexOf("\"")+linha.indexOf("\""))
		{
		  return;
		}
		
		switch(CaracteresEspeciais.get(idCaracEsp)){
		case "=":
			anaLexica += "   Caractere Especial: '"+CaracteresEspeciais.get(idCaracEsp)+"'";
			break;
		case "++":
			anaLexica += "   Caractere Especial: '"+CaracteresEspeciais.get(idCaracEsp)+"'";
			break;
		case "--":
			anaLexica += "   Caractere Especial: '"+CaracteresEspeciais.get(idCaracEsp)+"'";
			break;
		case ">":
			anaLexica += "   Caractere Especial: '"+CaracteresEspeciais.get(idCaracEsp)+"'";
			break;
		case "<":
			anaLexica += "   Caractere Especial: '"+CaracteresEspeciais.get(idCaracEsp)+"'";
			break;
		case "==":
			anaLexica += "   Caractere Especial: '"+CaracteresEspeciais.get(idCaracEsp)+"'";
			break;
		case "{":
			anaLexica += "   Caractere Especial: '"+CaracteresEspeciais.get(idCaracEsp)+"'";
			break;
		case "}":
			anaLexica += "   Caractere Especial: '"+CaracteresEspeciais.get(idCaracEsp)+"'";
			break;
		case ">=":
			anaLexica += "   Caractere Especial: '"+CaracteresEspeciais.get(idCaracEsp)+"'";
			break;
		case "<=":
			anaLexica += "   Caractere Especial: '"+CaracteresEspeciais.get(idCaracEsp)+"'";
			break;
		}
	}

	private void temPaReservada(String linha, int idDoPaReservada) {
		switch(PalavrasReservadas.get(idDoPaReservada)){
		case "int ":
			Variavel v = new Variavel();
			v.setTipo("int ");
			anaLexica += "   definicao de tipo: 'int'";
			try{
				char[] vetorDeChar;
				linha = linha.substring(linha.indexOf("int ")+3);
				if(linha.contains(",") == false){
					if(linha.contains("=")){
						vetorDeChar = new char[linha.indexOf("=")-2];
						linha.getChars(1, linha.indexOf("=")-1, vetorDeChar, 0);
						v.setNome(String.valueOf(vetorDeChar));
						anaLexica += "   Nome da variavel: '"+v.getNome()+"'";
						linha = linha.substring(linha.indexOf("=")+1);
						vetorDeChar = new char[linha.indexOf(";")-1];
						linha.getChars(1, linha.indexOf(";"), vetorDeChar, 0);
						v.setConteudo(String.valueOf(vetorDeChar));
					}else{
						vetorDeChar = new char[linha.indexOf(";")-1];
						linha.getChars(1, linha.indexOf(";"), vetorDeChar, 0);
						v.setNome(String.valueOf(vetorDeChar));
						anaLexica += "   Nome da variavel: '"+v.getNome()+"'";
						
					}
					System.out.println();
					Variaveis.add(v);
				}
					System.out.println("nome: "+v.getNome()+"\n"+"conteudo: "+v.getConteudo()+"\n"+"tipo: "+v.getTipo());
			}catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case "if":
			anaLexica += "   Definicao de condicao: 'if'";
			break;
		case "for":
			anaLexica += "   Comando de loop: 'for'";
			break;
		case "while":
			anaLexica += "   Comando de loop: 'while'";
			break;	
		case "double":
			anaLexica += "   Definicao de tipo: 'double'";
			break;
			
		}
		
	}

	//##########################################################

	private void temComando(String linha, int idDoComando) {
		switch(Comandos.get(idDoComando)){
			case "printf":
				try{
					char[] vetorDeChar;
					String resultado;
					String nomeDaVariavel;
					anaLexica += "   Funcao de impressao: 'printf'";
					linha = linha.substring(linha.indexOf("printf")+6);
					vetorDeChar = new char[linha.lastIndexOf("\"")-2];
					linha.getChars(linha.indexOf("\"")+1, linha.lastIndexOf("\""), vetorDeChar, 0);
					resultado = String.valueOf(vetorDeChar);
					anaLexica += "   Token String \""+resultado+"\"";
					linha = linha.substring(linha.lastIndexOf("\"")+1);
					System.out.println(linha);
					if(linha.contains(",")){
						if(linha.substring(1).contains(","))
							vetorDeChar = new char[linha.indexOf(",")-1];
						else{
							vetorDeChar = new char[linha.indexOf(")")-2];
							linha.getChars(linha.indexOf(" ")+1, linha.indexOf(";")-1, vetorDeChar, 0);
							nomeDaVariavel = String.valueOf(vetorDeChar);
							anaLexica += "   Variavel: '"+nomeDaVariavel+"'";
							for (int i = 0; i < Variaveis.size(); i++) {
								if (Variaveis.get(i).getNome().equals(nomeDaVariavel)) {
									resultado = resultado.replace("%d", Variaveis.get(i).getConteudo());
								}
							}
						}
					}
					resultadoNoConsole += resultado+"\n";
					//Variaveis.add(new Integer(variavel.toString()));
					System.out.println("\n\n"+String.valueOf(vetorDeChar)+"\n"+linha);
				}catch (Exception e) {
					e.printStackTrace();
					resultadoNoConsole = "Falha na compilacao do codigo, verifique a sintaxe!";
				}
				break;
			case " ":
				
				break;
		}
		
	}

	//##########################################################
	//retorna o resultado que sera exibido no textAreaConsole
	public String getResultadoNoConsole() {
		return resultadoNoConsole;
	}
	
}
