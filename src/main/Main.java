package main;


import InterfaceGrafica.visao.Tela;

public class Main {

    public static void main(String[] args) {
        try {

            Tela frame = new Tela();
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
