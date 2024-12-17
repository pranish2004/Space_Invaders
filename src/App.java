import javax.swing.*;
public class App{
    public static void main(String[]args)throws Exception{
  int tilesize=32;
  int rows=16;
  int columns=16;
  int boardwidth=tilesize*columns;
  int boardheight=tilesize*rows;

  JFrame frame=new JFrame("Space Invaders");
  frame.setSize(boardwidth,boardheight);
  frame.setVisible(true);
  frame.setLocationRelativeTo(null);
  frame.setResizable(false);
  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

     Spaceinvaders spaceinvaders=new Spaceinvaders();
     frame.add(spaceinvaders);
     frame.pack();
     spaceinvaders.requestFocus();
     frame.setVisible(true);
    }
}