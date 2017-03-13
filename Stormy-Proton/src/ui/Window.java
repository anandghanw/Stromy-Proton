package ui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Anandghan W on 3/8/17.
 */
public class Window {

    private DisplayWindow dw;

    public Window(int width, int height){
        dw = new DisplayWindow(width,height);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                dw.setVisible(true);
            }
        });
    }

    public void setVisible(Boolean visible){
        dw.setVisible(visible);
    }

    public void setImage(String file){
        dw.setImage(file);
    }

    public void setText(String text){
        dw.setText(text);
    }

    public void setBackgroundColor(Color color){
        dw.setBackgroundColor(color);
    }

    private class DisplayWindow extends JFrame{
        JLabel label;
        ImageIcon icon;
        JPanel panel;
        JLabel textLabel;

        DisplayWindow(int width, int height){
            textLabel = new JLabel("");
            textLabel.setFont(new Font("Serif", Font.PLAIN, 60));
            textLabel.setForeground(Color.WHITE);

            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setSize(width,height);

            label = new JLabel();

            panel = new JPanel();
            panel.setBackground(Color.BLACK);
            panel.add(label);
            panel.add(textLabel);
            this.getContentPane().add(panel);

        }

        void setImage(String file){
            icon = new ImageIcon("pictures/"+file+".png");
            Image image = icon.getImage(); // transform it
            Image newimg = image.getScaledInstance(300, 300,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
            icon = new ImageIcon(newimg);  // transform it back
            label.setIcon(icon);
            revalidate();
            repaint();
        }

        public void setBackgroundColor(Color color){
            panel.setBackground(color);
            revalidate();
            repaint();
        }

        public void setText(String str){
            textLabel.setText(str);
        }
    }
}

