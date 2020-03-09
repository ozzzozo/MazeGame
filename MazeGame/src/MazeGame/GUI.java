package MazeGame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class GUI extends JFrame {
    Dimension res = Toolkit.getDefaultToolkit().getScreenSize();
    
    private final double WIDTH = res.getWidth()/3;
    private final double HEIGHT = res.getHeight()/2;
    private int pl = 0;
    
    private int[] blocks = new int[100];
    private JLabel[] items = new JLabel[100];
    private Color[] og = new Color[100];
    private int level = 110;
    
    private final KeyListener kl = new KeyListener() {
        @Override
        public void keyTyped(KeyEvent e) {}

        @Override
        public void keyReleased(KeyEvent e) {}

        @Override
        public void keyPressed(KeyEvent e) {
            if(e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT) {
                move(0);
            } else if(e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN) {
                move(1);
            } else if(e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT) {
                move(2);
            } else if(e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP) {
                move(3);
            } else if(e.getKeyCode() == KeyEvent.VK_ENTER) {
            	dispose();
            	new GUI("1");
            }
        }
    };
    
    public GUI(String level) {
    	setSize((int)WIDTH, (int)HEIGHT);
    	setVisible(true);
    	setFocusable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	setTitle("Level " + level);
    	loadLevel(level);
        this.level = Integer.parseInt(level);
        setLayout(new GridLayout(10, 10));
        addKeyListener(kl);
    }
    
    public void win(boolean check) {
        if(check) {
            if(level <= 5) {
                dispose();
                level++;
                GUI gud = new GUI(level + "");
            } else {
                JOptionPane.showMessageDialog(null, "You win");
                dispose();
                new GUI("1");
                //System.exit(0);
            }
        }
    }
    
    public boolean checkForWin(int dr) {
        int next = 0;
        if(dr == 0) {
            next = pl + 1;
        } else if(dr == 1) {
            next = pl + 10;
        } else if(dr == 2) {
            next = pl - 1;
        } else if(dr == 3) {
            next = pl - 10;
        }
        
        return items[next].getBackground().getRGB() == new Color(153, 228, 146).getRGB();
    }
    
    public void move(int dr) {
        win(checkForWin(dr));
        if(dr == 0) {
        	System.out.println(pl);
            if(!(pl % 9 == 0) || pl == 0 || pl == 81 || pl == 36 || pl == 27 || pl == 63 || pl == 45) {
                System.out.println(items[pl+1].getBackground().getRGB());
                System.out.println(og[pl+1]);
                if(items[pl+1].getBackground().getRGB() == new Color(212, 212, 212).getRGB()) {
                    pl++;
                    items[pl - 1].setBackground(og[pl]);
                    items[pl].setBackground(new Color(255, 255, 255));
                }
            }
        } else if(dr == 1) {
            if(!(pl >= 90)) {
                if(items[pl + 10].getBackground().getRGB() == new Color(212, 212, 212).getRGB()) {
                    pl += 10;
                    items[pl - 10].setBackground(og[pl]);
                    items[pl].setBackground(new Color(255, 255, 255));
                }
            }
        } else if(dr == 2) {
            if(!(pl % 10 == 0) || !(pl == 0)) {
                System.out.println(items[pl-1].getBackground().getRGB());
                if(items[pl-1].getBackground().getRGB() == new Color(212, 212, 212).getRGB()) {
                    pl--;
                    items[pl + 1].setBackground(og[pl]);
                    items[pl].setBackground(new Color(255, 255, 255));
                }
            } 
        } else if(dr == 3) {
            if(!(pl <= 9)) {
                if(items[pl - 10].getBackground().getRGB() == new Color(212, 212, 212).getRGB()) {
                    pl -= 10;
                    items[pl + 10].setBackground(og[pl]);
                    items[pl].setBackground(new Color(255, 255, 255));
                }
            }
        }
    }
    
    public void init(int[] leveldata) {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                items[i*10 + j] = new JLabel();
                items[i*10 + j].setOpaque(true);
                items[i*10 + j].setText("");
                items[i * 10 + j].setBackground(new Color(200, 200, 200));
                og[i * 10 + j] = new Color(200, 200, 200);
                if(blocks[i * 10 + j] == 1) {
                    items[i * 10 + j].setBackground(new Color(212, 212, 212));
                    og[i * 10 + j] = new Color(212, 212, 212);
                } else if(blocks[i * 10 + j] == 2) {
                    items[i * 10 + j].setBackground(new Color(153, 228, 146));
                    og[i * 10 + j] = new Color(153, 228, 146);
                }
                items[i*10 + j].setVisible(true);
                add(items[i*10 + j]);
            }
        }
        for (int i = 0; i < 10; i++) {
            if(items[i * 10].getBackground().getRGB() == new Color(212, 212, 212).getRGB()) {
            	items[i * 10].setBackground(new Color(236, 236, 236));
            	og[i * 10] = new Color(212, 212, 212);  
            	pl = i * 10;
            	return;                	
            }
        }

    }
    
    public void loadLevel(String level) {
    	try {
            BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/src/Levels/" + level + ".txt"));
            String ln = br.readLine();
            int cur = 0;
            while(ln != null) {
                int state = Integer.parseInt(ln);
                System.out.println(state);
                blocks[cur] = state;
                cur++;
                ln = br.readLine();
            }
            br.close();
            init(blocks);
    	} catch(IOException e) {
            System.out.println(e);
    	}
    }
    
}
