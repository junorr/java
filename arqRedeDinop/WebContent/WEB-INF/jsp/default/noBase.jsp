<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/tld/c.tld" prefix="c"%>
<%@taglib uri="/WEB-INF/tld/fmt.tld" prefix="fmt"%>
<fmt:setLocale value="pt_BR" />
<fieldset id="fieldResult" class="fonte"
	style="color: #22407B; display: inline;">
	<legend> Atualização: </legend>
	<font class='fonte'>A base de dados GSV encontra-se em
		atualização no momento. Tente mais tarde.</font>
</fieldset>

<script>
		$('fieldset').corner();
	</script>