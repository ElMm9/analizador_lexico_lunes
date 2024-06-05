package com.example.analizador_lexico;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


//Programar la comprovacion de estructura de swich
//Programar la comprovacion de estructura de swich
public class HelloController implements Initializable {
    @FXML
    private  TextArea txtarea;
    @FXML
    private TableView<etiqueta> tblview;

    @FXML
    private TableColumn<etiqueta,String> clbcaracter;

    @FXML
    private TableColumn<etiqueta,String> cldcateg;
    @FXML
    private TableColumn<etiqueta,String> columna;
    @FXML
    private TableColumn<etiqueta,String> fila;
    @FXML
    private ListView<String> list;


    private ObservableList<etiqueta> guarda= FXCollections.observableArrayList();
    private List<String> clases= new ArrayList<>();
    private List<String> funcion= new ArrayList<>();
    private List<String> errores= new ArrayList<>();
    private List<String> caracteres= new ArrayList<>();
    private List<String> categoria= new ArrayList<>();

    private List<String> l_columna= new ArrayList<>();

    private List<String> l_fila= new ArrayList<>();

    private int indice=0;


    private boolean bandera_c = false;
    private String palabra_cadena="";

    private List<String> Palabra =  List.of("byte",
            "short", "int", "long",
            "char", "float",
            "double","String", "boolean",
            "void", "if",
            "else", "switch",
            "case", "default",
            "while", "do",
            "for", "break",
            "continue", "try",
            "catch", "finally",
            "throw", "throws",
            "private", "protected",
            "public", "class",
            "interface", "enum",
            "import", "package",
            "extends", "implements",
            "static", "final",
            "abstract", "default",
            "new", "instanceof",
            "this", "super",
            "return", "var",
            "synchronized", "volatile",
            "native", "transient",
            "assert", "strictfp",
            "const", "goto",
            "true", "false"
            ,"null","println"
            ,"out","System");

    private List<String> simbolos =List.of(
            "+","-","*","/","%","|","?","!", ">","<","=","&","#","(",")","¿",
            "@",";",".","¡","!","{",",",":","-","_","}","[","]","\"","\"\\\\(\\\"\"");

    public void bttnanaliza(ActionEvent event) {
        guarda.clear();
        tblview.setItems(guarda);
        errores.clear();
        list.getItems().clear();
        indice=0;
        String texto = txtarea.getText();

        String[] partes = texto.split("\n");
        guarda.clear();
        int i=0;
        for(String a: partes){
            divide(a, i);
            if(bandera_c){
               errores.add("Error en cerrado de comillas");
            }
            i++;
        }
        tblview.setItems(guarda);
        estructura();
        // Obtener los valores de las columnas y guardarlos en listas para facil acceso`------Nuevo
        ObservableList<etiqueta> items = tblview.getItems();
        caracteres.clear();  // Limpiar la lista antes de agregar
        categoria.clear();
        l_fila.clear();
        l_columna.clear();

        for (etiqueta item : items) {
            caracteres.add(clbcaracter.getCellData(item));  // Obtener el valor de la columna
            categoria.add(cldcateg.getCellData(item));  // Obtener el valor de la columna
            Object valorFila = fila.getCellData(item);
            String valorFilaComoTexto = (valorFila != null) ? valorFila.toString() : ""; // Conversión segura a String

            // Agregar el valor convertido a la lista 'l_fila'
            l_fila.add(valorFilaComoTexto);
            //l_columna.add(columna.getCellData(item));  // Obtener el valor de la columna

            Object valorFila2 = columna.getCellData(item);
            String valorFilaComoTexto2 = (valorFila2 != null) ? valorFila.toString() : ""; // Conversión segura a String

            // Agregar el valor convertido a la lista l_columna
            l_columna.add(valorFilaComoTexto2);
            //l_columna.add(columna.getCellData(item));  // Obtener el valor de la columna
        }


        //System.out.println(categoria.get(2));
        //pasar a la funcion para analizar las clases y funciones.

        //LLAMAR LA FUNCION PARA VERIFICAR LAS ESTRUCTURAS, CLASES Y FUNCIONES:
      //  estructura_anl();


        //errores
        list.getItems().addAll(errores);
    }


