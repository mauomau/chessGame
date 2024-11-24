public class Fou extends Piece {
    public Fou(String couleur) {
        super(couleur, "Fou");
    }

    @Override
    public boolean estMouvementValide(int x1, int y1, int x2, int y2, boolean capture, Piece[][] echiquier) {
        // Le fou se déplace en diagonale, donc la différence absolue entre x et y doit être égale
        if (Math.abs(x2 - x1) == Math.abs(y2 - y1)) {
            // Vérifier les obstacles
            return pasDObstacles(echiquier, x1, y1, x2, y2);
        }
        return false;
    }
}
