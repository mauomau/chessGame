public class Pion extends Piece {
    public Pion(String couleur) {
        super(couleur, "Pion");
    }

    @Override
    public boolean estMouvementValide(int x1, int y1, int x2, int y2, boolean capture, Piece[][] echiquier) {
        // Vérification des déplacements pour les pions blancs
        if (getCouleur().equals("Blanc")) {
            // Le pion peut se déplacer de 1 ou 2 cases au départ
            if (x1 == 6) {
                if (x2 == 5 && y1 == y2) {
                    // Première case de départ : une case en avant
                    return echiquier[x2][y2] == null; // Case vide
                } else if (x2 == 4 && y1 == y2) {
                    // Premier déplacement de 2 cases
                    return echiquier[x2][y2] == null; // Case vide
                }
            } else if (x2 == x1 - 1 && y1 == y2) {
                // Déplacement normal d'une case en avant
                return echiquier[x2][y2] == null; // Case vide
            } else if (x2 == x1 - 1 && Math.abs(y2 - y1) == 1) {
                // Capture en diagonale (1 case)
                if (capture) {
                    Piece pieceDestination = echiquier[x2][y2];
                    return pieceDestination != null && !pieceDestination.getCouleur().equals(getCouleur());
                }
            }
        }
        // Vérification des déplacements pour les pions noirs
        else if (getCouleur().equals("Noir")) {
            // Le pion peut se déplacer de 1 ou 2 cases au départ
            if (x1 == 1) {
                if (x2 == 2 && y1 == y2) {
                    // Première case de départ : une case en avant
                    return echiquier[x2][y2] == null; // Case vide
                } else if (x2 == 3 && y1 == y2) {
                    // Premier déplacement de 2 cases
                    return echiquier[x2][y2] == null; // Case vide
                }
            } else if (x2 == x1 + 1 && y1 == y2) {
                // Déplacement normal d'une case en avant
                return echiquier[x2][y2] == null; // Case vide
            } else if (x2 == x1 + 1 && Math.abs(y2 - y1) == 1) {
                // Capture en diagonale (1 case)
                if (capture) {
                    Piece pieceDestination = echiquier[x2][y2];
                    return pieceDestination != null && !pieceDestination.getCouleur().equals(getCouleur());
                }
            }
        }
        return false; // Si aucun des cas ci-dessus ne s'applique
    }

}