    public void estructura(/*List<etiqueta> guarda*/) {
        int i = 0;
        int j = 0;
        int k = 0;
        int l = 0;
        int a = 0;
        int b = 0;
        int c=0;
        for (etiqueta etiqueta : guarda) {
            switch (etiqueta.getString()) {
                case "{":
                    i++;
                    break;
                case "}":
                    j++;
                    break;
                case "(":
                    k++;
                    break;
                case ")":
                    l++;
                    break;
                case "[":
                    a++;
                    break;
                case "]":
                    b++;
                    break;
                case "for": if(reviewFor(guarda, c)){
                    errores.add("For mal declarado");
                }
                    break;
            }
            c++;
        }
        if (i < j) {
            errores.add("Error en abertura de llaves");
        } else if (i > j) {
            errores.add("Error en cerrado de llaves");
        }
        if (k < l) {
            errores.add("Error en abertura de parentesis");
        } else if (k > l) {
            errores.add("Error en cerrado de parentesis");
        }
        if (a < b) {
            errores.add("Error en abertura de corchetes");
        } else if (b > a) {
            errores.add("Error en cerrado de corchetes");
        }
    }
  /*  public void estructura_anl() {//------------------------- N
        //RECORRER EN LA LISTA DE CATEGORIAS PARA ENCONTRAR UN CLASS CLASE
        while (indice < categoria.toArray().length) {
            System.out.println("inicio");
            //IF PARA DETECTAR EN CATEGORIAS LA CLASE
            if (categoria.get(indice).equals("clase")) {
                lex_class();
                //pasamos a la funcion de variables de clase para ver si hay variables y en caso de que si comprobar que esten bien escritas
                lex_var_class();
            }
            if (categoria.get(indice).equals("funcion")){
                lex_funcion();
            }
            //AUMENTAR EL INDICE
            indice++;
        }
    }

    public void lex_class(){//---------------------N
        //verificar que se declaro con public o private
        try {
            if (!caracteres.get(indice - 2).equals("public") && !caracteres.get(indice - 2).equals("private")) {
                errores.add("Error en la declaración de la clase - No se encontró 'public' o 'private'. Columna: " + l_columna.get(indice) + " Fila: " + l_fila.get(indice));
            }

            if (!caracteres.get(indice-1).equals("class")){
                errores.add("error en la declaracion de la clase - funcion no se encontro class columna: "+l_columna.get(indice)+" fila: "+ l_fila.get(indice));

            }
        } catch (Exception e) { // catch por si la clase esta en el indice 0
            errores.add("error en la declaracion de la clase - funcion columna: "+l_columna.get(indice)+" fila: "+ l_fila.get(indice));
        }
        indice++;
        if (!caracteres.get(indice).equals("{")) {
            errores.add("Error en la declaracion de la clase - funcion columna: "+l_columna.get(indice)+" fila: "+ l_fila.get(indice));

        }
        indice++; //AVANZAR UNA POSICION EN LA LISTA
    }
    public void lex_var_class(){//---------------N
        String tipo_var;
        //BUCLE PARA LAS VARIABLES QUE PUEDEN ESTAR DECLARADAS, SALDRA CUANDDO SE TOPE CON UNA FUNCION
        while(indice<categoria.toArray().length){
            System.out.println("var");

            //si encuentra funcion se sale
            if (categoria.get(indice).equals("funcion")){
                break;
            }
            if (categoria.get(indice).equals("clase")){
                lex_class();
                break;
            }
            //si encuentra una clase checa que tenga el class y public o private para ver si es otra clase
            //if para deteccion de variable
            if (categoria.get(indice).equals("variable")){
                //verificamos que fue declarado, para ello veremos si en la posicion anterior del indice hay un tipo de variable
                tipo_var=caracteres.get(indice-1);

                if (
                        tipo_var.equals("int") ||
                                tipo_var.equals("double") ||
                                tipo_var.equals("float") ||
                                tipo_var.equals("String") ||
                                tipo_var.equals("boolean")
                ) {
                    System.out.println("tipo de variable"+tipo_var);
                    //si lo anterior fue correcto ahora retrocedemos 2 y vemos si cumple con public o private
                    if (caracteres.get(indice-2).equals("public")||caracteres.get(indice-2).equals("private")){
                        //pasamos ahora a ver si despues de la deteccion de variable tiene un = y si es asi que lo siguiente sea correcto
                        indice ++;
                        System.out.println("caracter"+caracteres.get(indice));
                        if (caracteres.get(indice).equals("=")){
                            // ahora viendo que hay un = toca verificar que tiene un valor.
                            indice ++; //avanzamos una posicion para ver di hay valor
                            if (tipo_var.equals("String")){//en caso de que sea String debe de cumplir con que lo que siga sea cadena.
                                indice++;
                                if (!categoria.get(indice).equals("cadena")){//si lo que sigue del igual no es una cadena para este caso es un error
                                    errores.add("error en la declaracion de una variable tipo de dato string: columna: "+l_columna.get(indice-1)+" fila: "+l_fila.get(indice-1));
                                }
                                indice++;

                            } else if (tipo_var.equals("boolean")) {
                                if (!caracteres.get(indice).equals("true")||!caracteres.get(indice).equals("false")){
                                    errores.add("error en la declaracion de una variable boolean: columna: "+l_columna.get(indice-1)+" fila: "+l_fila.get(indice-1));
                                }

                            }else {
                                if (!categoria.get(indice).equals("numero")){
                                    errores.add("error en la declaracion de una variable numerica: columna: "+l_columna.get(indice-1)+" fila: "+l_fila.get(indice-1));
                                }
                            }
                            indice++;// aumentamos en 1 el indice para ver si despues hay un ;
                            //una vez verificado el que tiene un valor, verificar que termine con punto y coma y listo.
                            if (!caracteres.get(indice).equals(";")){
                                //error porque el if es cuando no hay un ; despues
                                errores.add("error en la declaracion de una variable falta ; "+" fila: "+l_fila.get(indice-1));

                            }

                        } else if (!caracteres.get(indice).equals(";")) { //Este else if es cuando no tenga valor inicial y no cumpla con el ; debera de salir error
                            errores.add("Error en la declaracion de la variable falta ; columna: "+" fila: "+ l_fila.get(indice-1));
                        }

                    }
                    else {
                        //ERROR CUANDO NO DETENCTO PUBLIC O PRIVATE
                        errores.add("Error en la declaracion de la variable no se encontro public o private"+" fila: "+ l_fila.get(indice));

                    }
                }else {
                    //ERROR CUANDO NO SE DETECTO TIPO DE DATO
                    errores.add("Error en la declaracion de variables "+" fila: "+ l_fila.get(indice));
                }

            }
            indice ++;//aumentamos en 1 el indice para ver que hay en la siguiente posicion, mas variables, funciones o clases

        }
    }
    public void lex_funcion(){//----------------N
        //Bucle para todas las funciones que pueden a ver dentro de la clase
        while(indice < categoria.toArray().length){
            System.out.println("funcion");
            if (categoria.get(indice).equals("funcion")) {
                //verificar que una posicion atras tenga el tipo de funcion
                if (
                        caracteres.get(indice - 1).equals("void") ||
                                caracteres.get(indice - 1).equals("double") ||
                                caracteres.get(indice - 1).equals("int") ||
                                caracteres.get(indice - 1).equals("String") ||
                                caracteres.get(indice - 1).equals("boolean")
                ) {
                    //Ahora verificar que 2 posiciones atras tenga el public o private
                    if (caracteres.get(indice - 2).equals("public") || caracteres.get(indice - 2).equals("private")) {
                        //pasar ahora a verificar que continue con (
                        indice++;
                        if (caracteres.get(indice).equals("(")) {
                            //ahora pasamos a verificar todas las variables o parametros que maneja adentro.
                            //bucle para los parametros:
                            while (indice < categoria.toArray().length) {
                                //sale cuando detecta un ')'
                                indice++;
                                if (caracteres.get(indice).equals(")")) {
                                    break;
                                }
                                //verificar parametros:
                                if (categoria.get(indice).equals("variable")) {
                                    //verificar una posicion atras para ver si tiene el tipo de variable:
                                    if (
                                            caracteres.get(indice - 1).equals("float") ||
                                                    caracteres.get(indice - 1).equals("double") ||
                                                    caracteres.get(indice - 1).equals("int") ||
                                                    caracteres.get(indice - 1).equals("String") ||
                                                    caracteres.get(indice - 1).equals("boolean")
                                    ) {
                                        //ahora pasamos a ver que tengan la coma de separacion y si no que tenga el ) y salimos
                                        indice++;
                                        if (caracteres.get(indice).equals(")")) {
                                            //terminamos ciclo porque ya no hay mas variables.
                                            break;
                                        } else if (!caracteres.get(indice).equals(",")) {
                                            errores.add("Error en la declaracion de los parametros de la funcion " + " fila: " + l_fila.get(indice));
                                        }

                                    } else {
                                        //ERROR CUANDO DETECTA UNA VARIABLE SIN INDICAR EL TIPO DE DATO
                                        errores.add("Error en la declaracion de los parametros " + " fila: " + l_fila.get(indice));
                                    }
                                }
                            }

                        } else {
                            //ERROR de que no tiene ( la funcion
                            errores.add("Error en la declaracion de la funcion se esperaba una '('" + " fila: " + l_fila.get(indice));
                        }
                    } else {
                        //ERROR CUANDO NO se detecto el public o private
                        errores.add("Error en la declaracion de la funcion se esperaba un public o private " + " fila: " + l_fila.get(indice));
                    }
                } else {
                    //ERROR CUANDO NO se detecto el tipo de funcion
                    errores.add("Error en la declaracion del tipo de funcion " + " fila: " + l_fila.get(indice));
                }

                indice++;

            }
            if (caracteres.equals("}")){//deteccion del fin de la funcion.
                break;
            }
            indice++;


        }


    }
*/
    public void divide(String texto, int i){
       String[] partes = texto.split("\\s+|(?<=[a-zA-ZáéíóúÁÉÍÓÚ])(?=[^a-zA-ZáéíóúÁÉÍÓÚ\\d])|(?<=[^a-zA-ZáéíóúÁÉÍÓÚ\\d])(?=[a-zA-ZáéíóúÁÉÍÓÚ])|(?<=[^\\d])(?=\\d)|(?<=\\d)(?=[^\\d])|(?<=[^()\\wáéíóúÁÉÍÓÚ])(?=[()])|(?<=[()])|\\n|(?<=[^\\wáéíóúÁÉÍÓÚ])(?<=[^\\wáéíóúÁÉÍÓÚ])");
       int l=0;

        for (String parte : partes) {
            l = texto.indexOf(parte, l);
            analizar(parte, i, l);
            l+= parte.length();
        }
    }

