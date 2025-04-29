public class Equipment
{
    public enum EquipmentType{WEAPON, ARMOR}
    protected int attackBoost; // Boosting damage output by a percent
    protected int defenseBoost;// Boosting defence by a percent
    protected EquipmentType equipmentType;

    public Equipment(int attackBoost, int defenseBoost, EquipmentType equipmentType) {
        this.attackBoost = attackBoost;
        this.defenseBoost = defenseBoost;
        this.equipmentType = equipmentType;
    }
}
