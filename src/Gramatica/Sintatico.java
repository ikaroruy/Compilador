package Gramatica;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import javax.swing.JOptionPane;

public class Sintatico {

    //Arrays que serao preenchidos no decorrer do programa com as variaveis e funcoes do programa
    private ArrayList<Variavel> Variaveis = new ArrayList<Variavel>();
    private ArrayList<Funcao> Funcoes = new ArrayList<Funcao>();
    //Arrays de Tokens
    private ArrayList<String> Comandos = new ArrayList<String>();
    private ArrayList<String> PalavrasReservadas = new ArrayList<String>();
    private ArrayList<String> CaracteresEspeciais = new ArrayList<String>();
    private ArrayList<Character> Binario = new ArrayList<Character>();
    private ArrayList<String> StringsDeImpressao = new ArrayList<String>();
    private ArrayList<Character> Separadores = new ArrayList<Character>();
    //variaveis que sao usadas para interpretar o arquivo
    private String resultadoNoConsole = "";
    private FileReader arq;
    private BufferedReader lerArq;
    private String arquivo;
    private String anaSintatico = "";
    private boolean chavesAberta = false;
    private boolean parentesesAberto = false;
    private boolean temErro = false;
    private int contador;
    private boolean mainChave = false;

    public static enum TokenType {

        NUMERO("-?[0-9]+"), BINARIOOP("[*|/|-|+|=]"), PARENTESES("[(|)]"),
        ALFABETICO("[a-z|A-Z|a-z?0-9|A-Z?0-9]+");

        public final String padrao;

        private TokenType(String padrao) {
            this.padrao = padrao;
        }
    }

    public static class Token {

        public TokenType tipo;
        public String dado;

        public Token(TokenType type, String data) {
            this.tipo = type;
            this.dado = data;
        }

        @Override
        public String toString() {
            return String.format("(%s %s)", tipo.name(), dado);
        }
    }

    //Arraylist com todos os tokens do código
    public static ArrayList<Token> lex(String input) {

        ArrayList<Token> tokens = new ArrayList<Token>();

        StringBuffer tokenPatternsBuffer = new StringBuffer();
        for (TokenType tokenType : TokenType.values()) {
            tokenPatternsBuffer.append(String.format("|(?<%s>%s)", tokenType.name(), tokenType.padrao));
        }
        Pattern tokenPatterns = Pattern.compile(new String(tokenPatternsBuffer.substring(1)));

        Matcher matcher = tokenPatterns.matcher(input);
        while (matcher.find()) {
            if (matcher.group(TokenType.NUMERO.name()) != null) {
                tokens.add(new Token(TokenType.NUMERO, matcher.group(TokenType.NUMERO.name())));
                continue;
            } else if (matcher.group(TokenType.BINARIOOP.name()) != null) {
                tokens.add(new Token(TokenType.BINARIOOP, matcher.group(TokenType.BINARIOOP.name())));
                continue;
            } else if (matcher.group(TokenType.PARENTESES.name()) != null) {
                tokens.add(new Token(TokenType.PARENTESES, matcher.group(TokenType.PARENTESES.name())));
                continue;
            } else if (matcher.group(TokenType.ALFABETICO.name()) != null) {
                tokens.add(new Token(TokenType.ALFABETICO, matcher.group(TokenType.ALFABETICO.name())));
                continue;
            }
        }
//                System.out.println(tokens);
        return tokens;
    }

    //##########################################################
    public Sintatico(String arq) {
        this.arquivo = arq;
        adicionarNosArrays();
    }

