/*
 * Direitos Autorais Reservados (c) 2011 Juno Roesler
 * Contato: juno.rr@gmail.com
 * 
 * Esta biblioteca é software livre; você pode redistribuí-la e/ou modificá-la sob os
 * termos da Licença Pública Geral Menor do GNU conforme publicada pela Free
 * Software Foundation; tanto a versão 2.1 da Licença, ou (a seu critério) qualquer
 * versão posterior.
 * 
 * Esta biblioteca é distribuída na expectativa de que seja útil, porém, SEM
 * NENHUMA GARANTIA; nem mesmo a garantia implícita de COMERCIABILIDADE
 * OU ADEQUAÇÃO A UMA FINALIDADE ESPECÍFICA. Consulte a Licença Pública
 * Geral Menor do GNU para mais detalhes.
 * 
 * Você deve ter recebido uma cópia da Licença Pública Geral Menor do GNU junto
 * com esta biblioteca; se não, acesse 
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html, 
 * ou escreva para a Free Software Foundation, Inc., no
 * endereço 59 Temple Street, Suite 330, Boston, MA 02111-1307 USA.
 */
package us.pserver.smail.tutorial;

import us.pserver.smail.MailServer;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import us.pserver.smail.MailBox;
import us.pserver.smail.Message;
import us.pserver.smail.Messages;
import us.pserver.smail.SMail;
import us.pserver.smail.SMailException;
import us.pserver.smail.event.MailBoxEvent;
import us.pserver.smail.event.MailBoxListener;
import us.pserver.smail.filter.AddressFilter;
import us.pserver.smail.filter.ContentFilter;
import us.pserver.smail.filter.DateMessageFilter;
import us.pserver.smail.filter.HasAttachmentFilter;
import us.pserver.smail.filter.OldMessageFilter;
import us.pserver.smail.filter.SubjectFilter;


