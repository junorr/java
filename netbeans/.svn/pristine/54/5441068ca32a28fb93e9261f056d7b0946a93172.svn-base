/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.bb.disec.test;

import br.com.bb.disec.util.URLD;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;



/**
 *
 * @author juno
 */
public class TestURLD {
	
	
	public static void main(String[] args) throws MalformedURLException {
		String url = "http://disec.intranet.bb.com.br/planEstrategico";
		URL ul = new URL(url);
		System.out.println("* URL.........: "+ url);
		System.out.println();
		System.out.println("* URL.protocol: "+ ul.getProtocol());
		System.out.println("* URL.host....: "+ ul.getHost());
		System.out.println("* URL.port....: "+ ul.getPort());
		System.out.println("* URL.context.: "+ ul.getPath());
		System.out.println("* URL.path....: "+ ul.getPath());
		System.out.println();
		URLD ud = new URLD(url);
		System.out.println("* URLD.protocol: "+ ud.getProtocol());
		System.out.println("* URLD.host....: "+ ud.getHost());
		System.out.println("* URLD.port....: "+ ud.getPort());
		System.out.println("* URLD.context.: "+ ud.getContext());
		System.out.println("* URLD.path....: "+ Arrays.toString(ud.getPaths()));
		System.out.println("------------------------------------");

		url = "https://disec.intranet.bb.com.br/planEstrategico/projetos";
		ul = new URL(url);
		System.out.println("* URL.........: "+ url);
		System.out.println();
		System.out.println("* URL.protocol: "+ ul.getProtocol());
		System.out.println("* URL.host....: "+ ul.getHost());
		System.out.println("* URL.port....: "+ ul.getPort());
		System.out.println("* URL.context.: "+ ul.getPath());
		System.out.println("* URL.path....: "+ ul.getPath());
		System.out.println();
		ud = new URLD(url);
		System.out.println("* URLD.protocol: "+ ud.getProtocol());
		System.out.println("* URLD.host....: "+ ud.getHost());
		System.out.println("* URLD.port....: "+ ud.getPort());
		System.out.println("* URLD.context.: "+ ud.getContext());
		System.out.println("* URLD.path....: "+ Arrays.toString(ud.getPaths()));
		System.out.println("------------------------------------");
		
		url = "http://localhost.bb.com.br:8080/planEstrategico/10/equipe";
		ul = new URL(url);
		System.out.println("* URL.........: "+ url);
		System.out.println();
		System.out.println("* URL.protocol: "+ ul.getProtocol());
		System.out.println("* URL.host....: "+ ul.getHost());
		System.out.println("* URL.port....: "+ ul.getPort());
		System.out.println("* URL.context.: "+ ul.getPath());
		System.out.println("* URL.path....: "+ ul.getPath());
		System.out.println();
		ud = new URLD(url);
		System.out.println("* URLD.protocol: "+ ud.getProtocol());
		System.out.println("* URLD.host....: "+ ud.getHost());
		System.out.println("* URLD.port....: "+ ud.getPort());
		System.out.println("* URLD.context.: "+ ud.getContext());
		System.out.println("* URLD.path....: "+ Arrays.toString(ud.getPaths()));
		System.out.println("------------------------------------");
	}
	
}
