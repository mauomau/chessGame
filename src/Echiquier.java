import java.util.*;

public class Echiquier {
    private Case[][] plateau;
    private List<Piece> piecesCapturees;

    public Echiquier() {
        plateau = new Case[8][8];  // Un échiquier de 8x8 cases
        piecesCapturees = new ArrayList<>();
        initialiserEchiquier();
    }

    // Getter pour les pièces capturées
    public List<Piece> getPiecesCapturees() {
        return piecesCapturees;
    }

    // Initialiser les pièces sur l'échiquier
    private void initialiserEchiquier() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                plateau[i][j] = new Case(i, j);
            }
        }

        // Ajouter des pièces sur les cases de départ
        plateau[0][0].setPiece(new Tour("Noir"));
        plateau[0][7].setPiece(new Tour("Noir"));
        plateau[7][0].setPiece(new Tour("Blanc"));
        plateau[7][7].setPiece(new Tour("Blanc"));

        plateau[0][4].setPiece(new Roi("Noir"));
        plateau[7][4].setPiece(new Roi("Blanc"));

        plateau[0][3].setPiece(new Dame("Noir"));
        plateau[7][3].setPiece(new Dame("Blanc"));

        plateau[0][5].setPiece(new Fou("Noir"));
        plateau[0][2].setPiece(new Fou("Noir"));
        plateau[7][5].setPiece(new Fou("Blanc"));
        plateau[7][2].setPiece(new Fou("Blanc"));

        plateau[0][1].setPiece(new Cavalier("Noir"));
        plateau[0][6].setPiece(new Cavalier("Noir"));
        plateau[7][1].setPiece(new Cavalier("Blanc"));
        plateau[7][6].setPiece(new Cavalier("Blanc"));

        for (int i = 0; i<8 ; i++){
            plateau[1][i].setPiece(new Pion("Noir"));
        }
        for (int i = 0; i<8 ; i++){
            plateau[6][i].setPiece(new Pion("Blanc"));
        }

    }

    public Case getCase(int ligne, int colonne) {
        return plateau[ligne][colonne];
    }

    public Piece getPiece(int ligne, int colonne) {
        Case caseEchiquier = getCase(ligne, colonne);
        return caseEchiquier != null ? caseEchiquier.getPiece() : null;
    }


    // Vérifie si le mouvement est valide, prend en compte les autres pièces
    public boolean estDeplacementPossible(int x1, int y1, int x2, int y2) {
        Piece piece = getCase(x1, y1).getPiece();
        if (piece == null) {
            System.out.println("Aucune pièce à la position de départ");
            return false;
        }

        // Vérifier la pièce à destination
        Piece pieceDestination = getCase(x2, y2).getPiece();

        // Si la case de destination est occupée par une pièce de la même couleur, le mouvement est invalide
        if (pieceDestination != null && pieceDestination.getCouleur().equals(piece.getCouleur())) {
            System.out.println("Case occupée par une pièce alliée");
            return false;
        }

        // On passe le paramètre "capture" pour vérifier les captures dans les mouvements
        boolean capture = false;
        if (pieceDestination != null && !pieceDestination.getCouleur().equals(piece.getCouleur())) {
            capture = true; // Si la case de destination contient une pièce ennemie, il y a capture
        }

        // Vérification du mouvement avec la méthode modifiée
        Piece[][] echiquier = obtenirTableauPieces(); // On obtient un tableau de pièces

        System.out.println("Déplacement demandé de (" + x1 + ", " + y1 + ") à (" + x2 + ", " + y2 + ")");

        // Si le mouvement n'est pas valide, retourner false
        if (!piece.estMouvementValide(x1, y1, x2, y2, capture, echiquier)) {
            System.out.println("Mouvement non valide selon les règles");
            return false;
        }

        // Vérification si la case de destination est vide ou contient une pièce ennemie
        if (pieceDestination != null && pieceDestination.getCouleur().equals(piece.getCouleur())) {
            System.out.println("Case occupée par une pièce de la même couleur");
            return false;
        }

        afficherPiecesCapturees();  // Mettre à jour la liste des pièces capturées
        return true;
    }




    public void deplacerPiece(int x1, int y1, int x2, int y2) {
        Case caseDepart = getCase(x1, y1);
        Case caseArrivee = getCase(x2, y2);
        Piece piece = caseDepart.getPiece();

        if (piece != null) {
            Piece pieceCapturee = caseArrivee.getPiece();
            if (pieceCapturee != null && !pieceCapturee.getCouleur().equals(piece.getCouleur())) {
                piecesCapturees.add(pieceCapturee); // Ajouter la pièce capturée à la liste
                System.out.println("Pièce capturée : " + pieceCapturee.getType() + " " + pieceCapturee.getCouleur());
            }

            caseDepart.setPiece(null); // Libérer la case de départ
            caseArrivee.setPiece(piece); // Déplacer la pièce vers la case de destination
        }
    }

    public void capturePiece(Piece piece) {
        if (piece != null) {
            piecesCapturees.add(piece);  // Ajouter la pièce à la liste des pièces capturées

        }
    }

    public Piece[][] obtenirTableauPieces() {
        Piece[][] echiquier = new Piece[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                echiquier[i][j] = plateau[i][j].getPiece();
            }
        }
        return echiquier;
    }



    public void afficherEchiquier() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = plateau[i][j].getPiece();
                if (piece != null) {
                    System.out.print(piece.getClass().getSimpleName().charAt(0) + piece.getCouleur().charAt(0) + " ");
                    // Exemple : "TB" pour Tour Blanc, "PN" pour Pion Noir
                } else {
                    System.out.print(" .  "); // Case vide
                }
            }
            System.out.println();
        }
        System.out.println(" ------------------------------");
    }

    // Afficher les pièces capturées
    public void afficherPiecesCapturees() {
        System.out.println("Pièces capturées : ");
        for (Piece piece : piecesCapturees) {
            System.out.println(piece.getType() + " " + piece.getCouleur());
        }
    }
}
