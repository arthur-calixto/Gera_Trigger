package app;

import java.io.IOException;
import java.io.PrintWriter;

public class Gera_Trigger {

    public static void gerarTrigger(String tableName, String[] campos, String primaryKey, String filePath) throws IOException {
    	

    	String desktopPath = filePath;
        String nome_arquivo = desktopPath ;
        
        PrintWriter ps = new PrintWriter(nome_arquivo);
        
        ps.println("CREATE OR REPLACE TRIGGER TRG_I_U_D_" + tableName + "_ORC_LOG");
        ps.println ("BEFORE  INSERT OR UPDATE OR DELETE  ON " + tableName + " FOR EACH ROW");
        ps.println ("DECLARE P_ACAO VARCHAR2(10), P_CHAVEPK VARCHAR(400), @CODUSU_LOG VARCHAR(4000)");
        ps.println("DECLARE ");
        for (String campo : campos) {
        	ps.println("@"+campo+"_NEW VARCHAR(4000), \r\n"
        			+ "@"+campo+"_OLD VARCHAR(4000)");
        	
        }
        
        
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
    
}