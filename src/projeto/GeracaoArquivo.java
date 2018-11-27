package projeto;

import weka.classifiers.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Debug;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

import java.io.*;

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

            // varre todas as imagens
            for (int i = 0; i < arquivos.length; i++) {

                // atribui a imagem a variavel
                System.out.println(diretorio.getAbsolutePath() + "\\" + arquivos[i].getName());

                // Definição da classe - pica_pau
                if (arquivos[i].getName().toLowerCase().charAt(0) == 'p') {
                    classeDesenho = "pica_pau";
                } else {
                    classeDesenho = "zeca_urubu";
                }

                float[] tmp = ExtratorCaracteristicaImagem.extrair(diretorio.getAbsolutePath() + "\\" + arquivos[i].getName());

                exportacao += tmp[0] + "," + tmp[1] + "," + tmp[2] + "," + tmp[3] + "," + tmp[4] +
                        "," + tmp[5] + "," + tmp[6] + "," + tmp[7] + "," + classeDesenho + "\n";

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