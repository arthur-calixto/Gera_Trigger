package app;

//import java.awt.Desktop;
//import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class Gera_Trigger {

    public static void gerarTrigger(String tableName, String[] campos, String primaryKey, String filePath) throws IOException {
    	
    	// Obtendo o diretório da área de trabalho do usuário
        //String desktopPath = System.getProperty("user.home") + "\\Desktop\\";
    	String desktopPath = filePath;
        
        //String desktopPath = "C:\\TRIGGER\\";
        String nome_arquivo = desktopPath ;
        
        PrintWriter ps = new PrintWriter(nome_arquivo);
        
        ps.println("CREATE OR REPLACE TRIGGER TRG_I_U_D_" + tableName + "_LOG");
        ps.println ("BEFORE  INSERT OR UPDATE OR DELETE  ON " + tableName + " FOR EACH ROW");
        ps.println ("DECLARE P_ACAO VARCHAR2(10); P_CHAVEPK VARCHAR(400);");
        ps.println("BEGIN");
        ps.println ("P_CHAVEPK := 'PK [' || '"+ primaryKey +"' || '=' || :NEW."+primaryKey+" || ']';");

        // INSERT
        ps.println ("IF INSERTING THEN"); 
        for (String campo : campos) {
            ps.println ("IF :NEW."+campo+" IS NOT NULL THEN");   
            ps.println("  STP_GRAVATABLOG('"+tableName+"', P_CHAVEPK,'"+campo+"',:NEW.CODUSU,'INSERT',:NEW."+campo+",' ');");
            ps.println("END IF;");
        }
        ps.println ("RETURN;"); 
        // FIM DO LAÇO  INSERT
        
        // DELETE
        
        ps.println ("ELSIF DELETING THEN"); 
        for (String campo : campos) {
            ps.println ("IF :OLD."+campo+" IS NOT NULL THEN");   
            ps.println("  STP_GRAVATABLOG('"+tableName+"', P_CHAVEPK,'"+campo+"',:OLD.CODUSU,'DELETE',:OLD."+campo+",' ');");
            ps.println("END IF;");
        }
        ps.println ("RETURN;"); 
        // FIM DO LAÇO DELETE
        
        
        // UPDATE
        
        ps.println ("ELSIF UPDATING THEN");
        for (String campo : campos) {
            ps.println ("IF NVL(:OLD."+campo+", 0) <> NVL(:NEW."+campo+", 0) THEN"); 
            ps.println ("STP_GRAVATABLOG('"+tableName+"', P_CHAVEPK,'"+campo+"', :NEW."+campo+", 'UPDATE',:NEW."+campo+",:OLD."+campo+");");
            ps.println("END IF;");
        }
        ps.println ("RETURN;"); 
        
        ps.println ("END IF;"); 
        ps.println ("END;"); 
        
        //abrir_arquivo (nome_arquivo);
        ps.close();
        
        
    }
    /*
    public static void abrir_arquivo (String file_path) throws IOException {
    	File file = new File (file_path);
        Desktop desktop = Desktop.getDesktop();
         desktop.open(file);
         }
         */
    
}
