package app;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;

public class TriggerGenerator extends JFrame implements ActionListener {
    private JTextField tableNameField;
    private JTextField fieldsField;
    private JTextField primaryKeyField;
    private JButton generateButton;

    public TriggerGenerator() {
        setTitle("Trigger Generator");
        setSize(400, 250);
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

        generateButton = new JButton("Gerar Triggers");
        generateButton.setBounds(100, 130, 200, 25);
        generateButton.addActionListener(this);
        panel.add(generateButton);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == generateButton) {
            String tableName = tableNameField.getText();
            String fields = fieldsField.getText();
            String primaryKey = primaryKeyField.getText();

            try {
                // Separando os campos fornecidos
                String[] campos = fields.split(",\\s*");

                // Chamando o método da classe Gerar_Trigger para gerar as triggers
                Gera_Trigger.gerarTrigger(tableName, campos, primaryKey);

                // Exibir mensagem de sucesso
                JOptionPane.showMessageDialog(this, "Triggers geradas com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
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