    //##########################################################
    private void adicionarNosArrays() {

        Comandos.add("printf");
        Comandos.add("scanf");
        Comandos.add("main");
        Comandos.add("MULT(");
        Comandos.add("SOMA(");
        Comandos.add("SUB(");
        Comandos.add("DIV(");

        //adicionando palavras reservadas
        PalavrasReservadas.add("int");
        PalavrasReservadas.add("char");
        PalavrasReservadas.add("double");
        PalavrasReservadas.add("float ");
        PalavrasReservadas.add("if");
        PalavrasReservadas.add("for");
        PalavrasReservadas.add("while");
        PalavrasReservadas.add("switch");
        PalavrasReservadas.add("case");
        PalavrasReservadas.add("void");

        //adicionando strings de imprecao do printf
        StringsDeImpressao.add("%d");
        StringsDeImpressao.add("%f");
        StringsDeImpressao.add("%s");

        //adicionando caracteres especiais
        CaracteresEspeciais.add("(");
        CaracteresEspeciais.add(")");
        CaracteresEspeciais.add("&");
        CaracteresEspeciais.add("|");
        CaracteresEspeciais.add("{");
        CaracteresEspeciais.add("}");
        CaracteresEspeciais.add(";");
        CaracteresEspeciais.add("==");
        CaracteresEspeciais.add("+=");
        CaracteresEspeciais.add("!=");
        CaracteresEspeciais.add(">=");
        CaracteresEspeciais.add("<=");
        CaracteresEspeciais.add("<");
        CaracteresEspeciais.add(">");
        CaracteresEspeciais.add("++");
        CaracteresEspeciais.add("--");

        Binario.add('+');
        Binario.add('-');
        Binario.add('/');
        Binario.add('*');

        Separadores.add('\n');
        Separadores.add('\r');
        Separadores.add('\t');
        Separadores.add(' ');

    }

    private void ler() {
        try {
            arq = new FileReader(this.arquivo);
            lerArq = new BufferedReader(arq);
        } catch (Exception e) {
            resultadoNoConsole = "Erro de arquivo: " + e.getMessage() + ".\n";
        }
    }

