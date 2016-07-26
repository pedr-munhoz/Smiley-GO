import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;

class Keyboard {
   /*Variables related to the keyboard*/
   ArrayList<Integer> keys = new ArrayList<Integer>();
   ArrayList<Character> keys_c = new ArrayList<Character>();
}
class Mouse {
   /*Variables related to the mouse*/
   int x, y; //contains the x and y coordenates of the pointer
   int prev_x, prev_y; //contains the x and y coordenates of the previous pointer position
   boolean pressed; //defines if the right button of the mouse is been pressed
}
class Window {
   int height, width; //defines the dimentions of the program window
}

class Start extends JFrame { //creates a window for the program
   Munhoz_Engine canvas;

   public Start(Munhoz_Engine c){
      canvas = c;
      add(canvas);
      setVisible(true);
      canvas.setSize(400,400);
      canvas.init();
      pack();
      setDefaultCloseOperation(EXIT_ON_CLOSE);



      KeyListener keyListener = new KeyAdapter(){
         @Override
         public void keyPressed(KeyEvent e){
            if (!(canvas.keyboard.keys.contains(e.getKeyCode()))) {
               canvas.keyboard.keys.add(e.getKeyCode());
               canvas.keyboard.keys_c.add(e.getKeyChar());
            }
         }
         @Override
         public void keyReleased(KeyEvent e){
            canvas.keyboard.keys.remove(canvas.keyboard.keys.indexOf(e.getKeyCode()));
            canvas.keyboard.keys_c.remove(canvas.keyboard.keys_c.indexOf(e.getKeyChar()));
         }
      };
		addKeyListener(keyListener);
		canvas.addKeyListener(keyListener);
   }
}

class Munhoz_Engine extends Canvas {
   Keyboard keyboard = new Keyboard();
   Mouse mouse = new Mouse();
   Window window = new Window();

	Image offscreen = null;
	Graphics offgraphics = null;

   public Munhoz_Engine(){
      Timer t = new Timer(10, new ActionListener(){
         @Override
         public void actionPerformed (ActionEvent e){
            repaint();
         }
      });
		t.setCoalesce(false);
		t.start();

      MouseAdapter mouseAdapter = new MouseAdapter() {

         void mouse(MouseEvent e) {
            mouse.prev_x = mouse.x;
            mouse.prev_y = mouse.y;
            mouse.x = e.getX();
            mouse.y = e.getY();
         }

         @Override
         public void mouseMoved(MouseEvent e) {
            mouse(e);
         }

         @Override
         public void mouseDragged(MouseEvent e) {
            mouse(e);
         }

         @Override
         public void mousePressed(MouseEvent e) {
            mouse(e);
            mouse.pressed = true;
         }

         @Override
         public void mouseReleased(MouseEvent e) {
            mouse(e);
            mouse.pressed = false;
         }
      };

      addMouseListener(mouseAdapter);
      addMouseMotionListener(mouseAdapter);
   }

   public void paint(Graphics g){};

   public void init(){

   }
   void setObstacles(){

   }

   @Override
	public void update(Graphics g) {
		if (window.height != getHeight() || window.width != getWidth() || offscreen == null) {
			window.height = getHeight();
			window.width = getWidth();
			offscreen = createImage(window.width, window.height);
			if (offgraphics != null) {
				offgraphics.dispose();
			}
			offgraphics = offscreen.getGraphics();
         //setObstacles();
		}
		super.update(offgraphics);
		g.drawImage(offscreen, 0, 0, null);
	}

   @Override
   public void setSize(int w, int h) {
      setPreferredSize(new Dimension(w, h));
      window.width = w;
      window.height = h;
   }
}
