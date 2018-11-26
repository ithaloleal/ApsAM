package projeto;

import com.googlecode.javacv.cpp.opencv_core;

import static com.googlecode.javacv.cpp.opencv_core.cvGet2D;
import static com.googlecode.javacv.cpp.opencv_highgui.cvLoadImage;

/**
 * Created by ithalo on 26/11/2018.
 */
public class ExtratorCaracteristicaImagem {

    public static float[] extrair(String caminho) {
        double vermelho, verde, azul;
        float vermelhoTopetePicaPau = 0, amareloBicoPicaPau = 0, amareloPePicaPau = 0, azulCorpoPicaPau = 0;
        float laranjaBicoZecaUrubu = 0, pretoCorpoZecaUrubu = 0, verdeCalcaZecaUrubu = 0, laranjaPeZecaUrubu = 0;
        float[] dados = new float[8];

        opencv_core.IplImage imagemProcessada = cvLoadImage(caminho);

        for (int alt = 0; alt < imagemProcessada.height(); alt++) {
            for (int larg = 0; larg < imagemProcessada.width(); larg++) {

                // pega o rgb da imagem pixel a pixel
                opencv_core.CvScalar rgb = cvGet2D(imagemProcessada, alt, larg);

                // cvGet2D atribui posicao 0 - azul / 1 - verde / 2 vermelho
                azul = rgb.val(0);
                verde = rgb.val(1);
                vermelho = rgb.val(2);

                /////////////////////////////////////////////////
                // INICIO CARACTERISTICAS PICA PAU
                /////////////////////////////////////////////////

                // topete pica pau
                if (alt <= (imagemProcessada.height() / 2)) {
                    if (azul >= 0 && azul <= 79 && verde >= 162 && verde <= 254 && vermelho >= 181 && vermelho <= 236) {
                        vermelhoTopetePicaPau++;
                    }
                }

                // Amarelo bico pica-pau (metade para cima)
                if (alt <= (imagemProcessada.height() / 2)) {
                    if (azul >= 0 && azul <= 42 && verde >= 162 && verde <= 254 && vermelho >= 214 && vermelho <= 255) {
                        amareloBicoPicaPau++;
                    }
                }

                // azul corpo pica pau
                if (azul >= 162 && azul <= 189 &&
                        verde >= 47 && verde <= 115 &&
                        vermelho >= 0 && vermelho <= 43) {
                    azulCorpoPicaPau++;
                }

                // Amarelo pe pica-pau (parte de baixo da imagem)
                if (alt >= (imagemProcessada.height() / 2) + (imagemProcessada.height() / 3)) {
                    if (azul >= 0 && azul <= 42 && verde >= 162 && verde <= 254 && vermelho >= 214 && vermelho <= 255) {
                        amareloPePicaPau++;
                    }
                }

                /////////////////////////////////////////////////
                // INICIO CARACTERISTICAS ZECA URUBU
                /////////////////////////////////////////////////

                // Laranja bico zeca_urubu (metade para cima)
                if (alt < (imagemProcessada.height() / 2)) {
                    if (azul >= 0 && azul <= 45 && verde >= 107 && verde <= 187 && vermelho >= 188 && vermelho <= 244) {
                        laranjaBicoZecaUrubu++;
                    }
                }

                // preto corpo zeca urubu
                if (alt < ((imagemProcessada.height() / 2) + (imagemProcessada.height() / 4))) {
                    if (azul >= 0 && azul <= 56 && verde >= 0 && verde <= 50 && vermelho >= 0 && vermelho <= 40) {
                        pretoCorpoZecaUrubu++;
                    }
                }

                // verde calca zaca urubu
                if (alt > (imagemProcessada.height() / 2)) {
                    if (azul >= 10 && azul <= 50 && verde >= 44 && verde <= 145 && vermelho >= 12 && vermelho <= 75) {
                        verdeCalcaZecaUrubu++;
                    }
                }

                // laranja pe zaca urubu
                if (alt > (imagemProcessada.height() / 2) + (imagemProcessada.height() / 4)) {
                    if (azul >= 0 && azul <= 45 && verde >= 107 && verde <= 187 && vermelho >= 188 && vermelho <= 244) {
                        laranjaPeZecaUrubu++;
                    }
                }
            }
        }

        // Normaliza as características pelo número de pixels totais da imagem
        vermelhoTopetePicaPau = (vermelhoTopetePicaPau / (imagemProcessada.height() * imagemProcessada.width())) * 100;
        amareloBicoPicaPau = (amareloBicoPicaPau / (imagemProcessada.height() * imagemProcessada.width())) * 100;
        azulCorpoPicaPau = (azulCorpoPicaPau / (imagemProcessada.height() * imagemProcessada.width())) * 100;
        amareloPePicaPau = (amareloPePicaPau / (imagemProcessada.height() * imagemProcessada.width())) * 100;
        laranjaBicoZecaUrubu = (laranjaBicoZecaUrubu / (imagemProcessada.height() * imagemProcessada.width())) * 100;
        pretoCorpoZecaUrubu = (pretoCorpoZecaUrubu / (imagemProcessada.height() * imagemProcessada.width())) * 100;
        verdeCalcaZecaUrubu = (verdeCalcaZecaUrubu / (imagemProcessada.height() * imagemProcessada.width())) * 100;
        laranjaPeZecaUrubu = (laranjaPeZecaUrubu / (imagemProcessada.height() * imagemProcessada.width())) * 100;

        // Grava as características no vetor de características
        dados[0] = vermelhoTopetePicaPau;
        dados[1] = amareloBicoPicaPau;
        dados[2] = azulCorpoPicaPau;
        dados[3] = amareloPePicaPau;
        dados[4] = laranjaBicoZecaUrubu;
        dados[5] = pretoCorpoZecaUrubu;
        dados[6] = verdeCalcaZecaUrubu;
        dados[7] = laranjaPeZecaUrubu;

        return dados;
    }

}