    //##########################################################
    //verifica o contexto do codigo, fazendo a analise lexica
    public void VerificarContexto() {
        int numeroDaLinha = 1;
        System.out.println("\n\n\n");
        try {
            int temMain = 0;
            numeroDaLinha = 1;
            resultadoNoConsole = "================Saida:===============\n\n";
            anaSintatico += "\n\n\n";
            ler();

            String linha = lerArq.readLine(); // le a primeira linha

            while (linha != null) {
                ArrayList<Token> tokens = lex(linha);

                String[] Lin = new String[linha.length()];
                contador = 0;
                if (linha.contains("main")) {
                    temMain = 1;
                    if (linha.contains("{")) {
                        mainChave = true;
                    }
                }
                /*  +++  */
                if (mainChave == true) {
                    if (temMain == 1) {
                        for (int i = 0; i < Comandos.size(); i++) {
                            if (linha.contains(Comandos.get(i))) {
                                temComando(linha, i);
                                Lin[contador] = Comandos.get(i);
                                contador++;
                                break;
                            }
                        }
                        for (int i = 0; i < PalavrasReservadas.size(); i++) {
                            if (linha.contains(PalavrasReservadas.get(i))) {
                                Lin[contador] = PalavrasReservadas.get(i);
                                contador++;
                                break;
                            }
                        }
                        for (int i = 0; i < CaracteresEspeciais.size(); i++) {
                            if (linha.contains(CaracteresEspeciais.get(i))) {
                                temCaracEspecial(linha, i);
                                Lin[contador] = CaracteresEspeciais.get(i);
                                contador++;
                            }
                        }

                        boolean aux = false;
                        FOR:
                        for (int i = 0; i < PalavrasReservadas.size(); i++) {
                            for (Token token : tokens) {
                                if (token.tipo.toString().equals("ALFABETICO") & token.dado.toString().equals(PalavrasReservadas.get(i))) {
                                    //System.out.println(token);
                                    temPaReservada(linha, i);
                                    aux = true;
                                    break FOR;
                                }
                            }
                        }

                        if (aux == false) {
                            FOR:
                            for (int i = 0; i < Funcoes.size(); i++) {
                                for (Token token : tokens) {
                                    //System.out.println(token+"    "+Funcoes.get(i).getNome());
                                    if (token.tipo.toString().equals("ALFABETICO") & token.dado.toString().equals(Funcoes.get(i).getNome())
                                            & linha.contains(token.dado.toString() + "(")) {
                                        temFuncao(linha, tokens, Funcoes.get(i));

                                        for (Token to : tokens) {
//                                            System.out.print(to + "  ");
                                        }
                                        System.out.println();
                                        aux = true;
                                        break FOR;
                                    }
                                }
                            }
                        }

                        if (aux == false) {
                            FOR:
                            for (int i = 0; i < Binario.size(); i++) {
                                for (Token token : tokens) {
                                    if (token.tipo.toString().equals("BINARIOOP") & token.dado.toCharArray()[0] == Binario.get(i)) {
                                        temExprecoes(linha, numeroDaLinha);
                                        break FOR;
                                    }
                                }
                            }
                        }

                        if (parentesesAberto == true) {
                            anaSintatico += "Erro: parenteses nao foi fechado na linha " + numeroDaLinha + ".";
                            resultadoNoConsole = anaSintatico;
                            return;
                        }
                    }

                }
                if (linha.contains("};")) {
                    mainChave = false;
                }

                //essa parte sera a responsavel por interpretar as funcoes
                if (mainChave == false) {
                    for (int i = 0; i < tokens.size() - 1; i++) {
                        if (tokens.get(i).tipo.toString().equals("ALFABETICO") && tokens.get(i + 1).dado.equals("(")) {
                            //System.out.println("\n\n===============sss==============="+"\n"+tokens.get(i)+"\n"+tokens.get(i+1));
                            ArrayList<String> linhasDaFuncao = new ArrayList<String>();
                            while (linha != null) {
                                linhasDaFuncao.add(linha);
                                if (linha.contains("}")) {
                                    break;
                                }
                                numeroDaLinha++;
                                linha = lerArq.readLine();
                            }

                            processarFuncao(linhasDaFuncao);
                            break;
                        }
                    }

                }
                if (temErro == true) {
                    break;
                }
                numeroDaLinha++;
                linha = lerArq.readLine();

            }

            if (numeroDaLinha == -1) {
                anaSintatico += "Erro: chaves nao foi fechado";
                resultadoNoConsole = anaSintatico;
                return;
            } else if (temErro == true) {
                if (anaSintatico.contains("erro de variavel nao declarada") == false
                        && anaSintatico.contains("erro de variavel nao decladara!") == false
                        && anaSintatico.contains("Falha na compilacao do codigo, verifique a sintaxe!") == false
                        && anaSintatico.contains("Erro nos parenteses!") == false
                        && anaSintatico.contains("Erro na Funcao (argumentos nao correspondem)") == false) {
                    anaSintatico = "Erro na sintaxe!";
                }
                anaSintatico += "\n(Linha " + numeroDaLinha + ")\n";
                resultadoNoConsole = anaSintatico;
                return;
            }
            if (temMain == 0) {
                anaSintatico = "Erro: Main nao encontrada!";
                resultadoNoConsole = anaSintatico;
                return;
            }
            arq.close();
            resultadoNoConsole += anaSintatico;
        } catch (IOException e) {
            resultadoNoConsole = "Erro de arquivo: " + e.getMessage() + ".\n";
        }
        //System.out.println("=>=>=>"+resolverEquacaoInteira("a + be * b"));
        //System.out.println(Variaveis.get(Variaveis.size()-1).getNome());
    }

    private int resolverEquacaoInteira(String equacao) {
        int calculo = 0;
        ArrayList<Token> tokens = lex(equacao);
        try {
            for (Token token : tokens) {
                if (token.tipo.toString().equals("ALFABETICO")) {
                    for (Variavel variavel : Variaveis) {
                        if (variavel.getNome().equals(token.dado)) {
                            token.dado = variavel.getConteudo();
                            break;
                        }
                    }
                }
            }

            for (int i = 0; i < tokens.size(); i++) {
                if (i == 0) {
                    calculo = Integer.parseInt(tokens.get(0).dado);
                } else {
                    switch (tokens.get(i).dado) {
                        case "+":
                            calculo = calculo + Integer.parseInt(tokens.get(i + 1).dado);
                            break;
                        case "-":
                            calculo = calculo - Integer.parseInt(tokens.get(i + 1).dado);
                            break;
                        case "*":
                            calculo = calculo * Integer.parseInt(tokens.get(i + 1).dado);
                            break;
                        case "/":
                            calculo = calculo / Integer.parseInt(tokens.get(i + 1).dado);
                            break;
                    }
                    i++;
                }
            }
			System.out.println("=>=>"+calculo);
        } catch (Exception e) {
            //JOptionPane.showMessageDialog(null, e.getMessage());
            anaSintatico = "Erro na equacao!";
            temErro = true;
        }
        return calculo;
    }

