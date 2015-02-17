package com.DBUtils.servlets;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import util.ClusterUtils;

/**
 * Servlet implementation class WekaFileWriteServlet
 */
@WebServlet("/ClustererServlet")
public class ClustererServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ClustererServlet() {
		super();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Connection con = (Connection) getServletContext().getAttribute("DBConnection");
		String filepath = (String) getServletContext().getAttribute("clusterFilePath");
		File file = new File(filepath);
		
		/* Make the .arff file */
		ClusterUtils.createArffFile(con, file);
		
		/* Do clustering */
		ClusterUtils.makeClusters();
		
		PrintWriter printWriter = response.getWriter();
		printWriter.println("success");
	}
}