/**
 * <h2>Tutorial: Recebendo emails com SimpleMail.</h2>
 * 
 * <p style="font-size: medium;">
 * A biblioteca <code>SimpleMail</code> simplifica o processo de
 * recebimento e manipulação de emails, abstraindo a complexa 
 * e tediosa codificação exigida por <code>JavaMail</code>.<br>
 * Veremos a seguir, como receber emails 
 * utilizando o motor de conexão <code>SMail</code> e 
 * a caixa de mensagens <code>MailBox</code>.
 * <br><br>
 * Inicialmente, vamos configurar os dados do servidor 
 * e da conta de email, utilizando a classe encapsuladora
 * <code>MailServer</code>.
 * </p>
 * 
 * <pre> <font color="#000000"><span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#000000"> 1 </font></span>    MailServer server <font color="#000000"><strong>=</strong></font> <font color="#006699"><strong>new</strong></font> <font color="#9966ff">MailServer</font><font color="#000000"><strong>(</strong></font><font color="#000000"><strong>)</strong></font>
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#000000"> 2 </font></span>        .<font color="#9966ff">setName</font><font color="#000000"><strong>(</strong></font><font color="#ff00cc">&quot;</font><font color="#ff00cc">gmail_imap</font><font color="#ff00cc">&quot;</font><font color="#000000"><strong>)</strong></font> <font color="#ff8400">//</font><font color="#ff8400">nome</font><font color="#ff8400"> </font><font color="#ff8400">opcional.</font><font color="#ff8400"> </font><font color="#ff8400">&Uacute;til</font><font color="#ff8400"> </font><font color="#ff8400">no</font><font color="#ff8400"> </font><font color="#ff8400">caso</font><font color="#ff8400"> </font><font color="#ff8400">de</font><font color="#ff8400"> </font><font color="#ff8400">um</font><font color="#ff8400"> </font><font color="#ff8400">pool</font><font color="#ff8400"> </font><font color="#ff8400">de</font><font color="#ff8400"> </font><font color="#ff8400">servidores.</font>
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#000000"> 3 </font></span>        .<font color="#9966ff">setServer</font><font color="#000000"><strong>(</strong></font><font color="#ff00cc">&quot;</font><font color="#ff00cc">imap</font><font color="#ff00cc">.</font><font color="#ff00cc">gmail</font><font color="#ff00cc">.</font><font color="#ff00cc">com</font><font color="#ff00cc">&quot;</font><font color="#000000"><strong>)</strong></font>
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#000000"> 4 </font></span>        .<font color="#9966ff">setProtocol</font><font color="#000000"><strong>(</strong></font>MailServer.PROTOCOL_IMAPS<font color="#000000"><strong>)</strong></font>
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#990066"> 5 </font></span>        .<font color="#9966ff">setPort</font><font color="#000000"><strong>(</strong></font>MailServer.DEFAULT_IMAP_PORT<font color="#000000"><strong>)</strong></font>
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#000000"> 6 </font></span>        .<font color="#9966ff">setAuthentication</font><font color="#000000"><strong>(</strong></font><font color="#ff00cc">&quot;</font><font color="#ff00cc">juno</font><font color="#ff00cc">.</font><font color="#ff00cc">rr</font><font color="#ff00cc">@</font><font color="#ff00cc">gmail</font><font color="#ff00cc">.</font><font color="#ff00cc">com</font><font color="#ff00cc">&quot;</font>, <font color="#ff00cc">&quot;</font><font color="#ff00cc">mypassword</font><font color="#ff00cc">&quot;</font><font color="#000000"><strong>)</strong></font>;
 * </font></pre>
 * 
 * <p style="font-size: medium;">
 * Agora, criamos uma instância do motor de conexão 
 * <code>com.jpower.simplemail.SMail</code>,
 * informando o servidor configurado como argumento.
 * </p>
 * 
 * <pre> <font color="#000000"><span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#000000"> 7 </font></span>    SMail mail <font color="#000000"><strong>=</strong></font> <font color="#006699"><strong>new</strong></font> <font color="#9966ff">SMail</font><font color="#000000"><strong>(</strong></font>server<font color="#000000"><strong>)</strong></font>;
 * </font></pre>
 * 
 * <p style="font-size: medium;">
 * Com <code>SMail</code> configurado,
 * utilizamos seu método <code>receive( String ) </code> 
 * para obter a caixa de mensagens <code>MailBox</code>, 
 * passando como parâmetro o nome da pasta com os emails que queremos.
 * </p>
 * 
 * <pre> <font color="#000000"><span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#000000"> 8 </font></span>    MailBox box <font color="#000000"><strong>=</strong></font> <font color="#cc00cc">null</font>;
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#990066"> 9 </font></span>    <font color="#006699"><strong>try</strong></font> <font color="#000000"><strong>{</strong></font>
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#000000">10 </font></span>      box <font color="#000000"><strong>=</strong></font> mail.<font color="#9966ff">receive</font><font color="#000000"><strong>(</strong></font><font color="#ff00cc">&quot;</font><font color="#ff00cc">INBOX</font><font color="#ff00cc">&quot;</font><font color="#000000"><strong>)</strong></font>;
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#000000">11 </font></span>    <font color="#000000"><strong>}</strong></font> <font color="#006699"><strong>catch</strong></font><font color="#000000"><strong>(</strong></font>SMailException ex<font color="#000000"><strong>)</strong></font> <font color="#000000"><strong>{</strong></font>
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#000000">12 </font></span>      <font color="#ff8400">//</font><font color="#ff8400">Capture</font><font color="#ff8400"> </font><font color="#ff8400">e</font><font color="#ff8400"> </font><font color="#ff8400">trate</font><font color="#ff8400"> </font><font color="#ff8400">o</font><font color="#ff8400"> </font><font color="#ff8400">erro</font><font color="#ff8400"> </font><font color="#ff8400">adequadamente.</font>
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#000000">13 </font></span>      ex.<font color="#9966ff">printStackTrace</font><font color="#000000"><strong>(</strong></font><font color="#000000"><strong>)</strong></font>;
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#990066">14 </font></span>    <font color="#000000"><strong>}</strong></font>
 * </font></pre>
 * 
 * <p style="font-size: medium;">
 * A essa altura, se não ocorreu nenhum erro,
 * a classe <code>SMail</code> já estabeleceu conexão com o 
 * servidor, autenticou-se na conta de email 
 * e recuperou o cabeçalho de todas as mensagens da pasta.<br>
 * 
 * Por fim, podemos manipular as mensagens,
 * verificando remetentes, destinatários, lendo seu conteúdo
 * e salvando anexos. Vejamos alguns exemplos:
 * </p>
 * 
 * <pre> <font color="#000000"><span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#990066">15 </font></span>    <font color="#ff8400">//</font><font color="#ff8400">Recuperar</font><font color="#ff8400"> </font><font color="#ff8400">todas</font><font color="#ff8400"> </font><font color="#ff8400">as</font><font color="#ff8400"> </font><font color="#ff8400">mensagens</font><font color="#ff8400"> </font><font color="#ff8400">de</font><font color="#ff8400"> </font><font color="#ff8400">&quot;INBOX&quot;</font>
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#000000">16 </font></span>    Messages all <font color="#000000"><strong>=</strong></font> box.<font color="#9966ff">getMessages</font><font color="#000000"><strong>(</strong></font><font color="#000000"><strong>)</strong></font>;
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#000000">17 </font></span>    
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#000000">18 </font></span>    <font color="#ff8400">//</font><font color="#ff8400">Podemos</font><font color="#ff8400"> </font><font color="#ff8400">obter</font><font color="#ff8400"> </font><font color="#ff8400">uma</font><font color="#ff8400"> </font><font color="#ff8400">lista</font><font color="#ff8400"> </font><font color="#ff8400">ou</font><font color="#ff8400"> </font><font color="#ff8400">um</font><font color="#ff8400"> </font><font color="#ff8400">array</font><font color="#ff8400"> </font><font color="#ff8400">com</font><font color="#ff8400"> </font><font color="#ff8400">as</font><font color="#ff8400"> </font><font color="#ff8400">mensagens</font>
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#000000">19 </font></span>    List<font color="#000000"><strong>&lt;</strong></font>Message<font color="#000000"><strong>&gt;</strong></font> list <font color="#000000"><strong>=</strong></font> all.<font color="#9966ff">getList</font><font color="#000000"><strong>(</strong></font><font color="#000000"><strong>)</strong></font>;
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#990066">20 </font></span>    Message[] array <font color="#000000"><strong>=</strong></font> all.<font color="#9966ff">toArray</font><font color="#000000"><strong>(</strong></font><font color="#000000"><strong>)</strong></font>;
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#000000">21 </font></span>    
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#000000">22 </font></span>    <font color="#ff8400">//</font><font color="#ff8400">Recuperar</font><font color="#ff8400"> </font><font color="#ff8400">somente</font><font color="#ff8400"> </font><font color="#ff8400">novas</font><font color="#ff8400"> </font><font color="#ff8400">mensagens</font>
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#000000">23 </font></span>    Messages news <font color="#000000"><strong>=</strong></font> box.<font color="#9966ff">getNewMessages</font><font color="#000000"><strong>(</strong></font><font color="#000000"><strong>)</strong></font>;
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#000000">24 </font></span>    
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#990066">25 </font></span>    <font color="#ff8400">//</font><font color="#ff8400">Pesquisar</font><font color="#ff8400"> </font><font color="#ff8400">mensagens</font><font color="#ff8400"> </font><font color="#ff8400">por</font><font color="#ff8400"> </font><font color="#ff8400">assunto</font>
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#000000">26 </font></span>    Messages ms <font color="#000000"><strong>=</strong></font> all.<font color="#9966ff">filter</font><font color="#000000"><strong>(</strong></font><font color="#006699"><strong>new</strong></font> <font color="#9966ff">SubjectFilter</font><font color="#000000"><strong>(</strong></font><font color="#ff00cc">&quot;</font><font color="#ff00cc">hello</font><font color="#ff00cc">&quot;</font><font color="#000000"><strong>)</strong></font><font color="#000000"><strong>)</strong></font>;
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#000000">27 </font></span>    
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#000000">28 </font></span>    <font color="#ff8400">//</font><font color="#ff8400">Pesquisar</font><font color="#ff8400"> </font><font color="#ff8400">mensagens</font><font color="#ff8400"> </font><font color="#ff8400">por</font><font color="#ff8400"> </font><font color="#ff8400">remetente</font>
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#000000">29 </font></span>    ms <font color="#000000"><strong>=</strong></font> all.<font color="#9966ff">filter</font><font color="#000000"><strong>(</strong></font><font color="#006699"><strong>new</strong></font> <font color="#9966ff">AddressFilter</font><font color="#000000"><strong>(</strong></font>
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#990066">30 </font></span>        AddressFilter.FROM, <font color="#ff00cc">&quot;</font><font color="#ff00cc">Duke</font><font color="#ff00cc">&quot;</font><font color="#000000"><strong>)</strong></font><font color="#000000"><strong>)</strong></font>;
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#000000">31 </font></span>    
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#000000">32 </font></span>    <font color="#ff8400">//</font><font color="#ff8400">Pesquisar</font><font color="#ff8400"> </font><font color="#ff8400">mensagens</font><font color="#ff8400"> </font><font color="#ff8400">por</font><font color="#ff8400"> </font><font color="#ff8400">data</font><font color="#ff8400"> </font><font color="#ff8400">de</font><font color="#ff8400"> </font><font color="#ff8400">envio</font>
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#000000">33 </font></span>    ms <font color="#000000"><strong>=</strong></font> all.<font color="#9966ff">filter</font><font color="#000000"><strong>(</strong></font><font color="#006699"><strong>new</strong></font> <font color="#9966ff">DateMessageFilter</font><font color="#000000"><strong>(</strong></font>
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#000000">34 </font></span>        DateMessageFilter.BEFORE, <font color="#006699"><strong>new</strong></font> <font color="#9966ff">Date</font><font color="#000000"><strong>(</strong></font><font color="#000000"><strong>)</strong></font><font color="#000000"><strong>)</strong></font><font color="#000000"><strong>)</strong></font>;
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#990066">35 </font></span>    
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#000000">36 </font></span>    <font color="#ff8400">//</font><font color="#ff8400">Pesquisar</font><font color="#ff8400"> </font><font color="#ff8400">mensagens</font><font color="#ff8400"> </font><font color="#ff8400">com</font><font color="#ff8400"> </font><font color="#ff8400">anexos</font>
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#000000">37 </font></span>    ms <font color="#000000"><strong>=</strong></font> all.<font color="#9966ff">filter</font><font color="#000000"><strong>(</strong></font><font color="#006699"><strong>new</strong></font> <font color="#9966ff">HasAttachmentFilter</font><font color="#000000"><strong>(</strong></font><font color="#000000"><strong>)</strong></font><font color="#000000"><strong>)</strong></font>;
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#000000">38 </font></span>    
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#000000">39 </font></span>    <font color="#ff8400">//</font><font color="#ff8400">Combinando</font><font color="#ff8400"> </font><font color="#ff8400">filtros</font><font color="#ff8400"> </font><font color="#ff8400">de</font><font color="#ff8400"> </font><font color="#ff8400">pesquisa</font>
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#990066">40 </font></span>    ms <font color="#000000"><strong>=</strong></font> all.<font color="#9966ff">filter</font><font color="#000000"><strong>(</strong></font><font color="#006699"><strong>new</strong></font> <font color="#9966ff">OldMessageFilter</font><font color="#000000"><strong>(</strong></font><font color="#000000"><strong>)</strong></font><font color="#000000"><strong>)</strong></font>
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#000000">41 </font></span>        .<font color="#9966ff">filter</font><font color="#000000"><strong>(</strong></font><font color="#006699"><strong>new</strong></font> <font color="#9966ff">HasAttachmentFilter</font><font color="#000000"><strong>(</strong></font><font color="#000000"><strong>)</strong></font><font color="#000000"><strong>)</strong></font>
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#000000">42 </font></span>        .<font color="#9966ff">filter</font><font color="#000000"><strong>(</strong></font><font color="#006699"><strong>new</strong></font> <font color="#9966ff">ContentFilter</font><font color="#000000"><strong>(</strong></font><font color="#ff00cc">&quot;</font><font color="#ff00cc">fotos</font><font color="#ff00cc"> </font><font color="#ff00cc">das</font><font color="#ff00cc"> </font><font color="#ff00cc">f&eacute;rias</font><font color="#ff00cc">&quot;</font><font color="#000000"><strong>)</strong></font><font color="#000000"><strong>)</strong></font>;
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#000000">43 </font></span>    
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#000000">44 </font></span>    <font color="#ff8400">//</font><font color="#ff8400">Salvando</font><font color="#ff8400"> </font><font color="#ff8400">anexos</font><font color="#ff8400"> </font><font color="#ff8400">das</font><font color="#ff8400"> </font><font color="#ff8400">mensagens</font>
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#990066">45 </font></span>    Message fotos <font color="#000000"><strong>=</strong></font> ms.<font color="#9966ff">first</font><font color="#000000"><strong>(</strong></font><font color="#000000"><strong>)</strong></font>;
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#000000">46 </font></span>    <font color="#006699"><strong>try</strong></font> <font color="#000000"><strong>{</strong></font>
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#000000">47 </font></span>      fotos.<font color="#9966ff">getAttachments</font><font color="#000000"><strong>(</strong></font><font color="#000000"><strong>)</strong></font>.<font color="#9966ff">first</font><font color="#000000"><strong>(</strong></font><font color="#000000"><strong>)</strong></font>.<font color="#9966ff">saveTo</font><font color="#000000"><strong>(</strong></font>
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#000000">48 </font></span>          <font color="#006699"><strong>new</strong></font> <font color="#9966ff">File</font><font color="#000000"><strong>(</strong></font><font color="#ff00cc">&quot;</font><font color="#ff00cc">/</font><font color="#ff00cc">home</font><font color="#ff00cc">/</font><font color="#ff00cc">juno</font><font color="#ff00cc">/</font><font color="#ff00cc">fotos</font><font color="#ff00cc">/</font><font color="#ff00cc">foto1</font><font color="#ff00cc">.</font><font color="#ff00cc">jpg</font><font color="#ff00cc">&quot;</font><font color="#000000"><strong>)</strong></font><font color="#000000"><strong>)</strong></font>;
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#000000">49 </font></span>    <font color="#000000"><strong>}</strong></font> <font color="#006699"><strong>catch</strong></font><font color="#000000"><strong>(</strong></font>IOException ex<font color="#000000"><strong>)</strong></font> <font color="#000000"><strong>{</strong></font>
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#990066">50 </font></span>      <font color="#ff8400">//</font><font color="#ff8400">Capture</font><font color="#ff8400"> </font><font color="#ff8400">e</font><font color="#ff8400"> </font><font color="#ff8400">trate</font><font color="#ff8400"> </font><font color="#ff8400">o</font><font color="#ff8400"> </font><font color="#ff8400">erro</font><font color="#ff8400"> </font><font color="#ff8400">adequadamente.</font>
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#000000">51 </font></span>      ex.<font color="#9966ff">printStackTrace</font><font color="#000000"><strong>(</strong></font><font color="#000000"><strong>)</strong></font>;
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#000000">52 </font></span>    <font color="#000000"><strong>}</strong></font>
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#000000">53 </font></span>    
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#000000">54 </font></span>    <font color="#ff8400">//</font><font color="#ff8400">Marcando</font><font color="#ff8400"> </font><font color="#ff8400">mensagens</font><font color="#ff8400"> </font><font color="#ff8400">novas</font><font color="#ff8400"> </font><font color="#ff8400">como</font><font color="#ff8400"> </font><font color="#ff8400">lidas</font>
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#990066">55 </font></span>    box.<font color="#9966ff">read</font><font color="#000000"><strong>(</strong></font>news<font color="#000000"><strong>)</strong></font>;
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#000000">56 </font></span>    
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#000000">57 </font></span>    <font color="#ff8400">//</font><font color="#ff8400">Apagando</font><font color="#ff8400"> </font><font color="#ff8400">mensagens</font>
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#000000">58 </font></span>    box.<font color="#9966ff">delete</font><font color="#000000"><strong>(</strong></font>ms<font color="#000000"><strong>)</strong></font>;
 * </font></pre>
 * 
 * <p style="font-size: medium;">
 * Uma funcionalidade especialmente útil de <code>MailBox</code>,
 * é a notificação de novos emails. A cada intervalo de tempo
 * pré configurado, a conta de emails será verificada e, se for 
 * detectada uma nova mensagem, o ouvinte registrado será notificado 
 * através do método <code>messageReceived( MailBoxEvent )</code>.
 * </p>
 * 
 * <pre> <font color="#000000"><span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#000000">59 </font></span>    MailBoxListener listener <font color="#000000"><strong>=</strong></font> <font color="#006699"><strong>new</strong></font> <font color="#9966ff">MailBoxListener</font><font color="#000000"><strong>(</strong></font><font color="#000000"><strong>)</strong></font> <font color="#000000"><strong>{</strong></font>
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#990066">60 </font></span>      
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#000000">61 </font></span>      <font color="#009966"><strong>public</strong></font> <font color="#0099ff"><strong>void</strong></font> <font color="#9966ff">messageReceived</font><font color="#000000"><strong>(</strong></font>MailBoxEvent e<font color="#000000"><strong>)</strong></font> <font color="#000000"><strong>{</strong></font>
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#000000">62 </font></span>        System.out.<font color="#9966ff">println</font><font color="#000000"><strong>(</strong></font><font color="#ff00cc">&quot;</font><font color="#ff00cc"> </font><font color="#ff00cc">[</font><font color="#ff00cc">NEW</font><font color="#ff00cc"> </font><font color="#ff00cc">MESSAGE</font><font color="#ff00cc"> </font><font color="#ff00cc">RECEIVED</font><font color="#ff00cc">!</font><font color="#ff00cc">]</font><font color="#ff00cc">&quot;</font><font color="#000000"><strong>)</strong></font>;
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#000000">63 </font></span>        <font color="#0099ff"><strong>int</strong></font> n <font color="#000000"><strong>=</strong></font> e.<font color="#9966ff">getNewMessagesCount</font><font color="#000000"><strong>(</strong></font><font color="#000000"><strong>)</strong></font>;
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#000000">64 </font></span>        System.out.<font color="#9966ff">println</font><font color="#000000"><strong>(</strong></font><font color="#ff00cc">&quot;</font><font color="#ff00cc"> </font><font color="#ff00cc">[</font><font color="#ff00cc">New</font><font color="#ff00cc"> </font><font color="#ff00cc">messages</font><font color="#ff00cc"> </font><font color="#ff00cc">count</font><font color="#ff00cc">:</font><font color="#ff00cc"> </font><font color="#ff00cc">&quot;</font> <font color="#000000"><strong>+</strong></font> n <font color="#000000"><strong>+</strong></font> <font color="#ff00cc">&quot;</font><font color="#ff00cc">]</font><font color="#ff00cc">&quot;</font><font color="#000000"><strong>)</strong></font>;
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#990066">65 </font></span>      <font color="#000000"><strong>}</strong></font>
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#000000">66 </font></span>      
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#000000">67 </font></span>      <font color="#009966"><strong>public</strong></font> <font color="#0099ff"><strong>void</strong></font> <font color="#9966ff">reloadingMailBox</font><font color="#000000"><strong>(</strong></font>MailBoxEvent e<font color="#000000"><strong>)</strong></font> <font color="#000000"><strong>{</strong></font><font color="#000000"><strong>}</strong></font>
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#000000">68 </font></span>    <font color="#000000"><strong>}</strong></font>;
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#000000">69 </font></span>    
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#990066">70 </font></span>    box.<font color="#9966ff">add</font><font color="#000000"><strong>(</strong></font>listener<font color="#000000"><strong>)</strong></font>;
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#000000">71 </font></span>    
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#000000">72 </font></span>    <font color="#ff8400">//</font><font color="#ff8400">recarregar</font><font color="#ff8400"> </font><font color="#ff8400">as</font><font color="#ff8400"> </font><font color="#ff8400">mensagens</font><font color="#ff8400"> </font><font color="#ff8400">a</font><font color="#ff8400"> </font><font color="#ff8400">cada</font><font color="#ff8400"> </font><font color="#ff8400">15</font><font color="#ff8400"> </font><font color="#ff8400">minutos</font>
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#000000">73 </font></span>    box.<font color="#9966ff">setAutoReload</font><font color="#000000"><strong>(</strong></font><font color="#ff0000">15</font> <font color="#000000"><strong>*</strong></font> <font color="#ff0000">60</font> <font color="#000000"><strong>*</strong></font> <font color="#ff0000">1000</font><font color="#000000"><strong>)</strong></font>;
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#000000">74 </font></span>    
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#990066">75 </font></span>    <font color="#ff8400">//</font><font color="#ff8400">dado</font><font color="#ff8400"> </font><font color="#ff8400">o</font><font color="#ff8400"> </font><font color="#ff8400">longo</font><font color="#ff8400"> </font><font color="#ff8400">intervalo,</font><font color="#ff8400"> </font><font color="#ff8400">&eacute;</font><font color="#ff8400"> </font><font color="#ff8400">interessante</font><font color="#ff8400"> </font>
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#000000">76 </font></span>    <font color="#ff8400">//</font><font color="#ff8400">que</font><font color="#ff8400"> </font><font color="#ff8400">a</font><font color="#ff8400"> </font><font color="#ff8400">conex&atilde;o</font><font color="#ff8400"> </font><font color="#ff8400">seja</font><font color="#ff8400"> </font><font color="#ff8400">fechada</font><font color="#ff8400"> </font><font color="#ff8400">ap&oacute;s</font><font color="#ff8400"> </font><font color="#ff8400">a</font><font color="#ff8400"> </font><font color="#ff8400">verifica&ccedil;&atilde;o,</font>
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#000000">77 </font></span>    <font color="#ff8400">//</font><font color="#ff8400">para</font><font color="#ff8400"> </font><font color="#ff8400">evitar</font><font color="#ff8400"> </font><font color="#ff8400">a</font><font color="#ff8400"> </font><font color="#ff8400">aloca&ccedil;&atilde;o</font><font color="#ff8400"> </font><font color="#ff8400">desnecess&aacute;ria</font><font color="#ff8400"> </font><font color="#ff8400">de</font><font color="#ff8400"> </font><font color="#ff8400">recursos.</font>
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#000000">78 </font></span>    <font color="#ff8400">//</font><font color="#ff8400">N&atilde;o</font><font color="#ff8400"> </font><font color="#ff8400">seria</font><font color="#ff8400"> </font><font color="#ff8400">o</font><font color="#ff8400"> </font><font color="#ff8400">caso</font><font color="#ff8400"> </font><font color="#ff8400">se</font><font color="#ff8400"> </font><font color="#ff8400">o</font><font color="#ff8400"> </font><font color="#ff8400">tempo</font><font color="#ff8400"> </font><font color="#ff8400">fosse</font><font color="#ff8400"> </font><font color="#ff8400">de</font><font color="#ff8400"> </font><font color="#ff8400">15</font><font color="#ff8400"> </font><font color="#ff8400">segundos,</font><font color="#ff8400"> </font><font color="#ff8400">por</font><font color="#ff8400"> </font><font color="#ff8400">exemplo.</font>
 * <span style="background:#dbdbdb; border-right:solid 2px black; margin-right:5px; "><font color="#000000">79 </font></span>    box.<font color="#9966ff">setAutoCloseOnReload</font><font color="#000000"><strong>(</strong></font><font color="#cc00cc">true</font><font color="#000000"><strong>)</strong></font>;
 * </font></pre>
 * 
 * <p style="font-size: medium;">
 * Com isso, encerramos o tutorial de recebimento de emails
 * utilizando <code>SimpleMail</code>. <br>
 * Como vimos, todo o processo é facilitado pela biblioteca,
 * deixando o programador livre para a manipulação e tratamento 
 * das mensagens propriamente ditas.
 * <br>
 * Você pode ler também o tutorial de envio de emails,
 * na documentação da classe <code>SendingMail</code>.
 * </p>
 * 
 * @see us.pserver.smail.tutorial.SendingMail 
 * @see us.pserver.smail.MailServer 
 * @see us.pserver.smail.SMail 
 * @see us.pserver.smail.MailBox 
 * @see us.pserver.smail.Message 
 * @see us.pserver.smail.Messages
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 */
public class ReceivingMail {
  
