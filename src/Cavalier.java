public class Cavalier extends Piece{
    public Cavalier(String couleur) {
        super(couleur, "Cavalier");
    }

    @Override
    public boolean estMouvementValide(int x1, int y1, int x2, int y2) {
        // Le cavalier se déplace en "L"
        return (Math.abs(x2 - x1) == 2 && Math.abs(y2 - y1) == 1) || (Math.abs(x2 - x1) == 1 && Math.abs(y2 - y1) == 2);
    }

}
