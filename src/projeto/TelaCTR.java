package projeto;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

public class TelaCTR implements Initializable {

    @FXML private Label txtPicapau;
    @FXML private Label txtZecaUrubu;
    @FXML private Button btnSeleciona;
    @FXML private ImageView imvImagem;
    @FXML private TextField txtcaminho;

    @FXML
    void btnSeleciona_click(ActionEvent event) {
        try {
            FileChooser openFileDialog = new FileChooser();
            openFileDialog.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Arquivos de imagem", "*.jpg", "*.jpeg", "*.png"));
            openFileDialog.setTitle(" Imagens ");

            File imagem = openFileDialog.showOpenDialog(null);
            if (imagem != null) {
                imvImagem.setImage(fileToImage(imagem));
                txtcaminho.setText(imagem.getAbsolutePath());
            }

            ObjectInputStream modeloMP = new ObjectInputStream(getClass().getResourceAsStream("/arquivos/desenhos.model"));
            MultilayerPerceptron perceptron = (MultilayerPerceptron) modeloMP.readObject();
            modeloMP.close();

            ConverterUtils.DataSource ds = new ConverterUtils.DataSource(getClass().getResourceAsStream("/arquivos/desenhos.arff"));
            Instances instancias = ds.getDataSet();
            instancias.setClassIndex(instancias.numAttributes() - 1);

            float[] dados = ExtratorCaracteristicaImagem.extrair(txtcaminho.getText());

            Instance novo = new DenseInstance(instancias.numAttributes());
            novo.setDataset(instancias);
            novo.setValue(0, dados[0]);
            novo.setValue(1, dados[1]);
            novo.setValue(2, dados[2]);
            novo.setValue(3, dados[3]);
            novo.setValue(4, dados[4]);
            novo.setValue(5, dados[5]);
            novo.setValue(6, dados[6]);
            novo.setValue(7, dados[7]);

            double[] chance = perceptron.distributionForInstance(novo);

            DecimalFormat df = new DecimalFormat("0.##");
            txtPicapau.setText("Pica-pau: " + df.format(chance[0] * 100) + "% chance");
            txtZecaUrubu.setText("Zeca urubu: " + df.format(chance[1] * 100) + "% chance");
            System.out.println(chance[0]);
            System.out.println(chance[1]);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtPicapau.setText("");
        txtZecaUrubu.setText("");
    }

    public static Image fileToImage(File file) throws IOException {
        WritableImage retorno = null;

        BufferedImage bImage = ImageIO.read(file);
        retorno = SwingFXUtils.toFXImage(bImage, null);

        return retorno;
    }

}
