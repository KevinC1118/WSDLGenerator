package com.wsdlgenerator;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Blob;
import com.wsdlgenerator.util.CommonUtil;

public class DownloadServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2285124106878547379L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("application/octet-stream");
		resp.setHeader("Content-Disposition",
				"attachment; filename=\"download.zip\"");

		resp.getOutputStream().write(
				((Blob) CommonUtil.getCache().get(
						req.getParameterMap().keySet().toArray()[0]))
						.getBytes());
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(req, resp);
	}

}
