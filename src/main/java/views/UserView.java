package views;

import dao.DataDAO;
import model.Data;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class UserView {
    private String email;

    public UserView(String email){
     this.email = email;
    }
    public void home(){

        while(true){

            String[] options = {
                    "Show Hidden Files",
                    "Hide New File",
                    "Unhide File",
                    "Exit"
            };

            int ch = JOptionPane.showOptionDialog(
                    null,
                    "Welcome " + this.email,
                    "File Hider",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            switch (ch){

                case 0 -> {
                    try{
                        List<Data> files = DataDAO.getAllFiles(this.email);

                        StringBuilder sb = new StringBuilder("ID - File Name\n");

                        for(Data file: files){
                            sb.append(file.getId())
                                    .append(" - ")
                                    .append(file.getFileName())
                                    .append("\n");
                        }

                        JOptionPane.showMessageDialog(null,sb.toString());

                    }catch(SQLException e){
                        e.printStackTrace();
                    }
                }

                case 1 -> {
                    JFileChooser chooser = new JFileChooser();
                    int result = chooser.showOpenDialog(null);

                    if(result == JFileChooser.APPROVE_OPTION){

                        File f = chooser.getSelectedFile();

                        Data file = new Data(0,f.getName(),f.getAbsolutePath(),this.email);

                        try{
                            DataDAO.hideFile(file);
                            JOptionPane.showMessageDialog(null,"File Hidden Successfully");

                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }

                case 2 -> {
                    try{

                        List<Data> files = DataDAO.getAllFiles(this.email);

                        StringBuilder sb = new StringBuilder("ID - File Name\n");

                        for(Data file: files){
                            sb.append(file.getId())
                                    .append(" - ")
                                    .append(file.getFileName())
                                    .append("\n");
                        }

                        String idStr = JOptionPane.showInputDialog(sb + "\nEnter File ID to Unhide:");

                        int id = Integer.parseInt(idStr);

                        DataDAO.unhide(id);

                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }

                case 3 -> System.exit(0);
            }
        }
    }
}
