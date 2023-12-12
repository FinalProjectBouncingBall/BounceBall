import Texture.TextureReader;
import com.sun.opengl.util.Animator;
import com.sun.opengl.util.FPSAnimator;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.BitSet;
import java.util.Random;

public class Project1 extends JFrame {
    public static void main(String[] args) {
        new Project1();
        int X = 8;
        int Y = 83;
        int w=0 , z=0;
        for (int i = 0; i < 32; i++) {
            if(i % 8 == 0 && i != 0){
                z -= 11;
                w = 0;
            }
            bounceListener.matrix[i][0] = X + w;
            bounceListener.matrix[i][1]= Y + z;
            w += 11;
        }
        System.out.println( bounceListener.matrix[8][0]);
        System.out.println( bounceListener.matrix[9][0]);

    }

    public Project1() {
        GLCanvas glcanvas;
        Animator animator;

        bounceListener listener = new BouncingBallDisplay() {
        };
        glcanvas = new GLCanvas();
        glcanvas.addGLEventListener(listener);
        glcanvas.addKeyListener(listener);
        getContentPane().add(glcanvas, BorderLayout.CENTER);
        animator = new FPSAnimator(15);
        animator.add(glcanvas);
        animator.start();

        setTitle("bouncing ball");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 700);
        setLocationRelativeTo(null);
        setVisible(true);
        setFocusable(true);
        glcanvas.requestFocus();
    }
}
abstract  class bounceListener implements GLEventListener, KeyListener {

    protected String assetsFolderName = "Assets//Ball";
    public static int[][]matrix = new int[33][2];

}
class BouncingBallDisplay extends bounceListener {
    public static boolean isEmpty;

    int speed =5 ;
    double  angle = new Random().nextInt(100);
    int x1 = 0, y1 = 0,  xDirection = 50, yDirection = 50;
    int maxWidth = 100;
    int maxHeight = 100;

    String textureNames[] = {"RTS_Crate.png" , "Ball(1)_Asset.webp"};
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
                texture[i] = TextureReader.readTexture(assetsFolderName + "//" + textureNames[i] , true);
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
//                System.out.println(e);
                e.printStackTrace();
            }
        }
    }
    public void display(GLAutoDrawable gld) {
        GL gl = gld.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);       //Clear The Screen And The Depth Buffer
        gl.glLoadIdentity();
        DrawSprite(gl,x1,y1,1,0.6f);
        empty();
        for (int i = 0; i < 33; i++) {
            DrawSprite(gl, matrix[i][0], matrix[i][1], 0, 1);
        }
            for (int i = 32; i > -1; i--) {
                if (x1 >= matrix[i][0]-4 && x1 <= (matrix[i][0] + 13) && y1 >= matrix[i][1]-13 && y1 <= matrix[i][1]+2) {
                    matrix[i][0] = -20;
                    matrix[i][1] = -20;
                    yDirection = -5 *speed;
                    xDirection = -5 *speed;
                    y1 += (int) (yDirection * Math.cos(Math.toRadians(angle)));
                    x1 += (int) (xDirection * Math.sin(Math.toRadians(angle)));
                    System.out.println(x1 +" "+ y1);
                }
                DrawSprite(gl, matrix[i][0], matrix[i][1], 0, 1);
        }

        if (x1  +10>= 100) {
            angle = new Random().nextInt(0,90);
            xDirection = -5 *speed;
        }
        if (y1  +10 >= 100  ) {
            angle = new Random().nextInt(0,90);
            yDirection = -5 *speed;
        }
        if (x1  <= 0) {
            angle = new Random().nextInt(0,90);
            xDirection = 5 *speed;
        }
        if (y1  <= 0) {
            angle = new Random().nextInt(0,90);
            yDirection = 5 *speed;
        }
        y1 += (int) (yDirection * Math.cos(Math.toRadians(angle)));
        x1 += (int) (xDirection * Math.sin(Math.toRadians(angle)));

        if(isEmpty) {
            JOptionPane.showConfirmDialog(null, "YOU XON ! \n Would you like tp play again?");
        }
    }
    public static void empty(){
        int count =0;
        for(int i =0 ; i<bounceListener.matrix.length-1; i++){
            if (bounceListener.matrix[i][0] == -20  && bounceListener.matrix[i][1] == -20){
                count++;
            }
            if (count == 32)
                isEmpty = true;
        }
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }

    public void DrawSprite(GL gl,int x, int y, int index, float scale){
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);    // Turn Blending On
        gl.glPushMatrix();
        gl.glTranslated( x/(maxWidth/2.0) - 0.9, y/(maxHeight/2.0) - 0.9, 0);
        gl.glScaled(0.1*scale, 0.1*scale, 1);
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
    public void keyPressed(final KeyEvent event) {
        int keyCode = event.getKeyCode();
        keyBits.set(keyCode);
    }

    @Override
    public void keyReleased(final KeyEvent event) {
        int keyCode = event.getKeyCode();
        keyBits.clear(keyCode);
    }

    @Override
    public void keyTyped(final KeyEvent event) {
    }

}