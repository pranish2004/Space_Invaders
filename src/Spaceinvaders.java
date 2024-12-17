import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.*;

public class Spaceinvaders extends JPanel implements ActionListener, KeyListener {
    class block {
        int x;
        int y;
        int width;
        int height;
        Image img;
        Boolean alive = true; // aliens
        boolean used = false; // bullets

        block(int x, int y, int width, int height, Image img) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.img = img;
        }
    }

    int tilesize = 32;
    int rows = 16;
    int columns = 16;
    int boardwidth = tilesize * columns;
    int boardheight = tilesize * rows;

    Image planeimg;
    Image shipimg1;
    Image shipimg2;
    Image shipimg3;
    Image shipimg4;
    Image pranish;
    Image sivaram;
    Image prabha;
    Image nandha;
    Image raj;
    Image mathan;
    Image navaneethan;
    ArrayList<Image> alienimg = new ArrayList<>();

    int shipwidth = tilesize * 2;
    int shipheight = tilesize;
    int shipx = tilesize * columns / 2 - tilesize;
    int shipy = boardheight - tilesize * 2;

    block ship;
    Timer gameloop;
    ArrayList<block> alienarray;
    int alienwidth = tilesize * 2;
    int alienheight = tilesize;
    int alienx = tilesize;
    int alieny = tilesize;

    int alienrows = 2;
    int aliencolumns = 3;
    int shipvelocityx = tilesize;
    int alienvelocityx=1;
int aliencount =0;
ArrayList<block>bullets;
int bulletwidth=tilesize/8;
int bulletheight=tilesize/2;
int bulletvelocityy=-10;
int score=0;
boolean gameover=false;



    Spaceinvaders() {
        setPreferredSize(new Dimension(boardwidth, boardheight));
        setBackground(Color.white);
        setFocusable(true);
        addKeyListener(this);

        planeimg = new ImageIcon(getClass().getResource("./ship.png")).getImage();
        shipimg1 = new ImageIcon(getClass().getResource("./alien-cyan.png")).getImage();
        shipimg2 = new ImageIcon(getClass().getResource("./alien-magenta.png")).getImage();
        shipimg3 = new ImageIcon(getClass().getResource("./alien-yellow.png")).getImage();
        shipimg4 = new ImageIcon(getClass().getResource("./alien.png")).getImage();
        // pranish = new ImageIcon(getClass().getResource("./DSC_0010.JPG")).getImage();
        // prabha = new ImageIcon(getClass().getResource("./DSC_0016.JPG")).getImage();
        //sivaram = new ImageIcon(getClass().getResource("./DSC_0025.JPG")).getImage();
        // nandha = new ImageIcon(getClass().getResource("./DSC_0283.JPG")).getImage();

         //raj = new ImageIcon(getClass().getResource("./DSC_0563.png")).getImage();
        // mathan = new ImageIcon(getClass().getResource("./IMG_20231219-105047.jpg")).getImage();
        // navaneethan = new ImageIcon(getClass().getResource("./IMG_20231219-105139.jpg")).getImage();
        alienimg.add(shipimg1);
        alienimg.add(shipimg2);
        alienimg.add(shipimg3);
        alienimg.add(shipimg4);
        // alienimg.add(pranish);
        // alienimg.add(prabha);
         //alienimg.add(sivaram);
        // alienimg.add(raj);
        // alienimg.add(nandha);
        // alienimg.add(mathan);
        // alienimg.add(navaneethan);


        ship = new block(shipx, shipy, shipwidth, shipheight, planeimg);
        alienarray = new ArrayList<>();
        bullets=new ArrayList<block>();
        createAliens();

        gameloop = new Timer(1000 / 60, this);
        gameloop.start();
        
    }




    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        // Draw the ship
        g.drawImage(ship.img, ship.x, ship.y, ship.width, ship.height, null);

        // Draw aliens
        for (block alien : alienarray) {
            if (alien.alive) {
                g.drawImage(alien.img, alien.x, alien.y, alien.width, alien.height, null);
            }
        }
        g.setColor(Color.red);
        for(int i=0;i<bullets.size();i++){
          block bullet=bullets.get(i);  
          if(!bullet.used){
            g.fillRect(bullet.x, bullet.y, bullet.width,bullet.height);
          }
        }
        g.setColor(Color.white);//score
        g.setFont(new Font("Arial",Font.PLAIN,32));
        if(gameover){
            g.drawString("Game Over"+String.valueOf(score), 10, 35);
        }
        else{
            g.drawString(String.valueOf(score), 10, 35);
  
        }
    }




public void move(){
    for(int i=0;i<alienarray.size();i++){
        block alien=alienarray.get(i);
        if(alien.alive){
            alien.x+=alienvelocityx;
            if(alien.x+alienwidth>=boardwidth|| alien.x<=0){
                alienvelocityx*=-1;
                alien.x+=alienvelocityx*2;

                for(int j=0;j<alienarray.size();j++){
                    alienarray.get(j).y+=alienheight;
                }
            }
            if(alien.y>=ship.y){
                gameover=true;
            }
        }
    }
    for(int i=0;i<bullets.size();i++){
        block bullet=bullets.get(i);
        bullet.y+=bulletvelocityy;
        for(int j=0;j<alienarray.size();j++){
            block alien=alienarray.get(j);
            if(!bullet.used && alien.alive && detectcollision(bullet, alien)){
                bullet.used=true;
                alien.alive=false;
                aliencount--;
                score+=100;
            }
        }
    }





    //clear bullets
    while (bullets.size()>0 && (bullets.get(0).used||bullets.get(0).y<0)) {
        bullets.remove(0);
    }
    if(aliencount==0){
        aliencolumns=Math.min(aliencolumns+1,columns/2-2);
        alienrows=Math.min(alienrows+1,rows-6);
        alienarray.clear();
        bullets.clear();
        createAliens();
        alienvelocityx=1;
        score+= aliencolumns*alienrows*100;
    }
}




    public void createAliens() {
        Random random = new Random();
        for (int r = 0; r < alienrows; r++) {
            for (int c = 0; c < aliencolumns; c++) {
                int randomImgIndex = random.nextInt(alienimg.size());
                block alien = new block(alienx + c * alienwidth, alieny + r * alienheight, alienwidth, alienheight, alienimg.get(randomImgIndex));
                alienarray.add(alien);
            }
        }
        aliencount=alienarray.size();
    }
public boolean detectcollision(block a, block b){
    return a.x<b.x+b.width && 
    a.x+a.width>b.x &&
    a.y<b.y+b.height &&
    a.y+a.height>b.y;
}




    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(gameover){
            gameloop.stop();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
    }

    @Override
    public void keyPressed(KeyEvent e) {
       
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT && ship.x > 0) {
            ship.x -= shipvelocityx;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && ship.x + ship.width < boardwidth) {
            ship.x += shipvelocityx;
        }
        else if(e.getKeyCode()==KeyEvent.VK_SPACE){
     block bullet=new block(ship.x+shipwidth*15/32,ship.y,bulletwidth, bulletheight, null);
     bullets.add(bullet);
        }
        repaint();
        if(gameover){
            ship.x=shipx;
            alienarray.clear();;
            bullets.clear();
            score=0;
            alienvelocityx=1;
            aliencolumns=3;
            alienrows=2;
            gameover=false;
            createAliens();
            gameloop.start();
        }
    }
}
