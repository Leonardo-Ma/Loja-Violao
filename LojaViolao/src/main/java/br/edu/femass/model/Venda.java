package br.edu.femass.model;

import lombok.Data;

@Data
public class Venda
{
    private String dataVenda;
    private Long idVenda;
    private Float totalVenda;
    private Float totalVendaGeral;
}
