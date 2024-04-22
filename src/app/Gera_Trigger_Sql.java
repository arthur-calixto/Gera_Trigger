package app;

//import java.awt.Desktop;
//import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class Gera_Trigger_Sql {

    public static void gerarTrigger(String tableName, String[] campos, String primaryKey, String filePath) throws IOException {
    	

    	String desktopPath = filePath;
        String nome_arquivo = desktopPath ;
        
        PrintWriter ps = new PrintWriter(nome_arquivo);
        
        ps.println("CREATE OR ALTER TRIGGER TRG_I_U_D_" + tableName + "_LOG");
        ps.println ("ON " + tableName + " FOR  INSERT, UPDATE, DELETE");
        ps.println("AS\r\n"
        		+ "BEGIN");
        ps.println ("DECLARE @P_ACAO VARCHAR(20), @P_CHAVEPK VARCHAR(255), @CODUSU_LOG VARCHAR(4000)");
        
        ps.println("DECLARE");
        
        int i = 0;
        for (String campo : campos) {
        	if (i >0) {
        		ps.print(", ");
        	}
        	
        	ps.println ("@"+campo+"_NEW VARCHAR(4000),"); 
            ps.print ("@"+campo+"_OLD VARCHAR(4000)"); 
            
            i++;
        }
        
        ps.println();
        ps.println();
        
        ps.println("		  BEGIN \r\n"
        		+ "		  SELECT @CODUSU_LOG = CAST(CODUSU AS VARCHAR(100))\r\n"
        		+ "					FROM TSIULG\r\n"
        		+ "					WHERE SPID = @@SPID\r\n"
        		+ "\r\n"
        		+ "		END\r\n");
        
        //
        
        ps.println("DECLARE CUR_INSERTED CURSOR LOCAL FOR\r\n"
        		+ "    SELECT  ");
        
        
        i = 0;
        for (String campo : campos) {
        	if (i >0) {
        		ps.print(", ");
        	}
        	
        	ps.print (" CONVERT(VARCHAR(4000), NEW."+campo+")"); 
            i++;
        }
        ps.println();
        ps.println(" FROM INSERTED NEW");
        
        //
        
        ps.println();
        ps.println("DECLARE CUR_DELETED CURSOR LOCAL FOR\r\n"
        		+ "    SELECT  ");
        i = 0;
        for (String campo : campos) {
        	if (i >0) {
        		ps.print(", ");
        	}
        	
        	ps.print (" CONVERT(VARCHAR(4000), OLD."+campo+")"); 
            i++;
        }
        ps.println();
        ps.println(" FROM DELETED OLD");
        
        
        //
        ps.println();
        ps.println("DECLARE CUR_UPDATED CURSOR LOCAL FOR\r\n"
        		+ "    SELECT  ");
        
        i = 0;
        for (String campo : campos) {
        	if (i >0) {
        		ps.print(", ");
        	}
        	
        	ps.print (" CONVERT(VARCHAR(4000), NEW."+campo+")"); 
            i++;
        }
        ps.println();
        i = 0;
        for (String campo : campos) {
        	ps.print (", CONVERT(VARCHAR(4000), OLD."+campo+")"); 

        }
        ps.println();
        ps.println("FROM INSERTED NEW, DELETED  OLD\r\n"
        		+ "    WHERE NEW."+primaryKey+" = OLD."+primaryKey);
        
        ps.println();
        

        
     // INSERT
        
        ps.println("IF NOT EXISTS(SELECT 1 FROM DELETED) \r\n"
        		+ "    BEGIN\r\n"
        		+ "      OPEN CUR_INSERTED\r\n"
        		+ "      FETCH NEXT FROM CUR_INSERTED INTO");
        
        i = 0;
        for (String campo : campos) {
        	if (i >0) {
        		ps.print(", ");
        	}
        	
        	ps.print ("@"+campo+"_NEW"); 
            i++;
        }
        
        ps.println();
        		ps.println("      WHILE (@@FETCH_STATUS = 0)\r\n"
        		+ "");
        
        ps.println();
        ps.println();
        
	
       
        ps.println("BEGIN");
        ps.println ("SET @P_CHAVEPK = 'PK [' + '"+primaryKey+"' + '= ' + CONVERT(VARCHAR(4000), @"+primaryKey+"_NEW) + ']'");
        
        for (String campo : campos) {
            ps.println ("IF @"+campo+"_NEW IS NOT NULL");   
            ps.println("EXEC  .STP_GRAVATABLOG '"+tableName+"', @P_CHAVEPK,'"+campo+"',@CODUSU_LOG,'INSERT',@"+campo+"_NEW,' '");
            ps.println();
        }
        
		ps.println("FETCH NEXT FROM CUR_INSERTED INTO");
        
		i = 0;
		for (String campo : campos) {
			if (i > 0) {
				ps.print(", ");
			}

			ps.print("@"+campo + "_NEW");
			i++;
		}
		
		ps.println();
		ps.println ("END");
        ps.println ("RETURN"); 
        ps.println ("END");
        
        // DELETE
        
        ps.print("    ELSE IF NOT EXISTS(SELECT 1 FROM INSERTED) \r\n"
        		+ "    BEGIN\r\n"
        		+ "      OPEN CUR_DELETED \r\n");
        
        
        ps.println("FETCH NEXT FROM CUR_DELETED INTO ");
        
        i = 0;
        for (String campo : campos) {
        	if (i >0) {
        		ps.print(", ");
        	}
        	
        	ps.print ("@"+campo+"_OLD"); 
            i++;
        }
        
        ps.println();
        		ps.println("      WHILE (@@FETCH_STATUS = 0)\r\n"
        		+ "");
        
        ps.println();
        ps.println();
        
	
       
        ps.println("BEGIN");
        ps.println ("SET @P_CHAVEPK = 'PK [' + '"+primaryKey+"' + '= ' + CONVERT(VARCHAR(4000), @"+primaryKey+"_NEW) + ']'");
        
        for (String campo : campos) {
            ps.println ("IF @"+campo+"_NEW IS NOT NULL");   
            ps.println("EXEC  .STP_GRAVATABLOG '"+tableName+"', @P_CHAVEPK,'"+campo+"',@CODUSU_LOG,'DELETE',' ',@"+campo+"_OLD");
            ps.println();
        }
        
		ps.println("FETCH NEXT FROM CUR_DELETED INTO");
        
		i = 0;
		for (String campo : campos) {
			if (i > 0) {
				ps.print(", ");
			}

			ps.print("@"+campo + "_OLD");
			i++;
		}
		
		ps.println();
		ps.println ("END");
        ps.println ("RETURN"); 
        ps.println ("END");
        
        // UPDATE
        
        ps.println();
        ps.println();
        
        ps.print("    ELSE \r\n"
        		+ "    BEGIN\r\n"
        		+ "      OPEN CUR_UPDATED");
        
        ps.println();
        
        ps.println("FETCH NEXT FROM CUR_UPDATED INTO ");
        i = 0;
        for (String campo : campos) {
        	if (i >0) {
        		ps.print(", ");
        	}
        	
        	ps.print ("@"+campo+"_NEW"); 
            i++;
        }
        ps.println();
        i = 0;
        for (String campo : campos) {

        	ps.print (", @"+campo+"_OLD"); 

        }
        ps.println();
        
        
        ps.println();
        ps.println(" WHILE (@@FETCH_STATUS = 0)");
        
        ps.println();
        
        ps.println("BEGIN");
        ps.println ("SET @P_CHAVEPK = 'PK [' + '"+primaryKey+"' + '= ' + CONVERT(VARCHAR(4000), @"+primaryKey+"_NEW) + ']'");
        
        ps.println();
        for (String campo : campos) {
            ps.println ("IF ISNULL (@"+campo+"_OLD,0) <> ISNULL (@"+campo+"_NEW,0) ");   
            ps.println("EXEC  .STP_GRAVATABLOG '"+tableName+"', @P_CHAVEPK,'"+campo+"',@CODUSU_LOG,'UPDATE',@"+campo+"_NEW,@"+campo+"_NEW");
            ps.println();
        }
        
        ps.println("FETCH NEXT FROM CUR_UPDATED INTO ");
        i = 0;
        for (String campo : campos) {
        	if (i >0) {
        		ps.print(", ");
        	}
        	
        	ps.print ("@"+campo+"_NEW"); 
            i++;
        }
        ps.println();
        i = 0;
        for (String campo : campos) {

        	ps.print (", @"+campo+"_OLD"); 

        }
        ps.println();
        
		ps.println();
		ps.println ("END");
        ps.println ("RETURN"); 
        ps.println ("END");
        ps.println ("END");
        ps.println ("GO");
        

   
        ps.close();
        
        
    }

    
}
