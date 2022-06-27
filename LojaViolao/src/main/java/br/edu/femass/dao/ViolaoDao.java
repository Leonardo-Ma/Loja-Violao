package br.edu.femass.dao;

import br.edu.femass.model.*;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ViolaoDao extends DaoPostgres implements Dao<Violao>

{
    @Override
    public List<Violao> listar() throws Exception
    {
        String sql = "SELECT * FROM violao ORDER BY marca ASC";
        PreparedStatement ps = getPreparedStatement(sql, false);
        ResultSet rs = ps.executeQuery();

        List<Violao> violoes = new ArrayList<>();

        while (rs.next())
        {
            Violao violao = new Violao();
            violao.setMarca(rs.getString("marca"));
            violao.setTipoCorda(TipoCorda.valueOf(rs.getString("tipoCorda")));
            violao.setPrecoVenda(rs.getFloat("precoVenda"));
            violao.setId(rs.getLong("id"));
            violao.setEstoque(rs.getInt("estoque"));
            violoes.add(violao);
        }

        return violoes;
    }

    @Override
    public void gravar(Violao value) throws Exception
    {
        String sql = "INSERT INTO violao (marca, \"tipoCorda\", \"precoVenda\") VALUES (?, ?, ?)";
        PreparedStatement ps = getPreparedStatement(sql, true);
        ps.setString(1, value.getMarca());
        ps.setString(2, String.valueOf(value.getTipoCorda()));
        ps.setFloat(3, value.getPrecoVenda());

        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        value.setId(rs.getLong(1));
    }

    @Override
    public void alterar(Violao value) throws Exception
    {
        String sql = "UPDATE violao SET marca = ?, \"tipoCorda\" = ?, \"precoVenda\" = ?  WHERE id = ?";
        PreparedStatement ps = getPreparedStatement(sql, false);
        ps.setString(1, value.getMarca());
        ps.setString(2, String.valueOf(value.getTipoCorda()));
        ps.setFloat(3, value.getPrecoVenda());
        ps.setLong(4, value.getId());
        ps.executeUpdate();
    }

    @Override
    public void excluir(Violao value) throws Exception
    {
        String sql = "DELETE FROM violao WHERE id = ?";
        PreparedStatement ps = getPreparedStatement(sql, false);
        ps.setLong(1, value.getId());
        ps.executeUpdate();
    }

    public List<Compra> listarCompras() throws Exception
    {
        String sql = "SELECT * FROM compra ORDER BY \"dataCompra\" ASC";
        PreparedStatement ps = getPreparedStatement(sql, false);
        ResultSet rs = ps.executeQuery();

        List<Compra> compras = new ArrayList<>();

        while (rs.next())
        {
            Compra compra = new Compra();
            compra.setIdCompra(rs.getLong("idCompra"));
            compra.setDataCompra(rs.getString("dataCompra"));
            compra.setTotalCompra(rs.getFloat("totalCompra"));
            compras.add(compra);
        }

        return compras;
    }

    public List<Venda> listarVendas() throws Exception
    {
        String sql = "SELECT * FROM venda ORDER BY \"dataVenda\" ASC";
        PreparedStatement ps = getPreparedStatement(sql, false);
        ResultSet rs = ps.executeQuery();

        List<Venda> vendas = new ArrayList<>();

        while (rs.next())
        {
            Venda venda = new Venda();
            venda.setIdVenda(rs.getLong("idVenda"));
            venda.setDataVenda(rs.getString("dataVenda"));
            venda.setTotalVenda(rs.getFloat("totalVenda"));
            vendas.add(venda);
        }

        return vendas;
    }

    public void comprar(Violao value, Compra compra, DetalhesCompra detalhesCompra) throws Exception
    {
        String sql = "INSERT INTO compra (\"totalCompra\", \"dataCompra\") VALUES (?, ?)";
        PreparedStatement ps = getPreparedStatement(sql, true);
        ps.setFloat(1, compra.getTotalCompra());
        ps.setDate(2, Date.valueOf(compra.getDataCompra()));
        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        compra.setIdCompra(rs.getLong(1));


        sql = "INSERT INTO \"detalhesCompra\" (quantidade, \"precoUnitario\", \"dataCompra\", \"totalCompra\") VALUES (?, ?, ?, ?)";
        ps = getPreparedStatement(sql, true);
        ps.setInt(1, detalhesCompra.getQuantidade());
        ps.setFloat(2, detalhesCompra.getPrecoUnitario());
        ps.setDate(3, Date.valueOf(compra.getDataCompra()));
        ps.setFloat(4, compra.getTotalCompra());
        ps.executeUpdate();

        sql = "UPDATE violao SET estoque = ? WHERE id = ?";
        ps = getPreparedStatement(sql, false);
        ps.setInt(1, (value.getEstoque() + detalhesCompra.getQuantidade()));
        ps.setLong(2, value.getId());
        ps.executeUpdate();
    }

    public void vender(Violao value, Venda venda, DetalhesVenda detalhesVenda) throws Exception
    {
        String sql = "INSERT INTO venda (\"totalVenda\", \"dataVenda\") VALUES (?, ?)";
        PreparedStatement ps = getPreparedStatement(sql, true);
        ps.setFloat(1, venda.getTotalVenda());
        ps.setDate(2, Date.valueOf(venda.getDataVenda()));
        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        venda.setIdVenda(rs.getLong(1));


        sql = "INSERT INTO \"detalhesVenda\" (quantidade, \"precoUnitario\", \"dataVenda\", \"totalVenda\") VALUES (?, ?, ?, ?)";
        ps = getPreparedStatement(sql, true);
        ps.setInt(1, detalhesVenda.getQuantidade());
        ps.setFloat(2, detalhesVenda.getPrecoUnitario());
        ps.setDate(3, Date.valueOf(venda.getDataVenda()));
        ps.setFloat(4, venda.getTotalVenda());
        ps.executeUpdate();

        sql = "UPDATE violao SET estoque = ? WHERE id = ?";
        ps = getPreparedStatement(sql, false);
        ps.setInt(1, (value.getEstoque() - detalhesVenda.getQuantidade()));
        ps.setLong(2, value.getId());
        ps.executeUpdate();
    }

    public List<Violao> listarViolaoVenda() throws Exception
    {
        String sql = "SELECT * FROM violao WHERE estoque > 0 ORDER BY marca ASC";
        PreparedStatement ps = getPreparedStatement(sql, false);
        ResultSet rs = ps.executeQuery();

        List<Violao> violoes = new ArrayList<>();

        while (rs.next())
        {
            Violao violao = new Violao();
            violao.setMarca(rs.getString("marca"));
            violao.setTipoCorda(TipoCorda.valueOf(rs.getString("tipoCorda")));
            violao.setPrecoVenda(rs.getFloat("precoVenda"));
            violao.setId(rs.getLong("id"));
            violao.setEstoque(rs.getInt("estoque"));
            violoes.add(violao);
        }

        return violoes;
    }
}
