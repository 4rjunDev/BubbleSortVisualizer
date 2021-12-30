import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class Bubbles extends JFrame implements Runnable, KeyEventDispatcher
{
    // JFrame size:
    private int width;
    private int height;

    // Background drawing buffer:
    //private Image backbuffer;
    //private Graphics bg;

    // Number of values in the list:
    private final int NUM = 20;

    // List of random numbers
    private int list[] = new int [NUM];

    // Index of value that is currently being examined; it will be green
    private int showGreen=0;

    // Index of value that is currently being swapped; it will be red
    private int showRed=0;

    private int delay=100;

    private boolean restart=false;

    public static void main (String[] args)
    {
        new Bubbles();
    }

    public Bubbles()
    {
        super();
        this.setBounds(100,100,500,300); 
        width=this.getBounds().width;
        height=this.getBounds().height;
        makeList();
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        makeList();
        this.setVisible(true);

        //backbuffer = createImage(width, height);
        //bg = backbuffer.getGraphics();

        Thread th = new Thread (this);
        th.start ();

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);

    }

    public void makeList ()
    {
        for (int i = 0 ; i < NUM ; i++)
        {
            list [i] = (int) (Math.random() * 100);
        }
    }

    public void showList (Graphics g)
    {
        int x;
        g.clearRect(0,0,width,height);

        for (int i = 0 ; i < NUM ; i++)
        {
            x=list[i]*height/100;
            if (i==showGreen)
            {
                g.setColor(Color.green);
            }

            else if (i==showRed)
            {
                g.setColor(Color.red);
            }
            else
            {
                g.setColor(Color.gray);
            }
            g.fillRect(i*width/NUM,height-x-1,width/NUM,x);
            g.setColor(Color.red);
            g.drawRect(i*width/NUM,height-x-1,width/NUM,x);
        }
    }

    public void run()
    {
        int i, j, temp;
        while (true)
        {
            // Do repetitive things here
            for (j = 0 ; j < NUM - 1 ; j++)
            {
                if (restart)
                {
                    i=0;
                    j=0;
                    restart=false;
                }
                for (i = 0 ; i < NUM - j- 1 ; i++)
                {
                    showGreen = i;
                    if (list [i] > list [i + 1])
                    {
                        // Highlight the value that is going to be swapped
                        showRed=i+1;
                    }
                    else
                    {
                        // Set to -1 so nothing highlights if no swap is taking place
                        showRed=-1;
                    }
                    repaint();
                    try
                    {
                        Thread.sleep (delay);
                    }
                    catch (InterruptedException e)
                    {
                        // do nothing
                    }
                    // Now actually do the swap
                    if (list [i] > list [i + 1])
                    {
                        temp = list [i];
                        list [i] = list [i + 1];
                        list [i + 1] = temp;
                    }
                }
            }
            repaint();
        }
    }

    public boolean dispatchKeyEvent(KeyEvent e)
    {
        int key=e.getKeyCode();
        if (e.getID()==KeyEvent.KEY_PRESSED)
        {
            if (key>48 && key<58)
            {
                delay=100*(key-48);
            } 
            else if (key==48)
            {
                delay=25;
            }
            else if (key==32)
            {
                makeList();
                restart=true;
            }
            return true;
        }
        return false;
    }

    public void paint (Graphics g)
    {
        showList(g);
        g.setColor(Color.black);
        g.drawString("Bubblesort demonstration", 20, 20);
        g.drawString("Press 0-9 to change speed. Press 'space' to restart", 20, 40);
        //g.drawImage(backbuffer,0,0,this);
    }
}
