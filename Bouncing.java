
package BounceBall;




import Texture.TextureReader;
import com.sun.opengl.util.Animator;
import com.sun.opengl.util.FPSAnimator;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.BitSet;
import java.util.Random;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import Texture.TextureReader;
import com.sun.opengl.util.Animator;
import com.sun.opengl.util.FPSAnimator;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.BitSet;
import java.util.Random;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Bouncing extends JFrame {

    public static void main(String[] args) {
        new Bouncing();
        
    }
    public Bouncing() {
        GLCanvas glcanvas;
        Animator animator;
        
        MenuListener listener = new BallGLEventListener();
        glcanvas = new GLCanvas();
        glcanvas.addGLEventListener(listener);
        glcanvas.addKeyListener(listener);
        
        glcanvas.addMouseListener(listener);
        glcanvas.addMouseMotionListener(listener);
        
        getContentPane().add(glcanvas, BorderLayout.CENTER);
        animator = new FPSAnimator(24);
        animator.add(glcanvas);
        animator.start();
        setTitle("Bounce Ball");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 900);
        setLocationRelativeTo(null);
        setVisible(true);
        setFocusable(true);
        glcanvas.requestFocus();
    }
}

class BallGLEventListener extends MenuListener  {
       

    double scale = 1;
    int animationIndex = 0;
    int index= 0;
    private int direction =0;
    int maxWidth = 100;
    int maxHeight = 100;
    int xPosition ;
    int yPosition ;
    int count ;
    int x1 = 0, y1 = 0;
    double xDirection = 50, yDirection = 50;
    double angle;
    boolean changeState = false;
    double speed =1 ;

    private GLCanvas glc;
    String textureNames[] = {"BouncingBall2.jpg","flcustom.png","PngItem_5608811.png",
        "HeartTry.jpg","intersection2.jpg","Levels.jpg","Box.jpg"};
    TextureReader.Texture texture[] = new TextureReader.Texture[textureNames.length];
    int textures[] = new int[textureNames.length];
    
    public void init(GLAutoDrawable gld) {
      
        GL gl = gld.getGL();
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);    //This Will Clear The Background Color To Black
        gl.glEnable(GL.GL_TEXTURE_2D);  // Enable Texture Mapping
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);	
        gl.glGenTextures(textureNames.length, textures, 0);
        for(int i = 0; i < textureNames.length; i++){
            try {
                texture[i] = TextureReader.readTexture(assetsFolderName + "//"+ textureNames[i] , true);
                gl.glBindTexture(GL.GL_TEXTURE_2D, textures[i]);

//                mipmapsFromPNG(gl, new GLU(), texture[i]);
                new GLU().gluBuild2DMipmaps(
                    GL.GL_TEXTURE_2D,
                    GL.GL_RGBA, // Internal Texel Format,
                    texture[i].getWidth(), texture[i].getHeight(),
                    GL.GL_RGBA, // External format from image,
                    GL.GL_UNSIGNED_BYTE,
                    texture[i].getPixels() // Imagedata
                    );
            } catch( IOException e ) {
              System.out.println(e);
              e.printStackTrace();
            }
        }
    }
    
    public void display(GLAutoDrawable gld) {

        GL gl = gld.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);   
       
         play(gl);
        
    
    }
    public void play(GL gl){
        animationIndex = 1;
        if(xPosition>=80){
          DrawSprite(gl,80,0,animationIndex,3*scale);
          
        }
        else{
        DrawSprite(gl,xPosition,0,animationIndex,3*scale);
        }
        
        index=2;

    if (x1 +10>= 100) {
            angle = new Random().nextInt(100);
            xDirection = -5*speed;
        }
        if (y1 +7 >= 100) {
            angle = new Random().nextInt(100);
            yDirection = -5*speed;
            
        }
        if (x1  <= 0) {
            angle = new Random().nextInt(100);
            xDirection = 5*speed;
        }

        if(x1>xPosition-10&&x1<xPosition+20){
        if (y1 - 5 <= 0) {
            angle = new Random().nextInt(100);
            yDirection = 5*speed;
              }
        }
        
        if(y1<0){
            x1=50;
            y1=50;
            count++;
           if(count==3){
           x1=-110;
           y1=-100;
//           count--;
           JOptionPane.showMessageDialog(null, "Game Over");
           changeState=false;
           count=-1;
           }
        }
        
        y1 += (int) (yDirection * Math.cos(Math.toRadians(angle)));
        x1 += (int) (xDirection * Math.sin(Math.toRadians(angle)));

        DrawSprite(gl,x1,y1,index,0.6*scale);
        
        DrawSprite(gl,90,90,3,0.6*scale);
        
        if(count<=1){
        DrawSprite(gl,80,90,3,0.6*scale);
        }
        if(count==0){
        DrawSprite(gl,70,90,3,0.6*scale);
        }
    
    
    
    }
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }
   
     public void DrawSprite(GL gl,float x, float y, int index, double scale){
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);	// Turn Blending On
 
        gl.glPushMatrix();
            gl.glTranslated( x/(maxWidth/2.0) - 0.9, y/(maxHeight/2.0) - 0.9, 0);
            gl.glScaled(0.1*scale, 0.1*scale, 1);
            
