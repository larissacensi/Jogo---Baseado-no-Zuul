package fatpug;

/**
 * @author Larissa Censi
 */

public class Game {
    private final int PESO_MAX = 1000;
    private Parser parser;
    private Player player;
    private Item item;
        
    /**
     * Cria o jogo e inicializa o mapa interno.
     */
    
    public Game() 
    {
        player = new Player(PESO_MAX);
        player.setCurrentWeight(0);
        createRooms();
        parser = new Parser();
        
    }

    /**
     * Cria todas as salas e liga suas saídas.
     */
    
    private void createRooms()
    {
        Room outside, cantina, mercado, lab, office, restaurante;
        Item bolo, biscoitoMagico, Petit_gateau, sorvete, abobora, alface;
      
        // cria os itens
        bolo = new Item("bolo"," com uma calda de chocolate maravilhosa", 150);
        biscoitoMagico = new Item("biscoito_magico"," super poderoso", 50);
        Petit_gateau = new Item("Petit_gateau"," fresquinho e gostoso", 250);
        sorvete = new Item("sorvete"," de ovomaltine", 300);
        abobora = new Item("abobora", " que não te faz bem", 500); 
        alface = new Item("alface", " que tem um gosto horrivel", 250);
        
        // create the rooms
        outside = new Room("fora da entrada principal da universidade", abobora);
        cantina = new Room("em uma cantina", sorvete);
        mercado = new Room("no mercado do campus", Petit_gateau);
        lab = new Room("em um laboratório de informática", biscoitoMagico);
        office = new Room("na sala dos professores", bolo);
        restaurante = new Room("no restaurante do campus", alface);
        
        // initialise room exits
        outside.setExit("cantina", cantina);
        outside.setExit("laboratorio", lab);
        outside.setExit("mercado", mercado); 
        cantina.setExit("inicio", outside);  
        mercado.setExit("inicio", outside);    
        lab.setExit("inicio", outside);
        lab.setExit("office", office);
        lab.setExit("restaurante", restaurante); 
        restaurante.setExit("laboratorio", lab);
        office.setExit("laboratorio", lab);

        player.setCurrentRoom(outside);
    }

    /**
     *  A rotina de jogo principal. Faz um loop até o fim do jogo.
     */
    
    public void play() 
    {            
        printWelcome();

        // Entra o loop principal. Aqui lemos comandos repetidamente e 
        // os executamos até que o jogo termime.
                
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Obrigado por jogar.  Adeus.");
    }

