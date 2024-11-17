import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public class Main extends Application {

    private GridPane echiquier;
    private Echiquier echiquierJeu;
    private static final int TAILLE_CASE = 75;  // Taille de chaque case en pixels
    private ImageView pieceSelectionnee = null;
    private int ligneOrigine, colonneOrigine;
    private Rectangle caseSelectionnee = null;
    private int ligneSelectionnee = -1;
    private int colonneSelectionnee = -1;


    private void deplacerPiece(GridPane echiquier, int nouvelleLigne, int nouvelleColonne) {
        // Vérifier si une case a été sélectionnée et qu'une pièce est sélectionnée
        if (caseSelectionnee == null || pieceSelectionnee == null) {
            return;  // Si aucune case ou pièce n'est sélectionnée, on ne fait rien
        }

        // Enlever la surbrillance de la case sélectionnée
        caseSelectionnee.setStroke(Color.TRANSPARENT);
        caseSelectionnee = null;  // Réinitialiser

        // Récupérer le StackPane contenant la pièce
        StackPane stackPaneOrigine = (StackPane) pieceSelectionnee.getParent();

        // Vérifier si le déplacement est valide avec la classe Echiquier
        if (echiquierJeu.estDeplacementPossible(ligneSelectionnee, colonneSelectionnee, nouvelleLigne, nouvelleColonne)) {
            // Si le déplacement est valide, déplacer la pièce
            echiquierJeu.deplacerPiece(ligneSelectionnee, colonneSelectionnee, nouvelleLigne, nouvelleColonne);

            // Retirer le StackPane de sa position actuelle dans le GridPane
            echiquier.getChildren().remove(stackPaneOrigine);

            // Ajouter le StackPane à la nouvelle position dans le GridPane
            echiquier.add(stackPaneOrigine, nouvelleColonne, nouvelleLigne);

            // Mettre à jour les positions sélectionnées
            int[] nouvellePosition = getPositionDansEchiquier(echiquier, stackPaneOrigine);
            if (nouvellePosition != null) {
                ligneSelectionnee = nouvellePosition[0];
                colonneSelectionnee = nouvellePosition[1];
            }


            System.out.println("Nouvelle position : ligne = " + ligneSelectionnee + ", colonne = " + colonneSelectionnee);
        } else {
            System.out.println("Déplacement invalide");
        }

        if (echiquierJeu.estDeplacementPossible(ligneSelectionnee, colonneSelectionnee, nouvelleLigne, nouvelleColonne)) {
            echiquierJeu.deplacerPiece(ligneSelectionnee, colonneSelectionnee, nouvelleLigne, nouvelleColonne);

            // Debug : afficher l'état du plateau après le déplacement
            echiquierJeu.afficherEchiquier();
        }

        // Réinitialiser la pièce sélectionnée après le déplacement
        pieceSelectionnee = null;

        System.out.println("Ancienne position : Ligne = " + ligneOrigine + ", Colonne = " + colonneOrigine);
        System.out.println("Nouvelle position : Ligne = " + ligneSelectionnee + ", Colonne = " + colonneSelectionnee);

    }


    private void selectionnerCase(StackPane stackPane, int ligne, int colonne) {
        // Si une autre case est déjà sélectionnée, enlever sa surbrillance
        if (caseSelectionnee != null) {
            caseSelectionnee.setStroke(Color.TRANSPARENT);
        }

        // Si une pièce est déjà sélectionnée, on ne sélectionne pas une autre pièce
        if (pieceSelectionnee != null) {
            pieceSelectionnee = null;
            return;  // Sortir de la méthode si une pièce est déjà sélectionnée
        }

        // Surbrillance de la case sélectionnée
        Rectangle caseRect = (Rectangle) stackPane.getChildren().get(0); // Le fond de la case
        caseRect.setStroke(Color.YELLOW);
        caseSelectionnee = caseRect;

        // Vérifier si la case a une pièce (enfant à l'indice 1)
        if (stackPane.getChildren().size() > 1) {
            pieceSelectionnee = (ImageView) stackPane.getChildren().get(1); // La pièce sur la case
            int[] positionActuelle = getPositionDansEchiquier(echiquier, stackPane);
            if (positionActuelle != null) {
                ligneSelectionnee = positionActuelle[0];
                colonneSelectionnee = positionActuelle[1];
            }

        } else {
            pieceSelectionnee = null; // Aucune pièce sur cette case
        }

        // Mettre à jour la position sélectionnée
//        ligneSelectionnee = ligne;
//        colonneSelectionnee = colonne;

        System.out.println("Case sélectionnée : Ligne = " + ligneSelectionnee + ", Colonne = " + colonneSelectionnee);

    }



    // Méthode pour ajouter une pièce sur l'échiquier
    private void ajouterPiece(GridPane echiquier, String cheminImage, int ligne, int colonne) {
        // Création de la case et de l'image dans un StackPane
        Rectangle caseRect = new Rectangle(TAILLE_CASE - 3, TAILLE_CASE - 3);
        caseRect.setFill(Color.TRANSPARENT);

        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Image/" + cheminImage)));
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(TAILLE_CASE - 3);
        imageView.setFitWidth(TAILLE_CASE - 3);

        // Ajouter au StackPane et définir le clic
        StackPane stackPane = new StackPane(caseRect, imageView);
        stackPane.setOnMouseClicked(event -> selectionnerCase(stackPane, ligne, colonne));
        echiquier.add(stackPane, colonne, ligne);
    }

    private int[] getPositionDansEchiquier(GridPane echiquier, Node node) {
        for (Node child : echiquier.getChildren()) {
            if (child == node) {
                Integer ligne = GridPane.getRowIndex(child);
                Integer colonne = GridPane.getColumnIndex(child);
                return new int[] {
                        (ligne != null) ? ligne : 0,
                        (colonne != null) ? colonne : 0
                };
            }
        }
        return null;
    }


    @Override
    public void start(Stage primaryStage) {
        // Création d'un GridPane pour l'échiquier
        echiquier = new GridPane();
        echiquier.setGridLinesVisible(true);
        echiquierJeu = new Echiquier();  // Initialiser l'échiquier

        // Boucle pour créer les 64 cases de l'échiquier
        for (int ligne = 0; ligne < 8; ligne++) {
            for (int colonne = 0; colonne < 8; colonne++) {
                // Création d'un rectangle pour chaque case
                Rectangle caseRect = new Rectangle(TAILLE_CASE -3, TAILLE_CASE - 3);

                // Définition de la couleur de chaque case
                if ((ligne + colonne) % 2 == 0) {
                    caseRect.setFill(Color.WHITESMOKE);  // Couleur claire
                } else {
                    caseRect.setFill(Color.SADDLEBROWN);  // Couleur sombre
                }

                // Ajouter un bord transparent pour la surbrillance
                caseRect.setStrokeWidth(3); // Taille du bord
                caseRect.setStroke(Color.TRANSPARENT); // Couleur de départ invisible

                // Ajouter la case au GridPane dans un StackPane
                StackPane stackPane = new StackPane(caseRect);
                int finalLigne = ligne;
                int finalColonne = colonne;

                stackPane.setOnMouseClicked(event -> {
                    if (pieceSelectionnee != null) {
                        // Si une pièce est déjà sélectionnée, déplacer la pièce
                        deplacerPiece(echiquier, finalLigne, finalColonne);
                    } else {
                        // Sinon, sélectionner cette case (si une pièce y est présente)
                        selectionnerCase(stackPane, finalLigne, finalColonne);
                    }
                });

                // Ajout de la case au GridPane
                echiquier.add(stackPane, colonne, ligne);
            }
        }

        // Ajouter les tours
        ajouterPiece(echiquier, "tour_noir.png", 0, 0);
        ajouterPiece(echiquier, "tour_noir.png", 0, 7);
        ajouterPiece(echiquier, "tour_blanc.png", 7, 0);
        ajouterPiece(echiquier, "tour_blanc.png", 7, 7);

        // Ajouter les rois
        ajouterPiece(echiquier, "roi_noir.png", 0, 4);
        ajouterPiece(echiquier, "roi_blanc.png", 7, 4);
        // Ajouter les dames
        ajouterPiece(echiquier,"dame_noir.png",0,3);
        ajouterPiece(echiquier,"dame_blanc.png",7,3);
        // Ajouter les fou
        ajouterPiece(echiquier,"fou_noir.png",0,5);
        ajouterPiece(echiquier,"fou_noir.png",0,2);
        ajouterPiece(echiquier,"fou_blanc.png",7,5);
        ajouterPiece(echiquier,"fou_blanc.png",7,2);
        // Ajouter les cavaliers
        ajouterPiece(echiquier,"cavalier_noir.png",0,1);
        ajouterPiece(echiquier,"cavalier_noir.png",0,6);
        ajouterPiece(echiquier,"cavalier_blanc.png",7,1);
        ajouterPiece(echiquier,"cavalier_blanc.png",7,6);
        // Ajouter les pions
        for (int i = 0; i<8 ; i++){
            ajouterPiece(echiquier,"pion_noir.png",1,i);
        }
        for (int i = 0; i<8 ; i++){
            ajouterPiece(echiquier,"pion_blanc.png",6,i);
        }


        // Création de la scène et ajout de l'échiquier
        Scene scene = new Scene(echiquier, TAILLE_CASE * 8, TAILLE_CASE * 8);
        primaryStage.setTitle("Jeu d'Échecs");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