//            System.out.println(x +" " + y);
            gl.glBegin(GL.GL_QUADS);
                gl.glTexCoord2f(0.0f, 0.0f);
                gl.glVertex3f(-1.0f, -1.0f, -1.0f);
                gl.glTexCoord2f(1.0f, 0.0f);
                gl.glVertex3f(1.0f, -1.0f, -1.0f);
                gl.glTexCoord2f(1.0f, 1.0f);
                gl.glVertex3f(1.0f, 1.0f, -1.0f);
                gl.glTexCoord2f(0.0f, 1.0f);
                gl.glVertex3f(-1.0f, 1.0f, -1.0f);
            gl.glEnd();
        gl.glPopMatrix();
        
        gl.glDisable(GL.GL_BLEND);
    }
    
    public void DrawBackground(GL gl){
        gl.glEnable(GL.GL_BLEND);	
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[0]);	// Turn Blending On
        gl.glPushMatrix();
            gl.glBegin(GL.GL_QUADS);
                gl.glTexCoord2f(0.0f, 0.0f);
                gl.glVertex3f(-1.0f, -1.0f, -1.0f);
                gl.glTexCoord2f(1.0f, 0.0f);
                gl.glVertex3f(1.0f, -1.0f, -1.0f);
                gl.glTexCoord2f(1.0f, 1.0f);
                gl.glVertex3f(1.0f, 1.0f, -1.0f);
                gl.glTexCoord2f(0.0f, 1.0f);
                gl.glVertex3f(-1.0f, 1.0f, -1.0f);
            gl.glEnd();
        gl.glPopMatrix();
        gl.glDisable(GL.GL_BLEND);
    }
    
    
        

        
    public BitSet keyBits = new BitSet(256);
    
    
    @Override 
    public void keyPressed(final KeyEvent e) {
        int keyCode = e.getKeyCode();
        keyBits.set(keyCode);
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
           if(xPosition>20){
            xPosition -= 10;
           }
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if(xPosition<100){
            xPosition += 10;
            }
        }  
    } 
 
    @Override 
    public void keyReleased(final KeyEvent event) {
//        int keyCode = event.getKeyCode();
//        keyBits.clear(keyCode);
    } 
 
    @Override 
    public void keyTyped(final KeyEvent event) {
       
    } 
 
    public boolean isKeyPressed(final int keyCode) {
        return keyBits.get(keyCode);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(changeState==true){
        double x = e.getX();
        double y = e.getY();
        Component c = e.getComponent();
        double width = c.getWidth();
        double height = c.getHeight();

        xPosition = (int) ((x / width) * 100);
        yPosition = (int) (((y / height) * 100));
        yPosition = 100 - yPosition;
//        System.out.println(xPosition+ " "+yPosition);
    }
    }
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        double x = e.getX();
        double y = e.getY();
        
        Component c = e.getComponent();
        
        double width = c.getWidth();
        double height = c.getWidth();


        xPosition = (int) ((x / width) * 100);
        yPosition = (int) (((y / height) * 100));
        yPosition = 100 - yPosition;

        System.out.println(xPosition+" "+yPosition);

    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

}

