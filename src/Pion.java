import java.util.*;

public class Pion extends Piece {
    public Pion(String couleur) {
        super(couleur, "Pion");
    }

    @Override
    public List<int[]> calculerMouvementsPossibles(int ligne, int colonne, Echiquier echiquier) {
        List<int[]> mouvements = new ArrayList<>();
        Piece[][] plateau = echiquier.obtenirTableauPieces();
        int direction = getCouleur().equals("Blanc") ? -1 : 1; // Blancs vers le haut (-1), Noirs vers le bas (+1)

        // Mouvement d'une case en avant
        if (echiquier.estDansLesLimites(ligne + direction, colonne) &&
                plateau[ligne + direction][colonne] == null) {
            mouvements.add(new int[]{ligne + direction, colonne});

            // Mouvement de deux cases en avant (si c'est le premier mouvement)
            if ((getCouleur().equals("Blanc") && ligne == 6 || getCouleur().equals("Noir") && ligne == 1) &&
                    plateau[ligne + 2 * direction][colonne] == null &&
                    plateau[ligne + direction][colonne] == null) { // Vérifier que la case intermédiaire est vide
                mouvements.add(new int[]{ligne + 2 * direction, colonne});
            }
        }

        // Captures en diagonale (gauche et droite)
        for (int dCol : new int[]{-1, 1}) { // Teste les colonnes adjacentes (gauche et droite)
            int nouvelleColonne = colonne + dCol;
            if (echiquier.estDansLesLimites(ligne + direction, nouvelleColonne) &&
                    plateau[ligne + direction][nouvelleColonne] != null &&
                    !plateau[ligne + direction][nouvelleColonne].getCouleur().equals(getCouleur())) {
                mouvements.add(new int[]{ligne + direction, nouvelleColonne});
            }
        }

        return mouvements;
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
