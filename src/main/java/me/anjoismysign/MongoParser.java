package me.anjoismysign;

import com.mongodb.MongoClientURI;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import me.anjoismysign.anjo.libraries.PanelLib;
import me.anjoismysign.anjo.swing.AnjoPane;
import me.anjoismysign.anjo.swing.OptionType;
import me.anjoismysign.anjo.swing.components.AnjoTextField;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.*;


public class MongoParser {
    public static void main(String[] args) {
        PanelLib.showMessage("MongoParser",
                "I made this in my spare time. \n" +
                        "You are forced to either listen to my music \n" +
                        "or avoid it, such as lowering volume. \n\n" +
                        "-anjoismysignature");
        Player player = moondream();
        try {
            AnjoPane parser = AnjoPane.build("MongoParser", OptionType.OK,
                    new ImageIcon(MongoParser.class.getResource("/artwork.png"))
                            .getImage().getScaledInstance(256, 256, Image.SCALE_SMOOTH),
                    new AnjoTextField("MongoDB URL"));
            if (parser.didCancel()) {
                player.close();
                return;
            }
            String connection = parser.getTextFieldText(0);
            MongoClientURI uri = new MongoClientURI(connection);

            String host = uri.getHosts().get(0);
            String database = uri.getDatabase();
            String username = uri.getUsername();
            char[] uriPassword = uri.getPassword();
            String password = "";
            if (uriPassword != null)
                password = new String(uriPassword);
            if (password.equals("<password>"))
                password = "";

            AnjoTextField hostField = new AnjoTextField("Host");
            JTextField hostText = hostField.getComponent();
            hostText.setText(host);
            hostText.setEditable(false);
            hostField.addAnjoClickListener(anjoComponent -> {
                String text = hostText.getText();
                copyToClipboard(text);
            });
            AnjoTextField databaseField = new AnjoTextField("Database");
            JTextField databaseText = databaseField.getComponent();
            databaseText.setText(database);
            databaseText.setEditable(false);
            databaseField.addAnjoClickListener(anjoComponent -> {
                String text = databaseText.getText();
                copyToClipboard(text);
            });
            AnjoTextField usernameField = new AnjoTextField("Username");
            JTextField usernameText = usernameField.getComponent();
            usernameText.setText(username);
            usernameText.setEditable(false);
            usernameField.addAnjoClickListener(anjoComponent -> {
                String text = usernameText.getText();
                copyToClipboard(text);
            });
            AnjoTextField passwordField = new AnjoTextField("Password");
            JTextField passwordText = passwordField.getComponent();
            passwordText.setText(password);
            passwordText.setEditable(false);
            passwordField.addAnjoClickListener(anjoComponent -> {
                String text = passwordText.getText();
                copyToClipboard(text);
            });
            AnjoPane result = AnjoPane.build("MongoParser", OptionType.OK,
                    new ImageIcon(MongoParser.class.getResource("/artwork.png"))
                            .getImage().getScaledInstance(256, 256, Image.SCALE_SMOOTH),
                    hostField, databaseField, usernameField, passwordField);
            if (result.didCancel()) {
                player.close();
            }
        }  finally {
            if (player != null) {
                player.close();
                System.exit(0);
            }
        }
    }

    private static void copyToClipboard(String data){
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection selection = new StringSelection(data);
        clipboard.setContents(selection, null);
    }

    private static Player moondream(){
        try {
        InputStream fileInputStream = MongoParser.class.getResourceAsStream("/Moondream.mp3");
        Player player = new Player(fileInputStream);
        new Thread(() -> {
                try {
                    player.play();
                } catch (JavaLayerException e) {
                    e.printStackTrace();
                }
            }).start();
        return player;
        } catch (JavaLayerException e) {
        e.printStackTrace();
        }
        return null;
    }
}