  /**
   * Executa o tutorial. Se for utilizar, 
   * lebre-se de alterar os dados da conta de email.
   * @param args Não utilizado.
   */
  public static void main(String[] args) {
    MailServer server = new MailServer()
        .setName("gmail_imap") //nome opcional. Útil no caso de um pool de servidores.
        .setServer("imap.gmail.com")
        .setProtocol(MailServer.PROTOCOL_IMAPS)
        .setPort(MailServer.DEFAULT_IMAP_PORT)
        .setAuthentication("juno.rr@gmail.com", "passwd=$0988");
    
    SMail mail = new SMail(server);
    MailBox box = null;
    try {
      box = mail.receive("INBOX");
    } catch(SMailException ex) {
      //Capture e trate o erro adequadamente.
      ex.printStackTrace();
    }
    
    //Recuperar todas as mensagens de "INBOX"
    Messages all = box.getMessages();
    
    //Podemos obter uma lista ou um array com as mensagens
    List<Message> list = all.getList();
    Message[] array = all.toArray();
    
    //Recuperar somente novas mensagens
    Messages news = box.getNewMessages();
    
    //Pesquisar mensagens por assunto
    Messages ms = all.filter(new SubjectFilter("hello"));
    
    //Pesquisar mensagens por remetente
    ms = all.filter(new AddressFilter(
        AddressFilter.FROM, "Duke"));
    
    //Pesquisar mensagens por data de envio
    ms = all.filter(new DateMessageFilter(
        DateMessageFilter.BEFORE, new Date()));
    
    //Pesquisar mensagens com anexos
    ms = all.filter(new HasAttachmentFilter());
    
    //Combinando filtros de pesquisa
    ms = all.filter(new OldMessageFilter())
        .filter(new HasAttachmentFilter())
        .filter(new ContentFilter("fotos das férias"));
    
    //Salvando anexos das mensagens
    Message fotos = ms.first();
    try {
      fotos.getAttachments().first().saveTo(
          new File("/home/juno/fotos/foto1.jpg"));
    } catch(IOException ex) {
      //Capture e trate o erro adequadamente.
      ex.printStackTrace();
    }
    
    //Marcando mensagens novas como lidas
    box.read(news);
    
    //Apagando mensagens
    box.delete(ms);
    
    //Configurando MailBox para notificar a chegada de novos emails
    MailBoxListener listener = new MailBoxListener() {
      public void messageReceived(MailBoxEvent e) {
        System.out.println(" [NEW MESSAGE RECEIVED!]");
        int n = e.getNewMessagesCount();
        System.out.println(" [New messages count: " + n + "]");
      }
      public void reloadingMailBox(MailBoxEvent e) {}
    };
    
    box.add(listener);
    //Recarregar as mensagens a cada 15 minutos
    box.setAutoReload(15 * 60 * 1000);
    //dado o longo intervalo, é interessante 
    //que a conexão seja fechada após a verificação,
    //para não continuar com todos os recursos alocados
    //até a próxima verificação.
    //Não seria o caso se o tempo fosse de 15 segundos, por exmplo.
    box.setAutoCloseOnReload(true);
  }
  
}
