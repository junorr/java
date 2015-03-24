$(document).ready(function(){
	
	$('#conteudo').load('exiba?cmdo=arqRedeDinop.conteudo', function(){
		UsersOnline.initStatusSession({callback: updateUsersOnline});
		
		$('.nmAplic').html($('#urlCrumb table tbody tr:eq(0) td:eq(1)').html());
		
		/*
		$('#logoTesteira .containerFoto img').on('click', function(){
			$('#configPrefs').load('/exiba?cmdo=portal.configPrefs', function(){
				$(this).slideToggle('fast');
			});
		});
		*/
	});
		
	/*
	 * Fim do conteúdo index.
	 */

	/*
	 * Início de Bloco para outros scripts. Altere a vontade.
	 */
	
});

/*
 * Início do conteúdo index. NÃO REMOVER OU ALTERAR
 */