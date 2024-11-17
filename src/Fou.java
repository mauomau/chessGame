public class Fou extends Piece{
    public Fou(String couleur){
        super(couleur, "Fou");
    }

    @Override
    public boolean estMouvementValide(int x1, int y1, int x2, int y2) {
        // Le fou se déplace uniquement en diagonale
        return Math.abs(x2 - x1) == Math.abs(y2 - y1);
    }

}
