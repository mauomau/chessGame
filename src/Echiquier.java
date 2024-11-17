public class Echiquier {
    private Case[][] plateau;

    public Echiquier() {
        plateau = new Case[8][8];  // Un échiquier de 8x8 cases
        initialiserEchiquier();
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

    // Vérifie si le mouvement est valide, prend en compte les autres pièces
    public boolean estDeplacementPossible(int x1, int y1, int x2, int y2) {
        Piece piece = getCase(x1, y1).getPiece();
        if(piece == null) return false;
        if (!piece.estMouvementValide(x1, y1, x2, y2)) return false;

        // Vérifier la case de destination
        Piece pieceDestination = getCase(x2, y2).getPiece();
        if (pieceDestination != null && pieceDestination.getCouleur().equals(piece.getCouleur())) { return false; }  // Case occupée par une pièce de la même couleur

        // Utilise la méthode estMouvementValide de la pièce pour vérifier la validité du mouvement
        return true;

    }


    public void deplacerPiece(int x1, int y1, int x2, int y2) {
        Case caseDepart = getCase(x1, y1);
        Case caseArrivee = getCase(x2, y2);
        Piece piece = caseDepart.getPiece();

        if (piece != null) {
            caseDepart.setPiece(null); // Libérer la case de départ
            caseArrivee.setPiece(piece); // Déplacer la pièce vers la case de destination
        }
    }

    public void afficherEchiquier() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = plateau[i][j].getPiece();
                if (piece != null) {
                    System.out.print(piece.getClass().getSimpleName().charAt(0) + piece.getCouleur().charAt(0) + " ");
                    // Exemple : "TB" pour Tour Blanc, "PN" pour Pion Noir
                } else {
                    System.out.print(". "); // Case vide
                }
            }
            System.out.println();
        }
        System.out.println("-------------------------");
    }


}
