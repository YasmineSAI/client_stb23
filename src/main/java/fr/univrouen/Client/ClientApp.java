package fr.univrouen.Client;



//import javax.lang.model.element.Element;
import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.xml.sax.InputSource;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import org.jsoup.select.Elements;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.BufferedReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;

import java.awt.*;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;

import java.io.File;

import java.io.FileReader;

import java.io.IOException;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.net.URL;
import java.awt.Desktop;

import java.net.URISyntaxException;


import org.json.JSONObject;


import org.apache.http.HttpEntity;

import org.apache.http.HttpResponse;

import org.apache.http.client.HttpClient;

import org.apache.http.client.methods.*;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import org.apache.http.util.EntityUtils;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;




public class ClientApp extends JFrame {
	
	

    private JTextField urlField;

    private JTextField portField;

    private JTextArea responseArea;


    public ClientApp() {

        setTitle("Application Client");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(400, 400);

        setLayout(new BorderLayout());


        // Panel pour les paramètres de connexion

        JPanel connectionPanel = new JPanel(new FlowLayout());


        JLabel urlLabel = new JLabel("URL:");

        urlField = new JTextField(20);

        JLabel portLabel = new JLabel("Port:");

        portField = new JTextField(5);


        connectionPanel.add(urlLabel);

        connectionPanel.add(urlField);

        connectionPanel.add(portLabel);

        connectionPanel.add(portField);
        connectionPanel.setBackground(new Color(240, 240, 240)); // Couleur de fond
        


        // Panel pour afficher les informations retournées par le service REST

        JPanel responsePanel = new JPanel(new BorderLayout());


        JLabel responseLabel = new JLabel("Réponse:");

        responseArea = new JTextArea(10, 30);

        responseArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(responseArea);


        responsePanel.add(responseLabel, BorderLayout.NORTH);

        responsePanel.add(scrollPane, BorderLayout.CENTER);


        // Panel pour les opérations

        JPanel operationPanel = new JPanel(new FlowLayout());
        



        // Bouton pour afficher les informations d'aide

        JButton helpButton = new JButton("Aide");

        helpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String url = urlField.getText();
                int port = Integer.parseInt(portField.getText());
                String serviceURL = "http://" + url + ".cleverapps.io:" + port + "/help";

                String htmlResponse = loadHTMLPage(serviceURL);

                JEditorPane editorPane = new JEditorPane("text/html", htmlResponse);
                editorPane.setEditable(false);

                JScrollPane scrollPane = new JScrollPane(editorPane);
                responsePanel.removeAll();
                responsePanel.add(scrollPane, BorderLayout.CENTER);
                responsePanel.revalidate();
                responsePanel.repaint();
            }
        });


        // Bouton pour afficher la liste des STB (format XML)
        JButton stbListButton = new JButton("Liste des STB (XML)");
        stbListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String xmlResponse = getSTBListXML();
                responseArea.setText(xmlResponse);
            }
        });
        

     // Bouton pour afficher la liste des STB (format HTML)
        JButton stbListHTMLButton = new JButton("Liste des STB (HTML)");
        stbListHTMLButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String url = urlField.getText();
                int port = Integer.parseInt(portField.getText());
                String serviceURL = "http://" + url + ".cleverapps.io:" + port + "/stb23/resume/";

                String htmlResponse = loadHTMLPage(serviceURL);

                // Utiliser JSoup pour extraire le corps de la page HTML
                Document doc = Jsoup.parse(htmlResponse);
                Element body = doc.body();
                String htmlBody = body.html();

                JEditorPane editorPane = new JEditorPane("text/html", htmlBody);
                editorPane.setEditable(false);

                JScrollPane scrollPane = new JScrollPane(editorPane);
                responsePanel.removeAll();
                responsePanel.add(scrollPane, BorderLayout.CENTER);
                responsePanel.revalidate();
                responsePanel.repaint();
            }
        });

        
        
       

  
        

     
     
        JButton acceuilButton = new JButton("Acceuil");

        acceuilButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String url = urlField.getText();
                int port = Integer.parseInt(portField.getText());
                String serviceURL = "http://" + url + ".cleverapps.io:" + port + "/";

                String htmlResponse = loadHTMLPage(serviceURL);

                JEditorPane editorPane = new JEditorPane("text/html", htmlResponse);
                editorPane.setEditable(false);

                JScrollPane scrollPane = new JScrollPane(editorPane);
                responsePanel.removeAll();
                responsePanel.add(scrollPane, BorderLayout.CENTER);
                responsePanel.revalidate();
                responsePanel.repaint();
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
                //String xmlData = null;

                JTextArea xmlField = new JTextArea();
                xmlField.setRows(5);

                JOptionPane optionPane = new JOptionPane(
                        new JScrollPane(xmlField),
                        JOptionPane.PLAIN_MESSAGE,
                        JOptionPane.OK_CANCEL_OPTION
                );

                JDialog dialog = optionPane.createDialog(ClientApp.this, "Entrez le flux XML");
                dialog.setSize(800, 600);
                dialog.setVisible(true);

                String xmlData = xmlField.getText();

                if (!xmlData.isEmpty()) {
                    String serviceURL = "http://" + url + ".cleverapps.io:" + port + "/stb23/insert";
                    HttpClient httpClient = HttpClients.createDefault();

                	HttpPost httpPost = new HttpPost(serviceURL);

                    // Envoyer la requête POST avec le contenu XML saisi
                    String response = sendPOSTRequeste(serviceURL, xmlData);

                    // Traiter la réponse du serveur
                    if (response != null) {
                        // Obtenir le statut de la réponse

                	    System.out.print(response);
                    	// Assuming the response is in JSON format
                    	JSONObject jsonObject = new JSONObject(response);
                    	//int statusCode = jsonObject.getInt("status");
                         
                    	HttpResponse httpResponse = null;
						try {
							httpResponse = httpClient.execute(httpPost);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
 
                    	// Get the status code from the response
                    	int status = httpResponse.getStatusLine().getStatusCode();
                    	// Now you can use the obtained statusCode for further processing

						System.out.print(status);


                        if (status == 415) { // Remplacez "200" par le statut attendu pour une requête réussie
                            // Le traitement pour un statut réussi
                            responseArea.setText("Requête réussie. Réponse du serveur : " + response);
                        } else if (status == 400) { // Remplacez "400" par le statut d'erreur attendu
                            // Le traitement pour un statut d'erreur
                            responseArea.setText("Erreur : Requête invalide. Réponse du serveur : " + response);
                        } else {
                            // Le traitement pour d'autres statuts
                            responseArea.setText("Statut de réponse inattendu : " + status);
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


                String specId = JOptionPane.showInputDialog(ClientApp.this, "Entrez l'ID de la spécification :");


                if (specId != null && !specId.isEmpty()) {

                    String serviceURL = "http://" + url + ".cleverapps.io:" + port + "/stb23/delete/" + specId;

                    String response = sendDELETERequest(serviceURL);


                    responseArea.setText(response);

                }

            }

        });
       
        
     // Bouton pour afficher le détail d'une spécification (format XML)
        JButton specificationXMLButton = new JButton("Détail d'une spécification (XML)");
        specificationXMLButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String specId = JOptionPane.showInputDialog(ClientApp.this, "Entrez l'ID de la spécification :");
                if (specId != null && !specId.isEmpty()) {
                    String xmlResponse = getSpecificationXML(specId);

                    JEditorPane editorPane = new JEditorPane("text/xml", xmlResponse);
                    editorPane.setEditable(false);

                    JScrollPane scrollPane = new JScrollPane(editorPane);
                    responsePanel.removeAll();
                    responsePanel.add(scrollPane, BorderLayout.CENTER);
                    responsePanel.revalidate();
                    responsePanel.repaint();
                }
            }
        });
        

     // Bouton pour afficher le détail d'une spécification (format HTML)
        JButton specificationHTMLButton = new JButton("Détail d'une spécification (HTML)");
        specificationHTMLButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String specId = JOptionPane.showInputDialog(ClientApp.this, "Entrez l'ID de la spécification :");
                if (specId != null && !specId.isEmpty()) {
                	String htmlContent = getSpecificationHTML(specId);

                    JEditorPane editorPane = new JEditorPane("text/html", htmlContent);
                    editorPane.setEditable(false);

                    JScrollPane scrollPane = new JScrollPane(editorPane);
                    responsePanel.removeAll();
                    responsePanel.add(scrollPane, BorderLayout.CENTER);
                    responsePanel.revalidate();
                    responsePanel.repaint();
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

        JPanel mainPanel = new JPanel(new BorderLayout());


        // Ajoutez ici les autres composants nécessaires pour votre application


        // Ajoutez les panels principaux à la fenêtre

        add(connectionPanel, BorderLayout.NORTH);

      //  add(mainPanel, BorderLayout.CENTER);

        add(responsePanel, BorderLayout.CENTER);

        add(operationPanel, BorderLayout.SOUTH);


        setVisible(true);

    }
 // ...
    private int getResponseStatusCode(String serviceURL, String xmlData) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(serviceURL);
        
        try {
            // Définir les en-têtes et le corps de la requête POST
            httpPost.setHeader("Content-Type", "application/xml");
            httpPost.setEntity(new StringEntity(xmlData));
            
            // Exécuter la requête POST
            HttpResponse response = httpClient.execute(httpPost);
            
            // Obtenir le code de statut de la réponse
            int statusCode = response.getStatusLine().getStatusCode();
            
            // Consommer l'entité de la réponse pour libérer les ressources
            HttpEntity entity = response.getEntity();
            EntityUtils.consume(entity);
            
            return statusCode;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
            // Fermer le client HTTP
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

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

    private String sendPOSTRequeste(String serviceURL, String body) {
        try {
            URL url = new URL(serviceURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/xml");
            connection.setDoOutput(true);

            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(body.getBytes());
            outputStream.flush();
            outputStream.close();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                return response.toString();
            } else {
                return "Erreur lors de la requête POST. Code de réponse : " + responseCode;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Erreur lors de la requête POST : " + e.getMessage();
        }
    }


  
    private String sendPOSTRequest(String serviceURL, File xmlFile) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(serviceURL);

        try {
            // Définir les en-têtes et le corps de la requête POST
            httpPost.setHeader("Content-Type", "application/xml");
            httpPost.setEntity(new FileEntity(xmlFile));

            // Exécuter la requête POST
            CloseableHttpResponse response = httpClient.execute(httpPost);

            // Obtenir le statut de la réponse du serveur
            int statusCode = response.getStatusLine().getStatusCode();

            // Obtenir la réponse du serveur
            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(entity, "UTF-8");

            // Consommer l'entité de la réponse pour libérer les ressources
            EntityUtils.consume(entity);

            // Retourner la réponse du serveur avec le statut
            return statusCode + ":" + responseString;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            // Fermer le client HTTP et la réponse
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String extractResponseStatus(String response) {
        // Diviser la réponse en statut et contenu
        String[] parts = response.split(":", 2);
        if (parts.length == 2) {
            return parts[0];
        } else {
            return null;
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
            String serviceURL = "http://" + url + ".cleverapps.io:"+ port + "/stb23/resume/xml";
            return sendGETRequest(serviceURL);
        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur lors de la récupération de la liste des STB (format XML).";
        }
    }

    // Méthode pour récupérer la liste des STB au format HTML
   /* private String getSTBListHTML() {
    	try {
            String url = urlField.getText();
            int port = Integer.parseInt(portField.getText());
            String serviceURL = "http://" + url + ".cleverapps.io:" + port + "/stb23/resume";

            // Utiliser la méthode loadHTMLPage pour récupérer le contenu HTML
            String htmlResponse = loadHTMLPage(serviceURL);

            return htmlResponse;
        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur lors de la récupération du détail de la spécification (format HTML).";
        }
    } */
    
    private String getSTBListHTML() {
        try {
            String url = urlField.getText();
            int port = Integer.parseInt(portField.getText());
            String serviceURL = "http://" + url + ".cleverapps.io:" + port + "/stb23/resume";

            // Utiliser la méthode loadHTMLPage pour récupérer le contenu HTML
            String htmlResponse = loadHTMLPage(serviceURL);

            // Utiliser JSoup pour extraire le corps de la page HTML
            Document doc = Jsoup.parse(htmlResponse);
            Element body = doc.body();
            String htmlBody = body.html();

            return htmlBody;
        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur lors de la récupération du détail de la spécification (format HTML).";
        }
    }



    // Méthode pour récupérer le détail d'une spécification au format XML
    private String getSpecificationXML(String id) {
        try {
            String url = urlField.getText();
            int port = Integer.parseInt(portField.getText());
            String serviceURL = "http://" + url + ".cleverapps.io:" + port + "/stb23/xml/" + id;
            String xmlResponse = loadXMLPage(serviceURL);

            return xmlResponse;
        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur lors de la récupération du détail de la spécification (format XML).";
        }
    }

    private String loadXMLPage(String serviceURL) {
        try {
            HttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(serviceURL);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity responseEntity = httpResponse.getEntity();
            String xmlResponse = EntityUtils.toString(responseEntity);

            return xmlResponse;
        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur lors du chargement de la page XML.";
        }
    }

    // Méthode pour récupérer le détail d'une spécification au format HTML
   private String getSpecificationHTML(String id) {
        try {
            String url = urlField.getText();
            int port = Integer.parseInt(portField.getText());
            String serviceURL = "http://" + url + ".cleverapps.io:" + port + "/stb23/html/stb_details?id=" + id;

            // Utiliser la méthode loadHTMLPage pour récupérer le contenu HTML
            String htmlResponse = loadHTMLPage(serviceURL);

            return htmlResponse;
        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur lors de la récupération du détail de la spécification (format HTML).";
        }
    }  
    
   
    
    
    


    private String loadHTMLPage(String serviceURL) {
        try {
            HttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(serviceURL);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity responseEntity = httpResponse.getEntity();
            String htmlResponse = EntityUtils.toString(responseEntity);

            return htmlResponse;
        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur lors du chargement de la page HTML.";
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





