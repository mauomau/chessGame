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

    // Méthode abstraite pour les mouvements
    public abstract boolean estMouvementValide(int x1, int y1, int x2, int y2);
}

