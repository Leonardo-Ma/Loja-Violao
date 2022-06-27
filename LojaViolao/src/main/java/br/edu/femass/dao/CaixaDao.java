package br.edu.femass.dao;

import br.edu.femass.model.Compra;
import br.edu.femass.model.TotalGeralCompraVenda;
import br.edu.femass.model.Venda;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CaixaDao extends DaoPostgres
{
    public List<Compra> listarCompra(Date dataEscolhida) throws Exception
    {
        String sql = "SELECT * FROM compra WHERE \"dataCompra\" = ?";
        PreparedStatement ps = getPreparedStatement(sql, false);
        ps.setDate(1, dataEscolhida);
        ResultSet rs = ps.executeQuery();

        Float totalCompra = 0f;
        TotalGeralCompraVenda totalGeralCompra = new TotalGeralCompraVenda();

        List<Compra> compras = new ArrayList<>();

        while (rs.next())
        {
            Compra compra = new Compra();
            compra.setIdCompra(rs.getLong("idCompra"));
            compra.setDataCompra(rs.getString("dataCompra"));
            compra.setTotalCompra(rs.getFloat("totalCompra"));
            totalCompra += compra.getTotalCompra();
            totalGeralCompra.totalCompraGeral = totalCompra;
            compras.add(compra);
        }
        return compras;
    }

    public List<Venda> listarVendas(Date dataEscolhida) throws Exception
    {
        String sql = "SELECT * FROM venda WHERE \"dataVenda\" = ?";
        PreparedStatement ps = getPreparedStatement(sql, false);
        ps.setDate(1, dataEscolhida);
        ResultSet rs = ps.executeQuery();

        Float totalVenda = 0f;
        TotalGeralCompraVenda totalGeralVenda = new TotalGeralCompraVenda();

        List<Venda> vendas = new ArrayList<>();

        while (rs.next())
        {
            Venda venda = new Venda();
            venda.setIdVenda(rs.getLong("idVenda"));
            venda.setDataVenda(rs.getString("dataVenda"));
            venda.setTotalVenda(rs.getFloat("totalVenda"));
            totalVenda += venda.getTotalVenda();
            totalGeralVenda.totalVendaGeral = totalVenda;
            vendas.add(venda);
        }

        return vendas;
    }
}