package br.edu.femass.model;

import lombok.Getter;

@Getter
public enum TipoCorda
{
    Nylon("Nylon"), Aço("Aço");

    private String tipo;

    TipoCorda(String tipo)
    {
        this.tipo = tipo;
    }

    public String toString()
    {
        return tipo;
    }
}
