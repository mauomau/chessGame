import java.util.*;

public class Cavalier extends Piece {
    public Cavalier(String couleur) {
        super(couleur, "Cavalier");
    }

    @Override
    public List<int[]> calculerMouvementsPossibles(int ligne, int colonne, Echiquier echiquier) {
        List<int[]> mouvements = new ArrayList<>();
        Piece[][] plateau = echiquier.obtenirTableauPieces();

        // Tous les mouvements possibles du cavalier
        int[][] deltas = {{-2, -1}, {-2, 1}, {2, -1}, {2, 1}, {-1, -2}, {-1, 2}, {1, -2}, {1, 2}};
        for (int[] delta : deltas) {
            int x = ligne + delta[0], y = colonne + delta[1];
            if (echiquier.estDansLesLimites(x, y)) {
                if (plateau[x][y] == null || !plateau[x][y].getCouleur().equals(getCouleur())) {
                    mouvements.add(new int[]{x, y});
                }
            }
        }

        return mouvements;
    }


    @Override
    public boolean estMouvementValide(int x1, int y1, int x2, int y2, boolean capture, Piece[][] echiquier) {
        // Le cavalier se déplace en "L", soit deux cases dans une direction et une dans l'autre
        if (Math.abs(x2 - x1) == 2 && Math.abs(y2 - y1) == 1 || Math.abs(x2 - x1) == 1 && Math.abs(y2 - y1) == 2) {
            // Le cavalier n'a pas d'obstacle, il peut sauter par-dessus d'autres pièces
            return true;
        }
        return false;
    }
}
