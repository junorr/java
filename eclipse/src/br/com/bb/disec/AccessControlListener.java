package br.com.bb.disec;

import br.com.bb.disec.bean.DcrCtu;
import br.com.bb.disec.bean.LogRecord;
import br.com.bb.disec.bean.iface.IDcrCtu;
import br.com.bb.disec.bean.iface.IPflAcss;
import br.com.bb.disec.persistencia.AccessPersistencia;
import br.com.bb.disec.persistencia.CtuPersistencia;
import br.com.bb.disec.persistencia.LogPersistencia;
import br.com.bb.disec.util.AccessDeniedPage;
import br.com.bb.disec.util.URLD;
import br.com.bb.sso.util.ValveConnectionPool;
import br.com.bb.sso.valve.listener.ValveChainException;
import br.com.bb.sso.valve.listener.AuthenticationListener;
import br.com.bb.sso.valve.listener.IAuthenticationContext;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;



/**
 * Listener do AuthenticationValve responsável pela
 * autorização e log de acessos.
 * @author Juno Roesler - F6036477
 */
public class AccessControlListener implements AuthenticationListener {

	
	@Override
	public void beforeAuth(IAuthenticationContext ctx) {}


	@Override
	public void authFailed(IAuthenticationContext ctx) {}


	@Override
	public void authSuccess(IAuthenticationContext ctx) throws ValveChainException {
		//Obtém uma referência ao pool de conexões
		ValveConnectionPool pool = ValveConnectionPool.of(ctx.container());
		URLD url = URLD.of(ctx.request().getRequestURL().toString());
		CtuPersistencia ctp = new CtuPersistencia(pool, url);
		AccessPersistencia acp = new AccessPersistencia(pool);
		LogPersistencia lop = new LogPersistencia(pool);
		try {
			//recupera todos os conteúdos da tabela dcr_ctu
			//com base na URL acessada.
			List<IDcrCtu> ctus = ctp.findAll();
			if(ctus == null || ctus.isEmpty()) {
				ctx.logger(this.getClass()).log(
						"Nenhum conteúdo encontrado para a URL: %s", url
				);
				ctus = Arrays.asList((IDcrCtu)new DcrCtu().setCdCtu(99999));
			}
			//Verifica autorização de acesso para os conteúdos encontrados.
			boolean access = false;
			for(IDcrCtu ctu : ctus) {
				access = access || acp.checkAccess(
						ctx.user(), ctu.getCdCtu()
				);
				lop.log(new LogRecord(
						ctu.getCdCtu(), 
						ctx.user(), 
						ctx.request(), 
						access
				));
			}
			IDcrCtu ctu = ctus.get(0);
			//ctx.session().setAttribute("acessoAutorizado", access);
			
			//Se o acesso for negado, redireciona para a página 
			//de acesso negado definida em /resources/acessoNegado.html
			if(!access) {
				this.sendAccessDenied(ctx, ctu, acp.getListaPerfilAcesso());
				throw new ValveChainException("Acesso Negado ao conteúdo");
			}
		}
		catch(IOException | SQLException e) {
      e.printStackTrace();
			throw new ValveChainException(
					"Erro na autorização ao conteúdo: "+ url.getContext(), e
			);
		}
	}
	
	
	private void sendAccessDenied(IAuthenticationContext ctx, IDcrCtu ctu, List<IPflAcss> perfAcss) throws IOException {
		AccessDeniedPage page = new AccessDeniedPage(ctx.user(), ctu, perfAcss);
		page.writeResponse(ctx.response());
		ctx.response().flushBuffer();
	}
	
}
