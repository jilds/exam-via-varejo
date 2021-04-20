package com.jonathan.exam.model.enums;

public enum Message {

    // @formatter:off
	MSG_ERRO_OPERACAO("Ocorreu um erro na opera��o! Favor acionar suporte."),
    MSG_ERRO_SALVAR("Erro ao salvar registro!"),
    MSG_PLACA_REQUERIDA("Nenhuma placa foi informada, � obrigatorio a inser��o de uma placa."),
    MSG_ACESSO_NAO_AUTORIZADO("Acesso n�o autorizado."),
    MSG_NOT_FOUND("Nenhum registro encontrado."),
    MSG_ERRO_GENERICO("Erro ao realizar opera��o. Favor entrar em contato com o administrador do sistema.");

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