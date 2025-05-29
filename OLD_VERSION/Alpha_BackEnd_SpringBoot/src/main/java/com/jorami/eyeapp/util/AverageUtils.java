package com.jorami.eyeapp.util;

public class AverageUtils {

    /**
     * Permet de faire une moyenne de M
     * @param k1
     * @param k2
     * @param taille
     * @return La moyenne des M
     */
    public static float calculM(float[] k1, float[] k2, int taille) {
        float M = 0, tmpM = 0;
        for(int i = 0; i < taille; i++)
        {
            tmpM = (float) (0.5 * (k1[i] + k2[i]));
            M = M + tmpM;
        }
        return  M/taille; //Afin de faire une moyenne arithmétique
    }

    /**
     * Permet de faire une moyenne de JO
     * @param k1
     * @param k2
     * @param k1Axis
     * @return La moyenne des JO
     */
    public static float calculJO(float[] k1, float[] k2, float[] k1Axis, int taille) {
        float JO = 0, tmpJO = 0;
        for(int i = 0; i < taille; i++)
        {
            //tmpJO = (-0.5 *(k1[i] - k2[i])) * Math.cos(2 * k1Axis[i]);
            tmpJO = (float) (((-(k1[i] - k2[i]))/2) * Math.cos(Math.toRadians(2 * k1Axis[i])));
            JO = JO + tmpJO;
        }
        return JO/taille;
    }

    /**
     * Permet de faire une moyenne de J45
     * @param k1
     * @param k2
     * @param k1Axis
     * @return La moyenne de J45
     */
    public static float calculJ45(float k1[], float k2[], float k1Axis[], int taille) {
        float J45 = 0,tmpJ45 = 0;
        for(int i = 0; i < taille; i++)
        {
            //tmpJ45 = (-0.5 *(k1[i] - k2[i])) * Math.sin(2 * k1Axis[i]);
            tmpJ45 = (float) (((-(k1[i] - k2[i]))/2) * Math.sin(Math.toRadians(2 * k1Axis[i])));
            J45 = J45 + tmpJ45;
        }

        return J45/taille;
    }

    /**
     * Permet de calculer le nouveau k1 Axis afin de faire une moyenne Bio
     * @param J45
     * @param JO
     * @return new k1Axis
     */
    public static float calculAlpha(float J45, float JO) {
        float alpha = 0;

        alpha = (float) (Math.toDegrees(Math.atan(J45/JO)) * 0.5);

        if(alpha < 0 ) {
            alpha = alpha +180;
        }

        return (float) Math.round((alpha) * 100) / 100;
    }

    /**
     * Permet de calculer le nouveau k2 afin de faire une moyenne Bio
     * @param M
     * @param JO
     * @param alpha
     * @return newK2
     */
    public static float calculNewK2(float M, float JO, float alpha) {
        float newK2 = 0;
        return (float) Math.round((M + (JO / Math.cos(Math.toRadians(2 * alpha)))) * 100) / 100;
    }

    /**
     * Permet de calculer le nouveau k1 afin de faire une moyenne Bio
     * @param M
     * @param JO
     * @param alpha
     * @return newK1
     */
    public static float calculNewK1(float M, float JO, float alpha) {
        float newK1 = 0;
        return (float) Math.round((M - (JO/Math.cos(Math.toRadians(2*alpha)))) * 100) / 100;
    }

    /**
     * Permet de faire une moyenne arithmétique
     * @param x
     * @param taille
     * @return newK1
     */
    public static float calculMoyenneArithmetique(float x[], int taille) {
        float newMoyenne = 0;

        for(int i = 0; i < taille; i++)
        {
            if(x[i] == 0.0) {
                //Diminue la valeur de taille pour être juste lors de la division en fin de calcul
                taille -= 1;
            }
            newMoyenne = x[i] + newMoyenne;
        }
        //Si taille vaut 0, on la set à 1 pour éviter une division par 0 pour la division en fin de calcul
        if(taille == 0) {
            taille = 1;
        }

        return (float) Math.round((newMoyenne/taille) * 100) / 100;
    }
}

