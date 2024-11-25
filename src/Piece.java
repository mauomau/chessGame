import java.util.*;

public abstract class Piece {
    private String couleur; // La couleur de la pièce (blanc/noir)
    private String type;    // Le type de pièce (Roi, Reine, etc.)

    // Constructeur
    public Piece(String couleur, String type) {
        this.couleur = couleur;
        this.type = type;
    }

    public String getCouleur() {
        return couleur;
    }

    public String getType() {
        return type;
    }

    protected boolean pasDObstacles(Piece[][] echiquier, int x1, int y1, int x2, int y2) {
        // Mouvement vertical
        if (x1 == x2) {
            int start = Math.min(y1, y2) + 1;
            int end = Math.max(y1, y2);
            for (int y = start; y < end; y++) {
                if (echiquier[x1][y] != null) return false; // Obstacle trouvé
            }
        }
        // Mouvement horizontal
        else if (y1 == y2) {
            int start = Math.min(x1, x2) + 1;
            int end = Math.max(x1, x2);
            for (int x = start; x < end; x++) {
                if (echiquier[x][y1] != null) return false; // Obstacle trouvé
            }
        }
        // Mouvement diagonal
        else if (Math.abs(x2 - x1) == Math.abs(y2 - y1)) {
            int dx = (x2 > x1) ? 1 : -1;
            int dy = (y2 > y1) ? 1 : -1;
            int x = x1 + dx, y = y1 + dy;
            while (x != x2 && y != y2) {
                if (echiquier[x][y] != null) return false; // Obstacle trouvé
                x += dx;
                y += dy;
            }
        }
        return true; // Pas d'obstacles
    }

    public List<int[]> calculerMouvementsPossibles(int ligne, int colonne, Echiquier echiquier) {
        List<int[]> mouvements = new ArrayList<>();
        Piece[][] plateau = echiquier.obtenirTableauPieces(); // On récupère le tableau des pièces

        // Calcul des mouvements possibles en fonction de la pièce et de ses règles
        // Exemple pour un pion :
        int direction = getCouleur().equals("Blanc") ? -1 : 1; // Les blancs montent, les noirs descendent
        if (echiquier.estDansLesLimites(ligne + direction, colonne) && plateau[ligne + direction][colonne] == null) {
            mouvements.add(new int[]{ligne + direction, colonne}); // Mouvement d'un pion
        }

        // Ajoute d'autres règles spécifiques pour les pièces ici...
        return mouvements;
    }



    // Méthode abstraite pour les mouvements
    public abstract boolean estMouvementValide(int x1, int y1, int x2, int y2, boolean capture, Piece[][] echiquier);
}

