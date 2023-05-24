package client;


import javax.lang.model.element.Element;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.xml.sax.InputSource;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.nio.file.Files;

import java.awt.*;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;

import java.io.File;

import java.io.FileReader;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.apache.http.HttpEntity;

import org.apache.http.HttpResponse;

import org.apache.http.client.HttpClient;

import org.apache.http.client.methods.*;

import org.apache.http.entity.StringEntity;

import org.apache.http.impl.client.HttpClients;

import org.apache.http.util.EntityUtils;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class Client extends JFrame {

    private JTextField urlField;

    private JTextField portField;

    private JTextArea responseArea;


    public Client() {

    	setTitle("Application Client");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Panel principal pour les autres composants de l'interface utilisateur
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        /// Panel pour les paramètres de connexion
     // Panel pour les paramètres de connexion
        JPanel connectionPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        connectionPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        connectionPanel.setBackground(new Color(255, 204, 204)); // Couleur de fond rose clair

        JLabel urlLabel = new JLabel("URL:");
        urlLabel.setFont(new Font("Arial", Font.BOLD, 14));
        urlLabel.setForeground(new Color(255, 102, 102)); // Couleur du texte rose foncé
        JTextField urlField = new JTextField(20);
        urlField.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel portLabel = new JLabel("Port:");
        portLabel.setFont(new Font("Arial", Font.BOLD, 14));
        portLabel.setForeground(new Color(255, 102, 102)); // Couleur du texte rose foncé
        JTextField portField = new JTextField(5);
        portField.setFont(new Font("Arial", Font.PLAIN, 14));

        // Personnalisation des couleurs
        connectionPanel.setBackground(new Color(255, 204, 204)); // Couleur de fond rose clair
        connectionPanel.setBorder(BorderFactory.createLineBorder(new Color(255, 153, 153), 2)); // Bordure rose
        urlLabel.setForeground(new Color(255, 102, 102)); // Couleur du texte rose foncé
        portLabel.setForeground(new Color(255, 102, 102)); // Couleur du texte rose foncé
        urlField.setBackground(new Color(255, 255, 255)); // Couleur de fond blanc
        portField.setBackground(new Color(255, 255, 255)); // Couleur de fond blanc

        connectionPanel.add(urlLabel);
        connectionPanel.add(urlField);
        connectionPanel.add(portLabel);
        connectionPanel.add(portField);



        // Panel pour afficher les informations retournées par le service REST
        JPanel responsePanel = new JPanel(new BorderLayout());
        JLabel responseLabel = new JLabel("Réponse:");
        responseArea = new JTextArea(10, 30);
        responseArea.setEditable(false);
        responseArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(responseArea);
        responsePanel.add(responseLabel, BorderLayout.NORTH);
        responsePanel.add(scrollPane, BorderLayout.CENTER);

        // Panel pour les opérations
        JPanel operationPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        operationPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        



        // Bouton pour afficher les informations d'aide

        JButton helpButton = createStyledButton("Aide");
        helpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String url = urlField.getText();
                int port = Integer.parseInt(portField.getText());
                String serviceURL = "http://" + url + ":" + port + "/help";
                String response = sendGETRequest(serviceURL);
                responseArea.setText(response);
            }
        });


        // Bouton pour afficher la liste des STB (format XML)
        JButton stbListButton = createStyledButton("Liste des STB (XML)");
        stbListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String url = urlField.getText();
                int port = Integer.parseInt(portField.getText());
                String serviceURL = "http://" + url + ":" + port + "/stb23/html/stb_list";
                String response = sendGETRequest(serviceURL);
                responseArea.setText(response);
            }
        });
        

     // Bouton pour afficher la liste des STB (format HTML)
        JButton stbListHTMLButton = createStyledButton("Liste des STB (HTML)");
        stbListHTMLButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String url = urlField.getText();
                int port = Integer.parseInt(portField.getText());
                String serviceURL = "http://" + url + ":" + port + "/stb23/html/stb_list_html";
                String response = sendGETRequest(serviceURL);
                responseArea.setText(response);
            }
        });
     
     
        JButton acceuilButton = new JButton("Acceuil");

        acceuilButton.addActionListener(new ActionListener() {

            @Override

            public void actionPerformed(ActionEvent e) {

                String url = urlField.getText();

                int port = Integer.parseInt(portField.getText());


                String serviceURL = "http://" + url + ":" + port + "/";
          


                String response = sendGETRequest(serviceURL);


                responseArea.setText(response);

            }

        });


     // Bouton pour ajouter une STB
     // Bouton pour ajouter une STB
        JButton addStbButton = new JButton("Ajout d'une STB");

        addStbButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String url = urlField.getText();
                int port = Integer.parseInt(portField.getText());

                // Déclaration de la variable xmlData
                String xmlData = null;

                // Afficher une boîte de dialogue pour permettre à l'utilisateur de choisir la méthode d'ajout
                Object[] options = {"Saisie directe", "Sélectionner un fichier"};
                int choice = JOptionPane.showOptionDialog(Client.this,
                        "Choisissez une méthode d'ajout de STB",
                        "Ajout de STB",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]);

                if (choice == JOptionPane.YES_OPTION) {
                    // Saisie directe par l'utilisateur
                    JTextArea xmlField = new JTextArea();
                    xmlField.setRows(5);

                    JOptionPane optionPane = new JOptionPane(
                            new JScrollPane(xmlField),
                            JOptionPane.PLAIN_MESSAGE,
                            JOptionPane.OK_CANCEL_OPTION
                    );

                    JDialog dialog = optionPane.createDialog(Client.this, "Entrez le flux XML");
                    dialog.setSize(800, 600);
                    dialog.setVisible(true);

                    xmlData = xmlField.getText();
                } else if (choice == JOptionPane.NO_OPTION) {
                    // Sélection d'un fichier XML local
                	JFileChooser fileChooser = new JFileChooser();
                	int result = fileChooser.showOpenDialog(Client.this);
                	if (result == JFileChooser.APPROVE_OPTION) {
                	    File selectedFile = fileChooser.getSelectedFile();
                	    try {
                	        xmlData = new String(Files.readAllBytes(selectedFile.toPath()), StandardCharsets.UTF_8);
                	    } catch (IOException ex) {
                	        ex.printStackTrace();
                	        responseArea.setText("Erreur : Impossible de lire le fichier XML.");
                	        return;
                	    }
                	}
                }

                if (xmlData != null && !xmlData.isEmpty()) {
                    String serviceURL = "http://" + url + ":" + port + "/stb23/insert";

                    // Envoyer la requête POST avec le flux XML
                    String response = sendPOSTRequest(serviceURL, xmlData);

                    // Traiter la réponse du serveur
                    if (response != null) {
                        String status = extractStatusFromXML(response);
                        if (status != null) {
                            if (status.equals("INSERTED")) {
                                String id = extractIDFromXML(response);
                                responseArea.setText("STB ajoutée avec succès. ID : " + id);
                            } else if (status.equals("ERROR")) {
                                String detail = extractDetailFromXML(response);
                                if (detail.equals("DUPLICATED")) {
                                    responseArea.setText("Erreur : La STB existe déjà.");
                                } else if (detail.equals("INVALID")) {
                                    responseArea.setText("Erreur : Flux XML invalide.");
                                }
                            }
                        } else {
                            responseArea.setText("Erreur : Réponse du serveur invalide.");
                        }
                    } else {
                        responseArea.setText("Erreur : Aucune réponse du serveur.");
                    }
                }
            }
        });






        // Bouton pour supprimer une spécification

        JButton deleteSpecButton = new JButton("Suppression d'une spécification");

        deleteSpecButton.addActionListener(new ActionListener() {

            @Override

            public void actionPerformed(ActionEvent e) {

                String url = urlField.getText();

                int port = Integer.parseInt(portField.getText());


                String specId = JOptionPane.showInputDialog(Client.this, "Entrez l'ID de la spécification :");


                if (specId != null && !specId.isEmpty()) {

                    String serviceURL = "http://" + url + ":" + port + "/stb23/delete/" + specId;

                    String response = sendDELETERequest(serviceURL);


                    responseArea.setText(response);

                }

            }

        });


        // Bouton pour envoyer un article via saisie directe

      /*  JButton sendDirectButton = new JButton("Envoyer directement");

        sendDirectButton.addActionListener(new ActionListener() {

            @Override

            public void actionPerformed(ActionEvent e) {

                String url = urlField.getText();

                int port = Integer.parseInt(portField.getText());

                String article = JOptionPane.showInputDialog(ClientApp.this, "Entrez l'article :");


                if (article != null && !article.isEmpty()) {

                    // Effectue la connexion au service REST avec les paramètres fournis par l'utilisateur

                    String serviceURL = "http://" + url + ":" + port + "/api/service";

                    String response = sendArticle(serviceURL, article);


                    // Affiche les informations retournées par le service REST

                    responseArea.setText(response);

                }

            }

        });


        // Bouton pour envoyer un article via sélection d'un fichier XML local

        JButton sendFileButton = new JButton("Envoyer depuis un fichier");

        sendFileButton.addActionListener(new ActionListener() {

            @Override

            public void actionPerformed(ActionEvent e) {

                String url = urlField.getText();

                int port = Integer.parseInt(portField.getText());

                JFileChooser fileChooser = new JFileChooser();

                int result = fileChooser.showOpenDialog(ClientApp.this);


                if (result == JFileChooser.APPROVE_OPTION) {

                    File file = fileChooser.getSelectedFile();

                    String article = readXMLFile(file);


                    if (article != null && !article.isEmpty()) {

                        // Effectue la connexion au service REST avec les paramètres fournis par l'utilisateur

                        String serviceURL = "http://" + url + ":" + port + "/api/service";

                        String response = sendArticle(serviceURL, article);


                        // Affiche les informations retournées par le service REST

                        responseArea.setText(response);

                    }

                }

            }

        }); */
     // Bouton pour afficher le détail d'une spécification (format XML)
        JButton specificationXMLButton = createStyledButton("Détail d'une spécification (XML)");
        specificationXMLButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String url = urlField.getText();
                int port = Integer.parseInt(portField.getText());
                String specId = JOptionPane.showInputDialog(Client.this, "Entrez l'ID de la spécification :");
                if (specId != null && !specId.isEmpty()) {
                    String serviceURL = "http://" + url + ":" + port + "/stb23/html/stb_details?id=" + specId;
                    String response = sendGETRequest(serviceURL);
                    responseArea.setText(response);
                }
            }
        });
        

        // Bouton pour afficher le détail d'une spécification (format HTML)
        JButton specificationHTMLButton = createStyledButton("Détail d'une spécification (HTML)");
        specificationHTMLButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String url = urlField.getText();
                int port = Integer.parseInt(portField.getText());
                String specId = JOptionPane.showInputDialog(Client.this, "Entrez l'ID de la spécification :");
                if (specId != null && !specId.isEmpty()) {
                    String serviceURL = "http://" + url + ":" + port + "/stb23/html/stb_details_html?id=" + specId;
                    String response = sendGETRequest(serviceURL);
                    responseArea.setText(response);
                }
            }
        });
        operationPanel.add(acceuilButton);
        operationPanel.add(helpButton);
        operationPanel.add(stbListHTMLButton);
        operationPanel.add(stbListButton);
        operationPanel.add(specificationHTMLButton);
        operationPanel.add(specificationXMLButton);


       // operationPanel.add(sendDirectButton);

       // operationPanel.add(sendFileButton);

        

        //operationPanel.add(stbListButton);

        operationPanel.add(addStbButton);

        operationPanel.add(deleteSpecButton);
        

        // Panel principal pour les autres composants de l'interface utilisateur

        


        // Ajoutez ici les autres composants nécessaires pour votre application


        // Ajoutez les panels principaux à la fenêtre

        add(connectionPanel, BorderLayout.NORTH);

      //  add(mainPanel, BorderLayout.CENTER);

        add(responsePanel, BorderLayout.CENTER);

        add(operationPanel, BorderLayout.SOUTH);
        setContentPane(mainPanel);
        pack();
        setLocationRelativeTo(null); // Centrer la fenêtre
        setVisible(true);
    }

    private final JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(255, 192, 203)); // Couleur rose clair
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return button;
    }




    
    
 // ...

    private String extractStatusFromXML(String xmlResponse) {
        try {
            Document document = Jsoup.parse(xmlResponse);
            org.jsoup.nodes.Element statusElement = document.selectFirst("status");

            if (statusElement != null) {
                return statusElement.text();
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    private String extractIDFromXML(String xmlResponse) {
        try {
            Document document = Jsoup.parse(xmlResponse);
            org.jsoup.nodes.Element idElement = document.selectFirst("id");

            if (idElement != null) {
                return idElement.text();
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private String generateSTBXML(String stbName) {
        StringBuilder xmlBuilder = new StringBuilder();

        // Construire le flux XML avec les informations de la STB
        xmlBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xmlBuilder.append("<stb23>\n");
        xmlBuilder.append("  <titre>").append(stbName).append("</titre>\n");
        xmlBuilder.append("  <version>1.0</version>\n");
        //xmlBuilder.append("  <date>").append(getCurrentDate()).append("</date>\n");
        xmlBuilder.append("</stb23>");

        return xmlBuilder.toString();
    }
    private String extractDetailFromXML(String xmlResponse) {
        try {
            Document document = Jsoup.parse(xmlResponse);
            org.jsoup.nodes.Element detailElement = document.selectFirst("detail");

            if (detailElement != null) {
                return detailElement.text();
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

  /*  private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date();
        return dateFormat.format(currentDate);
    } */



    // Méthode pour envoyer un article au service REST

    private String sendArticle(String serviceURL, String article) {

        try {

            HttpClient httpClient = HttpClients.createDefault();

            HttpPost httpPost = new HttpPost(serviceURL);

            httpPost.setHeader("Content-Type", "application/xml");

            StringEntity requestEntity = new StringEntity(article);

            httpPost.setEntity(requestEntity);


            HttpResponse httpResponse = httpClient.execute(httpPost);

            HttpEntity responseEntity = httpResponse.getEntity();


            String response = EntityUtils.toString(responseEntity);


            return response;

        } catch (Exception e) {

            e.printStackTrace();

            return "Erreur lors de l'envoi de l'article.";

        }

    }


    // Méthode pour envoyer une requête GET

    private String sendGETRequest(String serviceURL) {
        try {
            HttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(serviceURL);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity responseEntity = httpResponse.getEntity();
            String response = EntityUtils.toString(responseEntity);

            // Utiliser Jsoup pour extraire le contenu affiché
            Document document = Jsoup.parse(response);
            String displayedContent = document.text();

            return displayedContent;
        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur lors de la requête GET.";
        }
    }



    // Méthode pour envoyer une requête POST

    private String sendPOSTRequest(String serviceURL, String body) {

        try {

            HttpClient httpClient = HttpClients.createDefault();

            HttpPost httpPost = new HttpPost(serviceURL);

            httpPost.setHeader("Content-Type", "application/xml");


            StringEntity requestEntity = new StringEntity(body);

            httpPost.setEntity(requestEntity);


            HttpResponse httpResponse = httpClient.execute(httpPost);

            HttpEntity responseEntity = httpResponse.getEntity();


            String response = EntityUtils.toString(responseEntity);


            return response;

        } catch (Exception e) {

            e.printStackTrace();

            return "Erreur lors de la requête POST.";

        }

    }


    // Méthode pour envoyer une requête DELETE

    private String sendDELETERequest(String serviceURL) {

        try {

            HttpClient httpClient = HttpClients.createDefault();

            HttpDelete httpDelete = new HttpDelete(serviceURL);


            HttpResponse httpResponse = httpClient.execute(httpDelete);

            HttpEntity responseEntity = httpResponse.getEntity();


            String response = EntityUtils.toString(responseEntity);


            return response;

        } catch (Exception e) {

            e.printStackTrace();

            return "Erreur lors de la requête DELETE.";

        }

    }
 // Méthode pour récupérer la liste des STB au format XML
    private String getSTBListXML() {
        try {
            String url = urlField.getText();
            int port = Integer.parseInt(portField.getText());
            String serviceURL = "http://" + url + ":" + port + "/stb23/resume/xml";
            return sendGETRequest(serviceURL);
        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur lors de la récupération de la liste des STB (format XML).";
        }
    }

    // Méthode pour récupérer la liste des STB au format HTML
    private String getSTBListHTML() {
        try {
            String url = urlField.getText();
            int port = Integer.parseInt(portField.getText());
            String serviceURL = "http://" + url + ":" + port + "/stb23/resume";
            return sendGETRequest(serviceURL);
        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur lors de la récupération de la liste des STB (format HTML).";
        }
    }

    // Méthode pour récupérer le détail d'une spécification au format XML
    private String getSpecificationXML(String id) {
        try {
            String url = urlField.getText();
            int port = Integer.parseInt(portField.getText());
            String serviceURL = "http://" + url + ":" + port + "/stb23/xml/" + id;
            return sendGETRequest(serviceURL);
        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur lors de la récupération du détail de la spécification (format XML).";
        }
    }

    // Méthode pour récupérer le détail d'une spécification au format HTML
    private String getSpecificationHTML(String id) {
        try {
            String url = urlField.getText();
            int port = Integer.parseInt(portField.getText());
            String serviceURL = "http://" + url + ":" + port + "/stb23/html/stb_details?id=" + id;
            return sendGETRequest(serviceURL);
        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur lors de la récupération du détail de la spécification (format HTML).";
        }
    }

    // Méthode pour lire le contenu d'un fichier XML

    private String readXMLFile(File file) {

        try (FileReader reader = new FileReader(file)) {

            StringBuilder sb = new StringBuilder();

            int character;

            while ((character = reader.read()) != -1) {

                sb.append((char) character);

            }

            return sb.toString();

        } catch (IOException e) {

            e.printStackTrace();

            return null;

        }

    }


    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {

            @Override

            public void run() {

                new ClientApp();

            }

        });

    }

}