    private void temFuncao(String linha, ArrayList<Token> tokensDaLinha, Funcao func) {
        boolean temF = false;
        for (Funcao funcao : Funcoes) {
            if (func.getCorpo().get(0).contains(funcao.getNome())) {
                temF = true;
                break;
            }

        }
        if (temF == true) {
            JOptionPane.showMessageDialog(null, " ");
        } else {
            Variavel v = new Variavel();
            if (linha.contains("=") && linha.contains(";")) {
                for (int i = 0; i < tokensDaLinha.size(); i++) {
                    if (tokensDaLinha.get(i).dado.equals("=")) {
                        for (int j = 0; j < Variaveis.size(); j++) {
                            if (Variaveis.get(j).getNome().equals(tokensDaLinha.get(i - 1).dado)) {
                                v = Variaveis.get(j);
//                                System.out.println("======>" + v.getNome());
                                break;
                            }
                        }
                        break;
                    }
                }
                if (linha.contains("()")) {
                    v.setConteudo(String.valueOf(resolverEquacaoInteira(func.getCorpo().get(0).substring(func.getCorpo().get(0).indexOf("return") + 6,
                            func.getCorpo().get(0).indexOf(";")))));
                } else {
                    v.setConteudo(resolverFuncaoComArgumento(func, tokensDaLinha));
                }
            } else {
                temErro = true;
            }
        }
    }

