import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Character
{
    protected int x, y, width, height;
    protected int health, maxHealth;
    protected int speed;
    protected int attack;
    protected int defence;
    protected BufferedImage texture;
    protected Equipment weapon, armor;
    protected String name;

    public Character(String name, int x, int y, int width, int height, int health, int speed, int attack, int defence, BufferedImage texture)
    {
        this.name = name;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.health = health;
        this.maxHealth = health;
        this.speed = speed;
        this.texture = texture;
        this.attack = attack;
        this.defence = defence;
        this.weapon = null;
    }

    public abstract void update();

    public void attack(Character target)
    {
        target.takeDamage(this.getAttack());
    }

    public void draw(Graphics g)
    {
        g.drawImage(texture, x, y, width, height, null);

        // Health bar
        g.setColor(Color.GRAY);
        g.fillRect(x, y - 10, width, 5);
        g.setColor(Color.GREEN);
        int healthBarWidth = (int) (((double) health / maxHealth) * width);
        g.fillRect(x, y - 10, healthBarWidth, 5);

        g.setColor(Color.WHITE);
        g.drawString(name, x, y - 15);
    }

    public Rectangle getBounds()
    {
        return new Rectangle(x, y, width, height);
    }

    public boolean isAlive()
    {
        return health > 0;
    }

    public void takeDamage(int damage)
    {
        health -= damage * (100 - getDefence()) / 100;
        if (health < 0) health = 0;
    }

    protected static BufferedImage loadTexture(String path)
    {
        try
        {
            return ImageIO.read(Character.class.getResource(path));
        }
        catch (Exception e)
        {
            BufferedImage img = new BufferedImage(40, 40, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = img.createGraphics();
            g.setColor(Color.MAGENTA);
            g.fillRect(0, 0, 40, 40);
            g.dispose();
            return img;
        }
    }

    public void setEquipment(Equipment equipment) {
        if (equipment.equipmentType == Equipment.EquipmentType.WEAPON) this.weapon = equipment;
        if (equipment.equipmentType == Equipment.EquipmentType.ARMOR) this.armor = equipment;
    }

    public int getDefence() {
        if (armor == null) return defence;
        return defence * (100 + armor.defenseBoost) / 100;
    }

    public int getAttack() {
        if (weapon == null) return attack;
        return attack * (100 + weapon.attackBoost) / 100;
    }
}