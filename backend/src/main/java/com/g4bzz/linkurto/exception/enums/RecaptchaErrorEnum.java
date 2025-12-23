package com.g4bzz.linkurto.exception.enums;

import lombok.Getter;

public enum RecaptchaErrorEnum {
    MISSING_INPUT_SECRET("A secretKey está ausente."),
    INVALID_INPUT_SECRET("A secretKey é inválida ou está incorreta."),
    MISSING_INPUT_RESPONSE("O token do captcha está ausente."),
    INVALID_INPUT_RESPONSE("O token do captcha é inválido ou está incorreto."),
    BAD_REQUEST("Houve um problema na verificação do captcha."),
    TIMEOUT_OR_DUPLICATE("O token do captcha não é mais válido: ou é muito antigo ou já foi usado"),
    INCORRECT_CAPTCHA_SOL("A resposta do captcha está incorreta.");

    @Getter
    private final String message;

    RecaptchaErrorEnum(String message) {
        this.message = message;
    }
}
