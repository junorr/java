if (typeof $ === 'undefined') {
    throw "! Error [micro-core.js]-> JQuery (>= 1.10) Required";
}

$.micro = function() {
  return new Micro();
};



/**
 * Biblioteca API para comunicação com os micro serviços. Esta biblioteca encapsula
 * toda a complexidade das requisições feitas ao servidor.
 * Depende da biblioteca JQuery 1.10 ou maior.
 */
function Micro() {

    var ref = this;
    ref.async = true;
    ref.streamLength = false;


    /**
     * Busca um cookie que esteja salvo no document da página.
     * @param  {String} cname Nome do cookie para pesquisa
     * @return {String}       Valor do cookie que está salvo ou vazio caso não encontre o cookie
     */
    ref.getCookie = function(cname) {
        var name = cname + "=";
        var ca = document.cookie.split(';');
        for (var i = 0; i < ca.length; i++) {
            var c = ca[i];
            while (c.charAt(0) === ' ') {
                c = c.substring(1);
            }
            if (c.indexOf(name) === 0) {
                return c.substring(name.length, c.length);
            }
        }
        return "";
    };
    

    /**
     * Verifica se o usuário está autenticado, lança uma excessão caso não esteja.
     */
    var checkAuth = function() {
        if (!ref.isAuth()) {
            throw "! Error [micro-core.js::checkAuth]-> Not Authenticated on Micro Service";
        }
    };
    

    /**
     * Retorna o usuário que foi autenticado pelo microserviço.
     * @return {Object} Objeto com todas as informações do usuário.
     */
    ref.user = function() {
        checkAuth();
        return ref.jwt().user;
    };
    

    /**
     * Valida se o usuário está autenticado pelo microserviço. Verifica se o token
     * de autenticação do microserviço e o usuário estão definidos dentro do sessionStorage
     * @return {Boolean} true | false Caso token e usuário estejam definidos | Caso contrário
     */
    ref.isAuth = function() {
        return sessionStorage.jwt !== "false" 
            && sessionStorage.jwt !== false
            && (typeof sessionStorage.jwt !== 'undefined') 
            && (typeof ref.jwt().user !== 'undefined');
    };
    
    
    ref.isExpired = function() {
      if(!ref.isAuth()) return true;
      var epoch = new Date().getTime() / 1000;
      var pld = ref.jwt().payload;
      return epoch >= (pld.cre + pld.exp);
    };
    

    /**
     * Verifica se o usuário está autenticado a uma URL específica do microserviço.
     * @param  {String}  url URL do microserviço
     * @return {Boolean} true | false   Caso a URL esteja contida no payload do JWT |
     *                                  Caso contrário ou caso o usuário não esteja autenticado
     */
    ref.isAuthUrl = function(url) {
        if (!ref.isAuth()) return false;
        aurl = ref.jwt().payload.url;
        if (typeof aurl === "string") {
            return url === aurl;
        } else {
            for (var i = 0; i < aurl.length; i++) {
                if (url === aurl[i] ||
                    aurl[i].startsWith(url)) {
                    return true;
                }
            }
            return false;
        }
    };
    

    /**
     * Transforma uma URL qualquer na URL para autenticação do microserviço.
     * @param  {String} url URL requisitada
     * @return {String}     URL de autenticação
     */
    var toAuthUrl = function(url) {
        if (!url) {
            throw "! Error [micro-core.js::toAuthUrl]-> Bad URL: " + url;
        }
        var ini = url.indexOf("://") + 3;
        var bar = url.indexOf("/", ini) + 1;
        return url.substring(0, bar) + "jwt";
    };
    

    /**
     * Busca os dados do JWT armazenado no sessionStorage.
     * @return {Object} Objeto JWT com todos os dados
     */
    ref.jwt = function() {
        try {
          return JSON.parse(sessionStorage.jwt);
        } catch(e) {
          return false;
        }
    };
    
    
    ref.setAsync = function(async) {
      if("boolean" === typeof async) {
        ref.async = async;
      }
      return this;
    };
    

    /**
     * Executa uma requisição ajax com o método GET
     * @param  {String} url       URL da requisição
     * @param  {Object} data      Objeto com os dados para requisição
     * @param  {Function} onsuccess Função a ser executada no caso de sucesso
     */
    ref.get = function(url, data, onsuccess, onerror) {
        ref.ajax(url, "GET", data, onsuccess, onerror);
    };
    

    /**
     * Executa uma requisição ajax com o método POST
     * @param  {String} url       URL da requisição
     * @param  {Object} data      Objeto com os dados para requisição
     * @param  {Function} onsuccess Função a ser executada no caso de sucesso
     */
    ref.post = function(url, data, onsuccess, onerror) {
        ref.ajax(url, "POST", data, onsuccess, onerror);
    };
    

    /**
     * Executa uma requisição ajax com o método PUT
     * @param  {String} url       URL da requisição
     * @param  {Object} data      Objeto com os dados para requisição
     * @param  {Function} onsuccess Função a ser executada no caso de sucesso
     */
    ref.put = function(url, data, onsuccess, onerror) {
        ref.ajax(url, "PUT", data, onsuccess, onerror);
    };
    

    /**
     * Executa uma requisição ajax com o método DELETE
     * @param  {String} url       URL da requisição
     * @param  {Object} data      Objeto com os dados para requisição
     * @param  {Function} onsuccess Função a ser executada no caso de sucesso
     */
    ref.delete = function(url, data, onsuccess, onerror) {
        ref.ajax(url, "DELETE", data, onsuccess, onerror);
    };
    

    /**
     * Reautentica o acesso a uma URL.
     * Este método reautentica não só a URL que foi passada por parametro como também
     * todas as outras que já tinham sido préviamente autenticadas.
     * @param  {String} url URL para ser autenticada
     */
    var reauth = function(url) {
        var aurl = [];
        aurl.push(url);
        if (ref.isAuth()) {
            if (typeof ref.jwt().url === "string") {
                aurl.push(ref.jwt().payload.url);
            } else {
                urls = ref.jwt().url;
                for (var i = 0; i < urls.length; i++) {
                    aurl.push(urls[i]);
                }
            }
        }
        ref.auth(toAuthUrl(url), aurl);
    };
    

    /**
     * Executa uma requisição ajax para um microserviço.
     * @param  {String} url       URL da requisição
     * @param  {String} meth      Método da requisição
     * @param  {Object} data      Objeto com os dados para requisição
     * @param  {Function} onsuccess Função a ser executada no caso de sucesso
     */
    ref.ajax = function(url, meth, data, onsuccess, onerror) {
        if (!url) {
          throw "! Error [micro-core.js::ajax]-> Bad URL: " + url;
        }
        if(!meth) {
          throw "! Error [micro-core.js::ajax]-> Bad Method: " + meth;
        }
        if(!ref.isAuth() || ref.isExpired() || !ref.isAuthUrl(url)) {
          reauth(url);
          checkAuth();
        }
        if(typeof data === "function") {
          if(typeof onsuccess === "function") {
            onerror = onsuccess;
          }
          onsuccess = data;
        }
        $.ajax({
            url: url,
            async: ref.async,
            method: meth,
            headers: {
                "Authorization": "Bearer " + ref.jwt().token
            },
            crossDomain: true,
            contentType: "application/json",
            data: (meth !== "GET" ? JSON.stringify(data) : data),
            //dataType: "text",
            global: false,
            complete: function(xhr, status) {
                var data;
                try {
                  data = JSON.parse(xhr.responseText);
                } catch(e) {
                  data = xhr.responseText;
                }
                if(xhr.status === 200) {
                  if(typeof onsuccess === 'function') {
                    onsuccess(data, xhr, status);
                  }
                }
                else if(typeof onerror === 'function') {
                  onerror(data, xhr, status);
                }
                else {
                  throw "! Error [micro-core.js::ajax]-> Request Failed (" + status + ") - " + xhr.status + " " + xhr.statusText;
                }
            }
        });
    };
    

    /**
     * Executa uma requisição ajax GET para um microserviço, fazendo 
     * streaming dos dados conforme chegam para a função/elemento ondata..
     * @param {String} url         URL da requisição.
     * @param {Function} ondata    Função a ser executada no recebimento de dados parciais.
     * @param {Function} onsuccess Função a ser executada no caso de sucesso.
     * @param {Function} onerror   Função a ser executada no caso de erro.
     */
    ref.stream = function(url, ondata, onsuccess, onerror) {
        if (!url) {
          throw "! Error [micro-core.js::ajax]-> Bad URL: " + url;
        }
        if(!ref.isAuth() || ref.isExpired() || !ref.isAuthUrl(url)) {
          reauth(url);
          checkAuth();
        }
        $.ajax({
            url: url,
            async: ref.async,
            method: "GET",
            headers: {
                "Authorization": "Bearer " + ref.jwt().token
            },
            crossDomain: true,
            processData: false,
            global: false,
            xhrFields: {
              onprogress: function(e) {
                var progressResponse;
                var response = e.currentTarget.response;
                if(ref.streamLength === false){
                  progressResponse = response;
                  ref.streamLength = response.length;
                }
                else {
                  progressResponse = response.substring(ref.streamLength);
                  ref.streamLength = response.length;
                }
                var type = typeof ondata;
                if(type === "function") {
                  ondata(progressResponse);
                }
                else if(type === "string"
                    || type === "object") {
                  var elt = $(ondata)[0];
                  $(elt).append(progressResponse);
                  elt.scrollTop = elt.scrollHeight - elt.clientHeight;
                }
              }
            },
            complete: function(xhr, status) {
                var data;
                try {
                  data = JSON.parse(xhr.responseText);
                } catch(e) {
                  data = xhr.responseText;
                }
                if(xhr.status === 200) {
                  if(typeof onsuccess === 'function') {
                    onsuccess(data, xhr, status);
                  }
                }
                else if(typeof onerror === 'function') {
                  onerror(data, xhr, status);
                }
                else {
                  throw "! Error [micro-core.js::ajax]-> Request Failed (" + status + ") - " + xhr.status + " " + xhr.statusText;
                }
            }
        });
    };
    

    /**
     * Simula uma autenticação de URL de um usuário específico.
     * @param  {String} url       URL para autenticar
     * @param  {String} key       Chave do usuário
     * @param  {Function} onsuccess Função a ser executada no caso de sucesso
     */
    ref.simulate = function(url, key, onsuccess, onerror) {
        if (!url) {
            throw "! Error [micro-core.js::simulate]-> Bad URL: " + url;
        }
        if (!key) {
            throw "! Error [micro-core.js::simulate]-> Bad User Key: " + key;
        }
        url = (url.charAt(url.length - 1) === '/' ?
            url : url + "/") + key;
        ref.post(url, ref.jwt().url, function(data, status, xhr) {
            setJWT(data);
            if(typeof onsuccess === "function") {
              onsuccess(data, status, xhr);
            }
        }, onerror);
    };
    

    /**
     * Salva as informações do token de autenticação no sessionStorage do browser
     * @param {String} jwt Token de autenticação codificado em base64
     */
    var setJWT = function(jwt) {
        if (!jwt) {
            throw "! Error [micro-core.js::setJWT]-> Bad JWT: " + jwt;
        }
        sessionStorage.jwt = false;
        var pt1 = jwt.indexOf(".") + 1;
        var pt2 = jwt.indexOf(".", pt1);
        var hdr = JSON.parse(
            atob(jwt.substring(0, pt1 -1))
        );
        var pld = JSON.parse(
            atob(jwt.substring(pt1, pt2))
        );
        pld.cre = hdr.cre;
        sessionStorage.jwt = JSON.stringify({
            token: jwt,
            user: pld.user,
            url: pld.url,
            payload: pld
        });
    };


    /**
     * Realiza a autenticação das URL que o usuário deseja acessar.
     * @param  {String} url        URL do microserviço de autenticação
     * @param  {[String]} urlArray   URLs que irão ser autenticadas
     * @param  {Function} oncomplete Função para ser executada quando a requisição
     *                               for completada
     */
    ref.auth = function(url, urlArray, oncomplete) {
        if (!url) {
            throw "! Error [micro-core.js::auth]-> Bad URL: " + url;
        }
        if (!urlArray) {
            throw "! Error [micro-core.js::auth]-> Bad URL Array: " + urlArray;
        }
        var bbssotk = ref.getCookie("BBSSOToken");
        var ssoacr = ref.getCookie("ssoacr");
        if (bbssotk === "") {
            throw "! Error [micro-core.js::auth]-> Bad Null BBSSOToken. User Must be Authenticated";
        }
        $.ajax({
            async: false,
            url: url,
            method: "POST",
            headers: {
                "X-BBSSOToken": bbssotk,
                "X-ssoacr": ssoacr
            },
            crossDomain: true,
            contentType: "application/json",
            data: JSON.stringify(urlArray),
            global: false,
            error: function(xhr, status) {
                sessionStorage.jwt = false;
                setTimeout(function() {
                  window.location = "http://login.intranet.bb.com.br/distAuth/UI/Login?goto=" + encodeURI(window.location);
                }, 4000);
                throw "! Error [micro-core.js::auth]-> Authentication Failed (" + status + ") - " + xhr.status + " " + xhr.statusText;
            },
            success: function(data, status, xhr) {
                setJWT(data);
            },
            complete: oncomplete
        });
    };
    
    
    ref.tag = function(elt) {
      if(!elt || typeof elt === "undefined") {
        return null;
      }
      return $(elt).prop("tagName");
    };
    
    
    ref.download = function(link, opts) {
      if(!opts || typeof opts === "undefined") {
        throw "! Error [micro-core.js::download]-> Bad Null Options: "+ JSON.stringify(opts);
      }
      if(!opts.url || typeof opts.url === "undefined") {
        throw "! Error [micro-core.js::download]-> Bad Null URL (opts.url)";
      }
      var a = $(link)[0];
      var atag = ref.tag(a);
      if(atag.toUpperCase() !== "A") {
        a = document.createElement("a");
        $(link).append(a);
      }
      ref.post(opts.url, opts, function(data) {
        var enc = encodeURI(data);
        var url = opts.url + (opts.url.endsWith("/") ? enc : "/" + enc);
        $(a).attr("href", url);
      });
    };

}
