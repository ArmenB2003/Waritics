import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener, KeyListener
{
    private Timer timer;
    private ArrayList<Character> entities;
    private Character player;
    private String statusMessage = "";
    private boolean up, down, left, right;
    private int level = 1;
    private Image background;
    private JButton move;


    public GamePanel()
    {
        setPreferredSize(new Dimension(800, 600));
        //setBackground(Color.BLACK);
        setLayout(null);

        background = new ImageIcon(getClass().getResource("./test.jpg")).getImage();



        setFocusable(true);
        addKeyListener(this);


        entities = new ArrayList<>();

        loadLevel(level);

        timer = new Timer(20, this);
        timer.start();
    }

    private void loadLevel(int level) {
        entities.clear();

        player = new Warrior(100, 100);
        entities.add(player);
        player.setEquipment(new Equipment(0, 200, Equipment.EquipmentType.ARMOR));
        player.setEquipment(new Equipment(200, 0, Equipment.EquipmentType.WEAPON));


        int enemyCount = 2 + level;
        for (int i = 0; i < enemyCount; i++) {
            Character enemy = (i % 2 == 0) ? new Mage(200 + i * 60, 100 + i * 40, player)
                                           : new Archer(200 + i * 60, 100 + i * 40, player);
            entities.add(enemy);
        }

        move = new JButton("Attack");
        move.setLayout(null);

        move.setFocusable(false);
        move.setBounds(500,300,100,50);
        move.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (Character character : entities) {
                    if (character != player && character.isAlive()) {
                        character.update();
                        player.attack(character);
                        statusMessage = player.name + " attacked " + character.name + "!";
                    }
                }
            }
        });

        add(move);

    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(background, 0, 0,800 ,600,null);
        for (Character e : entities) {
            if (e.isAlive()) e.draw(g);
        }

        g.setColor(Color.WHITE);
        g.drawString("Level: " + level, 700, 20);
        g.drawString(statusMessage, 10, 580);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (up && player.y >= 0)
            player.y -= player.speed;
        if (down && player.y <= 600 - 40)
            player.y += player.speed;
        if (left && player.x >= 0)
            player.x -= player.speed;
        if (right && player.x <= 800 - 40)
            player.x += player.speed;



        boolean enemiesAlive = false;

        for (Character character : entities) {
            if (character != player && character.isAlive()) {
                character.update();

                if (player.getBounds().intersects(character.getBounds())) {
                    character.attack(player);
                    statusMessage = character.name + " attacked you!";
                }

                if (character.getBounds().intersects(player.getBounds())) {
                    player.attack(character);
                    statusMessage = player.name + " attacked " + character.name + "!";
                }

                enemiesAlive = true;
            }
        }

        if (!enemiesAlive) {
            level++;
            loadLevel(level);
            statusMessage = "Level Up! Welcome to Level " + level;
        }


        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W)
            up = true;
        else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S)
            down = true;
        else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A)
            left = true;
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D)
            right = true;
        else if (e.getKeyCode() == KeyEvent.VK_SHIFT)
            player.speed = 7;
        else if (e.getKeyCode() == KeyEvent.VK_SPACE)
        {
            for (Character character : entities)
            {
                if (character != player && character.isAlive() && player.getBounds().intersects(character.getBounds()))
                {
                    character.takeDamage(40);
                    statusMessage = player.name + " used special attack on " + character.name + "!";
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W)
            up = false;
        else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S)
            down = false;
        else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A)
            left = false;
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D)
            right = false;
        else if (e.getKeyCode() == KeyEvent.VK_SHIFT)
            player.speed = 4;
    }

    @Override
    public void keyTyped(KeyEvent e)
    {
        return;
    }
}