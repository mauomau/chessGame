public class Roi extends Piece {

    public Roi(String couleur) {
        super(couleur, "Roi");
    }

    @Override
    public boolean estMouvementValide(int x1, int y1, int x2, int y2) {
        // Le roi se déplace d'une case dans toutes les directions
        return Math.abs(x2 - x1) <= 1 && Math.abs(y2 - y1) <= 1;
    }
}
