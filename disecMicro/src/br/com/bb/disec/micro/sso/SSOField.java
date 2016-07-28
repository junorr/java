package br.com.bb.disec.micro.sso;


/**
 * Enumeração dos nomes das informações do usuário
 * na resposta do servidor de autenticação SSO.
 * @author juno
 */
public enum SSOField {
	
	/**
	 * Prefixo da dependência ('prefixoDependencia').
	 */
	PRF_DEPE(SSOField.STR_PRF_DEPE),
	
	/**
	 * Código do pilar ('codigoPilar').
	 */
	COD_PILAR(SSOField.STR_COD_PILAR),
	
	/**
	 * Número do Telefone ('telephonenumber').
	 */
	PHONE(SSOField.STR_PHONE),
	
	/**
	 * Nome do usuário ('nm-idgl').
	 */
	NOME(SSOField.STR_NOME),
	
	/**
	 * Número do CPF ('nr-cpf').
	 */
	CPF(SSOField.STR_CPF),
	
	/**
	 * Código de responsabilidade funcional ('responsabilidadeFuncional').
	 */
	RESP_FUN(SSOField.STR_RESP_FUN),
	
	/**
	 * Código do tipo de dependência ('tipoDependencia').
	 */
	TIPO_DEPE(SSOField.STR_TIPO_DEPE),
	
	/**
	 * Prefixo da super ('prefixoSuperEstadual').
	 */
	PRF_SUPER(SSOField.STR_PRF_SUPER),
	
	/**
	 * Código do grupamento ('grupamento').
	 */
	GRUPAMENTO(SSOField.STR_GRUPAMENTO),
	
	/**
	 * UOR de equipe ('cd-eqp').
	 */
	UOR_EQP(SSOField.STR_UOR_EQP),
	
	/**
	 * Código IOR ('cd-ior').
	 */
	COD_IOR(SSOField.STR_COD_IOR),
	
	/**
	 * Número de celular ('mobile').
	 */
	MOBILE(SSOField.STR_MOBILE),
	
	/**
	 * Nome de guerra ('nomeGuerra').
	 */
	NOME_GUERRA(SSOField.STR_NOME_GUERRA),
	
	/**
	 * Código referência organizacional ('cd-ref-orgc').
	 */
	REF_ORGC(SSOField.STR_REF_ORGC),
	
	/**
	 * UF ('nomeUF').
	 */
	UF(SSOField.STR_UF),
	
	/**
	 * Chave + Nome ('cn').
	 */
	CHAVE_NOME(SSOField.STR_CHAVE_NOME),
	
	/**
	 * UOR da dependência ('cd-uor-dep').
	 */
	UOR_DEPE(SSOField.STR_UOR_DEPE),
	
	/**
	 * Telefone de casa ('homephone').
	 */
	HOME_PHONE(SSOField.STR_HOME_PHONE),
	
	/**
	 * Código da comissão ('codigoComissao').
	 */
	COD_COMISSAO(SSOField.STR_COD_COMISSAO),
	
	/**
	 * Chave (F0000000) ('chaveFuncionario').
	 */
	CHAVE(SSOField.STR_CHAVE),
	
	/**
	 * Código da instituição ('codigoInstituicao').
	 */
	COD_INST(SSOField.STR_COD_INST),
	
	/**
	 * Código MCI ('cd-cli').
	 */
	MCI(SSOField.STR_MCI),
	
	/**
	 * Endereço de e-mail ('mail').
	 */
	EMAIL(SSOField.STR_EMAIL),
	
	/**
	 * Prefixo da diretoria ('prefixoDiretoria').
	 */
	PRF_DIRETORIA(SSOField.STR_PRF_DIRETORIA),
	
	/**
	 * Nome da comissão ('tx-cmss-usu').
	 */
	NOME_COMISSAO(SSOField.STR_NOME_COMISSAO),
	
	/**
	 * Token ID de autenticação SSO ('tokenId').
	 */
	TOKENID(SSOField.STR_TOKENID),
	
