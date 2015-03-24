<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/tld/c.tld" prefix="c"%>
<%@taglib uri="/WEB-INF/tld/fmt.tld" prefix="fmt"%>
<fmt:setLocale value="pt_BR" />
<table class="errorTable">
	<tr>
		<td>Desculpe o incômodo</td>
	</tr>
	<tr>
		<td>Estamos adotando procedimentos para a regularização da
			ferramenta.</td>
	</tr>
	<tr>
		<td>Aplicação: <span id="aplic"></span></td>
	</tr>
	<%
	    try {
	%>
	<tr>
		<td class="alertTable"><c:out value="${mensagemDeErro}" /> <%
	    } catch (Exception e) {
	    }
	%></td>
	</tr>
</table>
<div id="urlCrumb" style="display: none;" class="">
	<table>
		<thead>
			<tr>
				<th>Chave</th>
				<th>Nome</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${beanCrumb}" var='beanCrumb'>
				<tr>
					<td><c:out value="${beanCrumb.chave}"></c:out></td>
					<td><c:out value="${beanCrumb.desc}"></c:out></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>
<script>
$('#aplic').html(function(){
	return $('#urlCrumb table tbody tr:last td:eq(0)').html();
});
</script>