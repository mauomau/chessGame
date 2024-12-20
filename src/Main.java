import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.util.*;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Main extends Application {

    private GridPane echiquier;
    private Echiquier echiquierJeu;
    private static final int TAILLE_CASE = 75;  // Taille de chaque case en pixels
    private ImageView pieceSelectionnee = null;
    private int ligneOrigine, colonneOrigine;
    private Rectangle caseSelectionnee = null;
    private int ligneSelectionnee = -1;
    private int colonneSelectionnee = -1;
    private VBox piecesCaptureesNoires = new VBox();
    private VBox piecesCaptureesBlanches = new VBox();
    // Stocker les cases surlignées pour les mouvements possibles
    private List<StackPane> casesSurlignees = new ArrayList<>();
    private Label joueurActuelLabel = new Label();
    private boolean tourDeJeu = false;







    private void deplacerPiece(GridPane echiquier, int nouvelleLigne, int nouvelleColonne) {
        if (caseSelectionnee == null || pieceSelectionnee == null) {
            return;
        }

        // Supprimer la surbrillance de la case sélectionnée
        caseSelectionnee.setStroke(Color.TRANSPARENT);
        caseSelectionnee = null;

        // Réinitialiser les surbrillances avant de déplacer la pièce
        reinitialiserSurbrillance(echiquier);  // Ajoute cette ligne pour réinitialiser les surbrillances avant chaque déplacement

        // Vérifier si le déplacement est valide dans l'échiquier logique
        if (echiquierJeu.estDeplacementPossible(ligneSelectionnee, colonneSelectionnee, nouvelleLigne, nouvelleColonne)) {
            // Récupérer le StackPane d'origine et de destination
            StackPane stackPaneOrigine = (StackPane) getNodeFromGridPane(echiquier, ligneSelectionnee, colonneSelectionnee);
            StackPane stackPaneDestination = (StackPane) getNodeFromGridPane(echiquier, nouvelleLigne, nouvelleColonne);

            // Gérer la capture si une pièce ennemie est présente sur la case cible
            Piece pieceCible = echiquierJeu.getPiece(nouvelleLigne, nouvelleColonne);
            Piece pieceDeplacee = echiquierJeu.getPiece(ligneSelectionnee, colonneSelectionnee);

            if (pieceCible != null && !pieceCible.getCouleur().equals(pieceDeplacee.getCouleur())) {
                echiquierJeu.capturePiece(pieceCible); // Mise à jour interne du jeu
                afficherPiecesCapturees(); // Met à jour l'affichage des pièces capturées

                // Supprimer visuellement la pièce capturée (on suppose que la pièce capturée est à l'index 1)
                stackPaneDestination.getChildren().remove(1); // Indice 1 est l'ImageView de la pièce
            }

            // Déplacer la pièce dans l'échiquier logique
            echiquierJeu.deplacerPiece(ligneSelectionnee, colonneSelectionnee, nouvelleLigne, nouvelleColonne);

            // Déplacer visuellement l'ImageView de la pièce
            stackPaneOrigine.getChildren().remove(pieceSelectionnee); // Retirer la pièce de la case d'origine
            stackPaneDestination.getChildren().add(pieceSelectionnee); // Ajouter la pièce à la nouvelle case

            // Mettre à jour les indices de ligne/colonne
            ligneSelectionnee = nouvelleLigne;
            colonneSelectionnee = nouvelleColonne;

            // Vérifier la promotion si un pion atteint la dernière rangée
            if (pieceDeplacee instanceof Pion) {
                boolean promotionEffectuee = echiquierJeu.verifierPromotion(nouvelleLigne, nouvelleColonne);
                if (promotionEffectuee) {
                    // Si une promotion est effectuée, mettre à jour l'affichage
                    Piece piecePromu = echiquierJeu.getCase(nouvelleLigne, nouvelleColonne).getPiece();
                    // Récupérer le StackPane de la case
                    StackPane stackPaneCasePromu = (StackPane) getNodeFromGridPane(echiquier,nouvelleLigne,nouvelleColonne);
                    if (stackPaneCasePromu.getChildren().size() > 1) {
                        stackPaneCasePromu.getChildren().remove(1);
                    }else{
                        stackPaneCasePromu.getChildren().remove(0);
                    }

                    Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(piecePromu.obtenirCheminImage())));
                    if (image.isError()) {
                        System.out.println("Erreur lors du chargement de l'image : " + piecePromu.obtenirCheminImage());
                        return;
                    }
                    ImageView imageView = new ImageView(image);
                    imageView.setFitHeight(TAILLE_CASE - 3);
                    imageView.setFitWidth(TAILLE_CASE - 3);

                    // Associer la pièce à l'image
                    imageView.setUserData(piecePromu);

                    // Ajouter l'ImageView au StackPane
                    stackPaneCasePromu.getChildren().add(imageView);

                }
            }


            // Changer de joueur après un déplacement valide
            joueurActuelLabel.setText("Tour du joueur : " + echiquierJeu.getJoueurActuel());

            // Debug : afficher la nouvelle position dans la console
            echiquierJeu.afficherEchiquier();
            System.out.println("Nouvelle position : ligne = " + ligneSelectionnee + ", colonne = " + colonneSelectionnee);
        } else {
            reinitialiserSurbrillance(echiquier);  // Ajoute cette ligne pour réinitialiser les surbrillances avant chaque déplacement
            pieceSelectionnee = null;
            System.out.println("Déplacement invalide");
        }

        // Réinitialiser la pièce sélectionnée pour permettre une nouvelle sélection
        pieceSelectionnee = null;
    }

    private void selectionnerCase(StackPane stackPane, int ligne, int colonne) {
        // Si une autre case est déjà sélectionnée, enlever sa surbrillance
        if (caseSelectionnee != null) {
            caseSelectionnee.setStroke(Color.TRANSPARENT);
        }

        if (pieceSelectionnee != null) {
            pieceSelectionnee = null;
            return;
        }

        if(!estTourDeJeuValide (ligne, colonne ) && tourDeJeu == true ) return;


        if (stackPane.getChildren().get(0) instanceof Rectangle) {
            Rectangle caseRect = (Rectangle) stackPane.getChildren().get(0);
            caseRect.setStroke(Color.GREENYELLOW);
            caseSelectionnee = caseRect;

            if (stackPane.getChildren().size() > 1 && stackPane.getChildren().get(1) instanceof ImageView) {
                pieceSelectionnee = (ImageView) stackPane.getChildren().get(1);
                int[] positionActuelle = getPositionDansEchiquier(echiquier, stackPane);
                if (positionActuelle != null) {
                    ligneSelectionnee = positionActuelle[0];
                    colonneSelectionnee = positionActuelle[1];
                }

                // Debug: afficher les mouvements possibles de la pièce sélectionnée
                Piece piece = echiquierJeu.getPiece(ligneSelectionnee, colonneSelectionnee);
                if (piece != null) {
                    List<int[]> mouvementsPossibles = echiquierJeu.getMouvementsPossibles(piece, ligneSelectionnee, colonneSelectionnee);
                    System.out.println("Mouvements possibles pour la pièce sélectionnée : " + mouvementsPossibles.size());
                    surlignerMouvementsPossibles(mouvementsPossibles, echiquier);
                }
            }
        }
    }

    private Node getNodeFromGridPane(GridPane gridPane, int ligne, int colonne) {
        for (Node node : gridPane.getChildren()) {
            Integer row = GridPane.getRowIndex(node);
            Integer col = GridPane.getColumnIndex(node);

            if (row == null) row = 0;
            if (col == null) col = 0;

            if (row == ligne && col == colonne) {
                // Ensure the node is a StackPane (not Group)
                if (node instanceof StackPane) {
                    return node;
//                } else {
//                    // If it's not a StackPane, create one
//                    StackPane stackPane = new StackPane();
//                    gridPane.add(stackPane, colonne, ligne);
//                    System.out.println("new StackPane created ! at colone : "+colonne+", ligne : "+ligne);
//                    return stackPane;
                }
            }
        }
        return null; // Node not found
    }

    // Méthode pour ajouter une pièce sur l'échiquier
    private void ajouterPiece(GridPane echiquier, String cheminImage, int ligne, int colonne, Piece piece) {
        // Récupérer le StackPane pour la position donnée
        StackPane stackPane = (StackPane) getNodeFromGridPane(echiquier, ligne, colonne);
        if (stackPane == null) {
            System.out.println("Erreur : StackPane à la position " + ligne + "," + colonne + " est null");
            return;
        }

        // Créer le Rectangle pour la case (si non existant)
        Rectangle caseRect = new Rectangle(TAILLE_CASE - 3, TAILLE_CASE - 3);
        caseRect.setFill(Color.TRANSPARENT);

        // Créer l'ImageView pour la pièce
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Image/" + cheminImage)));
        if (image.isError()) {
            System.out.println("Erreur lors du chargement de l'image : " + cheminImage);
            return;
        }
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(TAILLE_CASE - 3);
        imageView.setFitWidth(TAILLE_CASE - 3);

        // Associer la pièce à l'image
        imageView.setUserData(piece);

        // Ajouter l'ImageView au StackPane
        stackPane.getChildren().add(imageView);

        // Gérer le clic de la souris sur une pièce
        stackPane.setOnMouseClicked(event -> {
            if (pieceSelectionnee == null) {
                // Sélectionner une nouvelle pièce
                selectionnerCase(stackPane, ligne, colonne);
            } else {
                // Obtenir les coordonnées de la case cliquée
                int ligneClic = GridPane.getRowIndex(stackPane);
                int colonneClic = GridPane.getColumnIndex(stackPane);

                // Vérifier la pièce d'origine
                Piece origine = echiquierJeu.getPiece(ligneSelectionnee, colonneSelectionnee);
                // Vérifier la pièce cible (corrigé pour utiliser echiquierJeu)
                Piece cible = echiquierJeu.getPiece(ligneClic, colonneClic);

                // Debug : afficher l'état des pièces
                System.out.println("Origine: " + (origine != null ? origine.getCouleur() : "aucune"));
                System.out.println("Cible: " + (cible != null ? cible.getCouleur() : "aucune"));
                System.out.println("Déplacement demandé de (" + ligneSelectionnee + ", " + colonneSelectionnee + ") à (" + ligneClic + ", " + colonneClic + ")");

                if (origine != null && cible != null && !origine.getCouleur().equals(cible.getCouleur())) {
                    // Si la pièce cliquée est ennemie, effectuer un déplacement/capture
                    System.out.println("Capture de la pièce ennemie");
                    deplacerPiece(echiquier, ligneClic, colonneClic);
                } else if (origine != null && cible == null) {
                    // Si la case est vide, déplacer la pièce
                    System.out.println("Déplacement vers une case vide");
                    deplacerPiece(echiquier, ligneClic, colonneClic);
                } else {
                    reinitialiserSurbrillance(echiquier);  // Ajoute cette ligne pour réinitialiser les surbrillances avant chaque déplacement
                    pieceSelectionnee = null;
                    if (caseSelectionnee != null) {
                        caseSelectionnee.setStroke(Color.TRANSPARENT);
                        caseSelectionnee = null;
                    }
                    System.out.println("Action non valide");
                }
            }
        });
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

    private void afficherPiecesCapturees() {
        piecesCaptureesNoires.getChildren().clear();
        piecesCaptureesBlanches.getChildren().clear();

        for (Piece p : echiquierJeu.getPiecesCapturees()) {
            // Vérifier si la pièce a un type et une couleur
            if (p != null) {
                // Construction du chemin d'image basé sur le type et la couleur de la pièce
                String cheminImage = "/Image/" + p.getClass().getSimpleName().toLowerCase() + "_" + p.getCouleur().toLowerCase() + ".png";
                ImageView image = new ImageView(new Image(getClass().getResourceAsStream(cheminImage)));
                image.setFitWidth(50);
                image.setFitHeight(50);

                // Ajouter l'image à la liste correspondante selon la couleur
                if ("Blanc".equals(p.getCouleur())) {
                    piecesCaptureesBlanches.getChildren().add(image);
                } else {
                    piecesCaptureesNoires.getChildren().add(image);
                }
            }
        }
    }

    private void surlignerMouvementsPossibles(List<int[]> mouvements, GridPane echiquier) {
        // Réinitialiser toutes les surbrillances
        reinitialiserSurbrillance(echiquier);

        // Appliquer la surbrillance aux cases possibles
        for (int[] mouvement : mouvements) {
            int ligne = mouvement[0];
            int colonne = mouvement[1];
            StackPane caseSurlignee = (StackPane) getNodeFromGridPane(echiquier, ligne, colonne);
            if (caseSurlignee != null) {
                // Créer un cercle pour marquer la case possible
                Circle cercle = new Circle();
                cercle.setCenterX(30);  // Définir la position du centre du cercle
                cercle.setCenterY(30);  // Définir la position du centre du cercle
                cercle.setRadius(15);   // Taille du cercle
                cercle.setFill(Color.LIGHTGREEN);  // Couleur de remplissage
                cercle.setOpacity(0.7);  // Opacité pour un effet moderne
                cercle.setStroke(Color.GREEN);  // Bordure du cercle

                // Ajouter le cercle au StackPane de la case
                caseSurlignee.getChildren().add(cercle);

                casesSurlignees.add(caseSurlignee); // Ajouter à la liste des surbrillances
            }
        }
        System.out.println("Mouvements possibles surlignés: " + mouvements.size());  // Debug
    }

    private void reinitialiserSurbrillance(GridPane echiquier) {
        // Parcourir toutes les cases surlignées
        for (StackPane caseSurlignee : casesSurlignees) {
            // Supprimer uniquement les cercles ajoutés
            caseSurlignee.getChildren().removeIf(node -> node instanceof Circle);
        }
        // Vider la liste des surbrillances
        casesSurlignees.clear();
    }

    private boolean estTourDeJeuValide (int ligne, int colonne ){
        // Vérification pour empêcher la sélection d'une pièce appartenant à l'adversaire pour son tour de jeu
        Piece pieceTourDeJeu = echiquierJeu.getPiece(ligne, colonne);
        if (pieceTourDeJeu != null && !pieceTourDeJeu.getCouleur().equals(echiquierJeu.getJoueurActuel())) {
            System.out.println("Ce n'est pas votre tour !");
            joueurActuelLabel.setText("Tour du joueur : " + echiquierJeu.getJoueurActuel()+ ". Ce n'est pas votre Tour !");
            return false;
        }else {
            return true;
        }
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
                StackPane stackPaneCase = new StackPane(caseRect);
                int finalLigne = ligne;
                int finalColonne = colonne;

                stackPaneCase.setOnMouseClicked(event -> {
                    if (pieceSelectionnee == null) {
                        // Sélectionner une nouvelle pièce
                        selectionnerCase(stackPaneCase, finalLigne, finalColonne);

                        if(!estTourDeJeuValide (finalLigne, finalColonne )) return;


                        // Obtenir les mouvements possibles
                        Piece piece = echiquierJeu.getPiece(finalLigne, finalColonne);
                        if (piece != null) {
                            List<int[]> mouvementsPossibles = echiquierJeu.getMouvementsPossibles(piece, finalLigne, finalColonne);
                            surlignerMouvementsPossibles(mouvementsPossibles, echiquier);
                        }
                    } else {
                        // Réinitialiser toutes les surbrillances
                        reinitialiserSurbrillance(echiquier);

                        // Déplacer la pièce si possible
                        deplacerPiece(echiquier, finalLigne, finalColonne);
                    }
                });


                // Ajout de la case au GridPane
                echiquier.add(stackPaneCase, colonne, ligne);
            }
        }

        // Ajouter les tours
        ajouterPiece(echiquier, "tour_noir.png", 0, 0, new Tour("Noir"));
        ajouterPiece(echiquier, "tour_noir.png", 0, 7, new Tour("Noir"));
        ajouterPiece(echiquier, "tour_blanc.png", 7, 0, new Tour("Blanc"));
        ajouterPiece(echiquier, "tour_blanc.png", 7, 7, new Tour("Blanc"));

        // Ajouter les rois
        ajouterPiece(echiquier, "roi_noir.png", 0, 4, new Roi("Noir"));
        ajouterPiece(echiquier, "roi_blanc.png", 7, 4, new Roi("Blanc"));
        // Ajouter les dames
        ajouterPiece(echiquier,"dame_noir.png",0,3, new Dame("Noir"));
        ajouterPiece(echiquier,"dame_blanc.png",7,3, new Dame("Blanc"));
        // Ajouter les fou
        ajouterPiece(echiquier,"fou_noir.png",0,5, new Fou("Noir"));
        ajouterPiece(echiquier,"fou_noir.png",0,2, new Fou("Noir"));
        ajouterPiece(echiquier,"fou_blanc.png",7,5, new Fou("Blanc"));
        ajouterPiece(echiquier,"fou_blanc.png",7,2, new Fou("Blanc"));
        // Ajouter les cavaliers
        ajouterPiece(echiquier,"cavalier_noir.png",0,1, new Cavalier("Noir"));
        ajouterPiece(echiquier,"cavalier_noir.png",0,6, new Cavalier("Noir"));
        ajouterPiece(echiquier,"cavalier_blanc.png",7,1, new Cavalier("Blanc"));
        ajouterPiece(echiquier,"cavalier_blanc.png",7,6, new Cavalier("Blanc"));
        // Ajouter les pions
        for (int i = 0; i<8 ; i++){
            ajouterPiece(echiquier,"pion_noir.png",1,i, new Pion("Noir"));
        }
        for (int i = 0; i<8 ; i++){
            ajouterPiece(echiquier,"pion_blanc.png",6,i, new Pion("Blanc"));
        }


        // Création de la scène et ajout de l'échiquier
        HBox zoneCapturee = new HBox();
        zoneCapturee.getChildren().addAll(piecesCaptureesNoires, piecesCaptureesBlanches);
        zoneCapturee.setSpacing(20);

        BorderPane layout = new BorderPane();
        layout.setCenter(echiquier);
        layout.setBottom(zoneCapturee);

        joueurActuelLabel.setText("Tour du joueur : " + echiquierJeu.getJoueurActuel() + ((!tourDeJeu)?" ! Attention : Tour de jeu non applique au jeu !":""));
        joueurActuelLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        VBox vbox = new VBox(joueurActuelLabel, echiquier);
        vbox.setSpacing(10);

        Scene scene = new Scene(vbox, 600, 650); // Ajout d'espace pour les pièces capturées
        primaryStage.setScene(scene);
        primaryStage.setTitle("ChessGame");
        primaryStage.show();


//        Scene scene = new Scene(echiquier, TAILLE_CASE * 8, TAILLE_CASE * 8);
//        primaryStage.setTitle("Jeu d'Échecs");
//        primaryStage.setScene(scene);
//        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
