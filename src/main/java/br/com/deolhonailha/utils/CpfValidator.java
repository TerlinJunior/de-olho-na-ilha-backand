package br.com.deolhonailha.utils;

public class CpfValidator {
    public static boolean isValid(String cpf) {
        if (cpf == null) {
            return false;
        }
        // Remove caracteres não numéricos
        String cpfNumerico = cpf.replaceAll("[^0-9]", "");

        // Verifica se o CPF tem 11 dígitos
        if (cpfNumerico.length() != 11) {
            return false;
        }

        // Verifica se todos os dígitos são iguais (ex: 111.111.111-11), que são inválidos
        if (cpfNumerico.matches("(\\d)\\1{10}")) {
            return false;
        }

        // TODO: Implementar a lógica completa de validação do CPF
        // (cálculo do primeiro e do segundo dígito verificador).
        // Por enquanto, esta validação básica é suficiente para prosseguirmos.
        // Em um projeto real, você deve implementar o algoritmo completo aqui.

        return true; // Retorna true para permitir o desenvolvimento
    }
}