    public void analizar(String palabra, int i, int l) {
        if (!palabra.isEmpty()) {
            if (palabra.equals("\"")) {
                if (!bandera_c) {
                    bandera_c = true;
                } else {
                    bandera_c = false;
                    guarda.add(new etiqueta(palabra_cadena, "cadena", i, l));
                    palabra_cadena = "";
                }
                if (simbolos.contains(palabra)) {
                    guarda.add(new etiqueta(palabra, "simbolo", i, l));
                }
            } else {
                try {
                    if (bandera_c) {
                        palabra_cadena += palabra + " ";
                    } else {
                        palabra = palabra.trim();
                        if (Palabra.contains(palabra)) {
                            guarda.add(new etiqueta(palabra, "palabra reservada", i, l));
                        } else if (palabra.matches("\\d+")) {
                            guarda.add(new etiqueta(palabra, "numero", i, l));
                        } else if (simbolos.contains(palabra)) {
                            guarda.add(new etiqueta(palabra, "simbolo", i, l));

                        } else if (guarda.get(guarda.size() - 1).getString().equals("class")) {
                            guarda.add(new etiqueta(palabra, "clase", i, l));
                            clases.add(palabra);
                        } else {

                            if (clases.contains(palabra)) {
                                guarda.add(new etiqueta(palabra, "clase", i, l));
                            } else if (funcion.contains(palabra)) {
                                guarda.add(new etiqueta(palabra, "funcion", i, l));
                            } else {
                                guarda.add(new etiqueta(palabra, "variable", i, l));
                            }
                        }
                        if (palabra.equals("(")) {
                            if (guarda.size() > 1 && Palabra.contains(guarda.get(guarda.size() - 3).getString())) {
                                guarda.get(guarda.size() - 2).setEtiqueta("funcion");
                                funcion.add(guarda.get(guarda.size() - 2).string);
                            }
                        }
                    }
                } catch (Exception e) {
                    errores.add("error en la declaracion de clase - funciones fila 0");
                }
            }
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clbcaracter.setCellValueFactory(new PropertyValueFactory<>("string"));
        cldcateg.setCellValueFactory(new PropertyValueFactory<>("etiqueta"));
        columna.setCellValueFactory(new PropertyValueFactory<>("fila"));
        fila.setCellValueFactory(new PropertyValueFactory<>("colmna"));

        //estilo del list
        // Personaliza la celda del ListView de la lista de los reportes...
        list.setCellFactory(param -> new TextFieldListCell<String>(){
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);

                    // Aplica el color de fondo según el estado
                    if (item.contains("error")) {
                        setStyle("-fx-background-color: #CD6155;-fx-border-width: 0.3px; -fx-border-color: #1B2631;-fx-text-fill: white;");
                    }
                    else {
                        setStyle("-fx-background-color: #CD6155;-fx-border-width: 0.3px; -fx-border-color: #1B2631;-fx-text-fill: white;");  // Restablece el estilo si no es "completado" ni "pendiente"
                    }
                }
            }
        });
    }


    //-------------------       Funciones añadidas
    public boolean reviewFor(List<etiqueta> lista_expresiones, int i) {
        String texto_error = "";
        int estado = 0; // Auxiliar numérico para el análisis del ciclo

        /* Recorrido de la lista de expresiones */
        for (int j = i; j < lista_expresiones.size(); j++) {
            /* Obtención de la expresión y su tipo (Reservada, Símbolo, Variable, etc.)*/
            String expresion = lista_expresiones.get(j).getString();

            String tipo_expresion = lista_expresiones.get(j).getEtiqueta();

            if(lista_expresiones.get(j+1).getString().equals("=")&&tipo_expresion.equals("simbolo")){
                expresion+="=";
                j++;
            }
            if ((lista_expresiones.get(j+1).getString().equals("+") || lista_expresiones.get(j+1).getString().equals("-"))
                    && expresion.equals(lista_expresiones.get(j+1).getString())) {
                expresion += lista_expresiones.get(j+1).getString();
                j++;
            }

            /* ANÁLISIS SINTÁCTICO DEL CICLO FOR */
            switch (estado) {
                case 0: {
                    // Revisión de la palabra reservada "for"
                    if (expresion.equals("for")) {
                        estado = 1;
                    } else {
                        estado = -1;
                    }
                    break;
                }
                case 1: {
                    // Revisión del paréntesis de apertura después de la palabra "for"
                    if (expresion.equals("(")) {
                        estado = 2;
                    } else {
                        estado = -1;
                        texto_error = "Se esperaba un paréntesis de apertura '('.";
                        errores.add(texto_error);
                    }
                    break;
                }
                /* 1. SENTENCIA DE DECLARACIÓN */
                case 2: {
                    // Revisión del tipo de variable a declarar (int, float o double)
                    if (expresion.matches("^(int|float|double|char)")) {
                        estado = 3;
                    } else {
                        if (tipo_expresion.equals("variable")) {
                            estado = 4;
                        } else {
                            estado = -1;
                            texto_error = "Tipo de dato " + expresion + " no válido en la declaración.";
                            errores.add(texto_error);
                        }
                    }
                    break;
                }
                case 3: {
                    // Revisión del nombre de la variable a declarar
                    if (tipo_expresion.equals("variable")) {
                        estado = 4;
                    } else {
                        estado = -1;
                        texto_error = "Se esperaba nombre de variable.";
                        errores.add(texto_error);
                    }
                    break;
                }
                case 4: {
                    // Revisión del operador de asignación '='
                    if (expresion.equals("=")) {
                        estado = 5;
                    } else {
                        estado = -1;
                        texto_error = "Se esperaba operador de asignación '='.";
                        errores.add(texto_error);
                    }
                    break;
                }
                case 5: {
                    // Revisión de la expresión después del operador de asignación (Valor numérico o variable)
                    if (tipo_expresion.equals("numero") || tipo_expresion.equals("variable")) {
                        estado = 6;
                    } else {
                        estado = -1;
                        texto_error = "Se esperaba una expresión válida.";
                        errores.add(texto_error);
                    }
                    break;
                }
                case 6: {
                    // Revisión del punto y coma para terminar la sentencia de declaración
                    if (expresion.equals(";")) {
                        estado = 7;
                    } else {
                        estado = -1;
                        texto_error = "Se esperaba ';'";
                        errores.add(texto_error);
                    }
                    break;
                }

                /* 2. SENTENCIA DE CONDICIÓN */
                case 7: {
                    // Revisión de la primera expresión en la condición de tope (Valor o variable)
                    if (tipo_expresion.equals("numero") || tipo_expresion.equals("variable")) {
                        estado = 8;
                    } else {
                        estado = -1;
                        texto_error = "Se esperaba una expresión válida.";
                        errores.add(texto_error);
                    }
                    break;
                }
                case 8: {
                    // Revisión del operador relacional
                    if (expresion.matches("(!=|==|<|<=|>|>=)")) {
                        estado = 9;
                    } else {
                        estado = -1;
                        texto_error = "Se esperaba un operador de comparación válido.";
                        errores.add(texto_error);
                    }
                    break;
                }
                case 9: {
                    // Revisión de la segunda expresión en la condición de tope (Valor o variable)
                    if (tipo_expresion.equals("numero") || tipo_expresion.equals("variable")) {
                        estado = 10;
                    } else {
                        System.out.println(tipo_expresion);
                        estado = -1;
                        texto_error = "Se esperaba una expresión válida.";
                        errores.add(texto_error);
                    }
                    break;
                }
                case 10: {
                    // Revisión del punto y coma para terminar la sentencia de condición
                    if (expresion.equals(";")) {
                        estado = 11;
                    } else {
                        estado = -1;
                        texto_error = "Se esperaba ';'.";
                        errores.add(texto_error);
                    }
                    break;
                }

                /* 3. SENTENCIA DE SALTO */
                case 11: {
                    // Revisión del nombre de la variable de salto
                    if (tipo_expresion.equals("variable")) {
                        estado = 12;
                    } else {
                        estado = -1;
                        texto_error = "Se esperaba nombre de variable.";
                        errores.add(texto_error);
                    }
                    break;
                }
                case 12: {
                    /* OPERADORES VÁLIDOS DESPUÉS DE VARIABLE DE SALTO */
                    // Revisión de símbolos ++ o --
                    if (expresion.matches("(\\+\\+|--)")) {
                        estado = 13;
                    }
                    // Revisión de operador de asignación '='
                    else {
                        if (expresion.matches("=")) {
                            estado = 14;
                        }
                        // Revisión de otros operadores de asignación válidos
                        else {
                            if (expresion.matches("(\\+=|-=|\\*=|/=)")) {
                                estado = 16;
                            } else {
                                estado = -1;
                                texto_error = "Se esperaba un operador válido.";
                                errores.add(texto_error);
                            }
                        }
                    }
                    break;
                }
                case 13: {
                    // Revisión de paréntesis de cierre ')'
                    if (expresion.equals(")")) {
                        estado = 17;
                    } else {
                        estado = -1;
                        texto_error = "Se esperaba cierre de paréntesis ')'.";
                        errores.add(texto_error);
                    }
                    break;
                }
                case 14: {
                    // Revisión de expresión después del operador de asignación '=' (Variable o valor numérico)
                    if (tipo_expresion.matches("variable") | tipo_expresion.equals("numero")) {
                        estado = 15;
                    } else {
                        estado = -1;
                        texto_error = "Se esperaba nombre de variable.";
                        errores.add(texto_error);
                    }
                    break;
                }
                case 15: {
                    // Revisión de operador aritmético después de la expresión
                    if (expresion.matches("(\\+|-|\\*|/)")) {
                        estado = 16;
                    } else {
                        estado = -1;
                        texto_error = "Se esperaba un operador válido.";
                        errores.add(texto_error);
                    }
                    break;
                }
                case 16: {
                    // Revisión de expresión después del operador aritmético
                    if (tipo_expresion.equals("variable") | tipo_expresion.equals("numero")) {
                        estado = 13;
                    } else {
                        estado = -1;
                        texto_error = "Se esperaba una expresión válida.";
                        errores.add(texto_error);
                    }
                    break;
                }
                case 17: {
                    // Estado final correcto
                    System.out.println("El ciclo For está bien.");
                    //int saltos_palabra = j;
                    return false;
                }
                case -1: {
                    // Caso de error (Generación de mensaje de error)
                    String numLinea = String.valueOf(lista_expresiones.get(j - 1).getFila());
                    String numColumna = String.valueOf(lista_expresiones.get(j - 1).getColmna());

                    texto_error = "Línea " + numLinea + ", Columna " + numColumna + "\n" + texto_error;
                    errores.add(texto_error);
                    System.out.println("ERROR.\nExiste un error en la declaración del ciclo.");
                   // saltos_palabra = j;
                    return true;
                }
            }
        }
        // Analiza si el ciclo for se declaró en su totalidad
        if (estado != 17) {
            texto_error = "La declaración del ciclo For está incompleta";
            return true;
        }
        return false;
    }
   /* private void validarExpresiones(String linea, int numeroLinea, StringBuilder output) {
        String[] operadoresAritmeticos = {"+", "-", "*", "/", "%"};
        String[] operadoresLogicos = {"&&", "||", "!", "<", ">", "<=", ">=", "==", "!="};

        // Validar operaciones aritméticas
        for (String operador : operadoresAritmeticos) {
            if (linea.contains(operador)) {
                String[] tokens = linea.split("\\b" + Pattern.quote(operador) + "\\b");
                if (tokens.length > 1) {
                    for (int i = 0; i < tokens.length - 1; i++) {
                        output.append("Tipo: ").append("OPERACION_ARITMETICA | Valor: ").append(tokens[i].trim()).append(" | Línea: ").append(numeroLinea).append("\n");
                        output.append("Tipo: ").append("OPERADOR_ARITMETICO | Valor: ").append(operador).append(" | Línea: ").append(numeroLinea).append("\n");
                    }
                    output.append("Tipo: ").append("OPERACION_ARITMETICA | Valor: ").append(tokens[tokens.length - 1].trim()).append(" | Línea: ").append(numeroLinea).append("\n");
                }
            }
        }
        // Validar operaciones lógicas
        for (String operador : operadoresLogicos) {
            if (linea.contains(operador)) {
                String[] tokens = linea.split("\\b" + Pattern.quote(operador) + "\\b");
                if (tokens.length > 1) {
                    for (int i = 0; i < tokens.length - 1; i++) {
                        output.append("Tipo: ").append("OPERACION_LOGICA | Valor: ").append(tokens[i].trim()).append(" | Línea: ").append(numeroLinea).append("\n");
                        output.append("Tipo: ").append("OPERADOR_LOGICO | Valor: ").append(operador).append(" | Línea: ").append(numeroLinea).append("\n");
                    }
                    output.append("Tipo: ").append("OPERACION_LOGICA | Valor: ").append(tokens[tokens.length - 1].trim()).append(" | Línea: ").append(numeroLinea).append("\n");
                }
            }
        }
    }

    */

}