













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
    MailBoxListener listener = new MailBoxListener() {
    	
      public void messageReceived(MailBoxEvent e) {
        System.out.println(" [NEW MESSAGE RECEIVED!]");
        int n = e.getNewMessagesCount();
        System.out.println(" [New messages count: " + n + "]");
      }
      
      public void reloadingMailBox(MailBoxEvent e) {}
    };
    
    box.add(listener);
    
    //recarregar as mensagens a cada 15 minutos
    box.setAutoReload(15 * 60 * 1000);
    
    //dado o longo intervalo, é interessante 
    //que a conexão seja fechada após a verificação,
    //para evitar a alocação desnecessária de recursos.
    //Não seria o caso se o tempo fosse de 15 segundos, por exemplo.
    box.setAutoCloseOnReload(true);