    private String resolverFuncaoComArgumento(Funcao func, ArrayList<Token> tokensDaLinha) {
        String resultado = "";
        String[] variaveis = new String[func.getArgumentos().size() / 2];
        for(int i = 0; i<variaveis.length; i++) {
            System.out.println(variaveis[i]);
            
        }
        String eq = "";
        Funcao f = new Funcao();
        f.setNome(func.getNome());
        f.setTipo(func.getTipo());
        f.setCorpo(func.getCorpo());
        f.setArgumentos(func.getArgumentos());

        for (int i = 0; i < tokensDaLinha.size(); i++) {
            if (tokensDaLinha.get(i).dado.equals("(")) {
                int j = 0;
                try {
                    i++;
                    while (tokensDaLinha.get(i).dado.equals(")") == false) {
                        System.out.println(tokensDaLinha.get(i).dado);
                        variaveis[j] = tokensDaLinha.get(i).dado;
                        i++;
                        j++;
                    }

                    eq = f.getCorpo().get(0);
                    System.out.println(eq);

                    for (int k = 0; k < variaveis.length;) {
                        ArrayList<Token> tk = lex(eq);
                        for (Token token : tk) {
                            if (token.dado.equals("int") == false && token.tipo.toString().equals("BINARIOOP") == false
                                    && token.dado.equals("return") == false && token.dado.equals(variaveis[k - k]) == false
                                    && token.tipo.toString().equals("NUMERO") == false) {
                                System.out.println(tk);
                                System.out.println(token.dado + "   " + ((Token) f.getArgumentos().get(k + k + 1)).dado);
                                if (token.dado.equals(((Token) f.getArgumentos().get(k + k + 1)).dado)) {
                                    eq = eq.replaceAll(token.dado, variaveis[k]);
                                }

                            }
                        }
                        k++;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    anaSintatico = "Erro na Funcao (argumentos nao correspondem)";
                    temErro = true;
                    return resultado;
                }
            }
        }
        System.out.println("" + eq);

        resultado = String.valueOf(resolverEquacaoInteira(eq.substring(eq.indexOf("return") + 7, eq.indexOf(";"))));
        return resultado;
    }

    //processar a funcao criada
    private void processarFuncao(ArrayList<String> linhasDaFuncao) {
        for (String string : linhasDaFuncao) {
            System.out.println("\n" + string);
//         resultadoNoConsole += "\n" + string;

        }

        Funcao func = new Funcao();

        for (int i = 0; i < linhasDaFuncao.size(); i++) {
            ArrayList<Token> tokens = lex(linhasDaFuncao.get(i));
            if (i == 0) {
                for (int j = 0; j < tokens.size(); j++) {
                    if (tokens.get(j).dado.equals("(")) {
                        func.setNome(tokens.get(j - 1).dado);
                        int nArgs = j + 1;
                        while (tokens.get(nArgs).dado.equals(")") == false) {
                            func.getArgumentos().add(tokens.get(nArgs));
                            nArgs++;
                        }
                    }
                }
            } else if (i == linhasDaFuncao.size()) {

            } else {
                func.getCorpo().add(linhasDaFuncao.get(i));
            }
        }
        Funcoes.add(func);

//        System.out.println(Funcoes.get(0).getCorpo().get(0));
    }

    private ArrayList<Token> RetornaTokensPorTipo(String tipoToken, String linha) {
        ArrayList<Token> list = new ArrayList<Token>();
        ArrayList<Token> tokens = lex(linha);
        for (Token token : tokens) {
            if (tipoToken.equals(token.tipo.toString())) {
                list.add(token);
            }
        }

        return list;
    }

    //##########################################################
    private void temExprecoes(String linha, int nLinha) {
        ArrayList<Token> tokens = lex(linha);
        int count = -1;
        try {
            if (tokens.get(0).dado.equals("printf") == false) {
                String expressao = linha.substring(linha.indexOf("=") + 1, linha.indexOf(";"));
                for (int i = 0; i < Variaveis.size(); i++) {
                    //System.out.println(Variaveis.get(i).getNome()+"   "+tokens.get(0).dado);
                    if (Variaveis.get(i).getNome().equals(tokens.get(0).dado)) {
                        count = i;
                        break;
                    }
                }
                Variaveis.get(count).setConteudo(String.valueOf(resolverEquacaoInteira(expressao)));
            }

        } catch (Exception e) {
            anaSintatico = "erro de variavel nao declarada ou nao inicializada! - Linha " + nLinha + "\n";
            temErro = true;
        }
    }

    private void temCaracEspecial(String linha, int idCaracEsp) {
        if (linha.indexOf(CaracteresEspeciais.get(idCaracEsp)) > linha.indexOf("\"")
                && linha.indexOf(CaracteresEspeciais.get(idCaracEsp))
                < linha.substring(linha.indexOf("\"") + 1).indexOf("\"") + linha.indexOf("\"")) {
            return;
        }

        switch (CaracteresEspeciais.get(idCaracEsp)) {

            case "{":
                chavesAberta = true;
                break;
            case "}":
                if (chavesAberta == true) {
                    chavesAberta = false;
                } else {
                    temErro = true;
                }
                break;

            case "(":
                parentesesAberto = true;
                break;
            case ")":
                if (parentesesAberto == true) {
                    parentesesAberto = false;
                } else {
                    anaSintatico = "Erro nos parenteses!";
                    temErro = true;
                }
                break;
        }
    }

    private void temPaReservada(String linha, int idDoPaReservada) {
        switch (PalavrasReservadas.get(idDoPaReservada)) {
            case "int":
                Variavel v = new Variavel();
                v.setTipo("int ");

                try {
                    char[] vetorDeChar;
                    linha = linha.substring(linha.indexOf("int ") + 3);
                    if (linha.contains(",") == false) {
                        if (linha.contains("=")) {
                            vetorDeChar = new char[linha.indexOf("=") - 2];
                            linha.getChars(1, linha.indexOf("=") - 1, vetorDeChar, 0);
                            v.setNome(String.valueOf(vetorDeChar));

                            linha = linha.substring(linha.indexOf("=") + 1);
                            vetorDeChar = new char[linha.indexOf(";") - 1];
                            linha.getChars(1, linha.indexOf(";"), vetorDeChar, 0);
                            v.setConteudo(String.valueOf(vetorDeChar));
                        } else {
                            vetorDeChar = new char[linha.indexOf(";") - 1];
                            linha.getChars(1, linha.indexOf(";"), vetorDeChar, 0);
                            v.setNome(String.valueOf(vetorDeChar));

                        }
                        Variaveis.add(v);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
//                System.out.println("nome: "+v.getNome()+"\n"+"conteudo: "+v.getConteudo()+"\n"+"tipo: "+v.getTipo());
                break;
            case "double":

                break;
            case "if":

                break;
            case "for":

                break;
            case "while":

                break;
        }

    }

    //##########################################################
    private void temComando(String linha, int idDoComando) {
        switch (Comandos.get(idDoComando)) {
            case "printf":
                try {
                    char[] vetorDeChar;
                    String resultado;
                    String nomeDaVariavel;

                    linha = linha.substring(linha.indexOf("printf") + 6);
                    vetorDeChar = new char[linha.lastIndexOf("\"") - 2];
                    linha.getChars(linha.indexOf("\"") + 1, linha.lastIndexOf("\""), vetorDeChar, 0);
                    resultado = String.valueOf(vetorDeChar);

                    linha = linha.substring(linha.lastIndexOf("\"") + 1);
                    //System.out.println(linha);
                    if (linha.contains(",")) {
                        if (linha.substring(1).contains(",")) {
                            vetorDeChar = new char[linha.indexOf(",") - 1];
                        } else {
                            vetorDeChar = new char[linha.indexOf(")") - 2];
                            linha.getChars(linha.indexOf(" ") + 1, linha.indexOf(";") - 1, vetorDeChar, 0);
                            nomeDaVariavel = String.valueOf(vetorDeChar);

                            for (int i = 0; i < Variaveis.size(); i++) {
                                if (Variaveis.get(i).getNome().equals(nomeDaVariavel)) {
                                    resultado = resultado.replace("%d", Variaveis.get(i).getConteudo());
                                }
                            }
                        }
                    }
                    resultadoNoConsole += resultado + "\n";
                    //Variaveis.add(new Integer(variavel.toString()));
                    //System.out.println("\n\n"+String.valueOf(vetorDeChar)+"\n"+linha);
                } catch (Exception e) {
                    e.printStackTrace();
                    anaSintatico = "Falha na compilacao do codigo, verifique a sintaxe!\n";
                    temErro = true;
                    return;
                }
                break;
            case "SOMA(":
            case "SUB(":
            case "MULT(":
            case "DIV(":
                ArrayList<Token> tokens = lex(linha);
                int count = -1;
                String n1 = "",
                 n2 = "";
                for (int i = 0; i < tokens.size(); i++) {
                    if (tokens.get(i).dado.equals("(")) {
                        n1 = tokens.get(i + 1).dado;
                        n2 = tokens.get(i + 2).dado;
                        break;
                    }
                }

                for (int i = 0; i < Variaveis.size(); i++) {
//                    System.out.println(Variaveis.get(i).getNome()+"   "+tokens.get(0).dado);
                    if (Variaveis.get(i).getNome().equals(tokens.get(0).dado)) {
                        count = i;
                        break;
                    }
                }
                if (Comandos.get(idDoComando).equals("SOMA(")) {
                    Variaveis.get(count).setConteudo(String.valueOf(resolverEquacaoInteira(n1 + " + " + n2)));
                } else if (Comandos.get(idDoComando).equals("SUB(")) {
                    Variaveis.get(count).setConteudo(String.valueOf(resolverEquacaoInteira(n1 + " - " + n2)));
                } else if (Comandos.get(idDoComando).equals("MULT(")) {
                    Variaveis.get(count).setConteudo(String.valueOf(resolverEquacaoInteira(n1 + " * " + n2)));
                } else if (Comandos.get(idDoComando).equals("DIV(")) {
                    Variaveis.get(count).setConteudo(String.valueOf(resolverEquacaoInteira(n1 + " / " + n2)));
                } else {
                    temErro = true;
                }
                break;
        }

    }

    //##########################################################
    //retorna o resultado que sera exibido no textAreaConsole
    public String getResultadoNoConsole() {
        return resultadoNoConsole;
    }

}
