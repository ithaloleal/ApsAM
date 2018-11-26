package projeto;

import com.googlecode.javacv.cpp.opencv_core;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Debug;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

import java.io.*;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_highgui.cvLoadImage;

public class GeracaoArquivo {

    public static void main(String[] args) throws IOException {
        try {

            double vermelho, verde, azul;
            String classeDesenho;

            String exportacao = "@relation desenhos\n\n";
            exportacao += "@attribute vermelho_topete_pica_pau real\n";
            exportacao += "@attribute amarelo_bico_pica_pau real\n";
            exportacao += "@attribute azul_corpo_pica_pau real\n";
            exportacao += "@attribute amarelo_pe_pica_pau real\n";
            exportacao += "@attribute laranja_bico_xe_urubu real\n";
            exportacao += "@attribute preto_corpo_zeca_urubu real\n";
            exportacao += "@attribute verde_calca_zeca_urubu real\n";
            exportacao += "@attribute laranja_pe_zeca_urubu real\n";
            exportacao += "@attribute classe {pica_pau, zeca_urubu}\n\n";
            exportacao += "@data\n";

            File diretorio = new File("src\\imagens");
            File[] arquivos = diretorio.listFiles();

            float vermelhoTopetePicaPau, amareloBicoPicaPau, amareloPePicaPau, azulCorpoPicaPau;
            float laranjaBicoZecaUrubu, pretoCorpoZecaUrubu, verdeCalcaZecaUrubu, laranjaPeZecaUrubu;

            float[][] dados = new float[200][8];

            // varre todas as imagens
            for (int i = 0; i < arquivos.length; i++) {
                vermelhoTopetePicaPau = 0;
                amareloBicoPicaPau = 0;
                amareloPePicaPau = 0;
                azulCorpoPicaPau = 0;
                laranjaBicoZecaUrubu = 0;
                pretoCorpoZecaUrubu = 0;
                verdeCalcaZecaUrubu = 0;
                laranjaPeZecaUrubu = 0;

                // atribui a imagem a variavel
                System.out.println(diretorio.getAbsolutePath() + "\\" + arquivos[i].getName());
                opencv_core.IplImage imgOriginal = cvLoadImage(diretorio.getAbsolutePath() + "\\" + arquivos[i].getName());
                opencv_core.CvSize tamanhoImagemOriginal = cvGetSize(imgOriginal);

                // Imagem processada - tamanho, profundidade de cores e número de canais de cores
                opencv_core.IplImage imagemProcessada = cvCreateImage(tamanhoImagemOriginal, IPL_DEPTH_8U, 3);
                imagemProcessada = cvCloneImage(imgOriginal);

                // Definição da classe - pica_pau
                if (arquivos[i].getName().toLowerCase().charAt(0) == 'p') {
                    classeDesenho = "pica_pau";
                } else {
                    classeDesenho = "zeca_urubu";
                }

                // Varre a imagem pixel a pixel
                for (int alt = 0; alt < imagemProcessada.height(); alt++) {
                    for (int larg = 0; larg < imagemProcessada.width(); larg++) {

                        // pega o rgb da imagem pixel a pixel
                        CvScalar rgb = cvGet2D(imagemProcessada, alt, larg);

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
                vermelhoTopetePicaPau = (vermelhoTopetePicaPau / (imgOriginal.height() * imgOriginal.width())) * 100;
                amareloBicoPicaPau = (amareloBicoPicaPau / (imgOriginal.height() * imgOriginal.width())) * 100;
                azulCorpoPicaPau = (azulCorpoPicaPau / (imgOriginal.height() * imgOriginal.width())) * 100;
                amareloPePicaPau = (amareloPePicaPau / (imgOriginal.height() * imgOriginal.width())) * 100;
                laranjaBicoZecaUrubu = (laranjaBicoZecaUrubu / (imgOriginal.height() * imgOriginal.width())) * 100;
                pretoCorpoZecaUrubu = (pretoCorpoZecaUrubu / (imgOriginal.height() * imgOriginal.width())) * 100;
                verdeCalcaZecaUrubu = (verdeCalcaZecaUrubu / (imgOriginal.height() * imgOriginal.width())) * 100;
                laranjaPeZecaUrubu = (laranjaPeZecaUrubu / (imgOriginal.height() * imgOriginal.width())) * 100;

                // Grava as características no vetor de características
                dados[i][0] = vermelhoTopetePicaPau;
                dados[i][1] = amareloBicoPicaPau;
                dados[i][2] = azulCorpoPicaPau;
                dados[i][3] = amareloPePicaPau;
                dados[i][4] = laranjaBicoZecaUrubu;
                dados[i][5] = pretoCorpoZecaUrubu;
                dados[i][6] = verdeCalcaZecaUrubu;
                dados[i][7] = laranjaPeZecaUrubu;

                exportacao += dados[i][0] + "," + dados[i][1] + "," + dados[i][2] + "," + dados[i][3] + "," + dados[i][4] +
                        "," + dados[i][5] + "," + dados[i][6] + "," + dados[i][7] + "," + classeDesenho + "\n";

            }

            // Garva o arquivo
            File arquivo = new File("src/arquivos/desenhos.arff");
            FileOutputStream f = new FileOutputStream(arquivo);
            f.write(exportacao.getBytes());
            f.close();

            ConverterUtils.DataSource ds = new ConverterUtils.DataSource(new FileInputStream(arquivo));
            Instances instancias = ds.getDataSet();

            // Seta o atributo classe
            instancias.setClassIndex(instancias.numAttributes() - 1);

            MultilayerPerceptron perceptron = new MultilayerPerceptron();
            perceptron.buildClassifier(instancias);

            ObjectOutputStream classificador = new ObjectOutputStream(new FileOutputStream("src/arquivos/desenhos.model"));
            classificador.writeObject(perceptron);
            classificador.flush();
            classificador.close();

            System.out.println("\n\n\n==================== MATRIZ CONFUSAO ============================\n");
            Evaluation eval = new Evaluation(instancias);
            // A cada teste o Random seed soma + 1
            eval.crossValidateModel(perceptron, instancias, 10, new Debug.Random(1));
            double[][] matrizConfusao = eval.confusionMatrix();
            System.out.println(matrizConfusao[0][0] + " - " + matrizConfusao[0][1]);
            System.out.println(matrizConfusao[1][0] + " - " + matrizConfusao[1][1]);

            /*
            // Criação de novo registro
            Instance novo = new DenseInstance(instancias.numAttributes());
            novo.setDataset(instancias);
            novo.setValue(0, Float.parseFloat(lblLaranjaBart.getText()));
            novo.setValue(1, Float.parseFloat(lblAzulCalcao.getText()));
            novo.setValue(2, Float.parseFloat(lblAzulSapato.getText()));
            novo.setValue(3, Float.parseFloat(lblMarromHomer.getText()));
            novo.setValue(4, Float.parseFloat(lblAzulHomer.getText()));
            novo.setValue(5, Float.parseFloat(lblSapatoHomer.getText()));
            */
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}