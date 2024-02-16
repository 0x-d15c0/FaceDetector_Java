import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class FaceDetectionAppGUI extends JFrame {
    private JTextField inputField;
    private JLabel inputImageLabel;
    private JLabel outputImageLabel;

    public FaceDetectionAppGUI() {
        super("Face Detection App");

        // Set up the GUI components
        inputField = new JTextField();
        JButton browseButton = new JButton("Browse");
        JButton detectButton = new JButton("Detect Faces");

        inputImageLabel = new JLabel();
        outputImageLabel = new JLabel();

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("Enter image path: "), BorderLayout.WEST);
        panel.add(inputField, BorderLayout.CENTER);
        panel.add(browseButton, BorderLayout.EAST);
        panel.add(detectButton, BorderLayout.SOUTH);

        JPanel imagePanel = new JPanel(new GridLayout(1, 2));
        imagePanel.add(inputImageLabel);
        imagePanel.add(outputImageLabel);

        // Add action listeners
        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                browseImage();
            }
        });

        detectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                detectFaces();
            }
        });

        // Set up the main frame
        setLayout(new BorderLayout());
        add(panel, BorderLayout.NORTH);
        add(imagePanel, BorderLayout.CENTER);
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void browseImage() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            inputField.setText(selectedFile.getAbsolutePath());
            showImage(inputImageLabel, selectedFile.getAbsolutePath());
        }
    }

    private void detectFaces() {
        String imagePath = inputField.getText();
        if (!imagePath.isEmpty()) {
            try {
                String outputImagePath = "./images/output.jpg";
                FaceDetector faceDetector = new FaceDetector(imagePath, "haarcascade_frontalface_alt2.xml");
                faceDetector.detectAndSave(outputImagePath);
                showImage(outputImageLabel, outputImagePath);
                JOptionPane.showMessageDialog(this, "Face detection completed. Output saved as output.jpg");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please enter an image path.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showImage(JLabel label, String imagePath) {
        ImageIcon icon = new ImageIcon(imagePath);
        label.setIcon(icon);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FaceDetectionAppGUI();
            }
        });
    }
}
