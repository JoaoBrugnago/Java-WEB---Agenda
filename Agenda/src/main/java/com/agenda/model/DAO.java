package com.agenda.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DAO {
    //-- Parâmetros de conexão
    private String driver = "com.mysql.cj.jdbc.Driver";
    private String url = "jdbc:mysql://127.0.0.1:3306/dbagenda?useTimezone=true&serverTimezone=UTC";
    private String user = "root";
    private String password = "joaosoares2007";

    //-- Método de conexão
    private Connection conectar() {
        Connection con = null;
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url, user, password);
            return con;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    /* Crud create */
    public void inserirContato(JavaBeans contato) {
        String create = "insert into contatos (nome, fone, email) values (?, ?, ?)";

        try {
            Connection con = conectar();
            PreparedStatement pst = con.prepareStatement(create);
            pst.setString(1, contato.getNome());
            pst.setString(2, contato.getFone());
            pst.setString(3, contato.getEmail());
            pst.executeUpdate();
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /* Crud read */
    public ArrayList<JavaBeans> listarContatos() {
        ArrayList<JavaBeans> contatos = new ArrayList<>();
        String read = "select * from contatos order by nome";
        try {
            Connection con = conectar();
            PreparedStatement pst = con.prepareStatement(read);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                JavaBeans contato = new JavaBeans(rs.getString("idcon"), rs.getString("nome"),
                        rs.getString("fone"), rs.getString("email"));

                contatos.add(contato);
            }
            con.close();
            return contatos;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    /* Crud update */
    public void selecionarContato(JavaBeans contato) {
        String read2 = "select * from contatos where idcon = ?";
        try {
            Connection con = conectar();
            PreparedStatement pst = con.prepareStatement(read2);
            pst.setString(1, contato.getIdcon());
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                contato.setIdcon(rs.getString("idcon"));
                contato.setNome(rs.getString("nome"));
                contato.setFone(rs.getString("fone"));
                contato.setEmail(rs.getString("email"));
            }
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void alterarContato(JavaBeans contato) {
        String create = "update contatos set nome=?, fone=?, email=? where idcon=?";
        try {
            Connection con = conectar();
            PreparedStatement pst = con.prepareStatement(create);
            pst.setString(1, contato.getNome());
            pst.setString(2, contato.getFone());
            pst.setString(3, contato.getEmail());
            pst.setString(4, contato.getIdcon());
            pst.executeUpdate();
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /* Crud delete */
    public void deleterContato(JavaBeans contato) {
        String delete = "delete from contatos where idcon = ?";
        try {
            Connection con = conectar();
            PreparedStatement pst = con.prepareStatement(delete);
            pst.setString(1, contato.getIdcon());
            pst.executeUpdate();
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