	/**
	 * String de acessos ('acessosUsu').
	 */
	ACESSOS(SSOField.STR_ACESSOS);
	
	
	private SSOField(String name) {
		this.field = name;
	}
	
	
	private final String field;
	
	
	public String getName() {
		return field;
	}
	
	
	private static final String
			STR_PRF_DEPE = "prefixoDependencia",
			STR_COD_PILAR = "codigoPilar",
			STR_PHONE = "telephonenumber",
			STR_NOME = "nm-idgl",
			STR_CPF = "nr-cpf",
			STR_RESP_FUN = "responsabilidadeFuncional",
			STR_TIPO_DEPE = "tipoDependencia",
			STR_PRF_SUPER = "prefixoSuperEstadual",
			STR_GRUPAMENTO = "grupamento",
			STR_UOR_EQP = "cd-eqp",
			STR_COD_IOR = "cd-ior",
			STR_MOBILE = "mobile",
			STR_NOME_GUERRA = "nomeGuerra",
			STR_REF_ORGC = "cd-ref-orgc",
			STR_UF = "nomeUF",
			STR_CHAVE_NOME = "cn",
			STR_UOR_DEPE = "cd-uor-dep",
			STR_HOME_PHONE = "homephone",
			STR_COD_COMISSAO = "codigoComissao",
			STR_CHAVE = "chaveFuncionario",
			STR_COD_INST = "codigoInstituicao",
			STR_MCI = "cd-cli",
			STR_EMAIL = "mail",
			STR_PRF_DIRETORIA = "prefixoDiretoria",
			STR_NOME_COMISSAO = "tx-cmss-usu",
			STR_TOKENID = "tokenId",
			STR_ACESSOS = "acessosUsu";
	
	
	public static SSOField fromName(String name) {
		SSOField field = null;
		switch(name) {
			case STR_ACESSOS:
				field = ACESSOS;
				break;
			case STR_CHAVE:
				field = CHAVE;
				break;
			case STR_CHAVE_NOME:
				field = CHAVE_NOME;
				break;
			case STR_COD_COMISSAO:
				field = COD_COMISSAO;
				break;
			case STR_COD_INST:
				field = COD_INST;
				break;
			case STR_COD_IOR:
				field = COD_IOR;
				break;
			case STR_COD_PILAR:
				field = COD_PILAR;
				break;
			case STR_CPF:
				field = CPF;
				break;
			case STR_EMAIL:
				field = EMAIL;
				break;
			case STR_GRUPAMENTO:
				field = GRUPAMENTO;
				break;
			case STR_HOME_PHONE:
				field = HOME_PHONE;
				break;
			case STR_MCI:
				field = MCI;
				break;
			case STR_MOBILE:
				field = MOBILE;
				break;
			case STR_NOME:
				field = NOME;
				break;
			case STR_NOME_COMISSAO:
				field = NOME_COMISSAO;
				break;
			case STR_NOME_GUERRA:
				field = NOME_GUERRA;
				break;
			case STR_PHONE:
				field = PHONE;
				break;
			case STR_PRF_DEPE:
				field = PRF_DEPE;
				break;
			case STR_PRF_DIRETORIA:
				field = PRF_DIRETORIA;
				break;
			case STR_PRF_SUPER:
				field = PRF_SUPER;
				break;
			case STR_REF_ORGC:
				field = REF_ORGC;
				break;
			case STR_RESP_FUN:
				field = RESP_FUN;
				break;
			case STR_TIPO_DEPE:
				field = TIPO_DEPE;
				break;
			case STR_TOKENID:
				field = TOKENID;
				break;
			case STR_UF:
				field = UF;
				break;
			case STR_UOR_DEPE:
				field = UOR_DEPE;
				break;
			case STR_UOR_EQP:
				field = UOR_EQP;
				break;
			default:
				throw new IllegalArgumentException(
						"Nome SSOField Inválido: "+ name
				);
		}
		return field;
	}
	
}
