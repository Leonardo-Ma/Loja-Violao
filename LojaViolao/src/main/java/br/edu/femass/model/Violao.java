package br.edu.femass.model;

import lombok.Data;

@Data
public class Violao
{
    private Long id;
    private String marca;
    private TipoCorda tipoCorda;
    private Integer estoque = 0;
    private Float precoVenda;

    public String toString()
    {
        return "Id: " + this.id + " Marca: " + this.marca + " Tipo de corda: " + this.tipoCorda + " Preço de venda: " + this.precoVenda + ".";
    }
}
