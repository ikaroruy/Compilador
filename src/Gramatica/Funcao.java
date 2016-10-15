package Gramatica;

import java.util.ArrayList;

public class Funcao {
	private String nome;
	private ArrayList<Object> argumentos = new ArrayList<>();;
	private String tipo;
	private ArrayList<String> corpo = new ArrayList<>();
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public ArrayList<Object> getArgumentos() {
		return argumentos;
	}
	public void setArgumentos(ArrayList<Object> argumentos) {
		this.argumentos = argumentos;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public ArrayList<String> getCorpo() {
		return corpo;
	}
	public void setCorpo(ArrayList<String> corpo) {
		this.corpo = corpo;
	}
	
	
}
