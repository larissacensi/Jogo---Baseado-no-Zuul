package fatpug;

/**
 *
 * @author Larissa Censi
 */

public class CommandWords {
    // um array constante que contém todos os comandos válidos
    private static final String[] VALID_COMMANDS = {
        "ir_para", "sair", "ajuda", "examinar", "comer", "voltar", "pegar", "soltar", "itens"
    };

    /**
     * Construtor - inicializa os comandos
     */
    public CommandWords()
    {
        // nada a fazer no momento...
    }
    
    public String getCommandList()
    {
        return String.join(" ", VALID_COMMANDS);
    }

    /**
     * Checa se uma string é uma palavra válida. 
     * @param aString uma string 
     * @return true se a string é um comando válido, falso se não
     */
    public boolean isCommand(String aString)
    {
        for (String command : VALID_COMMANDS) {
            if (command.equals(aString)) {
                return true;
            }
        }
        // se chegamos aqui, a string não foi encontrada nos comandos
        return false;
    }
}
