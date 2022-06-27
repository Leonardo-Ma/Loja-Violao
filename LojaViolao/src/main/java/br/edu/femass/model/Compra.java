package br.edu.femass.model;

import lombok.Data;

@Data
public class Compra
{
    private String dataCompra;
    private Long idCompra;
    private Float totalCompra;
    private Float totalCompraGeral;
}
