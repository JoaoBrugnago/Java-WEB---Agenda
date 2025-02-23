package com.agenda.controller;

import com.agenda.model.DAO;
import com.agenda.model.JavaBeans;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;

public class Controller extends HttpServlet {
    private static final long serialVersionUID = 1L;
    DAO dao = new DAO();
    JavaBeans contato = new JavaBeans();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getServletPath();
        if (action.equals("/controller")) {
            contatos(request, response);
        } else if (action.equals("/insert")) {
            novoContato(request, response);
        } else if (action.equals("/select")) {
            listarContato(request, response);
        } else if (action.equals("/update")) {
            editarContato(request, response);
        } else if (action.equals("/delete")) {
            removerContato(request, response);
        } else if (action.equals("/report")) {
            gerarRelatorio(request, response);
        } else {
            response.sendRedirect("index.html");
        }
    }

    protected void contatos(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ArrayList<JavaBeans> lista = dao.listarContatos();
        request.setAttribute("contatos", lista);
        RequestDispatcher rd = request.getRequestDispatcher("agenda.jsp");
        rd.forward(request, response);
    }

    protected void novoContato(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        contato.setNome(request.getParameter("nome"));
        contato.setFone(request.getParameter("fone"));
        contato.setEmail(request.getParameter("email"));

        dao.inserirContato(contato);
        response.sendRedirect("controller");
    }

    protected void listarContato(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idcon = request.getParameter("idcon");
        contato.setIdcon(idcon);
        dao.selecionarContato(contato);
        request.setAttribute("idcon", contato.getIdcon());
        request.setAttribute("nome", contato.getNome());
        request.setAttribute("fone", contato.getFone());
        request.setAttribute("email", contato.getEmail());
        RequestDispatcher rd = request.getRequestDispatcher("editar.jsp");
        rd.forward(request, response);
    }

    protected void editarContato(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        contato.setIdcon(request.getParameter("idcon"));
        contato.setNome(request.getParameter("nome"));
        contato.setFone(request.getParameter("fone"));
        contato.setEmail(request.getParameter("email"));
        dao.alterarContato(contato);
        response.sendRedirect("controller");
    }

    protected void removerContato(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idcon = request.getParameter("idcon");
        contato.setIdcon(idcon);
        dao.deleterContato(contato);
        response.sendRedirect("controller");
    }

    protected void gerarRelatorio(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Document documento = new Document();
        try {
            //-- Tipo do documento
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", "inline; filename=" + "contatos.pdf");
            //-- Criar o documento
            PdfWriter.getInstance(documento, response.getOutputStream());
            //-- Abrir o documento
            documento.open();
            //-- Adicionar parágrafo
            documento.add(new Paragraph("Lista de contatos"));
            documento.add(new Paragraph(" "));
            //-- Criar uma tabela
            PdfPTable tabela = new PdfPTable(3);
            //-- Cabeçalho
            PdfPCell col1 = new PdfPCell(new Paragraph("Nome"));
            PdfPCell col2 = new PdfPCell(new Paragraph("Fone"));
            PdfPCell col3 = new PdfPCell(new Paragraph("E-mail"));
            tabela.addCell(col1);
            tabela.addCell(col2);
            tabela.addCell(col3);
            //-- Popular tabela com os contatos
            ArrayList<JavaBeans> lista = dao.listarContatos();
            for (int i = 0; i < lista.size(); i++) {
                tabela.addCell(lista.get(i).getNome());
                tabela.addCell(lista.get(i).getFone());
                tabela.addCell(lista.get(i).getEmail());
            }
            //-- Adicionar a tabela
            documento.add(tabela);
            //-- Fechar o documento
            documento.close();
        } catch (Exception e) {
            System.out.println(e);
            documento.close();
        }
    }
}
