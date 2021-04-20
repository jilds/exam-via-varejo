package com.jonathan.exam.model.enums;

public enum Message {

    // @formatter:off
	MSG_ERRO_OPERACAO("Ocorreu um erro na operação! Favor acionar suporte."),
    MSG_ERRO_SALVAR("Erro ao salvar registro!"),
    MSG_PLACA_REQUERIDA("Nenhuma placa foi informada, é obrigatorio a inserção de uma placa."),
    MSG_ACESSO_NAO_AUTORIZADO("Acesso não autorizado."),
    MSG_NOT_FOUND("Nenhum registro encontrado."),
    MSG_ERRO_GENERICO("Erro ao realizar operação. Favor entrar em contato com o administrador do sistema.");

    // @formatter:on

    private String description;

    Message(String description) {
        this.description = description;
    }

    public String getDescription() {

        return this.description;
    }

    @Override
    public String toString() {
        return this.description;
    }

}