    /**
     * Imprime a mensagem de boas vindas ao usuário.
     */
    
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Bem-vindo ao jogo do Fat PUG!");
        System.out.println("Fat PUG é um jogo de aventura, incrivelmente engordativo.");
        System.out.println("Digite 'ajuda' se você precisar de ajuda.");
        System.out.println();
        player.printLocationInfo();
        
    }
  

    /**
     * Dado um comando, processa (ou seja: executa) o comando.
     * @param command O comando a ser processado.
     * @return true Se o comando finaliza o jogo, senão, falso.
     */
    
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("Não sei o que você quer dizer...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("ajuda"))
            printHelp();
        else if (commandWord.equals("ir_para"))
            goRoom(command);
        else if (commandWord.equals("sair"))
            wantToQuit = quit(command);
        else if (commandWord.equals("examinar"))
            look();
        else if (commandWord.equals("comer"))
            eat(command);
        else if (commandWord.equals("voltar"))
            returnRoom(command);
        else if (commandWord.equals("pegar"))
            pegar(command);
        else if (commandWord.equals("soltar"))
            soltar(command);
        else if (commandWord.equals("itens"))
            imprimir();
        return wantToQuit;
    }
    
    public void pegar(Command command) {
         if(!command.hasSecondWord()) {
            System.out.println("Pegar o quê?");
        }else{
            if((player.getCurrentRoom().getItem()) != null){
                 if(command.getSecondWord().equals(player.getCurrentRoom().getItem().getNome())){
                    if((player.getCurrentRoom().getItem().canBePickedUp(player))){
                        System.out.println("Essa comida esta guardada");
                        player.addItem(player.getCurrentRoom().getItem());
                        player.getCurrentRoom().setItem(null);
                    }
                    else{
                        System.out.println("Essa comida é muito pesada, você não consegue transportar");
                    }
                 }else{
                    System.out.println("Não existe esse item");
                }        
            }else{
                System.out.println("Não tem nenhum item nesta sala"); 
        }
    }
}

    public void soltar(Command command) {
        int control = 0;
        if(player.getCurrentRoom().getItem() == null){
            if(!command.hasSecondWord()) {
                System.out.println("soltar o quê?");
            }else{
                if(!player.getItensPlayer().isEmpty()){
                    for(Item item: player.getItensPlayer()){
                        if( item.getNome().equals(command.getSecondWord())){
                            System.out.println("Você soltou a comida");
                            player.decrementCurrentWeight(item.getWeight());
                            player.getCurrentRoom().setItem(item);
                        }else{
                            control++;
                            //se o control for igual ao tamanho da lista
                            //quer dizer que ele passou por todos os elementos
                            // e não encontrou o digitado pelo user
                            if(control == player.getItensPlayer().size()){
                                System.out.println("Esse item não existe");
                            }
                        }
                    }
                }else{
                    System.out.println("Você não tem nenhum item para soltar");
                }
            }
        }else{
            System.out.println("Você não pode soltar essa comida aqui");
        }
        
    }
        
    public void eat(Command command) {
        int control = 0;
        if((player.getCurrentRoom().getItem()) != null){
            if(!command.hasSecondWord()) {
                System.out.println("Comer o quê?");
            }else{
                if(command.getSecondWord().equals(player.getCurrentRoom().getItem().getNome())){   
                    if("biscoito_magico".equals(player.getCurrentRoom().getItem().getNome())){
                        player.setCurrentWeight(player.getCurrentWeight() + 500);
                        System.out.println("Você consegue carregar mais 500 quilos");
                        System.out.println("Você comeu e ainda esta com fome.Pegue mais comida");
                        player.getCurrentRoom().setItem(null);
                    }else{
                        System.out.println("Você comeu e ainda esta com fome.Pegue mais comida");
                        player.getCurrentRoom().setItem(null);
                    }
                }else{
                        control++;
                        //se o control for igual ao tamanho da lista
                        //quer dizer que ele passou por todos os elementos
                        // e não encontrou o digitado pelo user
                        if(control == player.getItensPlayer().size()){
                            System.out.println("Esse item não existe");
                        }
                }
            }
        }else{
            System.out.println("Não tem nenhum item nesta sala"); 
        }
    }
    
       public void imprimir() {
           System.out.println("Você esta carregando esses itens: ");
            for(Item item: player.getItensPlayer()){
                System.out.println(item.getLongDescription());
            }
    }
        

    // implementations of user commands:

    /**
     * Imprime informações de ajuda.
     * Aqui imprimimos ua mensagem estúpida e listamos os comandos
     * disponíveis.
     */
    private void printHelp() 
    {
        System.out.println("Você está perdido. Você está alone. Você caminha");
        System.out.println("pela universidade.");
        System.out.println();
        System.out.println("Seus comandos são:");
        System.out.println("   " + parser.getCommandList());
    }
    
    private void returnRoom(Command command) //player
    {
        if (command.hasSecondWord()) {
            System.out.println("Voltar o quê?");
        } else {
            player.returnRoom(command);
        }         
    }
    
    /** 
     * Tenta ir para uma direção. Se há uma saída, entra na
     * nova sala, senão imprime uma mensagem de erro.
     */
    
    private void goRoom(Command command)
    {
        if(!command.hasSecondWord()) {
            // se não há segunda palavra, não sabemos onde ir...
            System.out.println("Ir para onde?");
            return;
        }
        String direction = command.getSecondWord();
        player.goRoom(direction); 
    }
    

    /** 
     * "Sair" foi digitado. Verifica o resto do comando para saber
     * se o usuário quer realmente sair do jogo.
     * @return true, se este comando sair do jogo, falso caso contrário.
     */
    
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Sair de do quê?");
            return false;
        }
        else {
            return true;  // significa que queremos sair
        }
    }

    private void look() {
       player.printLocationInfo();
    }
}
