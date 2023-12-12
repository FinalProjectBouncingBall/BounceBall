import Texture.TextureReader;
import com.sun.opengl.util.Animator;
import com.sun.opengl.util.FPSAnimator;
import org.w3c.dom.ls.LSOutput;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

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
import java.util.Scanner;

public class FinalProject extends JFrame {

    public static void main(String[] args) {
//        for (int i = 0; i < 10; i++) {
//            System.out.println(i%4);
//        }
        new FinalProject();
        int X = 8;
        int Y = 85;
        int w = 0, z = 0;
        soundEffect se = new soundEffect();
        for (int i = 0; i < 10; i++) {
            bounceListener.matrix[i] = X + w;
            bounceListener.matrix2[i] = Y + z;
            w += 11;
            z -= 11;

//            System.out.println(bounceListener.matrix[0] + " , " + bounceListener.matrix2[0]);
        }
    }


    public FinalProject() {
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

abstract class bounceListener implements GLEventListener, KeyListener {

    protected String assetsFolderName = "C:\\Assets";
    public static int[] matrix = new int[30];
    public static int[] matrix2 = new int[30];

}

class BouncingBallDisplay extends bounceListener {
    double angle;
    int xDirection = 50, yDirection = 50;
    int xPosition = 0;
    int yPosition = -600;
    int animationIndex = 0;
    int maxWidth = 100;
    int maxHeight = 100;
    int x = maxWidth / 2, y = maxHeight / 2;
    int xball = 0;
    int yball = 0;

    String textureNames[] = {"RTS_Crate.png", "Back (1).png"};
    TextureReader.Texture texture[] = new TextureReader.Texture[textureNames.length];
    int textures[] = new int[textureNames.length];


    public void init(GLAutoDrawable gld) {

        GL gl = gld.getGL();
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);    //This Will Clear The Background Color To Black

        gl.glEnable(GL.GL_TEXTURE_2D);  // Enable Texture Mapping
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glGenTextures(textureNames.length, textures, 0);

        for (int i = 0; i < textureNames.length; i++) {
            try {
                texture[i] = TextureReader.readTexture(assetsFolderName + "//" + textureNames[i], true);
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
            } catch (IOException e) {
//                System.out.println(e);
                e.printStackTrace();
            }
        }
    }

    public void soundEffect() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        Scanner scanner = new Scanner(System.in);
        File file = new File("C://Users//Sama//Downloads//mixkit-arcade-game-jump-coin-216.wav");
        AudioInputStream audioStream=AudioSystem.getAudioInputStream(file);
        Clip clip =AudioSystem.getClip();
        clip.open(audioStream);
        clip.start();
//        clipx(clip);
        String respond = scanner.next();
    }
    public void display(GLAutoDrawable gld) {

        GL gl = gld.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);       //Clear The Screen And The Depth Buffer
        gl.glLoadIdentity();

        System.out.println(xball+" " + yball);
        DrawBackground(gl);
        handleKeyPress();
        animationIndex = animationIndex % 2;
        DrawSprite(gl,xball,yball , 0, (float) 1);
        DrawSprite(gl,10,20 , 0, (float) 1);
        try {
            soundEffect();
        } catch (UnsupportedAudioFileException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 8; i++) {
                if (xball >= matrix[i] && xball <= (matrix[i] + 10) && (yball <= matrix2[j] && yball >= matrix2[j] - 10)) {
                    matrix[i] = -20;
                    matrix2[j] = -20;
                }
                DrawSprite(gl, matrix[i], matrix2[j], 0, 1);
//                animationIndex++;
            }
        }

        if (xball + 5 >= 100) {
            angle = new Random().nextInt(100);
            xDirection = -50;
        }
        if (yball + 10 >= 100) {
            angle = new Random().nextInt(100);
            yDirection = -50;

        }
        if (xball - 7 <= -100) {
            angle = new Random().nextInt(100);
            xDirection = 50;
        }
        if (yball - 5 <= -100) {
            angle = new Random().nextInt(100);
//            yDirection = 50;
        }
        if(xPosition==0){
        if (yball - 5 <= -100) {
            angle = new Random().nextInt(100);
//            yDirection = 50;
        }
        }
//////        if(Math.sqrt((Math.pow(xPosition-x1, 2)+Math.pow((20+yPosition-y1), 2)))==10){
//            if(200+xPosition<x1){
//            if (y1 - 50 <= -600) {
//         angle = new Random().nextInt(100);
//            yDirection = 50;
//            }
//        }
        yball += (int) (yDirection * Math.cos(Math.toRadians(angle)));
        xball += (int) (xDirection * Math.sin(Math.toRadians(angle)));
//        drawRectangle(drawable.getGL());
//

//        DrawGraph(gl);
        // DrawSprite(gl, x, y, 0, 1);
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }

    public void DrawSprite(GL gl, int x, int y, int index, float scale) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);    // Turn Blending On

        gl.glPushMatrix();
        gl.glTranslated(x / (maxWidth / 2.0) - 0.9, y / (maxHeight / 2.0) - 0.9, 0);
        gl.glScaled(0.1 * scale, 0.1 * scale, 1);
        //System.out.println(x +" " + y);
        gl.glBegin(GL.GL_QUADS);
        // Front Face
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

    public void DrawBackground(GL gl) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[1]);    // Turn Blending On

        gl.glPushMatrix();
        gl.glBegin(GL.GL_QUADS);
        // Front Face
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

    /*
     * KeyListener
     */

    public void handleKeyPress() {

        if (isKeyPressed(KeyEvent.VK_LEFT)) {
            if (x > 0) {
                x--;
            }
            animationIndex++;
        }
        if (isKeyPressed(KeyEvent.VK_RIGHT)) {
            if (x < maxWidth - 10) {
                x++;
            }
            animationIndex++;
        }
        if (isKeyPressed(KeyEvent.VK_DOWN)) {
            if (y > 0) {
                y--;
            }
            animationIndex++;
        }
        if (isKeyPressed(KeyEvent.VK_UP)) {
            if (y < maxHeight - 10) {
                y++;
            }
            animationIndex++;
        }
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
        // don't care
    }

    public boolean isKeyPressed(final int keyCode) {
        return keyBits.get(keyCode);
    }
}
class soundEffect {
    public static void main(String[] args) throws UnsupportedAudioFileException,IOException,LineUnavailableException{
        Scanner scanner = new Scanner(System.in);
        File file = new File("C://Users//Sama//Downloads//mixkit-arcade-game-jump-coin-216.wav");
        AudioInputStream audioStream=AudioSystem.getAudioInputStream(file);
        Clip clip =AudioSystem.getClip();
        clip.open(audioStream);
        clip.start();
//        clipx(clip);
        String respond = scanner.next();
//
    }
//    public static void clipx(Clip clip){
//        clip.start();
//    }
}