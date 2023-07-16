import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Scanner;

import static java.lang.Math.abs;
import static java.lang.Math.pow;


public class Main extends JFrame implements ActionListener {

    public JPanel buttons;
    public JButton[] ArrayOfButtons = new JButton[6];
    public int numIter = 400, xMove, yMove = 0;
    public double zoom = 150;
    public double zoomTwo = 50;
    public static double zx;
    public double zy;
    public double cx;
    public double cy;
    public double t;
    public int a_from_before;
    public BufferedImage I;

    double zxNull, zyNull, R;

    public Main() {
        super("123");

        //начальные параметры z_0
        Scanner in = new Scanner(System.in);
        System.out.print("z0 (zx): ");
        zxNull= in.nextDouble();
        System.out.print("z0 (zy): ");
        zyNull= in.nextDouble();

        //начальные параметры R
        System.out.print("R: ");
        R = in.nextDouble();

        //параметры a
        System.out.print("a: ");
        a_from_before = in.nextInt();

        setBounds(0, 0, 600, 600);
        //запрет изменения размера окна
        setResizable(false);
        //если закрывать окно
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        //проверка точек
        algorithm();

        //окно отрисовки
        Container contentPane = getContentPane();
        contentPane.setLayout(null); //инициализация


        buttons = new JPanel();
        //координаты и ширина с высотой панели с кнопками
        buttons.setBounds(0,0,600,20);
        //сетка кнопок из 1 строки и 6 колонок
        buttons.setLayout(new GridLayout(1,6));


        //массив кнопок
        ArrayOfButtons[0] = new JButton("+");
        ArrayOfButtons[1] = new JButton("-");
        ArrayOfButtons[2] = new JButton("^");
        ArrayOfButtons[3] = new JButton("v");
        ArrayOfButtons[4] = new JButton ("<");
        ArrayOfButtons[5] = new JButton(">");

        //добавляем на окно панель с кнопками
        contentPane.add(buttons);
        //Отрисовываем изображение
        contentPane.add(new picture());

        for (int x = 0; x<ArrayOfButtons.length;x++){
            //добавляем сами кнопки
            buttons.add(ArrayOfButtons[x]);
            //вешаем прослушиватель событий
            ArrayOfButtons[x].addActionListener(this);
        }

        validate();

    }

    public class picture extends JPanel{
        public picture(){
            setBounds(0,0,600,600);
        }

        @Override
        public void paint (Graphics g){
            super.paint(g);
            //отрисовка
            g.drawImage(I, 0, 0, this);
        }
    }

    public void algorithm(){
        //создается новый объект, который представляет собой изображение
        I = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);

        //проход по всем координатам пикселей на изображении
        for (int y = a_from_before; y < getHeight() - a_from_before; y++) { //Im
            for (int x = a_from_before; x < getWidth() - a_from_before; x++) { //Re
                //zx = zy = 0;
                zx=zxNull;
                zy=zyNull;

                cx = (x - 300+xMove) / abs(zoom);
                cy = (y - 300+yMove) / abs(zoom);
                int iter = numIter;
                while (zx * zx + zy * zy < R * R && iter > 0) { //R=2
                    t = zx * zx * zx * zx - 6 * zx * zx * zy * zy + zy * zy * zy * zy + cx;
                    zy = 4 * zx * zx * zx * zy - 4 * zx * zy * zy * zy + cy;
                    zx = t;

                    iter--;
                }
                I.setRGB(x, y, iter);
            }
        }
    }

    public void actionPerformed(ActionEvent w){
        String event = w.getActionCommand();

        switch (event){
            case "^":
                yMove-=100;
                break;
            case "v":
                yMove+=100;
                break;
            case "<":
                xMove-=100;
                break;
            case ">":
                xMove+=100;
                break;
            case "+":
                zoom+=zoomTwo;
                break;
            case "-":
                if (zoom > 0) zoom-=zoomTwo;
                break;
        }


        //пересчитываем
        algorithm();
        validate();
        //перерисовка
        repaint();
    }

    public static void main(String[] args) {

        Main frame = new Main();
        frame.setVisible(true);

        //Для сохранения картинки:
        Container content = frame.getContentPane();
        BufferedImage img = new BufferedImage(content.getWidth(), content.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = img.createGraphics();
        content.printAll(g2d);
        g2d.dispose();

        try {
            ImageIO.write(img, "png", new File("C:/Users/kozir/Desktop/123.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}