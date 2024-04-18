package app;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TriggerGenerator extends JFrame implements ActionListener {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private JTextField tableNameField;
    private JTextField fieldsField;
    private JTextField primaryKeyField;
    private JButton generateButton;
    private JButton selectDirectoryButton;
    private File selectedDirectory;
    private JCheckBox oracleCheckBox;
    private JCheckBox sqlServerCheckBox;

    public TriggerGenerator() {
        setTitle("Trigger Generator");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        getContentPane().add(panel);

        panel.setLayout(null);

        JLabel tableNameLabel = new JLabel("Nome da Tabela:");
        tableNameLabel.setBounds(10, 20, 150, 25);
        panel.add(tableNameLabel);

        tableNameField = new JTextField(20);
        tableNameField.setBounds(160, 20, 200, 25);
        panel.add(tableNameField);

        JLabel fieldsLabel = new JLabel("Campos:");
        fieldsLabel.setBounds(10, 50, 150, 25);
        panel.add(fieldsLabel);

        fieldsField = new JTextField(20);
        fieldsField.setBounds(160, 50, 200, 25);
        panel.add(fieldsField);

        JLabel primaryKeyLabel = new JLabel("Chave Primária (PK):");
        primaryKeyLabel.setBounds(10, 80, 150, 25);
        panel.add(primaryKeyLabel);

        primaryKeyField = new JTextField(20);
        primaryKeyField.setBounds(160, 80, 200, 25);
        panel.add(primaryKeyField);
        
        JLabel label = new JLabel("Selecione o Banco de Dados:");
        label.setBounds(10, 110, 250, 25);
        panel.add(label);

        oracleCheckBox = new JCheckBox("Oracle");
        oracleCheckBox.setBounds(180, 110, 100, 25);
        panel.add(oracleCheckBox);


        sqlServerCheckBox = new JCheckBox("SQL Server");
        sqlServerCheckBox.setBounds(280, 110, 100, 25);
        panel.add(sqlServerCheckBox);


        selectDirectoryButton = new JButton("Selecionar Diretório");
        selectDirectoryButton.setBounds(100, 160, 200, 25);
        selectDirectoryButton.addActionListener(this);
        panel.add(selectDirectoryButton);

        generateButton = new JButton("Gerar Triggers");
        generateButton.setBounds(100, 190, 200, 25);
        generateButton.addActionListener(this);
        panel.add(generateButton);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == selectDirectoryButton) {
          
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Selecionar Diretório de Salvamento");
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int result = fileChooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                selectedDirectory = fileChooser.getSelectedFile();
            }
        } else if (e.getSource() == generateButton) {
            String tableName = tableNameField.getText();
            String fields = fieldsField.getText();
            String primaryKey = primaryKeyField.getText();

            try {
                String[] campos = fields.split(",\\s*");

                // Verifica se o usuário selecionou um diretório antes de gerar as triggers
                if (selectedDirectory != null && (sqlServerCheckBox.isSelected()  || oracleCheckBox.isSelected())) {
                                      
                    // Chamando o método da classe Gerar_Trigger para gerar as trigger - Oracle
                    if (oracleCheckBox.isSelected()) {
                    String fileName = tableName + "_ORC_LOG.sql";
                    String filePath = selectedDirectory.getAbsolutePath() + File.separator + fileName;
                    Gera_Trigger.gerarTrigger(tableName, campos, primaryKey, filePath);
                    }
                    
                    // Chamando o método da classe Gerar_Trigger_Sql para gerar as trigger - Sql Server
                    if (sqlServerCheckBox.isSelected()) {
                    String fileName = tableName + "_SQL_LOG.sql";
                    String filePath = selectedDirectory.getAbsolutePath() + File.separator + fileName;	
                   Gera_Trigger_Sql.gerarTrigger(tableName, campos, primaryKey, filePath);
                    }

                    // Exibir mensagem de sucesso
                    JOptionPane.showMessageDialog(this, "Triggers geradas com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Por favor, selecione um diretório e o banco antes de gerar as triggers.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao gerar triggers.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        new TriggerGenerator();
    }
}
