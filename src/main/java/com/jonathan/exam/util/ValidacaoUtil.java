package com.jonathan.exam.util;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.data.domain.Page;

@SuppressWarnings("rawtypes")
public class ValidacaoUtil {

    private static final int[] pesoCPF = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};

    private static final int[] pesoCNPJ = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

    /**
     * Utilizado para verificar se um a array é valido.
     */
    private ValidacaoUtil() {
    }

    /**
     * Utilizado para verificar se uma Page é valida.
     */
    public static boolean pageValida(Page page) {
        return (page != null && !page.isEmpty());
    }

    /**
     * Utilizado para verificar se um a lista é valida.
     */
    public static boolean listaValida(Collection lista) {
        return lista != null && !lista.isEmpty();
    }

    /**
     * Utilizado para verificar se um map é valido.
     */
    public static boolean mapValido(Map map) {
        return map != null && !map.isEmpty();
    }

    public static boolean arrayValido(Object[] array) {
        return (array != null && array.length > 0);
    }

    /**
     * Utilizado para verificar se um id String é numero.
     */
    public static boolean stringEhNumerico(String id) {
        return NumberUtils.isCreatable(id);
    }

    /**
     * Utilizado para verificar se um id Integer é validao.
     */
    public static boolean integerValido(Integer id) {
        return (id != null && id > 0);
    }

    /**
     * Utilizado para verificar se um id Integer é validao.
     */
    public static boolean longValido(Long id) {
        return (id != null && id > 0);
    }

    /**
     * Utilizado para verificar se um id Integer é valido.
     */
    public static boolean dominioIntegerValido(Integer id) {
        return (id != null && id != 0);
    }

    /**
     * Utilizado para verificar se o texto é válido.
     */
    public static boolean stringValido(String texto) {
        return (texto != null && !texto.trim().equals(""));
    }

    /**
     * Utilizado para verificar se as string passadas são iguais.
     */
    public static boolean stringIguais(String primeira, String segunda) {
        if (primeira != null && segunda != null)
            return primeira.equals(segunda);
        return false;
    }

    /**
     * Utilizado para verificar se é uma data válida
     */
    public static boolean dataValido(Date date) {
        return (date != null && date.getTime() > 0);
    }

    /**
     * Retira máscara de formatação da string e também retira caracteres fora do
     * intervalo 32-127
     *
     * @param str que terá a máscara retirada
     * @return String Sem máscara
     */
    public static String removerMascara(String str) {
        StringBuilder sb = new StringBuilder(str);
        String aux = "";
        String charMasc = "./\\-\",:(){}[]%'";
        int i;
        int indice;
        char c;
        aux = str;
        for (i = 0; i < charMasc.length(); i++) {
            c = charMasc.charAt(i);
            while ((indice = aux.indexOf(c)) != -1) {
                sb.deleteCharAt(indice);
                aux = sb.toString();
            }
        }
        return aux;
    }

    public static boolean validaTamanhoString(String str, int tamanhoMaxStr) {
        if (str != null && !str.equals("") && str.length() > 0)
            return str.length() <= tamanhoMaxStr;
        else
            return false;
    }

    public static boolean tipoArquivoValido(String str) {
        return (str.endsWith("doc") || str.endsWith("DOC") ||
                str.endsWith("docx") || str.endsWith("DOCX") ||
                str.endsWith("rtf") || str.endsWith("RTF") ||
                str.endsWith("txt") || str.endsWith("TXT") ||
                str.endsWith("xls") || str.endsWith("XLS") ||
                str.endsWith("xlsx") || str.endsWith("XLSX") ||
                str.endsWith("pdf") || str.endsWith("PDF") ||
                str.endsWith("odt") || str.endsWith("ODT") ||
                str.endsWith("ods") || str.endsWith("ODS") ||
                str.endsWith("jpg") || str.endsWith("JPG") ||
                str.endsWith("jpeg") || str.endsWith("JPEG") ||
                str.endsWith("bmp") || str.endsWith("BMP") ||
                str.endsWith("gif") || str.endsWith("GIF") ||
                str.endsWith("png") || str.endsWith("PNG") ||
                str.endsWith("rar") || str.endsWith("RAR") ||
                str.endsWith("zip") || str.endsWith("ZIP") ||
                str.endsWith("mpg") || str.endsWith("MPG") ||
                str.endsWith("mpeg") || str.endsWith("MPEG") ||
                str.endsWith("avi") || str.endsWith("AVI"));
    }

    public static boolean emailValido(String email) {
        Pattern p = Pattern.compile(".+@.+\\.[a-zA-Z]+");
        return p.matcher(email).matches();
    }

    public static boolean cepValido(String cep) {
        Pattern p = Pattern.compile("[0-9]{5}-[0-9]{3}");
        return p.matcher(cep).matches();
    }

    private static int calcularDigito(String str, int[] peso) {
        int soma = 0;
        int digito = 0;
        for (int indice = str.length() - 1; indice >= 0; indice--) {
            digito = Integer.parseInt(str.substring(indice, indice + 1));
            soma += digito * peso[peso.length - str.length() + indice];
        }
        soma = 11 - soma % 11;
        return soma > 9 ? 0 : soma;
    }

    private static String padLeft(String text, char character) {
        return String.format("%11s", text).replace(' ', character);
    }

    public static boolean cpfValido(String cpf) {
        String cpfSemMascara = cpf.trim().replace(".", "").replace("-", "");
        if ((cpfSemMascara == null) || (cpfSemMascara.length() != 11))
            return false;
        for (int j = 0; j < 10; j++) {
            if (padLeft(Integer.toString(j), Character.forDigit(j, 10)).equals(cpfSemMascara))
                return false;
        }
        Integer digito1 = calcularDigito(cpfSemMascara.substring(0, 9), pesoCPF);
        Integer digito2 = calcularDigito(cpfSemMascara.substring(0, 9) + digito1, pesoCPF);
        return cpfSemMascara.equals(cpfSemMascara.substring(0, 9) + digito1.toString() + digito2.toString());
    }

    public static boolean cnpjValido(String cnpj) {
        String cnpjSemMascara = cnpj.trim().replace(".", "").replace("-", "").replace("/", "");
        if ((cnpjSemMascara == null) || (cnpjSemMascara.length() != 14))
            return false;
        Integer digito1 = calcularDigito(cnpjSemMascara.substring(0, 12), pesoCNPJ);
        Integer digito2 = calcularDigito(cnpjSemMascara.substring(0, 12) + digito1, pesoCNPJ);
        return cnpjSemMascara.equals(cnpjSemMascara.substring(0, 12) + digito1.toString() + digito2.toString());
    }
}